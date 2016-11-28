package relaxmore.doctorbot;


import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Slf4j
@LineMessageHandler
public class MessageHandler {

	private final ReplyMessageHandler replyMessageHandler;
	
	public MessageHandler(ReplyMessageHandler replyMessageHandler) {
		super();
		this.replyMessageHandler = replyMessageHandler;
	}

	@EventMapping
	public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
		// TODO: add authentication
		log.info("event: " + event);
		log.info("userId: " + event.getSource().getUserId());
		BotApiResponse response = replyMessageHandler.check1WhenStart(event);
		log.info("Sent messages: " + response);
	}

	@EventMapping
	public void handleTextMessageEvent(PostbackEvent event) throws IOException {
		// TODO: add authentication
		log.info("event: " + event);
		log.info("userId: " + event.getSource().getUserId());
		BotApiResponse response = replyMessageHandler.check2WhatHappen(event);
		log.info("Sent messages: " + response);
	}

	@EventMapping
	public void defaultMessageEvent(Event event) {
		log.info("event: " + event);
	}
	
}
