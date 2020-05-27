package ml.socshared.adapter.vk.vkclient.domain.part;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class LikesOfComment {
    private Integer count;
    @SerializedName("user_counts")
    private Integer userLikes;
    @SerializedName("can_like")
    private Integer canLike;
    @SerializedName("can_publish")
    private Boolean canPublish;
}