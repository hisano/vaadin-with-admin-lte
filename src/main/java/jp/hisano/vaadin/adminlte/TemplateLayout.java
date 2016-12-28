package jp.hisano.vaadin.adminlte;

import java.io.ByteArrayInputStream;
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
	private static TemplateEngine _templateEngine;

	static {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix(getTemplateDirectoryPath());
		templateResolver.setSuffix(".html");

		_templateEngine = new TemplateEngine();
		_templateEngine.setTemplateResolver(templateResolver);
	}

	private static String getTemplateDirectoryPath() {
		return Settings.getThemeDirectoryPath() + "templates/";
	}

	private final String _templateName;
	private final Set<String> _bodyClassNames;
	private final List<String> _scripts;
	private final List<DesignContext> _designContexts = Lists.newLinkedList();

	public TemplateLayout(String templateName, IContext context) {
		_templateName = templateName;
		String template = _templateEngine.process(templateName, context);
		Document templateDocument = Jsoup.parse(template);
		_bodyClassNames = templateDocument.body().classNames();
		_scripts = templateDocument.body().getElementsByTag("script").stream().map(element -> {
			element.remove();
			if (element.hasAttr("src")) {
				return "$('<script src=\"" + appendThemeDirectory(element.attr("src")) + "\"></script>').appendTo('body').remove();";
			} else {
				return element.data();
			}
		}).collect(Collectors.toList());
		List<Element> vaadinElements = replaceVaadinElements(templateDocument);
		template = templateDocument.html();

		setSizeFull();
		setTemplateContents(template);
		for (Element vaadinElement : vaadinElements) {
			DesignContext designContext = Design.read(new ByteArrayInputStream(vaadinElement.toString().getBytes(StandardCharsets.UTF_8)), null);
			_designContexts.add(designContext);
			addComponent(designContext.getRootComponent(), vaadinElement.attr("location"));
		}
	}

	private String appendThemeDirectory(String path) {
		if (path.startsWith("https:") || path.startsWith("http")) {
			return path;
		}
		return getTemplateDirectoryPath() + (_templateName.contains("/")? StringUtils.substringBeforeLast(_templateName, "/") + "/": "")  + path;
	}

	@Override
	public void attach() {
		super.attach();
		JavaScript.eval(_bodyClassNames.stream().map(className -> "$('body').addClass('" + className + "');").collect(Collectors.joining()) + _scripts.stream().collect(Collectors.joining()));
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
