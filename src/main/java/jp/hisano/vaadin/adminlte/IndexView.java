package jp.hisano.vaadin.adminlte;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import org.thymeleaf.context.Context;

final class IndexView extends CustomComponent implements View {
	static final String VIEW_NAME = "index";

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();

		TemplateLayout content = new TemplateLayout("../index", new Context());

		setCompositionRoot(content);
	}
}
