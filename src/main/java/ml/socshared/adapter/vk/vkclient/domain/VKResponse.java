package ml.socshared.adapter.vk.vkclient.domain;

public class VKResponse <ResponseType> {

    public VKResponse(ResponseType ok) {
        okValue = ok;
        error = null;
    }

    public VKResponse(Error fail) {
        okValue = null;
        error = fail;
    }


    public boolean isError() {
        return error != null;
    }

    public ResponseType getResponse() {
        return okValue;
    }
    public Error getError() {
        return error;
    }

    private ResponseType okValue;
    private Error error;
}
