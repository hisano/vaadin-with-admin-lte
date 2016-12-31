package jp.hisano.vaadin.adminlte;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import org.thymeleaf.context.Context;

final class TemplateView extends CustomComponent implements View {
	private final String _path;

	TemplateView(String path) {
		_path = path;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();

		TemplateLayout content = new TemplateLayout(_path, "contentArea", new Context());

		setCompositionRoot(content);
	}
}
