package ml.socshared.adapter.vk.vkclient.domain;

import lombok.Data;

@Data
public class User {
    private String id;
    private String first_name;
    private String last_name;
    private String deactivate;
    private boolean is_closed;
    private boolean can_access_closed;
}
