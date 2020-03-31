package ml.socshared.adapter.vk.vkclient.domain;

public enum GroupType {
    PAGE("page"),
    GROUP("group"),
    EVENT("event");

    private GroupType(String type) {
        _type = type;
    }

    public static GroupType fromString(String text) {
        for (GroupType t : GroupType.values()) {
            if (t._type.equalsIgnoreCase(text)) {
                return t;
            }
        }
        return null;
    }
    public String type() {
        return this._type;
    }
    private String _type;
}
