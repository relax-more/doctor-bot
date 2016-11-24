package relaxmore.doctorbot;

import com.google.common.collect.ImmutableList;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ReplyMessageHandler {
	
	private final LineMessagingService lineMessagingService;
	
	public ReplyMessageHandler(LineMessagingService lineMessagingService) {
		super();
		this.lineMessagingService = lineMessagingService;
	}

	public BotApiResponse reply(MessageEvent<TextMessageContent> event) throws IOException {
		
		String replyToken = event.getReplyToken();
		List<Message> messages = ImmutableList.of(new TextMessage("reply test"));
		return lineMessagingService
			.replyMessage(new ReplyMessage(replyToken, messages))
			.execute()
			.body();
		
	}
	
}
