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
						addStyleSheetInThemeDirectory(response, "bootstrap/css/bootstrap.min.css");
						addStyleSheet(response, "https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css");
						addStyleSheet(response, "https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css");
						addStyleSheetInThemeDirectory(response, "dist/css/AdminLTE.css");
						addStyleSheetInThemeDirectory(response, "dist/css/skins/_all-skins.min.css");
						addStyleSheetInThemeDirectory(response, "plugins/iCheck/square/blue.css");

						addScriptInThemeDirectory(response, "plugins/jQuery/jquery-2.2.3.min.js");
						addScriptInThemeDirectory(response, "bootstrap/js/bootstrap.min.js");
						addScriptInThemeDirectory(response, "plugins/iCheck/icheck.min.js");
					}

					private void addScriptInThemeDirectory(BootstrapPageResponse response, String relativePath) {
						response.getDocument().head().append("<script src=\"" + Settings.getThemeDirectoryPath() + relativePath + "\"/>");
					}

					private void addStyleSheetInThemeDirectory(BootstrapPageResponse response, String relativePath) {
						addStyleSheet(response, Settings.getThemeDirectoryPath() + relativePath);
					}

					private void addStyleSheet(BootstrapPageResponse response, String path) {
						response.getDocument().head().append("<link rel=\"stylesheet\" href=\"" + path + "\"/>");
					}

					@Override
					public void modifyBootstrapFragment(BootstrapFragmentResponse response) {
					}
				});
			}
		});
	}
}