package jp.hisano.vaadin.adminlte;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.beust.jcommander.internal.Lists;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.declarative.Design;

public class TemplateLayout extends CustomLayout {
	private static TemplateEngine _templateEngine;

	static {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("/VAADIN/themes/admin-lte/templates/");
		templateResolver.setSuffix(".html");

		_templateEngine = new TemplateEngine();
		_templateEngine.setTemplateResolver(templateResolver);
	}

	public TemplateLayout(String templateName, IContext context) {
		String template = _templateEngine.process(templateName, context);
		Document templateDocument = Jsoup.parse(template);
		List<Element> vaadinElements = replaceVaadinElements(templateDocument);
		template = templateDocument.html();

		setTemplateContents(template);
		for (Element vaadinElement : vaadinElements) {
			addComponent(Design.read(new ByteArrayInputStream(vaadinElement.toString().getBytes(StandardCharsets.UTF_8))), vaadinElement.attr("_id"));
		}
	}

	private List<Element> replaceVaadinElements(Document templateDocument) {
		List<Element> vaadinElements = Lists.newLinkedList();
		replaceVaadinElements(templateDocument.body(), vaadinElements);
		return vaadinElements;
	}

	private void replaceVaadinElements(Element parent, List<Element> vaadinElements) {
		for (Element child : parent.children()) {
			if (!child.tagName().startsWith("vaadin-")) {
				continue;
			}

			Element newChild = new Element(Tag.valueOf("div"), child.baseUri());
			newChild.attr("location", child.attr("_id"));

			child.before(newChild);
			child.remove();

			vaadinElements.add(child);
		}
	}
}
