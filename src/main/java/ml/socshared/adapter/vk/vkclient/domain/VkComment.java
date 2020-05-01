package ml.socshared.adapter.vk.vkclient.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import ml.socshared.adapter.vk.vkclient.domain.part.CommentThread;
import ml.socshared.adapter.vk.vkclient.domain.part.Likes;

import java.util.List;

@Data
public class VkComment {
    private int id;
    @SerializedName("from_id")
    private int fromId;
    @SerializedName("post_id")
    private int postId;
    @SerializedName("parents_stack")
    private List<Integer> parentsStack;
    @SerializedName("can_edit")
    private int canEdit;
    private long date;
    private String text;
    private Likes likes;
    private CommentThread thread;
}
