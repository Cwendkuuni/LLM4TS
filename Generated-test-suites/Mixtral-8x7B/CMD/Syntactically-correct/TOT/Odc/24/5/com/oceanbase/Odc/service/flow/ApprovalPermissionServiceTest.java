import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.iam.Role;
import com.oceanbase.odc.service.iam.User;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserService userService;

    @Mock
    private ResourceRoleService resourceRoleService;

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    private static final long TEST_USER_ID = 1L;
    private static final long TEST_ROLE_ID = 2L;
    private static final long TEST_APPROVAL_INSTANCE_ID = 3L;
    private static final String TEST_RESOURCE_ROLE_IDENTIFIER = "test_resource_role_identifier";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(approvalPermissionService, "currentUserId", TEST_USER_ID);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(TEST_USER_ID);
        userEntity.setName("test_user_name");

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(TEST_ROLE_ID);
        roleEntity.setName("test_role_name");

        when(userRepository.findById(TEST_USER_ID)).thenReturn(java.util.Optional.of(userEntity));
        when(roleRepository.findById(TEST_ROLE_ID)).thenReturn(java.util.Optional.of(roleEntity));

        User user = new User();
        user.setId(TEST_USER_ID);
        user.setName("test_user_name");

        Role role = new Role();
        role.setId(TEST_ROLE_ID);
        role.setName("test_role_name");

        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.getUserRoleIds(TEST_USER_ID)).thenReturn(new HashSet<>(Arrays.asList(TEST_ROLE_ID)));
        when(userService.getUserResourceRoleIdentifiers(TEST_USER_ID))
                .thenReturn(Collections.singleton(TEST_RESOURCE_ROLE_IDENTIFIER));

        when(resourceRoleService.getResourceRoleIdentifiersByUserId(TEST_USER_ID))
                .thenReturn(new HashSet<>(Arrays.asList(TEST_RESOURCE_ROLE_IDENTIFIER)));

        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(TEST_APPROVAL_INSTANCE_ID);
        userTaskInstanceEntity.setFlowInstanceId(TEST_APPROVAL_INSTANCE_ID);
        userTaskInstanceEntity.setStatus(FlowNodeStatus.EXECUTING);

        when(userTaskInstanceRepository.findById(TEST_APPROVAL_INSTANCE_ID))
                .thenReturn(java.util.Optional.of(userTaskInstanceEntity));

        UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity = new UserTaskInstanceCandidateEntity();
        userTaskInstanceCandidateEntity.setId(1L);
        userTaskInstanceCandidateEntity.setApprovalInstanceId(TEST_APPROVAL_INSTANCE_ID);
        userTaskInstanceCandidateEntity.setResourceRoleIdentifier(TEST_RESOURCE_ROLE_IDENTIFIER);

        when(userTaskCandidateRepository.findByApprovalInstanceId(TEST_APPROVAL_INSTANCE_ID))
                .thenReturn(Arrays.asList(userTaskInstanceCandidateEntity));

        UserResourceRoleEntity userResourceRoleEntity = new UserResourceRoleEntity();
        userResourceRoleEntity.setResourceRoleIdentifier(TEST_RESOURCE_ROLE_IDENTIFIER);
        userResourceRoleEntity.setUserId(TEST_USER_ID);

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
                .thenReturn(Arrays.asList(userResourceRoleEntity));
    }

    @Test
    public void testGetCandidates() {
        Set<User> users = approvalPermissionService.getCandidates(TEST_APPROVAL_INSTANCE_ID, null);
        assertThat(users, notNullValue());
        assertThat(users.size(), equalTo(1));
        User user = users.iterator().next();
        assertThat(user.getId(), equalTo(TEST_USER_ID));
        assertThat(user.getName(), equalTo("test_user_name"));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(TEST_APPROVAL_INSTANCE_ID);

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        Set<Long> roleIds = result.get(TEST_APPROVAL_INSTANCE_ID);
        assertThat(roleIds, notNullValue());
        assertThat(roleIds.size(), equalTo(0));

        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(TEST_APPROVAL_INSTANCE_ID);

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);
        assertThat(result, notNullValue());
        assertThat(result.size(), equalTo(1));
        Set<String> resourceRoleIdentifierIds = result.get(TEST_APPROVAL_INSTANCE_ID);
        assertThat(resourceRoleIdentifierIds, notNullValue());
        assertThat(resourceRoleIdentifierIds.size(), equalTo(1));
        assertThat(resourceRoleIdentifierIds.contains(TEST_RESOURCE_ROLE_IDENTIFIER), equalTo(true));

        verifyNoMoreInteractions(resourceRoleService);
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(TEST_APPROVAL_INSTANCE_ID);

        Set<Long> results = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(1));
        assertThat(results.contains(TEST_USER_ID), equalTo(true));

        verifyNoMoreInteractions(userRepository);
        verifyNoMoreInteractions(userRoleRepository);
        verifyNoMoreInteractions(userResourceRoleRepository);
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> roles = approvalPermissionService.getCandidateRoles(TEST_APPROVAL_INSTANCE_ID, null);
        assertThat(roles, notNullValue());
        assertThat(roles.size(), equalTo(0));

        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    public void testIsApprovable() {
        boolean result = approvalPermissionService.isApprovable(TEST_APPROVAL_INSTANCE_ID);
        assertThat(result, equalTo(true));

        verify(userTaskInstanceRepository).findById(TEST_APPROVAL_INSTANCE_ID);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> results = approvalPermissionService.getApprovableApprovalInstances();
        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(1));
        UserTaskInstanceEntity result = results.get(0);
        assertThat(result.getId(), equalTo(TEST_APPROVAL_INSTANCE_ID));

        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
        verify(userRepository).findByUserIds(Collections.singleton(TEST_USER_ID));
        verify(userRoleRepository).findByRoleIdIn(Collections.singleton(TEST_ROLE_ID));
        verify(userResourceRoleRepository).findByResourceIdsAndResourceRoleIdsIn(
                Collections.singleton(TEST_RESOURCE_ROLE_IDENTIFIER));
    }

    @Test
    public void testGetApprovableApprovalInstances_userId() {
        List<UserTaskInstanceEntity> results = approvalPermissionService.getApprovableApprovalInstances(TEST_USER_ID);
        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(1));
        UserTaskInstanceEntity result = results.get(0);
        assertThat(result.getId(), equalTo(TEST_APPROVAL_INSTANCE_ID));

        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(TEST_USER_ID,
                Collections.singleton(TEST_ROLE_ID));
        verify(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(TEST_USER_ID,
                Collections.singleton(TEST_RESOURCE_ROLE_IDENTIFIER));
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(TEST_APPROVAL_INSTANCE_ID,
                TEST_RESOURCE_ROLE_IDENTIFIER);

        verify(userTaskCandidateRepository).save(any(UserTaskInstanceCandidateEntity.class));
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> results = approvalPermissionService.listApprovableExternalInstances();
        assertThat(results, notNullValue());
        // assuming there is at least one executing instance with external approval
        assertThat(results.size(), equalTo(1));

        verify(userTaskInstanceRepository).findByStatus(FlowNodeStatus.EXECUTING);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        List<Long> flowInstanceIds = Collections.singletonList(TEST_APPROVAL_INSTANCE_ID);

        Map<Long, Set<UserEntity>> results = approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(1));
        Set<UserEntity> userEntities = results.get(TEST_APPROVAL_INSTANCE_ID);
        assertThat(userEntities, notNullValue());
        assertThat(userEntities.size(), equalTo(1));
        UserEntity userEntity = userEntities.iterator().next();
        assertThat(userEntity.getId(), equalTo(TEST_USER_ID));

        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                FlowNodeStatus.EXECUTING);
        verify(userTaskCandidateRepository).findByApprovalInstanceIdIn(flowInstanceIds);
        verify(userResourceRoleRepository).findByResourceIdsAndResourceRoleIdsIn(anySet());
        verify(userRepository).findByUserIds(any());
        verifyNoMoreInteractions(userRepository);
    }
}