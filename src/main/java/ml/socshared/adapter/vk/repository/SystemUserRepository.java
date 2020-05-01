package ml.socshared.adapter.vk.repository;

import ml.socshared.adapter.vk.domain.db.SystemUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SystemUserRepository extends CrudRepository<SystemUser, UUID> {
    SystemUser findByVkUserId(Integer vkId);
    SystemUser findByGroupVkId(String id);
}
