package ml.socshared.adapter.vk.domain.response;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import ml.socshared.adapter.vk.config.CustomLocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PostResponse {
   private UUID systemUserId;
   private String userId; //Id пользователя, который сделал пост
   private  String groupId;
   private  String postId;
   private  String message;
   private  String adapterId = "vk";
   private  Integer commentsCount;
   private  Integer likesCount = 0;
   private  Integer repostsCount = 0;
   private  Integer viewsCount = 0;

   @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
   private LocalDateTime createdDate;
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    private LocalDateTime updateDate;
}
