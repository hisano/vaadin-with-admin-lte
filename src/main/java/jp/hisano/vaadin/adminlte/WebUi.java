package jp.hisano.vaadin.adminlte;

import javax.servlet.annotation.WebServlet;

import org.thymeleaf.context.Context;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@Theme("admin-lte")
public class WebUi extends UI {
	@Override
	protected void init(VaadinRequest request) {
		TemplateLayout content = new TemplateLayout("index", new Context());

		TextField name = (TextField) content.getComponent("name");
		name.setValue("Hisano");

		setContent(content);
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = WebUi.class)
	public static class Servlet extends VaadinServlet {
	}
}
