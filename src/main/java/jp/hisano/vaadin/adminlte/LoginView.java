package jp.hisano.vaadin.adminlte;

import org.thymeleaf.context.Context;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;

final class LoginView extends CustomComponent implements View {
	static final String VIEW_NAME = "login";

	@Override
	public void enter(ViewChangeEvent event) {
		TemplateLayout content = new TemplateLayout("example/login", new Context());

		setCompositionRoot(content);

		TextField email = (TextField)content.getComponent("email");
		email.setValue("hisano@gmail.com");
		((Button)content.getComponent("submit")).addClickListener(e -> {
			Notification.show("Hello, " + email.getValue());
		});
	}
}
