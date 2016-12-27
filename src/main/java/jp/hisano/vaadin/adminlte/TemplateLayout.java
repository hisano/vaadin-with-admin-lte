package jp.hisano.vaadin.adminlte;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.beust.jcommander.internal.Lists;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.declarative.DesignContext;

public class TemplateLayout extends CustomLayout {
	private static TemplateEngine _templateEngine;

	static {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("/VAADIN/themes/admin-lte/templates/");
		templateResolver.setSuffix(".html");

		_templateEngine = new TemplateEngine();
		_templateEngine.setTemplateResolver(templateResolver);
	}

	private final List<DesignContext> _designContexts = Lists.newLinkedList();

	public TemplateLayout(String templateName, IContext context) {
		String template = _templateEngine.process(templateName, context);
		Document templateDocument = Jsoup.parse(template);
		List<Element> vaadinElements = replaceVaadinElements(templateDocument);
		template = templateDocument.html();

		setTemplateContents(template);
		for (Element vaadinElement : vaadinElements) {
			DesignContext designContext = Design.read(new ByteArrayInputStream(vaadinElement.toString().getBytes(StandardCharsets.UTF_8)), null);
			_designContexts.add(designContext);
			addComponent(designContext.getRootComponent(), vaadinElement.attr("location"));
		}
		setSizeFull();
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
