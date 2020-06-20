package ml.socshared.adapter.vk.domain.response;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ml.socshared.adapter.vk.config.CustomLocalDateTimeSerializer;

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
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime createdDate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    LocalDateTime updateDate;
}
