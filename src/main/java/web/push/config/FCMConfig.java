package web.push.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.List;

@Slf4j
@Configuration
public class FCMConfig {
    private static final String FB_SDK_KEY_PATH = "static/spring-webpush-firebase-adminsdk-jb4so-32fffcdc48.json";

    @Bean
    public FirebaseApp initial() throws IOException {
//        log.info("getInput : {}", new ClassPathResource(FB_SDK_KEY_PATH));
        GoogleCredentials googleCredentials = GoogleCredentials.fromStream(new ClassPathResource(FB_SDK_KEY_PATH).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
        googleCredentials.refreshIfExpired();
        var options = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
        return FirebaseApp.initializeApp(options, "quispush");
    }

    @Bean
    public ListeningExecutorService firebaseExecutor() {
        return MoreExecutors.newDirectExecutorService();
    }
}
