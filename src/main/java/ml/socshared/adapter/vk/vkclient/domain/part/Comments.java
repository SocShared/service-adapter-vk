package ml.socshared.adapter.vk.vkclient.domain.part;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Comments {
    private int count;
    @SerializedName("user_likes")
    private int userLikes;
    @SerializedName("can_like")
    private int canLike;
    @SerializedName("can_publish")
    private int canPublish;
}
