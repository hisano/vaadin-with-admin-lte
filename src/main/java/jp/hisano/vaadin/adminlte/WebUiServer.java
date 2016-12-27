package jp.hisano.vaadin.adminlte;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public final class WebUiServer {
	public static void main(String... args) throws Exception {
		WebAppContext context = new WebAppContext();
		ServletHandler handler = new ServletHandler();
		handler.addServletWithMapping(WebUi.Servlet.class, "/*");
		context.setServletHandler(handler);
		context.setResourceBase("web");

		Server server = new Server(8080);
		server.setHandler(context);
		server.start();
		server.join();
	}
}
