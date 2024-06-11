import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.MockBean;
import org.springframework.context.annotation.Primary;
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
import com.oceanbase.odc.service.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNode;
import com.oceanbase.odc.service.flow.model.FlowNode.FlowNodeBuilder;
import com.oceanbase.odc.service.flow.model.User;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;

@Configuration
class TestConfig {

    @Bean
    @Primary
    public UserTaskInstanceCandidateRepository userTaskCandidateRepository() {
        return mock(UserTaskInstanceCandidateRepository.class);
    }

    @Bean
    @Primary
    public UserTaskInstanceRepository userTaskInstanceRepository() {
        return mock(UserTaskInstanceRepository.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    @Primary
    public RoleRepository roleRepository() {
        return mock(RoleRepository.class);
    }

    @Bean
    @Primary
    public UserRoleRepository userRoleRepository() {
        return mock(UserRoleRepository.class);
    }

    @Bean
    @Primary
    public UserResourceRoleRepository userResourceRoleRepository() {
        return mock(UserResourceRoleRepository.class);
    }

    @Bean
    @Primary
    public AuthenticationFacade authenticationFacade() {
        return mock(AuthenticationFacade.class);
    }

    @Bean
    @Primary
    public ResourceRoleService resourceRoleService() {
        return mock(ResourceRoleService.class);
    }

    @Bean
    @Primary
    public UserService userService() {
        return mock(UserService.class);
    }
}

@Service
class TestApprovalPermissionService extends ApprovalPermissionService {

    public TestApprovalPermissionService(UserTaskInstanceCandidateRepository userTaskCandidateRepository,
            UserTaskInstanceRepository userTaskInstanceRepository, UserRepository userRepository,
            RoleRepository roleRepository, UserRoleRepository userRoleRepository,
            UserResourceRoleRepository userResourceRoleRepository, AuthenticationFacade authenticationFacade,
            ResourceRoleService resourceRoleService, UserService userService) {
        super(userTaskCandidateRepository, userTaskInstanceRepository, userRepository, roleRepository,
                userRoleRepository, userResourceRoleRepository, authenticationFacade, resourceRoleService,
                userService);
    }
}

public class ApprovalPermissionServiceTest {

    private static final Long APPROVAL_INSTANCE_ID = 1L;
    private static final Long USER_ID = 1L;
    private static final Long ROLE_ID = 1L;
    private static final String RESOURCE_ROLE_IDENTIFIER = "test";
    private static final String RESOURCE_ROLE_IDENTIFIER_2 = "test2";
    private static final String FLOW_INSTANCE_ID = "testFlowInstanceId";

    private ApprovalPermissionService approvalPermissionService;

    @Before
    public void setUp() {
        ApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        approvalPermissionService = context.getBean(TestApprovalPermissionService.class);

        // mock autowired objects
        when(authenticationFacade.currentUserId()).thenReturn(USER_ID);
        when(userRepository.findByUserIds(any())).thenReturn(Collections.singletonList(new UserEntity()));
        when(roleRepository.findByRoleIdsAndEnabled(any(), anyBoolean())).thenReturn(Collections.singletonList(
                new RoleEntity()));
        when(
                userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(any(), any()))
                        .thenReturn(Collections.singletonList(new UserResourceRoleEntity()));
        when(userService.getCurrentUserRoleIds()).thenReturn(Collections.singleton(ROLE_ID));
        when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(Collections.singleton(RESOURCE_ROLE_IDENTIFIER));
    }

    @Test
    public void testGetCandidates() {
        Set<User> candidates = approvalPermissionService.getCandidates(APPROVAL_INSTANCE_ID, true);
        assertNotNull(candidates);
    }

    @Test
    public void testgetInstanceId2CandidateRoleIds() {
        Map<Long, Set<Long>> instanceId2CandidateRoleIds = approvalPermissionService
                .getInstanceId2CandidateRoleIds(Collections.singleton(APPROVAL_INSTANCE_ID));
        assertNotNull(instanceId2CandidateRoleIds);
    }

    @Test
    public void testgetInstanceId2CandidateResourceRoleIdentifierIds() {
        Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds = approvalPermissionService
                .getInstanceId2CandidateResourceRoleIdentifierIds(Collections.singleton(APPROVAL_INSTANCE_ID));
        assertNotNull(instanceId2CandidateResourceRoleIdentifierIds);
    }

    @Test
    public void testgetCandidateUserIds() {
        Set<Long> candidateUserIds = approvalPermissionService.getCandidateUserIds(Collections.singleton(APPROVAL_INSTANCE_ID));
        assertNotNull(candidateUserIds);
    }

    @Test
    public void testgetCandidateRoles() {
        List<Role> candidateRoles = approvalPermissionService.getCandidateRoles(APPROVAL_INSTANCE_ID, true);
        assertNotNull(candidateRoles);
    }

    @Test
    public void testIsApprovable() {
        boolean approvable = approvalPermissionService.isApprovable(APPROVAL_INSTANCE_ID);
        assertTrue(approvable);
    }

    @Test
    public void testGetApprovableApprovalInstances() {
        List<UserTaskInstanceEntity> approvableApprovalInstances = approvalPermissionService
                .getApprovableApprovalInstances(USER_ID);
        assertNotNull(approvableApprovalInstances);
    }

    @Test
    public void testSetCandidateResourceRole() {
        approvalPermissionService.setCandidateResourceRole(APPROVAL_INSTANCE_ID, RESOURCE_ROLE_IDENTIFIER);
    }

    @Test
    public void testListApprovableExternalInstances() {
        List<UserTaskInstanceEntity> approvableExternalInstances = approvalPermissionService
                .listApprovableExternalInstances();
        assertNotNull(approvableExternalInstances);
    }

    @Test
    public void testGetCandidatesByFlowInstanceIds() {
        Map<Long, Set<UserEntity>> candidatesByFlowInstanceIds = approvalPermissionService
                .getCandidatesByFlowInstanceIds(Collections.singleton(APPROVAL_INSTANCE_ID));
        assertNotNull(candidatesByFlowInstanceIds);
    }
}