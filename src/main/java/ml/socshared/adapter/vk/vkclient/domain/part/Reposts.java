package ml.socshared.adapter.vk.vkclient.domain.part;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Reposts {
    private int count;
    @SerializedName("user_reposted")
    private int userReposted;
}
