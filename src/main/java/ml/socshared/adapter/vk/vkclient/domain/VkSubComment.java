package ml.socshared.adapter.vk.vkclient.domain;

import com.google.gson.annotations.SerializedName;

public class VkSubComment extends VkComment {
    @SerializedName("reply_to_user")
    int replyToUserId;
    @SerializedName("reply_to_comment")
    int replyToCommentId;
}
