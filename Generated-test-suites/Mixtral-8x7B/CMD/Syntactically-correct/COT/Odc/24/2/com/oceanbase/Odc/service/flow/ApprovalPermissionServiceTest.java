import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.ResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.Role;
import com.oceanbase.odc.service.iam.User;
import com.oceanbase.odc.service.iam.UserService;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;

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
    private UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity;
    private UserEntity userEntity;
    private RoleEntity roleEntity;
    private UserResourceRoleEntity userResourceRoleEntity;
    private List<UserTaskInstanceEntity> userTaskInstanceEntities;
    private List<UserTaskInstanceCandidateEntity> userTaskInstanceCandidateEntities;
    private List<RoleEntity> roleEntities;
    private List<UserResourceRoleEntity> userResourceRoleEntities;

    @Before
    public void setUp() {
        userTaskInstanceEntity1 = new UserTaskInstanceEntity();
        userTaskInstanceEntity1.setId(1L);
        userTaskInstanceEntity1.setFlowInstanceId(1L);
        userTaskInstanceEntity1.setStatus(FlowNodeStatus.EXECUTING);

        userTaskInstanceEntity2 = new UserTaskInstanceEntity();
        userTaskInstanceEntity2.setId(2L);
        userTaskInstanceEntity2.setFlowInstanceId(2L);
        userTaskInstanceEntity2.setStatus(FlowNodeStatus.EXECUTING);

        userTaskInstanceCandidateEntity = new UserTaskInstanceCandidateEntity();
        userTaskInstanceCandidateEntity.setApprovalInstanceId(1L);
        userTaskInstanceCandidateEntity.setRoleId(1L);

        userEntity = new UserEntity();
        userEntity.setId(1L);

        roleEntity = new RoleEntity();
        roleEntity.setId(1L);

        userResourceRoleEntity = new UserResourceRoleEntity();
        userResourceRoleEntity.setResourceRoleId("resourceRole1:role1");
        userResourceRoleEntity.setUserId(1L);

        userTaskInstanceEntities = Arrays.asList(userTaskInstanceEntity1, userTaskInstanceEntity2);
        userTaskInstanceCandidateEntities = Arrays.asList(userTaskInstanceCandidateEntity);
        roleEntities = Arrays.asList(roleEntity);
        userResourceRoleEntities = Arrays.asList(userResourceRoleEntity);

        Mockito.when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(userTaskInstanceEntities);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserId(anyLong()))
                .thenReturn(Arrays.asList(userTaskInstanceEntity1));
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), any()))
                .thenReturn(userTaskInstanceEntities);
        Mockito.when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(anyLong(), any()))
                .thenReturn(userTaskInstanceEntities);
        Mockito.when(userTaskInstanceRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity1));
        Mockito.when(userTaskInstanceRepository.findByFlowInstanceIdAndStatus(any(), any()))
                .thenReturn(userTaskInstanceEntities);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceId(anyLong()))
                .thenReturn(userTaskInstanceCandidateEntities);
        Mockito.when(userTaskCandidateRepository.findByApprovalInstanceIds(any()))
                .thenReturn(userTaskInstanceCandidateEntities);
        Mockito.when(userRepository.findByRoleIdsAndEnabled(any(), anyBoolean()))
                .thenReturn(Arrays.asList(userEntity));
        Mockito.when(userRepository.findByUserIdsAndEnabled(any(), anyBoolean()))
                .thenReturn(Arrays.asList(userEntity));
        Mockito.when(userRepository.findByUserIdsOrRoleIds(any(), any(), anyBoolean()))
                .thenReturn(Arrays.asList(userEntity));
        Mockito.when(userRepository.findByRoleIds(any()))
                .thenReturn(Arrays.asList(userEntity));
        Mockito.when(userRepository.findByUserIds(any()))
                .thenReturn(Arrays.asList(userEntity));
        Mockito.when(roleRepository.findByRoleIdsAndEnabled(any(), anyBoolean()))
                .thenReturn(roleEntities);
        Mockito.when(roleRepository.findByRoleIds(any()))
                .thenReturn(roleEntities);
        Mockito.when(userRoleRepository.findByRoleIdIn(any()))
                .thenReturn(Arrays.asList(new UserRoleEntity()));
        Mockito.when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any(), any()))
                .thenReturn(userResourceRoleEntities);
        Mockito.when(authenticationFacade.currentUserId())
                .thenReturn(1L);
        Mockito.when(userService.getCurrentUserRoleIds())
                .thenReturn(Arrays.asList(1L));
        Mockito.when(userService.getUserRoleIds(anyLong()))
                .thenReturn(Arrays.asList(1L));
        Mockito.when(userService.getCurrentUserResourceRoleIdentifiers())
                .thenReturn(Arrays.asList("resourceRole1:role1"));
        Mockito.when(resourceRoleService.getResourceRoleIdentifiersByUserId(anyLong()))
                .thenReturn(Arrays.asList("resourceRole1:role1"));
    }

    @Test
    public void testGetCandidates() {
        Set<User> users = approvalPermissionService.getCandidates(1L, true);
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(userEntity, users.iterator().next());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> map = approvalPermissionService.getInstanceId2CandidateRoleIds(
                Arrays.asList(1L, 2L));
        Assert.assertEquals(1, map.size());
        Assert.assertEquals(1, map.get(1L).size());
        Assert.assertEquals(1L, map.get(1L).iterator().next());
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> map = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                Arrays.asList(1L, 2L));
        Assert.assertEquals(1, map.size());
        Assert.assertEquals(1, map.get(1L).size());
        Assert.assertEquals("resourceRole1:role1", map.get(1L).iterator().next());
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> userIds = approvalPermissionService.getCandidateUserIds(Arrays.asList(1L, 2L));
        Assert.assertEquals(1, userIds.size());
        Assert.assertEquals(1L, userIds.iterator().next());
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> roles = approvalPermissionService.getCandidateRoles(1L, true);
        Assert.assertEquals(1, roles.size());
        Assert.assertEquals(roleEntity, roles.get(0));
    }

    @Test
    public void testIsApprovable() {
        Mockito.when(approvalPermissionService.getApprovableApprovalInstances())
                .thenReturn(userTaskInstanceEntities);
        Mockito.when(userTaskInstanceRepository.findById(1L))
                .thenReturn(Optional.ofNullable(userTaskInstanceEntity1));
        Assert.assertTrue(approvalPermissionService.isApprovable(1L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> instances =
                approvalPermissionService.getApprovableApprovalInstances(1L);
        Assert.assertEquals(2, instances.size());
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(1L, "resourceRole1:role1");
        Mockito.verify(userTaskCandidateRepository, Mockito.times(1))
                .save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> instances =
                approvalPermissionService.listApprovableExternalInstances();
        Assert.assertEquals(1, instances.size());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> candidates =
                approvalPermissionService.getCandidatesByFlowInstanceIds(Arrays.asList(1L, 2L));
        Assert.assertEquals(1, candidates.size());
        Assert.assertEquals(1, candidates.get(1L).size());
        Assert.assertEquals(userEntity, candidates.get(1L).iterator().next());
    }
}