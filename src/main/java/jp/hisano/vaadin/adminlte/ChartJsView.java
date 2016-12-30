package jp.hisano.vaadin.adminlte;

import org.thymeleaf.context.Context;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;

final class ChartJsView extends CustomComponent implements View {
	static final String VIEW_NAME = "chartjs";

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();

		TemplateLayout content = new TemplateLayout("charts/chartjs", "contentArea", new Context());

		setCompositionRoot(content);
	}
}
