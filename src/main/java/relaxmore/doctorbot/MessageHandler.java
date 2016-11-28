package relaxmore.doctorbot;


import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@LineMessageHandler
public class MessageHandler {
	final Logger slf4jLogger = LoggerFactory.getLogger("com.linecorp.bot.client.wire");

	private final ReplyMessageHandler replyMessageHandler;
	
	public MessageHandler(ReplyMessageHandler replyMessageHandler) {
		super();
		this.replyMessageHandler = replyMessageHandler;
	}

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
		// TODO: add authentication
		slf4jLogger.info("event: " + event);
		BotApiResponse response = replyMessageHandler.reply(event);
		slf4jLogger.info("Sent messages: " + response);
	}
	
	@EventMapping
	public void defaultMessageEvent(Event event) {
		slf4jLogger.info("event: " + event);
	}
	
}
