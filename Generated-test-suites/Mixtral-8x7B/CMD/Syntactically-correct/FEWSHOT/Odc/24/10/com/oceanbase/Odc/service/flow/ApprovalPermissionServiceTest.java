import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.common.lang.Pair;
import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateRepository;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

public class ApprovalPermissionServiceTest {

private ApprovalPermissionService approvalPermissionService;

@Before
public void setup() {
UserService userService = mock(UserService.class);
ResourceRoleService resourceRoleService = mock(ResourceRoleService.class);
AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
UserRepository userRepository = mock(UserRepository.class);
RoleRepository roleRepository = mock(RoleRepository.class);
UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);

approvalPermissionService = new ApprovalPermissionService();
approvalPermissionService.userService = userService;
approvalPermissionService.resourceRoleService = resourceRoleService;
approvalPermissionService.authenticationFacade = authenticationFacade;
approvalPermissionService.userTaskCandidateRepository = userTaskCandidateRepository;
approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
approvalPermissionService.userRepository = userRepository;
approvalPermissionService.roleRepository = roleRepository;
approvalPermissionService.userRoleRepository = userRoleRepository;
approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;

doReturn(new ArrayList<Long>()).when(userService).getCurrentUserRoleIds();
doReturn(new ArrayList<String>()).when(resourceRoleService).getResourceRoleIdentifiersByUserId(anyLong());
doReturn(1L).when(authenticationFacade).currentUserId();
doReturn(new UserTaskInstanceEntity()).when(userTaskInstanceRepository).findByCandidateUserId(anyLong());
 }

@Test
public void testGetCandidates() {
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskCandidateRepository).when(approvalPermissionServiceSpy).userTaskCandidateRepository;

UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
entity1.setRoleId(1L);

UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
entity2.setUserId(1L);

UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
entity3.setRoleId(2L);

UserTaskInstanceCandidateEntity entity4 = new UserTaskInstanceCandidateEntity();
entity4.setUserId(2L);

UserTaskInstanceCandidateEntity entity5 = new UserTaskInstanceCandidateEntity();
entity5.setResourceRoleIdentifier("roleIdentifier1");

UserTaskInstanceCandidateEntity entity6 = new UserTaskInstanceCandidateEntity();
entity6.setResourceRoleIdentifier("roleIdentifier2");

doReturn(Arrays.asList(entity1, entity2)).when(userTaskCandidateRepository)
.findByApprovalInstanceId(anyLong());

Set<User> result = approvalPermissionServiceSpy.getCandidates(1L, true);
assertEquals(2, result.size());
assertTrue(result.stream().anyMatch(user -> user.getId().equals(1L)));
assertTrue(result.stream().anyMatch(user -> user.getId().equals(2L)));
}

@Test
public void testGetInstanceId2CandidateRoleIds() {
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskCandidateRepository).when(approvalPermissionServiceSpy).userTaskCandidateRepository;

UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
entity1.setRoleId(1L);

UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
entity2.setRoleId(2L);

doReturn(Arrays.asList(entity1, entity2)).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(anyCollection());

Map<Long, Set<Long>> result = approvalPermissionServiceSpy.getInstanceId2CandidateRoleIds(
Arrays.asList(1L, 2L));
assertEquals(2, result.size());
assertEquals(1L, (long) result.get(1L).iterator().next());
assertEquals(2L, (long) result.get(2L).iterator().next());
}

@Test
public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskCandidateRepository).when(approvalPermissionServiceSpy).userTaskCandidateRepository;

UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
entity1.setResourceRoleIdentifier("roleIdentifier1");

UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
entity2.setResourceRoleIdentifier("roleIdentifier2");

doReturn(Arrays.asList(entity1, entity2)).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(anyCollection());

Map<Long, Set<String>> result = approvalPermissionServiceSpy.getInstanceId2CandidateResourceRoleIdentifierIds(
Arrays.asList(1L, 2L));
assertEquals(2, result.size());
assertEquals("roleIdentifier1", result.get(1L).iterator().next());
assertEquals("roleIdentifier2", result.get(2L).iterator().next());
}

@Test
public void testGetCandidateUserIds() {
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskCandidateRepository).when(approvalPermissionServiceSpy).userTaskCandidateRepository;
UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);
approvalPermissionServiceSpy.userRoleRepository = userRoleRepository;
approvalPermissionServiceSpy.userResourceRoleRepository = userResourceRoleRepository;

UserRoleEntity userRoleEntity1 = new UserRoleEntity();
userRoleEntity1.setUserId(1L);

UserRoleEntity userRoleEntity2 = new UserRoleEntity();
userRoleEntity2.setUserId(2L);

UserResourceRoleEntity userResourceRoleEntity1 = new UserResourceRoleEntity();
userResourceRoleEntity1.setUserId(1L);

UserResourceRoleEntity userResourceRoleEntity2 = new UserResourceRoleEntity();
userResourceRoleEntity2.setUserId(2L);

doReturn(Arrays.asList(userRoleEntity1, userRoleEntity2))
.when(userRoleRepository)
.findByRoleIdIn(anyCollection());

