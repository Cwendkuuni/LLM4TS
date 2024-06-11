import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

public class ApprovalPermissionServiceTest {

private ApprovalPermissionService approvalPermissionService;

@Before
public void setUp() {
// Initialize mocked dependencies
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
UserRepository userRepository = mock(UserRepository.class);
UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);
ResourceRoleService resourceRoleService = mock(ResourceRoleService.class);
AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
UserService userService = mock(UserService.class);

// Set up expected behavior for mocked dependencies
when(authenticationFacade.currentUserId()).thenReturn(1L);

approvalPermissionService = new ApprovalPermissionService();
approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
approvalPermissionService.userRepository = userRepository;
approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
approvalPermissionService.resourceRoleService = resourceRoleService;
approvalPermissionService.authenticationFacade = authenticationFacade;
approvalPermissionService.userService = userService;
}

// Test getCandidates method
@Test
public void testGetCandidates() {
// Test case 1: Empty approvalInstanceId and enabled
Set<User> result1 = approvalPermissionService.getCandidates(null, null);
assertEquals(Collections.emptySet(), result1);

// Test case 2: Valid approvalInstanceId and enabled = true
Set<UserTaskInstanceCandidateEntity> entities = new HashSet<>();
entities.add(new UserTaskInstanceCandidateEntity(1L, null, null, null));
when(userTaskInstanceRepository.findByApprovalInstanceId(1L))
.thenReturn(entities);
when(userRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), true))
.thenReturn(Collections.singletonList(new UserEntity(1, "testUser", null)));
Set<User> result2 = approvalPermissionService.getCandidates(1L, true);
assertEquals(Collections.singleton(new User(1, "testUser", null)), result2);

// Test case 3: Valid approvalInstanceId and enabled = false
entities.clear();
entities.add(new UserTaskInstanceCandidateEntity(1L, 1L, null, null));
when(userTaskInstanceRepository.findByApprovalInstanceId(1L))
.thenReturn(entities);
when(userRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), false))
.thenReturn(Collections.singletonList(new UserEntity(1, "testUser", null)));
Set<User> result3 = approvalPermissionService.getCandidates(1L, false);
assertEquals(Collections.singleton(new User(1, "testUser", null)), result3);

// Test case 4: Valid approvalInstanceId and null enabled
entities.clear();
entities.add(new UserTaskInstanceCandidateEntity(1L, 1L, null, null));
when(userTaskInstanceRepository.findByApprovalInstanceId(1L))
.thenReturn(entities);
when(userRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), true))
.thenReturn(Collections.singletonList(new UserEntity(1, "testUser", null)));
when(userRepository.findByRoleIdsAndEnabled(Collections.singleton(1L), false))
.thenReturn(Collections.singletonList(new UserEntity(2, "testUser2", null)));
Set<User> result4 = approvalPermissionService.getCandidates(1L, null);
assertEquals(Collections.singleton(new User(1, "testUser", null)), result4);
}

// Test isApprovable method
@Test
public void testIsApprovable() {
// Test case 1: Empty userIds
List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
when(userTaskInstanceRepository.findByCandidateUserIdIn(Collections.emptySet()))
.thenReturn(userTaskInstances);
assertTrue(approvalPermissionService.isApprovable(1L));

// Test case 2: Non-empty userIds
UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
userTaskInstance.setId(1L);
userTaskInstances.add(userTaskInstance);
when(userTaskInstanceRepository.findByCandidateUserIdIn(Collections.singleton(1L)))
.thenReturn(userTaskInstances);
assertTrue(approvalPermissionService.isApprovable(1L));

// Test case 3: Non-empty userIds but no executing instances
userTaskInstances.clear();
when(userTaskInstanceRepository.findByCandidateUserIdIn(Collections.singleton(1L)))
.thenReturn(userTaskInstances);
assertFalse(approvalPermissionService.isApprovable(1L));
}

// Test listApprovableExternalInstances method
@Test
public void testListApprovableExternalInstances() {
// Test case 1: Empty executing instances
List<UserTaskInstanceEntity> userTaskInstances = new ArrayList<>();
when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
.thenReturn(userTaskInstances);
assertEquals(Collections.emptyList(), approvalPermissionService.listApprovableExternalInstances());

// Test case 2: Non-empty executing instances with external approvalId and externalFlowInstanceId
UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
userTaskInstance.setExternalApprovalId("testExternalApprovalId");
userTaskInstance.setExternalFlowInstanceId("testExternalFlowInstanceId");
userTaskInstances.add(userTaskInstance);
when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
.thenReturn(userTaskInstances);
assertEquals(Collections.singletonList(userTaskInstance),
approvalPermissionService.listApprovableExternalInstances());
}

// Test getCandidatesByFlowInstanceIds method
@Test
public void testGetCandidatesByFlowInstanceIds() {
// Test case 1: Empty flowInstanceIds
Map<Long, Set<UserEntity>> result1 = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptySet());
assertEquals(Collections.emptyMap(), result1);

// Test case 2: Non-empty flowInstanceIds but no executing approval instances
Map<Long, Long> approvalInstanceId2FlowInstanceId = new HashMap<>();
approvalInstanceId2FlowInstanceId.put(1L, 1L);
when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L),
FlowNodeStatus.EXECUTING))
.thenReturn(Collections.emptyList());
Map<Long, Set<UserEntity>> result2 = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singleton(1L));
assertEquals(Collections.emptyMap(), result2);

// Test case 3: Non-empty flowInstanceIds and executing approval instances
UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
userTaskInstance.setId(1L);
UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity = new UserTaskInstanceCandidateEntity();
userTaskInstanceCandidateEntity.setResourceRoleIdentifier("testResourceRoleIdentifier1");
List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
entities.add(userTaskInstanceCandidateEntity);
when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L),
FlowNodeStatus.EXECUTING))
.thenReturn(Collections.singletonList(userTaskInstance));
when(userTaskInstanceRepository.findByApprovalInstanceId(1L))
.thenReturn(entities);
when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("testResourceRoleIdentifier1"),
Collections.singleton("testResourceRoleIdentifier1")))
.thenReturn(Collections.singletonList(new UserResourceRoleEntity(1L, "testResourceId", "testResourceRoleIdentifier1")));
when(userRepository.findByUserIds(Collections.singleton(1L)))
.thenReturn(Collections.singletonList(new UserEntity(1, "testUser", null)));
Map<Long, Set<UserEntity>> result3 = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singleton(1L));
assertEquals(Collections.singletonMap(1L, Collections.singleton(new UserEntity(1, "testUser", null))), result3);
}
}