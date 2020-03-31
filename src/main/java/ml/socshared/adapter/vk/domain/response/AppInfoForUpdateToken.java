package ml.socshared.adapter.vk.domain.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AppInfoForUpdateToken {
    private String accessToken;
    private UUID systemUserID;
}