doReturn(Arrays.asList(userResourceRoleEntity1, userResourceRoleEntity2))
.when(userResourceRoleRepository)
.findByResourceIdsAndResourceRoleIdsIn(anyCollection(), anyCollection());

Set<Long> result = approvalPermissionServiceSpy.getCandidateUserIds(Arrays.asList(1L, 2L));
assertEquals(2, result.size());
assertTrue(result.contains(1L));
assertTrue(result.contains(2L));
}

@Test
public void testGetCandidateRoles() {
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskCandidateRepository).when(approvalPermissionServiceSpy).userTaskCandidateRepository;
RoleRepository roleRepository = mock(RoleRepository.class);
approvalPermissionServiceSpy.roleRepository = roleRepository;

UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
entity1.setRoleId(1L);

UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
entity2.setRoleId(2L);

doReturn(Arrays.asList(entity1, entity2)).when(userTaskCandidateRepository)
.findByApprovalInstanceId(anyLong());

doReturn(Arrays.asList(new Role(1L), new Role(2L)))
.when(roleRepository)
.findByRoleIdsAndEnabled(anyCollection(), anyBoolean());

List<Role> result = approvalPermissionServiceSpy.getCandidateRoles(1L, true);
assertEquals(2, result.size());
assertTrue(result.stream().anyMatch(role -> role.getId().equals(1L)));
assertTrue(result.stream().anyMatch(role -> role.getId().equals(2L)));
}

@Test
public void testIsApprovable() {
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskInstanceRepository).when(approvalPermissionServiceSpy).userTaskInstanceRepository;

doReturn(new UserTaskInstanceEntity()).when(userTaskInstanceRepository).findByCandidateUserId(anyLong());

boolean result = approvalPermissionServiceSpy.isApprovable(1L);
assertTrue(result);
}

@Test
public void testGetApprovableApprovalInstances() {
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskInstanceRepository).when(approvalPermissionServiceSpy).userTaskInstanceRepository;

doReturn(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity()))
.when(userTaskInstanceRepository)
.findByCandidateUserIdOrRoleIds(anyLong(), anyCollection());

List<UserTaskInstanceEntity> result = approvalPermissionServiceSpy.getApprovableApprovalInstances(1L);
assertEquals(2, result.size());
assertTrue(result.stream().anyMatch(entity -> entity.getCandidateUserId().equals(1L)));
}

@Test
public void testGetApprovableApprovalInstances_withRoleIds() {
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskInstanceRepository).when(approvalPermissionServiceSpy).userTaskInstanceRepository;

Set<Long> roleIds = new HashSet<>();
roleIds.add(1L);
roleIds.add(2L);

doReturn(Arrays.asList(new UserTaskInstanceEntity(), new UserTaskInstanceEntity()))
.when(userTaskInstanceRepository)
.findByCandidateUserIdOrRoleIds(anyLong(), anyCollection());

List<UserTaskInstanceEntity> result = approvalPermissionServiceSpy.getApprovableApprovalInstances(1L, roleIds);
assertEquals(2, result.size());
assertTrue(result.stream().anyMatch(entity -> entity.getCandidateUserId().equals(1L)));
}

@Test
public void testIsApprovable_false() {
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskInstanceRepository).when(approvalPermissionServiceSpy).userTaskInstanceRepository;

doReturn(Collections.emptyList()).when(userTaskInstanceRepository).findByCandidateUserId(anyLong());

boolean result = approvalPermissionServiceSpy.isApprovable(1L);
assertFalse(result);
}

@Test
public void testGetCandidatesByFlowInstanceIds() {
UserTaskInstanceCandidateRepository userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);
ApprovalPermissionService approvalPermissionServiceSpy = spy(approvalPermissionService);
doReturn(userTaskCandidateRepository).when(approvalPermissionServiceSpy).userTaskCandidateRepository;
doReturn(userResourceRoleRepository).when(approvalPermissionServiceSpy).userResourceRoleRepository;

UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
entity1.setFlowInstanceId(1L);

UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
entity2.setFlowInstanceId(2L);

UserResourceRoleEntity userResourceRoleEntity1 = new UserResourceRoleEntity();
userResourceRoleEntity1.setResourceId("resourceId1");
userResourceRoleEntity1.setResourceRoleId("resourceRoleId1");

UserResourceRoleEntity userResourceRoleEntity2 = new UserResourceRoleEntity();
userResourceRoleEntity2.setResourceId("resourceId2");
userResourceRoleEntity2.setResourceRoleId("resourceRoleId2");

doReturn(Arrays.asList(entity1, entity2)).when(userTaskCandidateRepository).findByApprovalInstanceIdAndStatus(
anyLong(), any(FlowNodeStatus.class));

doReturn(Arrays.asList(userResourceRoleEntity1, userResourceRoleEntity2))
.when(userResourceRoleRepository)
.findByResourceIdsAndResourceRoleIdsIn(anyCollection(), anyCollection());

Map<Long, Set<UserEntity>> result = approvalPermissionServiceSpy.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L));
assertEquals(2, result.size());
assertEquals(1, result.get(1L).size());
assertEquals(1, result.get(2L).size());
}

}