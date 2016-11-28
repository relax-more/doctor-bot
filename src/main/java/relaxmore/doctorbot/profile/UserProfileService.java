package relaxmore.doctorbot.profile;

import com.linecorp.bot.client.LineMessagingServiceBuilder;
import com.linecorp.bot.model.profile.UserProfileResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import retrofit2.Response;

import java.io.IOException;

@Slf4j
@Service
public class UserProfileService {
    public UserProfileResponse getProfie(){
        Response<UserProfileResponse> response = null;
        try {
            response = LineMessagingServiceBuilder
                    .create(System.getenv("LINE_BOT_CHANNEL_TOKEN"))
                    .build()
                    .getProfile("<userId>")
                    .execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            log.error("failed to get profile.", e);
            // TODO: return error message to user
        }
        log.error(response.code() + " " + response.message());
        // TODO: return error message to user
        throw new RuntimeException();
    }
}
