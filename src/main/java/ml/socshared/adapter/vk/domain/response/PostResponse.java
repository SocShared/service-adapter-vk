package ml.socshared.adapter.vk.domain.response;


import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PostResponse {
    UUID systemUserId;
    String groupId;
    String postId;
    String message;
    String adapterId = "vk";
    int likesCount = 0;
    int repostsCount = 0;
    int viewsCount = 0;
    Date createdDate = new Date();
    Date updateDate;
}
