package ml.socshared.adapter.vk.exception.impl;

public class VkAuthorizationException extends HttpBadRequestException
{
    public VkAuthorizationException(String msg) {
        super(msg);
    }
}
