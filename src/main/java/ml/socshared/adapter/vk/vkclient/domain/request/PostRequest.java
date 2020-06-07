package ml.socshared.adapter.vk.vkclient.domain.request;

import lombok.Data;

@Data
public class PostRequest {
    String owner_id;
    String from_group = "1";
    String message;
    String close_comments = "0";
    String v = "103.5";
    String access_token;
}
