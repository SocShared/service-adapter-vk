package ml.socshared.adapter.vk.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"subCommentCount"})
public class SubCommentResponse extends CommentResponse {

    String superCommentId;

    public SubCommentResponse(CommentResponse comment) {
        super(comment);
        superCommentId = null;
    }


}
