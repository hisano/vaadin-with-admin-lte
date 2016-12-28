package jp.hisano.vaadin.adminlte;

import org.thymeleaf.context.Context;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

@Theme("admin-lte")
public final class AdminLteUI extends UI {
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
}
