package ml.socshared.adapter.vk.domain.response;

import lombok.Data;

import java.util.UUID;

@Data
public class SocUserInfoResponse {
    private UUID systemUserId;
    private String accountId;
    private String firstName;
    private String lastName;
    final private String email = "";
    final private String socialNetwork = "VK";
}
