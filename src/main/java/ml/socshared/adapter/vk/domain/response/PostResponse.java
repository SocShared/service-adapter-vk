package ml.socshared.adapter.vk.domain.response;


import lombok.Data;

import java.util.Date;
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
    Date createdDate = new Date();//TODO поменять на LocalDateTime
    Date updateDate;
}
