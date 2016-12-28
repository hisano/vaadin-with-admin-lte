package jp.hisano.vaadin.adminlte;

import org.thymeleaf.context.Context;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;

final class InvoiceView extends CustomComponent implements View {
	static final String VIEW_NAME = "invoice";

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();

		TemplateLayout content = new TemplateLayout("example/invoice", new Context());

		setCompositionRoot(content);

		Label total = (Label) content.getComponent("total");
		total.setValue("$265.245");
	}
}
