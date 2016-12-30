package jp.hisano.vaadin.adminlte;

import java.io.ByteArrayInputStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.common.collect.Lists;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.declarative.DesignContext;

public final class TemplateLayout extends CustomLayout {
	private static final String TEMPLATE_DIRECTORY_NAME = "templates";

	private static final TemplateEngine _templateEngine;
	static {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix(Settings.getThemeDirectoryPath() + TEMPLATE_DIRECTORY_NAME + "/");
		templateResolver.setSuffix(".html");
		if (isDebuggerAttached()) {
			templateResolver.setCacheable(false);
		}

		_templateEngine = new TemplateEngine();
		_templateEngine.setTemplateResolver(templateResolver);
	}

	private static boolean isDebuggerAttached() {
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}

	static Element getTemplateElement(String templateName, IContext context, String id) {
		Document templateDocument = Jsoup.parse(_templateEngine.process(templateName, context));
		convertImagePaths(Settings.getThemeDirectoryPath(), templateName, templateDocument);
		Element element = templateDocument.getElementById(id);
		element.removeAttr("id");
		return element;
	}

	private static void convertImagePaths(String pathPrefix, String templateName, Document templateDocument) {
		for (Element element: templateDocument.getElementsByTag("img")) {
			if (element.hasAttr("src")) {
				element.attr("src", appendThemeDirectory(pathPrefix, templateName, element.attr("src")));
			}
		}
	}

	private static String appendThemeDirectory(String pathPrefix, String templateName, String path) {
		if (path.startsWith("https:") || path.startsWith("http")) {
			return path;
		}
		return pathPrefix + TEMPLATE_DIRECTORY_NAME + "/" + (templateName.contains("/")? StringUtils.substringBeforeLast(templateName, "/") + "/": "")  + path;
	}

	private final String _title;
	private final Set<String> _bodyClassNames;
	private final List<String> _scripts;

	private final List<DesignContext> _designContexts = Lists.newLinkedList();

	public TemplateLayout(String templateName, IContext context) {
		this(templateName, null, context);
	}

	public TemplateLayout(String templateName, String elementId, IContext context) {
		Document templateDocument = Jsoup.parse(_templateEngine.process(templateName, context));
		convertImagePaths("../", templateName, templateDocument);
		_title = parseTitle(templateDocument);
		_bodyClassNames = templateDocument.body().classNames();
		_scripts = templateDocument.body().getElementsByTag("script").stream().map(element -> {
			element.remove();
			if (element.hasAttr("src")) {
				return "$('<script src=\"" + appendThemeDirectory(Settings.getThemeDirectoryPath(), templateName, element.attr("src")) + "\"></script>').appendTo('body').remove();";
			} else {
				return element.data();
			}
		}).collect(Collectors.toList());
		List<Element> vaadinElements = replaceVaadinElements(templateDocument);

		setSizeFull();
		setTemplateContents(elementId == null? templateDocument.html(): templateDocument.getElementById(elementId).html());
		for (Element vaadinElement : vaadinElements) {
			DesignContext designContext = Design.read(new ByteArrayInputStream(vaadinElement.toString().getBytes(StandardCharsets.UTF_8)), null);
			_designContexts.add(designContext);
			addComponent(designContext.getRootComponent(), vaadinElement.attr("location"));
		}
	}

	private String parseTitle(Document templateDocument) {
		Elements elements = templateDocument.getElementsByTag("title");
		if (elements.isEmpty()) {
			return null;
		}
		return elements.get(0).text();
	}

	@Override
	public void attach() {
		super.attach();

		StringBuilder allScripts = new StringBuilder();
		if (_title != null) {
			allScripts.append("document.title = '" + _title +"';\n");
		}
		allScripts.append(_bodyClassNames.stream().map(className -> "$('body').addClass('" + className + "');\n").collect(Collectors.joining()));
		allScripts.append(_scripts.stream().map(script -> script + "\n") .collect(Collectors.joining()));

		JavaScript.eval("" + allScripts);
	}

	private List<Element> replaceVaadinElements(Document templateDocument) {
		List<Element> vaadinElements = Lists.newLinkedList();
		replaceVaadinElements(templateDocument.body(), vaadinElements);
		return vaadinElements;
	}

	private void replaceVaadinElements(Element parent, List<Element> vaadinElements) {
		for (Element child : parent.children()) {
			if (!child.tagName().startsWith("vaadin-")) {
				replaceVaadinElements(child, vaadinElements);
				continue;
			}

			String id = UUID.randomUUID().toString();
			Element newChild = new Element(Tag.valueOf("div"), child.baseUri());
			newChild.attr("location", id);
			child.attr("location", id);

			child.before(newChild);
			child.remove();

			vaadinElements.add(child);
		}
	}

	public Component getComponent(String id) {
		return _designContexts.stream().filter(designContext -> designContext.getComponentByLocalId(id) != null).findFirst().orElseThrow(IllegalArgumentException::new).getComponentByLocalId(id);
	}
}
