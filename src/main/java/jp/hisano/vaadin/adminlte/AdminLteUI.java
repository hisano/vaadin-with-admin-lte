package jp.hisano.vaadin.adminlte;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme(Settings.THEME_NAME)
public final class AdminLteUI extends UI {
	@Override
	protected void init(VaadinRequest request) {
		Navigator navigator = new Navigator(this, this);

		navigator.addView("", new ChartJsView());

		navigator.addView(ChartJsView.VIEW_NAME, new ChartJsView());
		navigator.addView(InvoiceView.VIEW_NAME, new InvoiceView());
		navigator.addView(LoginView.VIEW_NAME, new LoginView());
	}
}
