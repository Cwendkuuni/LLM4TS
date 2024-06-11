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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

@RunWith(MockitoJUnitRunner.class)
public class ApprovalPermissionServiceTest {

    @InjectMocks
    private ApprovalPermissionService approvalPermissionService;

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
    private ResourceRoleService resourceRoleService;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private UserService userService;

    private UserTaskInstanceCandidateEntity candidateEntity;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    private UserEntity userEntity;

    private Role role;

    private UserResourceRoleEntity userResourceRoleEntity;

    @Before
    public void setup() {
        userEntity = new UserEntity();
        userEntity.setId(1L);

        role = new Role();
        role.setId(2L);

        userResourceRoleEntity = new UserResourceRoleEntity();
        userResourceRoleEntity.setUserId(1L);
        userResourceRoleEntity.setResourceRoleId("3:4");

        candidateEntity = new UserTaskInstanceCandidateEntity();
        candidateEntity.setApprovalInstanceId(5L);
        candidateEntity.setRoleId(2L);
        candidateEntity.setResourceRoleIdentifier("3:4");

        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(5L);
        userTaskInstanceEntity.setFlowInstanceId(6L);
    }

    @Test
    public void testGetCandidates() {
        Set<User> users = new HashSet<>();
        users.add(new User(userEntity));

        when(userTaskCandidateRepository.findByApprovalInstanceId(5L))
                .thenReturn(Collections.singletonList(candidateEntity));
        when(userRepository.findByRoleIdsAndEnabled(Collections.singleton(2L), true))
                .thenReturn(Collections.singletonList(userEntity));

        assertEquals(users, approvalPermissionService.getCandidates(5L, true));
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> result = new HashMap<>();
        result.put(5L, new HashSet<>(Collections.singleton(2L)));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(Collections.singleton(5L)))
                .thenReturn(Collections.singletonList(candidateEntity));

        assertEquals(result, approvalPermissionService.getInstanceId2CandidateRoleIds(Collections.singleton(5L)));
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> result = new HashMap<>();
        result.put(5L, new HashSet<>(Collections.singleton("3:4")));

        when(userTaskCandidateRepository.findByApprovalInstanceIds(Collections.singleton(5L)))
                .thenReturn(Collections.singletonList(candidateEntity));

        assertEquals(result,
                approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singleton(5L)));
    }

    @Test
    public void testGetCandidateUserIds() {
        Set<Long> result = new HashSet<>(Collections.singleton(1L));

        when(userRoleRepository.findByRoleIdIn(Collections.singleton(2L)))
                .thenReturn(Collections.singletonList(new UserRoleEntity()));
        when(userRoleEntity.getUserId()).thenReturn(1L);

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("3:4")))
                .thenReturn(Collections.singletonList(userResourceRoleEntity));

        assertEquals(result, approvalPermissionService.getCandidateUserIds(Collections.singleton(5L)));
    }

    @Test
    public void testGetCandidateRoles() {
        List<Role> result = new ArrayList<>();
        result.add(role);

        when(userTaskCandidateRepository.findByApprovalInstanceId(5L))
                .thenReturn(Collections.singletonList(candidateEntity));
        when(roleRepository.findByRoleIdsAndEnabled(Collections.singleton(2L), true))
                .thenReturn(Collections.singletonList(role));

        assertEquals(result, approvalPermissionService.getCandidateRoles(5L, true));
    }

    @Test
    public void testIsApprovable() {
        when(authenticationFacade.currentUserId()).thenReturn(1L);
        when(userService.getUserRoleIds(1L)).thenReturn(Collections.singleton(2L));
        when(userService.getResourceRoleIdentifiersByUserId(1L))
                .thenReturn(Collections.singleton("3:4"));
        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singleton("3:4")))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        when(userTaskInstanceRepository.findByCandidateUserId(1L))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));
        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
                1L, Collections.singleton("3:4")))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));

        assertTrue(approvalPermissionService.isApprovable(5L));
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> result = new ArrayList<>();
        result.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByCandidateUserId(1L))
                .thenReturn(result);

        assertEquals(result, approvalPermissionService.getApprovableApprovalInstances(1L));
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(5L, "3:4");

        verify(userTaskCandidateRepository, times(1)).save(candidateEntity);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> result = new ArrayList<>();
        result.add(userTaskInstanceEntity);

        when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
                .thenReturn(result);
        when(userTaskInstanceEntity.getExternalApprovalId()).thenReturn("1");
        when(userTaskInstanceEntity.getExternalFlowInstanceId()).thenReturn("2");

        assertEquals(result, approvalPermissionService.listApprovableExternalInstances());
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> result = new HashMap<>();
        Set<UserEntity> users = new HashSet<>();
        users.add(userEntity);

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singleton(6L), FlowNodeStatus.EXECUTING))
                .thenReturn(Collections.singletonList(userTaskInstanceEntity));

        when(userTaskCandidateRepository.findCandidateResourceRoleIdentifierIdsByApprovalInstanceIds(
                Collections.singleton(5L)))
                .thenReturn(Collections.singleton(userResourceRoleEntity));

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(Collections.singleton("3:4")))
                .thenReturn(Collections.singletonList(userResourceRoleEntity));

        when(userRepository.findByUserIds(Collections.singleton(1L)))
                .thenReturn(Collections.singletonList(userEntity));

        result.put(6L, users);

        assertEquals(result, approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singleton(6L)));
    }
}