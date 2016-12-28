package jp.hisano.vaadin.adminlte;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("admin-lte")
public final class AdminLteUI extends UI {
	@Override
	protected void init(VaadinRequest request) {
		Navigator navigator = new Navigator(this, this);
		navigator.addView("", new LoginView());
	}
}
