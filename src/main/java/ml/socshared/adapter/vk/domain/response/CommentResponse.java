package ml.socshared.adapter.vk.domain.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class CommentResponse {
    private UUID systemUserId;
    private String groupId;
    private String postId;
    private String commentId;
    private String message;
    private int likesCount;
    private int subCommentCount;
    private Date createdDate;
    private Date updateDate;

    public CommentResponse(){
        systemUserId = null;
        groupId = null;
        postId = null;
        commentId = null;
        message = null;
        likesCount = 0;
        subCommentCount = 0;
        createdDate = null;
        updateDate = null;
    }
    public CommentResponse(CommentResponse comment) {
        systemUserId = comment.systemUserId;
        groupId = comment.groupId;
        postId = comment.postId;
        commentId = comment.commentId;
        message = comment.message;
        likesCount = comment.likesCount;
        subCommentCount = comment.subCommentCount;
        createdDate = comment.createdDate;
        updateDate = comment.updateDate;
    }
}
