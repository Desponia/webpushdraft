package web.push.remote.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FcmMessageV1 {
    boolean validate_only;
    Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        Notification notification;
        String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        String title;
        String body;
        String image;
    }
}
