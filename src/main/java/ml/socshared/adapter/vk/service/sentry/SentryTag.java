package ml.socshared.adapter.vk.service.sentry;

public enum SentryTag {
    GetUserInfo("type", "get_user_info"),
    CommentsOfPost("type", "get_comments_of_post"),
    CommentOfPost("type", "get_comment_of_post"),
    GetSubComments("type", "get_sub_comments"),
    GetSubCommentById("type", "get_sub_comment_by_id"),
    GetUserGroups("type", "get_user_groups"),
    GetUserGroup("type", "get_user_group"),
    SelectGroup("type", "select_group"),
    GetPosts("type", "get_posts"),
    GetPost("type", "get_post_by_id"),
    AddPost("type", "get_post_by_id"),
    UpdatePost("type", "update_post"),
    DeletePost("type", "delete_post");

    SentryTag(String t, String tag) {
        type = t;
        sentryTag = tag;
    }

    public String type() {
        return type;
    }
    public String value() {
        return sentryTag;
    }

    private String sentryTag;
    private String type;
    public static final String service_name = "VKS";

}
