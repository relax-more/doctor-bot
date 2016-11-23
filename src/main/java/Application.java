import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static java.util.Collections.singletonList;

/**
 * Created by tsuchiyayuusuke on 2016/11/23.
 */
@SpringBootApplication
@LineMessageHandler
public class Application {

    @Autowired
    private LineMessagingClient lineMessagingClient;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        System.out.println("event: " + event);
        final BotApiResponse apiResponse = lineMessagingClient
                .replyMessage(new ReplyMessage(event.getReplyToken(),
                        singletonList(new TextMessage(event.getMessage().getText()))))
                .get();
        System.out.println("Sent messages: " + apiResponse);
    }

    @EventMapping
    public void defaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }
}

