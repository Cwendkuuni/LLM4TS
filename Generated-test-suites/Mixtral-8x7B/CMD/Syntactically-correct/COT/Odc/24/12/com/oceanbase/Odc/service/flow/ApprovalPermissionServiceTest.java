import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

/**
 * ApprovalPermissionServiceTest
 *
 * @author yh263208
 * @date 2022-03-09 19:07
 * @since ODC_release_3.3.0
 */
@Service
@SkipAuthorize("odc internal usage")
public class ApprovalPermissionServiceTest {

    private ApprovalPermissionService approvalPermissionService;

    private UserTaskInstanceRepository userTaskInstanceRepository;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private UserRoleRepository userRoleRepository;
    private UserResourceRoleRepository userResourceRoleRepository;
    private UserService userService;
    private AuthenticationFacade authenticationFacade;
    private ResourceRoleService resourceRoleService;
    private UserTaskInstanceCandidateRepository userTaskCandidateRepository;

    @Before
    public void setup() {
        userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
        userRepository = mock(UserRepository.class);
        roleRepository = mock(RoleRepository.class);
        userRoleRepository = mock(UserRoleRepository.class);
        userResourceRoleRepository = mock(UserResourceRoleRepository.class);
        userService = mock(UserService.class);
        authenticationFacade = mock(AuthenticationFacade.class);
        resourceRoleService = mock(ResourceRoleService.class);
        userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);

        approvalPermissionService = new ApprovalPermissionService();
        approvalPermissionService.userTaskInstanceRepository = userTaskInstanceRepository;
        approvalPermissionService.userRepository = userRepository;
        approvalPermissionService.roleRepository = roleRepository;
        approvalPermissionService.userRoleRepository = userRoleRepository;
        approvalPermissionService.userResourceRoleRepository = userResourceRoleRepository;
        approvalPermissionService.userService = userService;
        approvalPermissionService.authenticationFacade = authenticationFacade;
        approvalPermissionService.resourceRoleService = resourceRoleService;
        approvalPermissionService.userTaskCandidateRepository = userTaskCandidateRepository;

        // initialize mocks
        doReturn(new ArrayList<>()).when(userTaskInstanceRepository).findByCandidateUserId(any());
        doReturn(new ArrayList<>()).when(userTaskInstanceRepository).findByCandidateUserIdOrResourceRoleIdentifier(
                any(), any());
        doReturn(new ArrayList<>()).when(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(any(), any());
        doReturn(new ArrayList<>()).when(userTaskInstanceRepository).findByApprovalInstanceId(any());
        doReturn(new ArrayList<>()).when(userTaskInstanceRepository).findByApprovalInstanceIds(any());
        doReturn(new ArrayList<>()).when(userRepository).findByRoleIdsAndEnabled(any(), any());
        doReturn(new ArrayList<>()).when(userRepository).findByUserIdsAndEnabled(any(), any());
        doReturn(new ArrayList<>()).when(userRepository).findByUserIdsOrRoleIds(any(), any(), any());
        doReturn(new ArrayList<>()).when(roleRepository).findByRoleIdsAndEnabled(any(), any());
        doReturn(new ArrayList<>()).when(userRoleRepository).findByRoleIdIn(any());
        doReturn(new ArrayList<>()).when(userRoleRepository).findByRoleIds(any());
        doReturn(new ArrayList<>()).when(userResourceRoleRepository).findByResourceIdsAndResourceRoleIdsIn(any());
        doReturn(new ArrayList<>()).when(userResourceRoleRepository).findByResourceIds(any());
    }

    @Test
    public void testGetCandidates() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        approvalPermissionService.getCandidates(approvalInstanceId, enabled);
    }

    @Test
    public void testGetInstanceId2CandidateRoleIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
    }

    @Test
    public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
    }

    @Test
    public void testGetCandidateUserIds() {
        Collection<Long> approvalInstanceIds = new ArrayList<>();
        approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
    }

    @Test
    public void testGetCandidateRoles() {
        Long approvalInstanceId = 1L;
        Boolean enabled = true;
        approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
    }

    @Test
    public void testIsApprovable() {
        Long approvalInstanceId = 1L;
        approvalPermissionService.isApprovable(approvalInstanceId);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        Long approvalInstanceId = 1L;
        approvalPermissionService.getApprovableApprovalInstances(approvalInstanceId);
    }

    @Test
    public void testGetApprovableApprovalInstances_with_userId() {
        long userId = 1;
        approvalPermissionService.getApprovableApprovalInstances(userId);
    }

    @Test
    public void testSetCandidateResourceRole() {
        Long approvalInstanceId = 1L;
        String resourceRoleIdentifier = "resource_role_identifier";
        approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
    }

    @Test
    public void testListApprovableExternalInstances() {
        approvalPermissionService.listApprovableExternalInstances();
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds);
    }

}