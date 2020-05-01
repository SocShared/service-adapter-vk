package ml.socshared.adapter.vk.vkclient.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class ErrorType {
    public ErrorType() {}
    public ErrorType(ErrorCode error) {
        errorCode = error;
    }
    @SerializedName("error_code")
    private ErrorCode errorCode;

    @SerializedName("error_msg")
    private String errorMsg;

    @SerializedName("request_params")
    private List<RequestParams> requestParams;

}
