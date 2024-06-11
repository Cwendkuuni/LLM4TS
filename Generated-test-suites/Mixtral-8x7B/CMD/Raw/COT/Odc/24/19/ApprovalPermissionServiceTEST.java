import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.apache.commons.collections4.CollectionUtils;
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

    private UserTaskInstanceEntity userTaskInstanceEntity1;
    private UserTaskInstanceEntity userTaskInstanceEntity2;
    private UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity1;
    private UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity2;
    private UserEntity userEntity1;
    private UserEntity userEntity2;
    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private UserRoleEntity userRoleEntity1;
    private UserRoleEntity userRoleEntity2;
    private UserResourceRoleEntity userResourceRoleEntity1;

    @Before
    public void setUp() {
        userEntity1 = new UserEntity();
        userEntity1.setId(1L);

        userEntity2 = new UserEntity();
        userEntity2.setId(2L);

        roleEntity1 = new RoleEntity();
        roleEntity1.setId(3L);

        roleEntity2 = new RoleEntity();
        roleEntity2.setId(4L);

        userRoleEntity1 = new UserRoleEntity();
        userRoleEntity1.setRoleId(roleEntity1.getId());
        userRoleEntity1.setUserId(userEntity1.getId());

        userRoleEntity2 = new UserRoleEntity();
        userRoleEntity2.setRoleId(roleEntity2.getId());
        userRoleEntity2.setUserId(userEntity2.getId());

        userResourceRoleEntity1 = new UserResourceRoleEntity();
        userResourceRoleEntity1.setResourceId("resource1");
        userResourceRoleEntity1.setResourceRoleId("resourceRole1");
        userResourceRoleEntity1.setUserId(userEntity1.getId());

        userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        userTaskInstanceEntity1.setId(5L);
        userTaskInstanceEntity1.setCandidateUserId(userEntity1.getId());
        userTaskInstanceEntity1.setStatus(FlowNodeStatus.EXECUTING);

        userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        userTaskInstanceEntity2.setId(6L);
        userTaskInstanceEntity2.setCandidateUserId(userEntity2.getId());
        userTaskInstanceEntity2.setStatus(FlowNodeStatus.EXECUTING);

        userTaskInstanceCandidateEntity1 = new UserTaskInstanceCandidateEntity();
        userTaskInstanceCandidateEntity1.setApprovalInstanceId(userTaskInstanceEntity1.getId());
        userTaskInstanceCandidateEntity1.setResourceRoleIdentifier("resourceRole1");

        userTaskInstanceCandidateEntity2 = new UserTaskInstanceCandidateEntity();
        userTaskInstanceCandidateEntity2.setApprovalInstanceId(userTaskInstanceEntity2.getId());
        userTaskInstanceCandidateEntity2.setRoleId(roleEntity2.getId());
    }

    @Test
    public void testGetCandidates() {
        Set<User> users = new HashSet<>();
        users.add(new User(userEntity1));
        when(userRepository.findByRoleIdsAndEnabled(any(), anyBoolean())).thenReturn(Arrays.asList(userEntity1));
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(Arrays.asList(userTaskInstanceCandidateEntity1, userTaskInstanceCandidateEntity2));
        when(userRepository.findByUserIds(any())).thenReturn(Arrays.asList(userEntity1, userEntity2));

        Set<User> result = approvalPermissionService.getCandidates(1L, true);

        verify(userRepository, times(1)).findByRoleIdsAndEnabled(any(), anyBoolean());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(anyLong());
        verify(userRepository, times(1)).findByUserIds(any());

        assertEquals("The result should contain 1 user.", 1, result.size());
        assertEquals("The result should contain the correct user.", userEntity1.getId(), result.iterator().next().getId());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> result = new HashMap<>();
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(
                Arrays.asList(userTaskInstanceCandidateEntity1, userTaskInstanceCandidateEntity2));

        result = approvalPermissionService.getInstanceId2CandidateRoleIds(Arrays.asList(1L, 2L));

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(any());

        assertEquals("The result should contain two entries.", 2, result.size());
        assertEquals("The first entry key should be 1.", 1L, result.keySet().toArray()[0]);
        assertEquals("The second entry key should be 2.", 2L, result.keySet().toArray()[1]);
        assertEquals("The first entry value should contain 4.", 1, result.get(1L).size());
        assertEquals("The second entry value should contain 3.", 1, result.get(2L).size());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> result = new HashMap<>();
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(
                Arrays.asList(userTaskInstanceCandidateEntity1, userTaskInstanceCandidateEntity2));

        result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Arrays.asList(1L, 2L));

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(any());

        assertEquals("The result should contain two entries.", 2, result.size());
        assertEquals("The first entry key should be 1.", 1L, result.keySet().toArray()[0]);
        assertEquals("The second entry key should be 2.", 2L, result.keySet().toArray()[1]);
        assertEquals("The first entry value should contain 'resourceRole1'.", 1, result.get(1L).size());
        assertEquals("The second entry value should be empty.", 0, result.get(2L).size());
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> result = new HashSet<>();
        when(userTaskCandidateRepository.findByApprovalInstanceIds(any())).thenReturn(
                Arrays.asList(userTaskInstanceCandidateEntity1, userTaskInstanceCandidateEntity2));
        when(userRoleRepository.findByRoleIdIn(any())).thenReturn(Arrays.asList(userRoleEntity1));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any(), any()))
                .thenReturn(Arrays.asList(userResourceRoleEntity1));

        result = approvalPermissionService.getCandidateUserIds(Arrays.asList(1L, 2L));

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceIds(any());
        verify(userRoleRepository, times(1)).findByRoleIdIn(any());
        verify(userResourceRoleRepository, times(1))
                .findByResourceIdsAndResourceRoleIdsIn(any(), any());

        assertEquals("The result should contain two user ids.", 2, result.size());
        assertEquals("The first user id should be 1.", 1L, result.toArray()[0]);
        assertEquals("The second user id should be 2.", 2L, result.toArray()[1]);
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> roles = new ArrayList<>();
        roles.add(new Role(roleEntity1));
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(Arrays.asList(userTaskInstanceCandidateEntity1, userTaskInstanceCandidateEntity2));
        when(roleRepository.findByRoleIdsAndEnabled(any(), anyBoolean())).thenReturn(Arrays.asList(roleEntity1));

        roles = approvalPermissionService.getCandidateRoles(1L, true);

        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(anyLong());
        verify(roleRepository, times(1)).findByRoleIdsAndEnabled(any(), anyBoolean());

        assertEquals("The result should contain 1 role.", 1, roles.size());
        assertEquals("The result should contain the correct role.", roleEntity1.getId(), roles.get(0).getId());
    }

    @Test
    public void testIsApprovable() {
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singletonList(roleEntity1.getId()));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.singletonList("resourceRole1"));
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(any())).thenReturn(
                Arrays.asList(userTaskInstanceEntity1));
        when(userRoleRepository.findByRoleIdIn(any())).thenReturn(Collections.singletonList(userRoleEntity1));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any(), any()))
                .thenReturn(Collections.singletonList(userResourceRoleEntity1));
        when(userRepository.findByUserIds(any())).thenReturn(Arrays.asList(userEntity1));

        boolean result = approvalPermissionService.isApprovable(5L);

        verify(authenticationFacade, times(1)).currentUserId();
        verify(userService, times(1)).getCurrentUserRoleIds();
        verify(userService, times(1)).getCurrentUserResourceRoleIdentifiers();
        verify(userTaskInstanceRepository, times(1))
                .findByResourceRoleIdentifierIn(any());
        verify(userRoleRepository, times(1)).findByRoleIdIn(any());
        verify(userResourceRoleRepository, times(1))
                .findByResourceIdsAndResourceRoleIdsIn(any(), any());
        verify(userRepository, times(1)).findByUserIds(any());

        assertEquals("The result should be true.", true, result);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(userTaskInstanceEntity1);
        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING)).thenReturn(userTaskInstanceEntities);

        List<UserTaskInstanceEntity> result = approvalPermissionService.listApprovableExternalInstances();

        verify(userTaskInstanceRepository, times(1)).findByStatus(FlowNodeStatus.EXECUTING);

        assertEquals("The result should contain 1 user task instance entity.", 1, result.size());
        assertEquals("The result should contain the correct user task instance entity.",
                userTaskInstanceEntity1.getId(), result.get(0).getId());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> result = new HashMap<>();
        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(any(), any()))
                .thenReturn(Arrays.asList(userTaskInstanceEntity1));
        when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(Arrays.asList(userTaskInstanceCandidateEntity1));
        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any(), any()))
                .thenReturn(Arrays.asList(userResourceRoleEntity1));
        when(userRepository.findByUserIds(any())).thenReturn(Arrays.asList(userEntity1));

        result = approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L));

        verify(userTaskInstanceRepository, times(1))
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(any(), any());
        verify(userTaskCandidateRepository, times(1)).findByApprovalInstanceId(anyLong());
        verify(userResourceRoleRepository, times(1))
                .findByResourceIdsAndResourceRoleIdsIn(any(), any());
        verify(userRepository, times(1)).findByUserIds(any());

        assertEquals("The result should contain 1 entry.", 1, result.size());
        assertEquals("The entry key should be 5.", 5L, result.keySet().toArray()[0]);
        assertEquals("The entry value should contain 1 user entity.", 1, result.get(5L).size());
        assertEquals("The entry value should contain the correct user entity.", userEntity1.getId(),
                result.get(5L).iterator().next().getId());
    }
}