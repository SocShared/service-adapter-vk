package ml.socshared.adapter.vk.vkclient.domain.part;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Likes {
    private int count;
    @SerializedName("user_counts")
    private int userLikes;
    @SerializedName("can_like")
    private int canLike;
    @SerializedName("can_publish")
    private boolean canPublish;
}
