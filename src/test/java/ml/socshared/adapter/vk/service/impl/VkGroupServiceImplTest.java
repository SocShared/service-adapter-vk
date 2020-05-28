//package ml.socshared.adapter.vk.service.impl;
//
//import ml.socshared.adapter.vk.domain.db.SystemUser;
//import ml.socshared.adapter.vk.domain.response.GroupResponse;
//import ml.socshared.adapter.vk.domain.response.Page;
//import ml.socshared.adapter.vk.exception.impl.HttpBadRequestException;
//import ml.socshared.adapter.vk.service.ApplicationService;
//import ml.socshared.adapter.vk.service.VkAuthorizationService;
//import ml.socshared.adapter.vk.service.VkGroupService;
//import ml.socshared.adapter.vk.vkclient.VKClient;
//import ml.socshared.adapter.vk.vkclient.domain.GroupType;
//import ml.socshared.adapter.vk.vkclient.domain.Paginator;
//import ml.socshared.adapter.vk.vkclient.domain.VkGroup;
//import ml.socshared.adapter.vk.vkclient.exception.VKClientException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mockito;
//
//import java.util.Arrays;
//import java.util.UUID;
//
//class VkGroupServiceImplTest {
//
////    VkAuthorizationService auth = Mockito.mock(VkAuthorizationService.class);
////    VKClient client = Mockito.mock(VKClient.class);
////    ApplicationService app = Mockito.mock(ApplicationService.class);
////
////    VkGroupService service = new VkGroupServiceImpl(auth, client, app);
////
////    @Test
////    void getUserGroupsInvalidPaginationOfPage() {
////        Assertions.assertThrows(HttpBadRequestException.class, ()->service.getUserGroups(
////                UUID.randomUUID(), -10, 20
////        ));
////    }
////
////    @Test
////    void getUserGroupsInvalidPaginationOfSize() {
////        Assertions.assertThrows(HttpBadRequestException.class, ()->service.getUserGroups(
////                UUID.randomUUID(), 10, 250
////        ));
////    }
////
////    @Test
////    void getUserGroups() throws VKClientException {
////        Paginator<VkGroup> clientReturn = new Paginator<>();
////        VkGroup g1 = new VkGroup();
////        g1.setName("g1");
////        g1.setId("-1354313");
////        g1.setDescription("g1 group for test");
////        g1.setType(GroupType.PAGE);
////
////        VkGroup g2 = new VkGroup();
////        g2.setName("g1");
////        g2.setId("-112122");
////        g2.setDescription("g2 group for test");
////        g2.setType(GroupType.EVENT);
////
////        SystemUser user = new SystemUser();
////        user.setId(UUID.randomUUID());
////        user.setAccessToken("16546512e1ds3a1d");
////        user.setGroupVkId("465465");
////
////        clientReturn.setResponse(Arrays.asList(g1, g2));
////        Mockito.doReturn(clientReturn)
////                .when(client)
////                .getGroupsInfo(ArgumentMatchers.any(String.class), ArgumentMatchers.any(),
////                               ArgumentMatchers.any(), ArgumentMatchers.any(),
////                               ArgumentMatchers.any());
////        Mockito.doReturn(user)
////                .when(auth)
////                .getUser(user.getId());
////        Page<GroupResponse> result = service.getUserGroups(user.getId(), 0, 10);
////        Assertions.assertEquals(result.getPage(), 0);
////        Assertions.assertEquals(result.getSize(), 2);
////    }
//}