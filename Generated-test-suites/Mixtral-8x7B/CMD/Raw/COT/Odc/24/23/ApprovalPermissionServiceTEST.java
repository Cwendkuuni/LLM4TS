import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceCandidateEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.iam.UserEntity;
import com.oceanbase.odc.metadb.iam.UserRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.UserResourceRoleRepository;
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.ApprovalPermissionService;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.User;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;

public class ApprovalPermissionServiceTest {

    @Mock
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserResourceRoleRepository userResourceRoleRepository;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private ResourceRoleService resourceRoleService;

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        approvalPermissionService = new ApprovalPermissionService();
        approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
        approvalPermissionService.userRepository = userRepository;
        approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
        approvalPermissionService.authenticationFacade = authenticationFacade;
        approvalPermissionService.resourceRoleService = resourceRoleService;
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setRoleId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setUserId(2L);

        Set<User> expected = new HashSet<>();
        expected.add(new User(2L));

        doReturn(Collections.singletonList(entity1)).when(userTaskCandidateRepository).findByApprovalInstanceId(anyLong());
        doReturn(Collections.singletonList(entity2)).when(userTaskCandidateRepository).findByApprovalInstanceId(anyLong());
        doReturn(new UserEntity()).when(userRepository).findById(eq(2L));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expected, result);
        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(userRepository).findById(2L);
    }

    @Test
    public void testGetCandidates_with_role() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setRoleId(1L);

        Role role = new Role();
        role.setId(1L);

        Set<User> expected = new HashSet<>();
        expected.add(new User(3L));

        doReturn(Collections.singletonList(entity1)).when(userTaskCandidateRepository).findByApprovalInstanceId(anyLong());
        doReturn(role).when(roleRepository).findById(eq(1L));
        doReturn(new UserEntity()).when(userRepository).findById(eq(3L));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expected, result);
        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findById(1L);
        verify(userRepository).findById(3L);
    }

    @Test
    public void testGetCandidates_with_disabled_role() {
        Long approvalInstanceId = 1L;
        Boolean enabled = false;
        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setRoleId(1L);

        Role role = new Role();
        role.setId(1L);

        Set<User> expected = new HashSet<>();

        doReturn(Collections.singletonList(entity1)).when(userTaskCandidateRepository).findByApprovalInstanceId(anyLong());
        doReturn(role).when(roleRepository).findById(eq(1L));

        Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

        assertEquals(expected, result);
        verify(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);
        verify(roleRepository).findById(1L);
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Long approvalInstanceId1 = 1L;
        Long approvalInstanceId2 = 2L;
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(approvalInstanceId1);
        approvalInstanceIds.add(approvalInstanceId2);

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId1);
        entity1.setRoleId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId1);
        entity2.setRoleId(2L);
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        entity3.setApprovalInstanceId(approvalInstanceId2);
        entity3.setRoleId(3L);

        Map<Long, Set<Long>> expected = new HashMap<>();
        expected.put(approvalInstanceId1, new HashSet<>());
        expected.get(approvalInstanceId1).add(1L);
        expected.get(approvalInstanceId1).add(2L);
        expected.put(approvalInstanceId2, new HashSet<>());
        expected.get(approvalInstanceId2).add(3L);

        doReturn(Arrays.asList(entity1, entity2, entity3))
                .when(userTaskCandidateRepository).findByApprovalInstanceIds(anySet());

        Map<Long, Set<Long>> result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);

        assertEquals(expected, result);
        verify(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Long approvalInstanceId1 = 1L;
        Long approvalInstanceId2 = 2L;
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(approvalInstanceId1);
        approvalInstanceIds.add(approvalInstanceId2);

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId1);
        entity1.setResourceRoleIdentifier("identifier1");
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId1);
        entity2.setResourceRoleIdentifier("identifier2");
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        entity3.setApprovalInstanceId(approvalInstanceId2);
        entity3.setResourceRoleIdentifier("identifier3");

        Map<Long, Set<String>> expected = new HashMap<>();
        expected.put(approvalInstanceId1, new HashSet<>());
        expected.get(approvalInstanceId1).add("identifier1");
        expected.get(approvalInstanceId1).add("identifier2");
        expected.put(approvalInstanceId2, new HashSet<>());
        expected.get(approvalInstanceId2).add("identifier3");

        doReturn(Arrays.asList(entity1, entity2, entity3))
                .when(userTaskCandidateRepository).findByApprovalInstanceIds(anySet());

        Map<Long, Set<String>> result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(
                approvalInstanceIds);

        assertEquals(expected, result);
        verify(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);
    }

    @Test
    public void testGetCandidateUserIds() {
        Long approvalInstanceId1 = 1L;
        Long approvalInstanceId2 = 2L;
        Set<Long> approvalInstanceIds = new HashSet<>();
        approvalInstanceIds.add(approvalInstanceId1);
        approvalInstanceIds.add(approvalInstanceId2);

        UserTaskInstanceCandidateEntity entity1 = new UserTaskInstanceCandidateEntity();
        entity1.setApprovalInstanceId(approvalInstanceId1);
        entity1.setRoleId(1L);
        UserTaskInstanceCandidateEntity entity2 = new UserTaskInstanceCandidateEntity();
        entity2.setApprovalInstanceId(approvalInstanceId1);
        entity2.setResourceRoleIdentifier("identifier1");
        UserTaskInstanceCandidateEntity entity3 = new UserTaskInstanceCandidateEntity();
        entity3.setApprovalInstanceId(approvalInstanceId2);
        entity3.setResourceRoleIdentifier("identifier2");

        when(userRoleRepository.findByRoleIdIn(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(new UserRoleEntity(1L, 10L)));

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(
                Collections.singletonList("identifier1"),
                Collections.singletonList("identifier1")))
                .thenReturn(Collections.singletonList(new UserResourceRoleEntity(1L, 10L, "identifier1")));

        when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(
                Collections.singletonList("identifier2"),
                Collections.singletonList("identifier2")))
                .thenReturn(Collections.singletonList(new UserResourceRoleEntity(2L, 11L, "identifier2")));

        when(userRepository.findByUserIds(Collections.singletonList(10L)))
                .thenReturn(Collections.singletonList(new UserEntity(10L, "username1", true)));

        when(userRepository.findByUserIds(Collections.singletonList(11L)))
                .thenReturn(Collections.singletonList(new UserEntity(11L, "username2", true)));

        Set<Long> expected = new HashSet<>();
        expected.add(10L);
        expected.add(11L);

        assertEquals(expected, approvalPermissionService.getCandidateUserIds(approvalInstanceIds));

        verify(userRoleRepository).findByRoleIdIn(Collections.singletonList(1L));
        verify(userResourceRoleRepository)
                .findByResourceIdsAndResourceRoleIdsIn(Collections.singletonList("identifier1"),
                        Collections.singletonList("identifier1"));
        verify(userResourceRoleRepository)
                .findByResourceIdsAndResourceRoleIdsIn(Collections.singletonList("identifier2"),
                        Collections.singletonList("identifier2"));
        verify(userRepository).findByUserIds(Collections.singletonList(10L));
        verify(userRepository).findByUserIds(Collections.singletonList(11L));
    }

    // Add more test cases for other methods as needed
}