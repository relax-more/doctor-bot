package relaxmore.doctorbot;

import com.google.common.base.Strings;
import com.linecorp.bot.client.LineMessagingService;
import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.spring.boot.LineBotProperties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.net.InetSocketAddress;
import java.net.Proxy;

@Slf4j
@SpringBootApplication
public class LineBotApplication {
    public static void main(String[] args) {
        SpringApplication.run(LineBotApplication.class, args);
    }

    @Autowired
    private LineBotProperties lineBotProperties;

    /**
     * @return
     * @see https://github.com/line/line-bot-sdk-java/blob/9459fef17aa1add2c59c7fb07a397d87babc82fd/line-bot-api-client/src/main/java/com/linecorp/bot/client/LineMessagingServiceBuilder.java#L134
     */
    @Bean
    public OkHttpClient.Builder okHttpClientBuilder() {
        String proxyUrl = System.getenv("LINE_OUTBOUND_URL");
        String proxyPort = System.getenv("LINE_OUTBOUND_PORT");
        // TODO: Set env for local runner
        if (Strings.isNullOrEmpty(proxyUrl) || Strings.isNullOrEmpty(proxyPort)){

            proxyUrl = "http://localhost.com";
            proxyPort = "80";
        }

        log.info("proxyUrl:proxyPort=" + proxyUrl + ":" + proxyPort);

        return new OkHttpClient.Builder().proxy(
                new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyUrl, Integer.valueOf(proxyPort))));
    }

    @Bean
    public LineMessagingService lineMessagingService(OkHttpClient.Builder okHttpClientBuilder) {
        return LineMessagingServiceBuilder
                .create(lineBotProperties.getChannelToken())
                .apiEndPoint(lineBotProperties.getApiEndPoint())
                .connectTimeout(lineBotProperties.getConnectTimeout())
                .readTimeout(lineBotProperties.getReadTimeout())
                .writeTimeout(lineBotProperties.getWriteTimeout())
                .okHttpClientBuilder(okHttpClientBuilder)
                .build();
    }
}
