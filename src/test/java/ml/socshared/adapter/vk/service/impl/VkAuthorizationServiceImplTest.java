//package ml.socshared.adapter.vk.service.impl;
//
//import ml.socshared.adapter.vk.domain.db.SystemUser;
//import ml.socshared.adapter.vk.service.ApplicationService;
//import ml.socshared.adapter.vk.service.VkAuthorizationService;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import java.util.UUID;
//
//import static org.mockito.Mockito.mock;
//
//class VkAuthorizationServiceImplTest {
//
//    ApplicationService appService = mock(ApplicationService.class);
//
//    VkAuthorizationService authService = new VkAuthorizationServiceImpl(appService);
////
////    @Test
////   public void getUserSuccessful() {
////        UUID testUUID = UUID.randomUUID();
////        SystemUser corretUser = new SystemUser();
////        corretUser.setId(testUUID);
////        Mockito.doReturn(corretUser)
////                .when(appService)
////                    .getUser(testUUID);
////        SystemUser user = authService.getUser(testUUID);
////        Assertions.assertEquals(user, corretUser);
////    }
//
//}