package jp.hisano.vaadin.adminlte;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import org.thymeleaf.context.Context;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@Theme("admin-lte")
public class WebUi extends UI {
	@Override
	protected void init(VaadinRequest request) {
		TemplateLayout content = new TemplateLayout("login", new Context());

		setContent(content);

		TextField email = (TextField)content.getComponent("email");
		email.setValue("hisano@gmail.com");
		((Button)content.getComponent("submit")).addClickListener(e -> {
			Notification.show("Hello, " + email.getValue());
		});
	}

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = WebUi.class)
	public static class Servlet extends VaadinServlet {
		@Override
		protected void servletInitialized() throws ServletException {
			super.servletInitialized();
			getService().addSessionInitListener(new SessionInitListener() {
				@Override
				public void sessionInit(SessionInitEvent event) {
					event.getSession().addBootstrapListener(new BootstrapListener() {
						@Override
						public void modifyBootstrapPage(BootstrapPageResponse response) {
							String path = "VAADIN/themes/admin-lte";
							response.getDocument().head().append("<script src=\"" + path + "/plugins/jQuery/jquery-2.2.3.min.js\"/>");
							response.getDocument().head().append("<script src=\"" + path + "/bootstrap/js/bootstrap.min.js\"/>");
							response.getDocument().head().append("<script src=\"" + path + "/plugins/iCheck/icheck.min.js\"/>");
						}

						@Override
						public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
						}
					});
				}
			});
		}
	}
}
