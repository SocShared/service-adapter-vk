package ml.socshared.adapter.vk.domain.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.UUID;

@Data
public class OkAddApp {
    @SerializedName(value = "userSystemID")
    private UUID userID;
    private UUID appID;
}
