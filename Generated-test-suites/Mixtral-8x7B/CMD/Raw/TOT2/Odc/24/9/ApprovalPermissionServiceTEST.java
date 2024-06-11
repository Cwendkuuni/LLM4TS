import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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

 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 private UserTaskInstanceEntity userTaskInstanceEntity;
 private UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity;
 private UserEntity userEntity;
 private RoleEntity roleEntity;
 private UserResourceRoleEntity userResourceRoleEntity;
 private UserRoleEntity userRoleEntity;

 @Before
 public void setup() {
 userEntity = new UserEntity();
 userEntity.setId(1L);

 roleEntity = new RoleEntity();
 roleEntity.setId(2L);

 userResourceRoleEntity = new UserResourceRoleEntity();
 userResourceRoleEntity.setUserId(1L);
 userResourceRoleEntity.setResourceRoleId("resourceRoleIdentifier");

 userRoleEntity = new UserRoleEntity();
 userRoleEntity.setUserId(1L);
 userRoleEntity.setRoleId(2L);

 userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(3L);
 userTaskInstanceEntity.setFlowInstanceId(4L);
 userTaskInstanceEntity.setStatus(FlowNodeStatus.EXECUTING);

 userTaskInstanceCandidateEntity = new UserTaskInstanceCandidateEntity();
 userTaskInstanceCandidateEntity.setApprovalInstanceId(5L);
 userTaskInstanceCandidateEntity.setRoleId(2L);
 userTaskInstanceCandidateEntity.setResourceRoleIdentifier("resourceRoleIdentifier");
 }

 @Test
 public void testGetCandidates() {
 Set<User> users = new HashSet<>();
 users.add(new User(userEntity));
 when(userRepository.findByRoleIdsAndEnabled(anySet(), any())).thenReturn(Collections.singletonList(userEntity));
 when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity));

 assertEquals(users, approvalPermissionService.getCandidates(5L, null));
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 Map<Long, Set<Long>> expected = new HashMap<>();
 expected.put(5L, new HashSet<>(Collections.singleton(2L)));

 when(userTaskCandidateRepository.findByApprovalInstanceIds(anySet()))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity));

 assertEquals(expected, approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singleton(5L)));
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 Map<Long, Set<String>> expected = new HashMap<>();
 expected.put(5L, new HashSet<>(Collections.singleton("resourceRoleIdentifier")));

 when(userTaskCandidateRepository.findByApprovalInstanceIds(anySet()))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity));

 assertEquals(expected, approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
 Collections.singleton(5L)));
 }

 @Test
 public void testGetCandidateUserIds() {
 Set<Long> expected = new HashSet<>(Collections.singleton(1L));

 when(userRoleRepository.findByRoleIdIn(anySet()))
 .thenReturn(Collections.singletonList(userRoleEntity));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(Collections.singletonList(userResourceRoleEntity));

 assertEquals(expected, approvalPermissionService.getCandidateUserIds(Collections.singleton(5L)));
 }

 @Test
 public void testGetCandidateRoles() {
 List<Role> expected = new ArrayList<>();
 expected.add(new Role(roleEntity));

 when(roleRepository.findByRoleIdsAndEnabled(anySet(), any()))
 .thenReturn(Collections.singletonList(roleEntity));
 when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity));

 assertEquals(expected, approvalPermissionService.getCandidateRoles(5L, null));
 }

 @Test
 public void testIsApprovable() {
 when(userTaskInstanceRepository.findByStatus(any()))
 .thenReturn(Collections.singletonList(userTaskInstanceEntity));
 when(userTaskInstanceRepository.findById(anyLong()))
 .thenReturn(userTaskInstanceEntity);

 assertEquals(true, approvalPermissionService.isApprovable(5L));
 }

 @Test
 public void testGetApprovableApprovalInstances() {
 List<UserTaskInstanceEntity> expected = new ArrayList<>();
 expected.add(userTaskInstanceEntity);

 when(userService.getUserRoleIds(anyLong()))
 .thenReturn(Collections.singleton(2L));
 when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong()))
 .thenReturn(Collections.singleton("resourceRoleIdentifier"));
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anySet()))
 .thenReturn(expected);

 assertEquals(expected, approvalPermissionService.getApprovableApprovalInstances(5L));
 }

 @Test
 public void testSetCandidateResourceRole() {
 when(userTaskCandidateRepository.save(any())).thenReturn(userTaskInstanceCandidateEntity);

 approvalPermissionService.setCandidateResourceRole(5L, "resourceRoleIdentifier");
 }

 @Test
 public void testListApprovableExternalInstances() {
 List<UserTaskInstanceEntity> expected = new ArrayList<>();
 expected.add(userTaskInstanceEntity);

 when(userTaskInstanceRepository.findByStatus(any()))
 .thenReturn(Collections.singletonList(userTaskInstanceEntity));

 assertEquals(expected, approvalPermissionService.listApprovableExternalInstances());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 Map<Long, Set<UserEntity>> expected = new HashMap<>();

 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anySet(), any()))
 .thenReturn(Collections.singletonList(userTaskInstanceEntity));

 when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity));

 when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong()))
 .thenReturn(Collections.singleton("resourceRoleIdentifier"));

 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(Collections.singletonList(userResourceRoleEntity));

 when(userRepository.findByUserIds(anySet()))
 .thenReturn(Collections.singletonList(userEntity));

 assertEquals(expected, approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singleton(4L)));
 }
}