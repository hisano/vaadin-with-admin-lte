package jp.hisano.vaadin.adminlte;

import javax.servlet.annotation.WebServlet;

import org.thymeleaf.context.Context;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;

@Theme("admin-lte")
public class WebUi extends UI {
	@Override
	protected void init(VaadinRequest request) {
		setContent(new TemplateLayout("index", new Context()));

		// VerticalLayout layout = new VerticalLayout();
		// layout.setMargin(true);
		// layout.setSpacing(true);
		//
		// TextField text = new TextField("Name");
		// layout.addComponent(text);
		//
		// Button button = new Button("Hello");
		// layout.addComponent(button);
		//
		// setContent(layout);
		// setContent(new TextField("Name"));
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = WebUi.class)
	public static class Servlet extends VaadinServlet {
	}
}
