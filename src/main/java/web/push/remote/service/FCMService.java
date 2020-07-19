package web.push.remote.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import web.push.remote.dto.FcmMessageV1;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FCMService {
    private static final String FB_SDK_KEY_PATH = "spring-webpush-firebase-adminsdk-jb4so-32fffcdc48.json";
    private static final String API_URL = "https://fcm.googleapis.com/v1/projects/spring-webpush/message:send";
    private static final String MEDIA_TYPE_JSON = "application/json";
    //    private static final String CHARSET_UTF_8 = "UTF-8";
    private static final String MESSAGE_ENCODING_UTF_8 = "UTF-8";
    private static final String MESSAGE_HEADER_CONTTYPE = String.format("%s; charset=%s", MEDIA_TYPE_JSON, MESSAGE_ENCODING_UTF_8.toLowerCase());

    private final ObjectMapper objectMapper;

    public void sendMessageTo(String targetToken, String title, String body) throws IOException {
        String message = makeMessage(targetToken, title, body);
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get(MESSAGE_HEADER_CONTTYPE));
        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, MESSAGE_HEADER_CONTTYPE)
                .build();
        Response response = client.newCall(request).execute();
        log.info("response ::: {} ", response.body().string());
    }

    private String makeMessage(String targetToken, String title, String body) throws JsonProcessingException {
        FcmMessageV1 fcmMessageV1 = FcmMessageV1.builder()
                .message(FcmMessageV1.Message.builder()
                        .token(targetToken)
                        .notification(FcmMessageV1.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build())
                        .build())
                .validate_only(false)
                .build();
        return objectMapper.writeValueAsString(fcmMessageV1);
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(FB_SDK_KEY_PATH).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
//        var options = FirebaseOptions
//                .builder()
//                .setCredentials(googleCredentials)
//                .build();
        return googleCredentials.getAccessToken().getTokenValue();
//        return FirebaseApp.initializeApp(options, "quispush");
    }
}
