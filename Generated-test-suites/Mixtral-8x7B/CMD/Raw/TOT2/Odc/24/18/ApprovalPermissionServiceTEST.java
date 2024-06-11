import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
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

@Service
@SkipAuthorize("odc internal usage")
public class ApprovalPermissionServiceTest {

@Autowired
private UserService userService;
@Autowired
private ResourceRoleService resourceRoleService;
@Autowired
private AuthenticationFacade authenticationFacade;
@Autowired
private UserTaskInstanceCandidateRepository userTaskCandidateRepository;
@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;
@Autowired
private UserRepository userRepository;
@Autowired
private RoleRepository roleRepository;
@Autowired
private UserRoleRepository userRoleRepository;
@Autowired
private UserResourceRoleRepository userResourceRoleRepository;

private ApprovalPermissionService approvalPermissionService;

@Before
public void setUp() {
userService = mock(UserService.class);
resourceRoleService = mock(ResourceRoleService.class);
authenticationFacade = mock(AuthenticationFacade.class);
userTaskCandidateRepository = mock(UserTaskInstanceCandidateRepository.class);
userTaskInstanceRepository = mock(UserTaskInstanceRepository.class);
userRepository = mock(UserRepository.class);
roleRepository = mock(RoleRepository.class);
userRoleRepository = mock(UserRoleRepository.class);
userResourceRoleRepository = mock(UserResourceRoleRepository.class);

approvalPermissionService = new ApprovalPermissionService();
approvalPermissionService.setUserService(userService);
approvalPermissionService.setResourceRoleService(resourceRoleService);
approvalPermissionService.setAuthenticationFacade(authenticationFacade);
approvalPermissionService.setUserTaskCandidateRepository(userTaskCandidateRepository);
approvalPermissionService.setUserTaskInstanceRepository(userTaskInstanceRepository);
approvalPermissionService.setUserRepository(userRepository);
approvalPermissionService.setRoleRepository(roleRepository);
approvalPermissionService.setUserRoleRepository(userRoleRepository);
approvalPermissionService.setUserResourceRoleRepository(userResourceRoleRepository);
}

@Test
public void testGetCandidates() {
// Test with enabled = true
Long approvalInstanceId = 1L;
Boolean enabled = true;
doReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity())).when(userTaskCandidateRepository)
.findByApprovalInstanceId(approvalInstanceId);
doReturn(Collections.singletonList(1L)).when(userRepository).findByRoleIdsAndEnabled(anySet(), eq(enabled))
.stream().map(UserEntity::getId).collect(Collectors.toList());
doReturn(Collections.emptyList()).when(userRepository).findByUserIdsAndEnabled(anySet(), eq(enabled))
.stream().map(UserEntity::getId).collect(Collectors.toList());
doReturn(Collections.singletonList(new Pair<>("ROLE", 1L))).when(userTaskCandidateRepository)
.findByApprovalInstanceId(approvalInstanceId);
doReturn(Collections.singletonList(1L)).when(userRoleRepository).findByRoleIdIn(anySet())
.stream().map(UserRoleEntity::getUserId).collect(Collectors.toSet());
doReturn(Collections.singletonList(new UserEntity())).when(userRepository).findByUserIds(anySet());

Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
assertEquals(1, result.size());

// Test with enabled = false
enabled = false;
doReturn(Collections.singletonList(new UserEntity())).when(userRepository).findByRoleIdsAndEnabled(anySet(), eq(enabled))
.stream().map(UserEntity::getId).collect(Collectors.toList());
doReturn(Collections.singletonList(1L)).when(userRepository).findByUserIdsAndEnabled(anySet(), eq(enabled))
.stream().map(UserEntity::getId).collect(Collectors.toList());

result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
assertEquals(1, result.size());

// Test with enabled = null
enabled = null;
doReturn(Collections.singletonList(new UserEntity())).when(userRepository).findByRoleIdsAndEnabled(anySet(), eq(enabled))
.stream().map(UserEntity::getId).collect(Collectors.toList());
doReturn(Collections.singletonList(1L)).when(userRepository).findByUserIdsAndEnabled(anySet(), eq(enabled))
.stream().map(UserEntity::getId).collect(Collectors.toList());

result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);
assertEquals(1, result.size());
}

@Test
public void testGetInstanceId2CandidateRoleIds() {
// Test with empty approvalInstanceIds
Collection<Long> approvalInstanceIds = new ArrayList<>();
doReturn(Collections.emptyList()).when(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);

Map<Long, Set<Long>> result = approvalPermissionService
.getInstanceId2CandidateRoleIds(approvalInstanceIds);
assertEquals(0, result.size());

// Test with non-empty approvalInstanceIds
approvalInstanceIds.add(1L);
doReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity())).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);
doReturn(Collections.singletonList(1L)).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);
doReturn(Collections.singletonList(new Pair<>("ROLE", 1L))).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);
doReturn(Collections.singletonList(new UserRoleEntity())).when(userRoleRepository).findByRoleIdIn(anySet());

result = approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds);
assertEquals(1, result.size());
}

@Test
public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
// Test with empty approvalInstanceIds
Collection<Long> approvalInstanceIds = new ArrayList<>();
doReturn(Collections.emptyList()).when(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);

Map<Long, Set<String>> result = approvalPermissionService
.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
assertEquals(0, result.size());

// Test with non-empty approvalInstanceIds
approvalInstanceIds.add(1L);
doReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity())).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);
doReturn(Collections.singletonList(new Pair<>("RESOURCE_ROLE_IDENTIFIER", "123"))).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);

result = approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);
assertEquals(1, result.size());
}

@Test
public void testGetCandidateUserIds() {
// Test with empty approvalInstanceIds
Collection<Long> approvalInstanceIds = new ArrayList<>();
doReturn(Collections.emptyList()).when(userTaskCandidateRepository).findByApprovalInstanceIds(approvalInstanceIds);

Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
assertEquals(0, result.size());

// Test with non-empty approvalInstanceIds
approvalInstanceIds.add(1L);
doReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity())).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);
doReturn(Collections.singletonList(new Pair<>("ROLE", 1L))).when(userTaskCandidateRepository)
.findByApprovalInstanceIds(approvalInstanceIds);
doReturn(Collections.singletonList(new UserRoleEntity())).when(userRoleRepository).findByRoleIdIn(anySet());
doReturn(Collections.singletonList(new UserResourceRoleEntity())).when(userResourceRoleRepository)
.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet());

result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);
assertEquals(1, result.size());
}

@Test
public void testGetCandidateRoles() {
// Test with empty approvalInstanceId
Long approvalInstanceId = null;
Boolean enabled = null;
doReturn(Collections.emptyList()).when(userTaskCandidateRepository).findByApprovalInstanceId(approvalInstanceId);

List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
assertEquals(0, result.size());

// Test with non-empty approvalInstanceId
approvalInstanceId = 1L;
doReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity())).when(userTaskCandidateRepository)
.findByApprovalInstanceId(approvalInstanceId);
doReturn(Collections.singletonList(new Pair<>("ROLE", 1L))).when(userTaskCandidateRepository).findByApprovalInstanceId(
approvalInstanceId);
doReturn(Collections.singletonList(new RoleEntity())).when(roleRepository).findByRoleIdsAndEnabled(anySet(), eq(true))
.stream().map(RoleEntity::new).collect(Collectors.toList());

result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);
assertEquals(1, result.size());
}

@Test
public void testIsApprovable() {
// Test with non-existent approvalInstanceId
Long approvalInstanceId = 999L;
doReturn(Collections.emptyList()).when(userTaskInstanceRepository).findByApprovalInstanceId(approvalInstanceId);

Boolean result = approvalPermissionService.isApprovable(approvalInstanceId);
assertFalse(result);

// Test with existing approvalInstanceId
approvalInstanceId = 1L;
doReturn(Collections.singletonList(new UserTaskInstanceEntity())).when(userTaskInstanceRepository)
.findByApprovalInstanceId(approvalInstanceId);

result = approvalPermissionService.isApprovable(approvalInstanceId);
assertTrue(result);
}

@Test
public void testGetApprovableApprovalInstances() {
// Test with empty userId
long userId = -1L;
doReturn(Collections.emptyList()).when(userRoleRepository).findByRoleIdIn(anySet());
doReturn(Collections.emptySet()).when(userResourceRoleRepository).findByResourceIdsAndResourceRoleIdsIn(anySet(),
anySet());
doReturn(Collections.singletonList(new UserTaskInstanceEntity())).when(userTaskInstanceRepository)
.findByCandidateUserId(userId);

List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);
assertEquals(0, result.size());

// Test with non-empty userId
userId = 1L;
doReturn(Collections.singletonList(new UserRoleEntity())).when(userRoleRepository).findByRoleIdIn(anySet());
doReturn(Collections.singletonList(new UserResourceRoleEntity())).when(userResourceRoleRepository)
.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet());
doReturn(Collections.singletonList(new UserTaskInstanceEntity())).when(userTaskInstanceRepository)
.findByCandidateUserId(userId);

result = approvalPermissionService.getApprovableApprovalInstances(userId);
assertEquals(1, result.size());
}

@Test
public void testSetCandidateResourceRole() {
// Test with null values
Long approvalInstanceId = null;
String resourceRoleIdentifier = null;

approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

// Test with valid values
approvalInstanceId = 1L;
resourceRoleIdentifier = "123";

approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);
// No need to verify the behavior, just check if it doesn't throw an exception
}
}