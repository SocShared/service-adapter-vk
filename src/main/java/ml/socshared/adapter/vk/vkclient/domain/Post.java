package ml.socshared.adapter.vk.vkclient.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import ml.socshared.adapter.vk.vkclient.domain.part.Comments;
import ml.socshared.adapter.vk.vkclient.domain.part.Likes;
import ml.socshared.adapter.vk.vkclient.domain.part.Reposts;
import ml.socshared.adapter.vk.vkclient.domain.part.Views;

@Data
public class Post {
    private int id;
    @SerializedName("from_id")
    private int fromId;
    @SerializedName("owner_id")
    private int ownerId;
    private int date;
    @SerializedName("marked_as_ads")
    private int markedAsAds;
    @SerializedName("post_type")
    private String postType;
    private String text;
    @SerializedName("can_pin")
    private int canPin;
    private Comments comments;
    private Likes likes;
    private Reposts reposts;
    private Views views;

}
