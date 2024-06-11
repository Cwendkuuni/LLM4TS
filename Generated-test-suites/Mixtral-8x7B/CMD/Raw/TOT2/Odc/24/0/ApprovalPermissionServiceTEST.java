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

import com.oceanbase.odc.common.lang.Pair;
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
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

import lombok.NonNull;

@SuppressWarnings("unchecked")
public class ApprovalPermissionServiceTest {

 private ApprovalPermissionService approvalPermissionService;

 @Before
 public void setup() {
 UserService userService = mock(UserService.class);
 ResourceRoleService resourceRoleService = mock(ResourceRoleService.class);
 AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
 UserRepository userRepository = mock(UserRepository.class);
 RoleRepository roleRepository = mock(RoleRepository.class);
 UserRoleRepository userRoleRepository = mock(UserRoleRepository.class);
 UserResourceRoleRepository userResourceRoleRepository = mock(UserResourceRoleRepository.class);
 UserTaskInstanceRepository userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);

 when(userService.getCurrentUserRoleIds()).thenReturn(Collections.emptySet());
 when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.emptySet());
 when(authenticationFacade.currentUserId()).thenReturn(1L);
 when(userRepository.findByUserIdsAndEnabled(Collections.emptySet(), true)).thenReturn(Collections.emptyList());
 when(userRepository.findByRoleIdsAndEnabled(Collections.emptySet(), true)).thenReturn(Collections.emptyList());
 when(userRepository.findByUserIdsOrRoleIds(Collections.emptySet(), Collections.emptySet(), true))
 .thenReturn(Collections.emptyList());
 when(roleRepository.findByRoleIdsAndEnabled(Collections.emptySet(), true)).thenReturn(Collections.emptyList());
 when(userRoleRepository.findByRoleIdIn(Collections.emptySet())).thenReturn(Collections.emptyList());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.emptySet()))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.emptySet()))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.emptySet()))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findByApprovalInstanceIdAndStatus(1L, FlowNodeStatus.EXECUTING))
 .thenReturn(Collections.emptyList());
 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singleton(1L),
 FlowNodeStatus.EXECUTING, 0))
 .thenReturn(Collections.emptyList());

 approvalPermissionService = new ApprovalPermissionService();
 approvalPermissionService.userService = userService;
 approvalPermissionService.resourceRoleService = resourceRoleService;
 approvalPermissionService.authenticationFacade = authenticationFacade;
 approvalPermissionService.userRepository = userRepository;
 approvalPermissionService.roleRepository = roleRepository;
 approvalPermissionService.userRoleRepository = userRoleRepository;
 approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
 approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
 }

 @Test
 public void testGetCandidates_withEnabledNull() {
 Set<User> candidates = approvalPermissionService.getCandidates(1L, null);
 assertTrue(candidates.isEmpty());
 }

 @Test
 public void testGetCandidates_withEnabledTrue() {
 Set<User> candidates = approvalPermissionService.getCandidates(1L, true);
 assertTrue(candidates.isEmpty());
 }

 @Test
 public void testGetCandidates_withEnabledFalse() {
 Set<User> candidates = approvalPermissionService.getCandidates(1L, false);
 assertTrue(candidates.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(
 Collections.singleton(1L));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
 Collections.singleton(1L));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetCandidateUserIds() {
 Set<Long> result = approvalPermissionService.getCandidateUserIds(Collections.singleton(1L));
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetCandidateRoles() {
 List<Role> roles = approvalPermissionService.getCandidateRoles(1L, true);
 assertTrue(roles.isEmpty());
 }

 @Test
 public void testIsApprovable() {
 assertTrue(approvalPermissionService.isApprovable(1L));
 }

 @Test
 public void testGetApprovableApprovalInstances() {
 List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances();
 assertTrue(instances.isEmpty());
 }

 @Test
 public void testGetApprovableApprovalInstances_withUserId() {
 List<UserTaskInstanceEntity> instances = approvalPermissionService.getApprovableApprovalInstances(1L);
 assertTrue(instances.isEmpty());
 }

 @Test
 public void testSetCandidateResourceRole() {
 UserTaskInstanceCandidateEntity candidateEntity = new UserTaskInstanceCandidateEntity();
 candidateEntity.setApprovalInstanceId(1L);
 approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier");
 }

 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> instances = approvalPermissionService.listApprovableExternalInstances();
 assertTrue(instances.isEmpty());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds_withEmptyFlowInstanceIds() {
 Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.emptySet());
 assertTrue(result.isEmpty());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(
 Collections.singleton(1L));
 assertTrue(result.isEmpty());
 }
}