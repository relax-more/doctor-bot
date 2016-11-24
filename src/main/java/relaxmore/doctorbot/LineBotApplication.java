package relaxmore.doctorbot;

import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.net.Proxy;

@SpringBootApplication
public class LineBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(LineBotApplication.class, args);
    }

    /**
     * @return
     * @see https://github.com/line/line-bot-sdk-java/blob/9459fef17aa1add2c59c7fb07a397d87babc82fd/line-bot-api-client/src/main/java/com/linecorp/bot/client/LineMessagingServiceBuilder.java#L134
     */
    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {
        String lineOutBoundUrl = System.getProperty("LINE_OUTBOUND_URL");
        int lineOutBoundPort = Integer.valueOf(System.getProperty("LINE_OUTBOUND_PORT"));

        return new OkHttpClient.Builder().proxy(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress(lineOutBoundUrl, lineOutBoundPort)));
    }
}
