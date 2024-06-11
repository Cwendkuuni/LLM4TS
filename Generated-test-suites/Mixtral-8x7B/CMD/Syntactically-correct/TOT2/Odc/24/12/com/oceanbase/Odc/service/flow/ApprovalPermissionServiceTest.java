import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

import lombok.NonNull;

public class ApprovalPermissionServiceTest {

 @Mock
 private UserService userService;

 @Mock
 private ResourceRoleService resourceRoleService;

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

 @Mock
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Mock
 private UserRepository userRepository;

 @Mock
 private RoleRepository roleRepository;

 @Mock
 private UserRoleRepository userRoleRepository;

 @Mock
 private UserResourceRoleRepository userResourceRoleRepository;

 @Mock
 private ResourceRoleRepository resourceRoleRepository;

 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 // Test for getCandidates method
 @Test
 public void testGetCandidates() {
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 Set<User> expectedUsers = new HashSet<>();
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
 when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(expectedUsers);
 when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean())).thenReturn(expectedUsers);
 when(userRepository.findByUserIdsOrRoleIds(anySet(), anySet(), anyBoolean()))
 .thenReturn(expectedUsers);

 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
 assertEquals(expectedUsers, result);
 }

 // Test for getInstanceId2CandidateRoleIds method
 @Test
 public void testgetInstanceId2CandidateRoleIds() {
 Collection<Long> approvalInstanceIds = new ArrayList<>();
 Map<Long, Set<Long>> expectedMap = new HashMap<>();
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(new ArrayList<>());
 assertEquals(expectedMap, approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds));
 }

 // Test for getInstanceId2CandidateResourceRoleIdentifierIds method
 @Test
 public void testgetInstanceId2CandidateResourceRoleIdentifierIds() {
 Collection<Long> approvalInstanceIds = new ArrayList<>();
 Map<Long, Set<String>> expectedMap = new HashMap<>();
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(new ArrayList<>());
 assertEquals(expectedMap, approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds));
 }

 // Test for getCandidateUserIds method
 @Test
 public void testgetCandidateUserIds() {
 Collection<Long> approvalInstanceIds = new ArrayList<>();
 Set<Long> expectedUserIds = new HashSet<>();
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(new ArrayList<>());
 when(userRoleRepository.findByRoleIdIn(anySet())).thenReturn(new ArrayList<>());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(new ArrayList<>());
 assertEquals(expectedUserIds, approvalPermissionService.getCandidateUserIds(approvalInstanceIds));
 }

 // Test for getCandidateRoles method
 @Test
 public void testgetCandidateRoles() {
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 List<Role> expectedRoles = new ArrayList<>();
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId)).thenReturn(new ArrayList<>());
 when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(expectedRoles);

 List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
 assertEquals(expectedRoles, result);
 }

 // Test for isApprovable method
 @Test
 public void testIsApprovable() {
 Long approvalInstanceId = 1L;
 List<UserTaskInstanceEntity> approvableApprovalInstances = new ArrayList<>();
 when(authenticationFacade.currentUserId()).thenReturn(1L);
 when(userService.getCurrentUserRoleIds()).thenReturn(new ArrayList<>());
 when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new ArrayList<>());
 when(userTaskInstanceRepository.findByCandidateUserId(1L)).thenReturn(new ArrayList<>());
 when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>()))
 .thenReturn(new ArrayList<>());
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>()))
 .thenReturn(approvableApprovalInstances);
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anySet()))
 .thenCallRealMethod();
 assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
 }

 // Test for listApprovableExternalInstances method
 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> externalInstances = new ArrayList<>();
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(externalInstances);
 assertEquals(externalInstances, approvalPermissionService.listApprovableExternalInstances());
 }

 // Test for getCandidatesByFlowInstanceIds method
 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 Map<Long, Set<UserEntity>> expectedMap = new HashMap<>();
 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
 FlowNodeStatus.EXECUTING)).thenReturn(new ArrayList<>());
 when(resourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(new ArrayList<>());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(new ArrayList<>());
 when(userRepository.findByUserIds(anySet())).thenReturn(new ArrayList<>());
 when(userRoleRepository.findByRoleIdIn(anySet())).thenReturn(new ArrayList<>());
 when(resourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(new ArrayList<>());
 assertEquals(expectedMap, approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds));
 }

}