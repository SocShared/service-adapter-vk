package ml.socshared.adapter.vk.domain.response;


import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PostResponse {
    UUID systemUserId;
    String userId; //Id пользователя, который сделал пост
    String groupId;
    String postId;
    String message;
    String adapterId = "vk";
    Integer commentsCount;
    Integer likesCount = 0;
    Integer repostsCount = 0;
    Integer viewsCount = 0;
    LocalDateTime createdDate;
    LocalDateTime updateDate;
}
