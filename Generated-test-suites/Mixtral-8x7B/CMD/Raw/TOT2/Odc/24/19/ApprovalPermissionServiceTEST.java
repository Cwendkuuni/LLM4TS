import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
private List<UserTaskInstanceEntity> userTaskInstanceEntities;
private List<UserTaskInstanceCandidateEntity> userTaskInstanceCandidateEntities;
private List<UserEntity> userEntities;
private List<RoleEntity> roleEntities;
private List<UserResourceRoleEntity> userResourceRoleEntities;

@Before
public void setup() {
userEntity = new UserEntity();
userEntity.setId(1L);

roleEntity = new RoleEntity();
roleEntity.setId(2L);

userResourceRoleEntity = new UserResourceRoleEntity();
userResourceRoleEntity.setUserId(userEntity.getId());
userResourceRoleEntity.setResourceRoleId("resourceRoleIdentifier");

userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setId(3L);
userTaskInstanceEntity.setCandidateUserId(userEntity.getId());
userTaskInstanceEntity.setStatus(FlowNodeStatus.EXECUTING);

userTaskInstanceCandidateEntity = new UserTaskInstanceCandidateEntity();
userTaskInstanceCandidateEntity.setApprovalInstanceId(4L);
userTaskInstanceCandidateEntity.setRoleId(roleEntity.getId());
userTaskInstanceCandidateEntity.setResourceRoleIdentifier("resourceRoleIdentifier");

userTaskInstanceEntities = new ArrayList<>();
userTaskInstanceEntities.add(userTaskInstanceEntity);

userTaskInstanceCandidateEntities = new ArrayList<>();
userTaskInstanceCandidateEntities.add(userTaskInstanceCandidateEntity);

userEntities = new ArrayList<>();
userEntities.add(userEntity);

roleEntities = new ArrayList<>();
roleEntities.add(roleEntity);

userResourceRoleEntities = new ArrayList<>();
userResourceRoleEntities.add(userResourceRoleEntity);
}

@Test
public void testGetCandidates() {
when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(userTaskInstanceCandidateEntities);
when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(userEntities);
when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean())).thenReturn(userEntities);
when(userRepository.findByUserIdsOrRoleIds(anySet(), anySet(), anyBoolean()))
.thenReturn(userEntities);

Set<User> result = approvalPermissionService.getCandidates(1L, true);
assertEquals(1, result.size());
}

@Test
public void testGetInstanceId2CandidateRoleIds() {
when(userTaskCandidateRepository.findByApprovalInstanceIds(anySet()))
.thenReturn(userTaskInstanceCandidateEntities);

Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(new ArrayList<>());
assertEquals(0, result.size());
}

@Test
public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
when(userTaskCandidateRepository.findByApprovalInstanceIds(anySet()))
.thenReturn(userTaskInstanceCandidateEntities);

Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(new ArrayList<>());
assertEquals(0, result.size());
}

@Test
public void testGetCandidateUserIds() {
when(userTaskCandidateRepository.findByApprovalInstanceIds(anySet()))
.thenReturn(userTaskInstanceCandidateEntities);
when(userRoleRepository.findByRoleIdIn(anySet())).thenReturn(userRoleEntities);
when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
.thenReturn(userResourceRoleEntities);

Set<Long> result = approvalPermissionService.getCandidateUserIds(new ArrayList<>());
assertEquals(0, result.size());
}

@Test
public void testGetCandidateRoles() {
when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong())).thenReturn(userTaskInstanceCandidateEntities);
when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(roleEntities);

List<Role> result = approvalPermissionService.getCandidateRoles(1L, true);
assertEquals(1, result.size());
}

@Test
public void testIsApprovable() {
when(authenticationFacade.currentUserId()).thenReturn(1L);
when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<>());
when(userService.getCurrentUserResourceRoleIdentifiers())
.thenReturn(new ArrayList<>());
when(userTaskInstanceRepository.findByCandidateUserId(anyLong()))
.thenReturn(userTaskInstanceEntities);
when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), anySet()))
.thenReturn(userTaskInstanceEntities);
when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anySet()))
.thenReturn(userTaskInstanceEntities);

boolean result = approvalPermissionService.isApprovable(1L);
assertEquals(true, result);
}

@Test
public void testGetApprovableApprovalInstances() {
when(userService.getUserRoleIds(anyLong())).thenReturn(new HashSet<>());
when(userService.getUserResourceRoleIdentifiersByUserId(anyLong()))
.thenReturn(new ArrayList<>());
when(userTaskInstanceRepository.findByCandidateUserId(anyLong()))
.thenReturn(userTaskInstanceEntities);
when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), anySet()))
.thenReturn(userTaskInstanceEntities);
when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), anySet()))
.thenReturn(userTaskInstanceEntities);

List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(1L);
assertEquals(1, result.size());
}

@Test
public void testSetCandidateResourceRole() {
approvalPermissionService.setCandidateResourceRole(1L, "resourceRoleIdentifier");
verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
}

@Test
public void testListApprovableExternalInstances() {
when(userTaskInstanceRepository.findByStatus(any()))
.thenReturn(userTaskInstanceEntities);

List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();
assertEquals(1, result.size());
}

@Test
public void testGetCandidatesByFlowInstanceIds() {
Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = new HashMap<>();
instanceId2CandidateResourceRoleIdentifierIds.put(1L, new HashSet<>(Arrays.asList("resourceRoleIdentifier")));

when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anySet(), any()))
.thenReturn(userTaskInstanceEntities);

when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
.thenReturn(userResourceRoleEntities);

when(userRepository.findByUserIds(anySet()))
.thenReturn(userEntities);

Map<Long, Set<UserEntity>> result = approvalPermissionService.getCandidatesByFlowInstanceIds(new ArrayList<>());
assertEquals(0, result.size());
}
}