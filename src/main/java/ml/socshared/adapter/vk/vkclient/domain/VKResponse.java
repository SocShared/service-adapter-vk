package ml.socshared.adapter.vk.vkclient.domain;

public class VKResponse <ResponseType> {

    public VKResponse(ResponseType ok) {
        okValue = ok;
        errorType = null;
    }

    public VKResponse(ErrorType fail) {
        okValue = null;
        errorType = fail;
    }
    public VKResponse() {}

   public void setResponse(ResponseType response) {
        okValue = response;
    }

    public boolean isError() {
        return errorType != null;
    }


    public ResponseType getResponse() {
        return okValue;
    }
    public ErrorType getError() {
        return errorType;
    }

    private ResponseType okValue = null;
    private ErrorType errorType = null;
}
