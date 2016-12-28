package jp.hisano.vaadin.adminlte;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.jsoup.nodes.Element;
import org.thymeleaf.context.Context;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.TextField;

final class WidgetsView extends CustomComponent implements View {
	static final String VIEW_NAME = "widgets";

	@Override
	public void enter(ViewChangeEvent event) {
		setSizeFull();

		TemplateLayout content = new TemplateLayout("widgets", new Context());

		setCompositionRoot(content);

		TextField message = (TextField) content.getComponent("message");
		Button send = (Button) content.getComponent("send");
		send.addClickListener(clickEvent -> {
			String text = message.getValue();
			message.setValue("");
			message.focus();

			Context context = new Context();
			context.setVariable("direct_chat_message_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("d MMM h:mm a").withLocale(Locale.US)).replace("AM", "am").replace("PM", "pm"));
			context.setVariable("direct_chat_message_text", text);

			Element textElement = TemplateLayout.getTemplateElement("widgets", context, "direct-chat-message-id");
			textElement.removeAttr("style");
			String escapedTextElement = textElement.html().replace("\n", "").replace("'", "\\'");

			JavaScript.eval("$('#direct-chat-messages-id').append('" + escapedTextElement + "');");
		});
	}
}
