package relaxmore.doctorbot.validate;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class Validator {
    public boolean sigunature() throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        String channelSecret = System.getenv("LINE_BOT_CHANNEL_SECRET");
        String httpRequestBody = "";
        SecretKeySpec key = new SecretKeySpec(channelSecret.getBytes(), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        byte[] source = httpRequestBody.getBytes("UTF-8");
        String signature = Base64.encodeBase64String(mac.doFinal(source));
        // Compare X-Line-Signature request header string and the signature
        // TODO:
        return true;
    }
}
