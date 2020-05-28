//package ml.socshared.adapter.vk.service.impl;
//
//class VkCommentServiceImplTest {
//
//
////    VKClient client = Mockito.mock(VKClient.class);
////    VkAuthorizationService auth = Mockito.mock(VkAuthorizationService.class);
////    VkCommentServiceImpl service = new VkCommentServiceImpl(auth, client);
////
////
////
////
////    @Test
////    void getCommentsOfPost() {
////    }
////
//
////    @Test
////    void getCommentOfPost() throws VKClientException {
////        final String vkGroupId = "465465465";
////        final UUID sUserId = UUID.randomUUID();
////        final String accessToken = "sdfsdf5745sad4f654sdf";
////        final int vkCommentId = 19;
////        final int vkPostId = 1000;
////        VkComment comment = new VkComment();
////        comment.setCanEdit(1);
////        comment.setDate(0);
////        comment.setFromId(vkPostId);
////        comment.setId(vkCommentId);
////        Likes likes = new Likes();
////        likes.setCanLike(1);
//
////        likes.setCanPublish(1);
//
////        likes.setCount(51);
////        likes.setUserLikes(1);
////        comment.setLikes(likes);
////        comment.setText("Test comment text");
////        CommentThread thread = new CommentThread();
////        thread.setCount(5);
////        comment.setThread(thread);
////        comment.setPostId(vkPostId);
////
////        Mockito.doReturn(comment)
////                .when(client)
////                .getCommentById(ArgumentMatchers.anyString(),
////                        ArgumentMatchers.anyString(),
////                        ArgumentMatchers.anyInt(),
////                        ArgumentMatchers.anyInt());
////
////        SystemUser sUser = new SystemUser();
////        sUser.setGroupVkId(vkGroupId);
////        sUser.setAccessToken(accessToken);
////        sUser.setId(sUserId);
////
////        Mockito.doReturn(sUser)
////                .when(auth)
////                .getUser(ArgumentMatchers.eq(sUserId));
////        CommentResponse result = service.getCommentOfPost(sUserId, vkGroupId, String.valueOf(vkPostId), String.valueOf(vkCommentId), 0, 1);
////
////        Assertions.assertEquals(Integer.parseInt(result.getCommentId()), comment.getId());
////    }
//
////
////    @Test
////    void getCommentsOfSuperComment() {
////    }
////
////    @Test
////    void getCommentOfSuperComment() {
////    }
//
//}