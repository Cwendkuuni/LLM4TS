import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
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

public class ApprovalPermissionServiceTest {

 private ApprovalPermissionService approvalPermissionService;
 private UserTaskInstanceRepository userTaskInstanceRepository;
 private UserRepository userRepository;
 private RoleRepository roleRepository;
 private UserRoleRepository userRoleRepository;
 private UserResourceRoleRepository userResourceRoleRepository;

 @Before
 public void setup() {
 userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
 userRepository = mock(UserRepository.class);
 roleRepository = mock(RoleRepository.class);
 userRoleRepository = mock(UserRoleRepository.class);
 userResourceRoleRepository = mock(UserResourceRoleRepository.class);
 approvalPermissionService = spy(new ApprovalPermissionService());
 doReturn(userTaskInstanceRepository).when(approvalPermissionService).userTaskInstanceRepository;
 doReturn(userRepository).when(approvalPermissionService).userRepository;
 doReturn(roleRepository).when(approvalPermissionService).roleRepository;
 doReturn(userRoleRepository).when(approvalPermissionService).userRoleRepository;
 doReturn(userResourceRoleRepository).when(approvalPermissionService).userResourceRoleRepository;
 }

 // Test getCandidates method
 @Test
 public void testGetCandidates() {
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<UserEntity> users = new HashSet<>();
 users.add(new UserEntity(1L, "user1", null));
 users.add(new UserEntity(2L, "user2", null));

 doReturn(users).when(userRepository).findByRoleIdsAndEnabled(anySet(), anyBoolean());
 doReturn(users).when(userRepository).findByUserIdsAndEnabled(anySet(), anyBoolean());

 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 assertEquals(2, result.size());
 verify(userRepository, times(1)).findByRoleIdsAndEnabled(anySet(), anyBoolean());
 verify(userRepository, times(1)).findByUserIdsAndEnabled(anySet(), anyBoolean());
 }

 // Test isApprovable method
 @Test
 public void testIsApprovable() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
 userTaskInstanceEntities.add(new UserTaskInstanceEntity(1L, 1L, null, null, null, null));

 doReturn(userTaskInstanceEntities).when(userTaskInstanceRepository)
 .findByApprovalInstanceIdAndStatus(anyLong(), any(FlowNodeStatus.class));

 boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

 assertTrue(result);
 verify(userTaskInstanceRepository, times(1)).findByApprovalInstanceIdAndStatus(anyLong(), any(FlowNodeStatus.class));
 }

 // Test getApprovableApprovalInstances method
 @Test
 public void testGetApprovableApprovalInstances() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
 userTaskInstanceEntities.add(new UserTaskInstanceEntity(1L, 1L, userId, null, null, null));
 userTaskInstanceEntities.add(
 new UserTaskInstanceEntity(2L, 2L, null, null, null, FlowNodeStatus.EXECUTING));

 doReturn(userTaskInstanceEntities).when(userTaskInstanceRepository)
 .findByCandidateUserIdOrStatus(anyLong(), any(FlowNodeStatus.class));

 List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);

 assertEquals(1, result.size());
 verify(userTaskInstanceRepository, times(1)).findByCandidateUserIdOrStatus(anyLong(), any(FlowNodeStatus.class));
 }

 // Test setCandidateResourceRole method
 @Test
 public void testSetCandidateResourceRole() {
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "test";

 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

 verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
 }

 // Test listApprovableExternalInstances method
 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
 userTaskInstanceEntities.add(new UserTaskInstanceEntity(1L, 1L, null, null, "external1", null));
 userTaskInstanceEntities.add(new UserTaskInstanceEntity(2L, 2L, null, null, "external2", null));

 doReturn(userTaskInstanceEntities).when(userTaskInstanceRepository)
 .findByStatusAndExternalApprovalIdIsNotNull(any(FlowNodeStatus.class));

 List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();

 assertEquals(2, result.size());
 verify(userTaskInstanceRepository, times(1))
 .findByStatusAndExternalApprovalIdIsNotNull(any(FlowNodeStatus.class));
 }

 // Test getCandidatesByFlowInstanceIds method
 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Long flowInstanceId1 = 1L;
 Long flowInstanceId2 = 2L;
 Long approvalInstanceId1 = 11L;
 Long approvalInstanceId2 = 12L;
 UserEntity user1 = new UserEntity(1L, "user1", null);
 UserEntity user2 = new UserEntity(2L, "user2", null);
 UserEntity user3 = new UserEntity(3L, "user3", null);

 List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
 userTaskInstanceEntities.add(new UserTaskInstanceEntity(approvalInstanceId1, flowInstanceId1, null, null, null, null));
 userTaskInstanceEntities.add(new UserTaskInstanceEntity(approvalInstanceId2, flowInstanceId2, null, null, null, null));

 doReturn(userTaskInstanceEntities).when(userTaskInstanceRepository)
 .findApprovalInstanceIdByFlowInstanceIdAndStatus(anySet(), any(FlowNodeStatus.class));

 Set<String> resourceRoleIdentifiers = new HashSet<>();
 resourceRoleIdentifiers.add("test1");
 resourceRoleIdentifiers.add("test2");

 Map<String, Set<Long>> identifier2UserIds = new HashMap<>();
 identifier2UserIds.put("test1:resource1", new HashSet<Long>() {{ add(1L); }});
 identifier2UserIds.put("test2:resource2", new HashSet<Long>() {{ add(2L); }});
 identifier2UserIds.put("test2:resource3", new HashSet<Long>() {{ add(3L); }});

 Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = new HashMap<>();
 instanceId2CandidateResourceRoleIdentifierIds.put(flowInstanceId1, resourceRoleIdentifiers);
 instanceId2CandidateResourceRoleIdentifierIds.put(flowInstanceId2, resourceRoleIdentifiers);

 Map<Long, Set<UserEntity>> result = approvalPermissionService
 .getCandidatesByFlowInstanceIds(new HashSet<Long>() {{ add(flowInstanceId1); add(flowInstanceId2); }});

 assertEquals(2, result.size());
 assertTrue(result.containsKey(flowInstanceId1));
 assertTrue(result.containsKey(flowInstanceId2));
 assertEquals(2, result.get(flowInstanceId1).size());
 assertEquals(1, result.get(flowInstanceId2).size());
 assertTrue(result.get(flowInstanceId1).contains(user1));
 assertTrue(result.get(flowInstanceId1).contains(user2));
 assertTrue(result.get(flowInstanceId2).contains(user3));

 verify(userTaskInstanceRepository, times(1))
 .findApprovalInstanceIdByFlowInstanceIdAndStatus(anySet(), any(FlowNodeStatus.class));
 verify(userResourceRoleRepository, times(1)).findByResourceIdsAndResourceRoleIdsIn(anySet());
 }
}