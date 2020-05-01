package ml.socshared.adapter.vk.domain.response;

import lombok.Data;
import ml.socshared.adapter.vk.vkclient.domain.GroupType;

import java.util.Date;
import java.util.UUID;

@Data
public class GroupResponse {
    UUID systemUserId;
    String groupId;
    String name;
    String adapterID = "vk";
    boolean isSelected;//TODO добавление поля в GateWay - путем запроса к хранилищу
    int membersCount;
    GroupType type;
    Date createData;
}
