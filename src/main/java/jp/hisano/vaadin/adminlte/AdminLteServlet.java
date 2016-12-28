package jp.hisano.vaadin.adminlte;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.BootstrapFragmentResponse;
import com.vaadin.server.BootstrapListener;
import com.vaadin.server.BootstrapPageResponse;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.VaadinServlet;

@WebServlet(value = "/*", asyncSupported = true)
@VaadinServletConfiguration(productionMode = true, ui = AdminLteUI.class)
public final class AdminLteServlet extends VaadinServlet {
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
						response.getDocument().head().append("<link rel=\"stylesheet\" href=\"" + path + "/bootstrap/css/bootstrap.min.css\"/>");
						response.getDocument().head().append("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css\"/>");
						response.getDocument().head().append("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css\"/>");
						response.getDocument().head().append("<link rel=\"stylesheet\" href=\"" + path + "/dist/css/AdminLTE.css\"/>");
						response.getDocument().head().append("<link rel=\"stylesheet\" href=\"" + path + "/plugins/iCheck/square/blue.css\"/>");

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