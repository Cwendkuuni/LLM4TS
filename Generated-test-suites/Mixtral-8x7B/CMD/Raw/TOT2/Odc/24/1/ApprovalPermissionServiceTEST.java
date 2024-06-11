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
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserRoleRepository;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;

public class ApprovalPermissionServiceTest {

private ApprovalPermissionService approvalPermissionService;

@Before
public void setup() {
approvalPermissionService = new ApprovalPermissionService();

// Initialize mocked dependencies
UserRepository userRepository = mock(UserRepository.class);
UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);

approvalPermissionService.setUserRepository(userRepository);
approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
approvalPermissionService.setUserRoleRepository(userRoleRepository);
}

// Test method: getCandidates
@Test
public void testGetCandidates() {
// Prepare test data
Long approvalInstanceId = 1L;
Boolean enabled = true;

// Define mock behaviors
List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
entities.add(new UserTaskInstanceCandidateEntity() {{
setRoleId(1L);
setEnabled(enabled);
}});
entities.add(new UserTaskInstanceCandidateEntity() {{
setUserId(1L);
setEnabled(enabled);
}});
when(approvalPermissionService.getUserTaskCandidateRepository().findByApprovalInstanceId(approvalInstanceId))
.thenReturn(entities);

// Execute method under test
Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

// Assert results
assertEquals(2, result.size());
assertTrue(result.contains(new User() {{
setId(1L);
setEnabled(enabled);
}}));
}

// Test method: getInstanceId2CandidateRoleIds
@Test
public void testGetInstanceId2CandidateRoleIds() {
// Prepare test data
Collection<Long> approvalInstanceIds = Collections.singleton(1L);

// Define mock behaviors
List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
entities.add(new UserTaskInstanceCandidateEntity() {{
setRoleId(1L);
setApprovalInstanceId(1L);
}});
when(approvalPermissionService.getUserTaskCandidateRepository().findByApprovalInstanceIds(approvalInstanceIds))
.thenReturn(entities);

// Execute method under test
Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

// Assert results
assertEquals(1, result.size());
assertEquals(1, result.get(1L).size());
assertEquals(1L, (long) result.get(1L).iterator().next());
}

// Test method: getInstanceId2CandidateResourceRoleIdentifierIds
@Test
public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
// Prepare test data
Collection<Long> approvalInstanceIds = Collections.singleton(1L);

// Define mock behaviors
List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
entities.add(new UserTaskInstanceCandidateEntity() {{
setResourceRoleIdentifier("identifier1");
setApprovalInstanceId(1L);
}});
when(approvalPermissionService.getUserTaskCandidateRepository().findByApprovalInstanceIds(approvalInstanceIds))
.thenReturn(entities);

// Execute method under test
Map<Long, Set<String>> result =
approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

// Assert results
assertEquals(1, result.size());
assertEquals(1, result.get(1L).size());
assertEquals("identifier1", result.get(1L).iterator().next());
}

// Test method: getCandidateUserIds
@Test
public void testGetCandidateUserIds() {
// Prepare test data
Collection<Long> approvalInstanceIds = Collections.singleton(1L);

// Define mock behaviors
List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
entities.add(new UserTaskInstanceCandidateEntity() {{
setRoleId(1L);
setApprovalInstanceId(1L);
}});
entities.add(new UserTaskInstanceCandidateEntity() {{
setResourceRoleIdentifier("identifier1");
setApprovalInstanceId(1L);
}});
when(approvalPermissionService.getUserTaskCandidateRepository()
.findByApprovalInstanceIds(approvalInstanceIds))
.thenReturn(entities);

when(approvalPermissionService.getUserRoleRepository().findByRoleIdIn(Collections.singleton(1L)))
.thenReturn(Collections.singletonList(new UserRoleEntity() {{
setUserId(1L);
}}));

when(approvalPermissionService.getUserResourceRoleRepository()
.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("identifier1"),
Collections.singleton("identifier1")))
.thenReturn(Collections.singletonList(new UserResourceRoleEntity() {{
setUserId(1L);
}}));

// Execute method under test
Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

// Assert results
assertEquals(1, result.size());
assertEquals(1L, (long) result.iterator().next());
}

// Test method: getCandidateRoles
@Test
public void testGetCandidateRoles() {
// Prepare test data
Long approvalInstanceId = 1L;
Boolean enabled = true;

// Define mock behaviors
List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
entities.add(new UserTaskInstanceCandidateEntity() {{
setRoleId(1L);
setEnabled(enabled);
setApprovalInstanceId(approvalInstanceId);
}});
when(approvalPermissionService.getUserTaskCandidateRepository()
.findByApprovalInstanceId(approvalInstanceId))
.thenReturn(entities);

when(approvalPermissionService.getRoleRepository().findByRoleIdsAndEnabled(Collections.singleton(1L), enabled))
.thenReturn(Collections.singletonList(new Role() {{
setId(1L);
setEnabled(enabled);
}}));

// Execute method under test
List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

// Assert results
assertEquals(1, result.size());
assertEquals(1L, (long) result.get(0).getId());
}

