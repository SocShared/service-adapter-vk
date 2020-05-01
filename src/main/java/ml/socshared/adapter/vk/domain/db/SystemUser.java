package ml.socshared.adapter.vk.domain.db;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "system_user")
@Data
public class SystemUser {
    @Id
    private UUID id;

    @Column(name="vk_user_id", unique=true)
    private String vkUserId;

    @Column(name="vk_group_id", unique=true)
    private String groupVkId;

    @Column(name="acess_token")
    private String accessToken;
}
