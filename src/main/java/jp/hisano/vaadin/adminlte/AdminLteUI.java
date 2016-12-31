package jp.hisano.vaadin.adminlte;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.thymeleaf.context.Context;

@Theme(Settings.THEME_NAME)
public final class AdminLteUI extends UI {
	@Override
	protected void init(VaadinRequest request) {
		switch (getPage().getLocation().getPath()) {
			case "/login":
				TemplateLayout content = new TemplateLayout("templates/example/login.html", new Context());

				setContent(content);

				TextField email = (TextField)content.getComponent("email");
				((Button)content.getComponent("submit")).addClickListener(e -> {
					Notification.show("Hello, " + email.getValue() + "!");
				});
				break;
			default:
				Navigator navigator = new Navigator(this, createContentArea());

				navigator.addView("", new TemplateView("index.html"));
				navigator.addView("index2", new TemplateView("index2.html"));

				navigator.addView(WidgetsView.VIEW_NAME, new WidgetsView());

				navigator.addView("chartjs", new TemplateView("pages/charts/chartjs.html"));
				navigator.addView("morris", new TemplateView("pages/charts/morris.html"));
				navigator.addView("flot", new TemplateView("pages/charts/flot.html"));
				navigator.addView("inline", new TemplateView("pages/charts/inline.html"));

				navigator.addView(InvoiceView.VIEW_NAME, new InvoiceView());
				break;
		}
	}

	private VerticalLayout createContentArea() {
		VerticalLayout contentArea = new VerticalLayout();
		contentArea.setSizeFull();

		TemplateLayout frameView = createFrameView();
		frameView.addComponent(contentArea, "contentArea");

		setContent(frameView);
		return contentArea;
	}

	private TemplateLayout createFrameView() {
		Context context = new Context();
		context.setVariable("useAsFrame", true);
		return new TemplateLayout("index.html", context);
	}
}