// Test method: isApprovable
@Test
public void testIsApprovable() {
// Prepare test data
Long approvalInstanceId = 1L;

// Define mock behaviors
when(approvalPermissionService.getApprovableApprovalInstances().contains(new UserTaskInstanceEntity() {{
setId(approvalInstanceId);
}}))
.thenReturn(true);

// Execute method under test
boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

// Assert results
assertTrue(result);
}

// Test method: getApprovableApprovalInstances
@Test
public void testGetApprovableApprovalInstances() {
// Prepare test data
long userId = 1L;

// Define mock behaviors
when(approvalPermissionService.getUserRepository().findByUserIds(Collections.singleton(userId)))
.thenReturn(Collections.singletonList(new UserEntity() {{
setId(userId);
}}));

when(approvalPermissionService.getUserRoleRepository().findByRoleIdIn(Collections.singleton(1L)))
.thenReturn(Collections.singletonList(new UserRoleEntity() {{
setUserId(userId);
}}));

when(approvalPermissionService.getResourceRoleService()
.getResourceRoleIdentifiersByUserId(userId))
.thenReturn(Collections.singleton("identifier1"));

when(approvalPermissionService.getUserResourceRoleRepository()
.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("identifier1"),
Collections.singleton("identifier1")))
.thenReturn(Collections.singletonList(new UserResourceRoleEntity() {{
setUserId(userId);
}}));

// Execute method under test
List<UserTaskInstanceEntity> result =
approvalPermissionService.getApprovableApprovalInstances(userId);

// Assert results
assertEquals(3, result.size());
}

// Test method: setCandidateResourceRole
@Test
public void testSetCandidateResourceRole() {
// Prepare test data
Long approvalInstanceId = 1L;
String resourceRoleIdentifier = "identifier1";

// Execute method under test
approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

// Assert side effects
UserTaskInstanceCandidateEntity entity =
approvalPermissionService.getUserTaskCandidateRepository().findByApprovalInstanceId(approvalInstanceId)
.stream().findFirst().orElse(null);
assertNotNull(entity);
assertEquals(resourceRoleIdentifier, entity.getResourceRoleIdentifier());
}

// Test method: listApprovableExternalInstances
@Test
public void testListApprovableExternalInstances() {
// Prepare test data

// Execute method under test
List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();

// Assert results
assertTrue(result.size() > 0);
for (UserTaskInstanceEntity entity : result) {
assertEquals(FlowNodeStatus.EXECUTING, entity.getStatus());
assertNotNull(entity.getExternalApprovalId());
assertNotNull(entity.getExternalFlowInstanceId());
}
}

// Test method: getCandidatesByFlowInstanceIds
@Test
public void testGetCandidatesByFlowInstanceIds() {
// Prepare test data
Collection<Long> flowInstanceIds = Collections.singleton(1L);

// Define mock behaviors
UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity() {{
setId(1L);
setFlowInstanceId(1L);
setStatus(FlowNodeStatus.EXECUTING);
setExternalApprovalId("approval1");
setExternalFlowInstanceId("instance1");
}};

UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity() {{
setId(2L);
setFlowInstanceId(2L);
setStatus(FlowNodeStatus.EXECUTING);
setExternalApprovalId("approval2");
setExternalFlowInstanceId("instance2");
}};

UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
FlowNodeStatus.EXECUTING))
.thenReturn(Collections.singletonList(1L));

when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
.thenReturn(Collections.singletonList(entity1));

when(userTaskInstanceRepository.findByExternalApprovalIdAndExternalFlowInstanceIdAndStatus(
"approval1",
"instance1",
FlowNodeStatus.EXECUTING))
.thenReturn(Collections.singletonList(entity2));

UserRepository userRepository = mock(UserRepository.class);
when(userRepository.findByUserIds(Collections.singleton(1L)))
.thenReturn(Collections.singletonList(new UserEntity() {{
setId(1L);
}}));

UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);
when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("identifier1"),
Collections.singleton("identifier1")))
.thenReturn(Collections.singletonList(new UserResourceRoleEntity() {{
setUserId(1L);
}}));

approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
approvalPermissionService.setUserRepository(userRepository);
approvalPermissionService.setUserResourceRoleRepository(userResourceRoleRepository);

// Execute method under test
Map<Long, Set<UserEntity>> result =
approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);

// Assert results
assertEquals(1, result.size());
assertEquals(1, result.get(1L).size());
assertEquals(1L, (long) result.get(1L).iterator().next().getId());
}
}