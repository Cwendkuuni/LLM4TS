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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

import lombok.NonNull;

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

 @Before
 public void init() {
 MockitoAnnotations.initMocks(this);
 }

 // Test getCandidates method
 @Test
 public void testGetCandidates() {
 // Test data
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(getUserTaskInstanceCandidateEntities());
 when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(getUsers());
 when(userRepository.findByUserIdsAndEnabled(anySet(), anyBoolean())).thenReturn(getUsers());

 // Call method
 Set<User> result = approvalPermissionService.getCandidates(approvalInstanceId, enabled);

 // Assert
 assertEquals(4, result.size());
 }

 // Test getInstanceId2CandidateRoleIds method
 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 // Test data
 Collection<Long> approvalInstanceIds = new ArrayList<Long>() {{
 add(1L);
 add(2L);
 add(3L);
 }};
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(getUserTaskInstanceCandidateEntities());

 // Call method
 Map<Long, Set<Long>> result = approvalPermissionService
 .getInstanceId2CandidateRoleIds(approvalInstanceIds);

 // Assert
 assertEquals(new HashSet<Long>() {{
 add(1L);
 add(2L);
 }}, result.get(1L));
 assertEquals(new HashSet<Long>() {{
 add(3L);
 }}, result.get(2L));
 assertEquals(new HashSet<Long>() {{
 add(4L);
 }}, result.get(3L));
 }

 // Test getInstanceId2CandidateResourceRoleIdentifierIds method
 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 // Test data
 Collection<Long> approvalInstanceIds = new ArrayList<Long>() {{
 add(1L);
 add(2L);
 add(3L);
 }};
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(getUserTaskInstanceCandidateEntities());

 // Call method
 Map<Long, Set<String>> result = approvalPermissionService
 .getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds);

 // Assert
 assertEquals(new HashSet<String>() {{
 add("identifier1");
 add("identifier2");
 }}, result.get(1L));
 assertEquals(new HashSet<String>() {{
 add("identifier3");
 }}, result.get(2L));
 assertEquals(new HashSet<String>() {{
 add("identifier4");
 }}, result.get(3L));
 }

 // Test getCandidateUserIds method
 @Test
 public void testGetCandidateUserIds() {
 // Test data
 Collection<Long> approvalInstanceIds = new ArrayList<Long>() {{
 add(1L);
 add(2L);
 add(3L);
 }};
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(getUserTaskInstanceCandidateEntities());
 when(userRoleRepository.findByRoleIdIn(anySet())).thenReturn(getUserRoleEntities());
 when(userRepository.findByUserIds(anySet())).thenReturn(getUsers());

 // Call method
 Set<Long> result = approvalPermissionService.getCandidateUserIds(approvalInstanceIds);

 // Assert
 assertEquals(new HashSet<Long>() {{
 add(1L);
 add(2L);
 add(3L);
 add(4L);
 }}, result);
 }

 // Test getCandidateRoles method
 @Test
 public void testGetCandidateRoles() {
 // Test data
 Long approvalInstanceId = 1L;
 Boolean enabled = true;
 when(userTaskCandidateRepository.findByApprovalInstanceId(approvalInstanceId))
 .thenReturn(getUserTaskInstanceCandidateEntities());
 when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean())).thenReturn(getRoles());

 // Call method
 List<Role> result = approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled);

 // Assert
 assertEquals(2, result.size());
 }

 // Test isApprovable method
 @Test
 public void testIsApprovable() {
 // Test data
 Long approvalInstanceId = 1L;
 when(authenticationFacade.currentUserId()).thenReturn(1L);
 when(userService.getCurrentUserRoleIds()).thenReturn(new HashSet<Long>() {{
 add(1L);
 }});
 when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(new HashSet<String>() {{
 add("identifier1");
 }});
 when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(anySet()))
 .thenReturn(getUserTaskInstanceEntities());
 when(userTaskInstanceRepository.findByCandidateUserId(1L))
 .thenReturn(getUserTaskInstanceEntities());
 when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, anySet()))
 .thenReturn(getUserTaskInstanceEntities());
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, anySet()))
 .thenReturn(getUserTaskInstanceEntities());

 // Call method
 boolean result = approvalPermissionService.isApprovable(approvalInstanceId);

 // Assert
 assertTrue(result);
 }

 // Test getApprovableApprovalInstances method
 @Test
 public void testGetApprovableApprovalInstances() {
 // Test data
 long userId = 1L;
 when(userService.getUserRoleIds(userId)).thenReturn(new HashSet<Long>() {{
 add(1L);
 }});
 when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId))
 .thenReturn(new HashSet<String>() {{
 add("identifier1");
 }});
 when(userTaskInstanceRepository.findByCandidateUserId(userId))
 .thenReturn(getUserTaskInstanceEntities());
 when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, anySet()))
 .thenReturn(getUserTaskInstanceEntities());
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, anySet()))
 .thenReturn(getUserTaskInstanceEntities());

 // Call method
 List<UserTaskInstanceEntity> result = approvalPermissionService.getApprovableApprovalInstances(userId);

 // Assert
 assertEquals(3, result.size());
 }

 // Test setCandidateResourceRole method
 @Test
 public void testSetCandidateResourceRole() {
 // Test data
 Long approvalInstanceId = 1L;
 String resourceRoleIdentifier = "resourceRoleIdentifier";

 // Call method
 approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier);

 // Assert
 verify(userTaskCandidateRepository, times(1)).save(any(UserTaskInstanceCandidateEntity.class));
 }

 // Test listApprovableExternalInstances method
 @Test
 public void testListApprovableExternalInstances() {
 // Test data
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(getUserTaskInstanceEntities());

 // Call method
 List<UserTaskInstanceEntity> result = approvalPermissionService
 .listApprovableExternalInstances();

 // Assert
 assertEquals(2, result.size());
 }

 // Test getCandidatesByFlowInstanceIds method
 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 // Test data
 Collection<Long> flowInstanceIds = new ArrayList<Long>() {{
 add(1L);
 add(2L);
 add(3L);
 }};
 when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(anySet(), any()))
 .thenReturn(getApprovalInstanceIdByFlowInstanceId());
 when(userTaskCandidateRepository.findByApprovalInstanceIds(anySet()))
 .thenReturn(getUserTaskInstanceCandidateEntities());
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
 .thenReturn(getUserResourceRoleEntities());
 when(userRepository.findByUserIds(anySet())).thenReturn(getUsers());

 // Call method
 Map<Long, Set<UserEntity>> result = approvalPermissionService
 .getCandidatesByFlowInstanceIds(flowInstanceIds);

 // Assert
 assertEquals(new HashSet<UserEntity>() {{
 add(new UserEntity());
 add(new UserEntity());
 }}, result.get(1L));
 assertEquals(new HashSet<UserEntity>() {{
 add(new UserEntity());
 }}, result.get(2L));
 assertEquals(new HashSet<UserEntity>() {{
 add(new UserEntity());
 }}, result.get(3L));
 }

 // Helper methods to create mock objects
 private List<UserTaskInstanceCandidateEntity> getUserTaskInstanceCandidateEntities() {
 List<UserTaskInstanceCandidateEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceCandidateEntity() {{
 setRoleId(1L);
 setResourceRoleIdentifier("identifier1");
 }});
 entities.add(new UserTaskInstanceCandidateEntity() {{
 setRoleId(2L);
 setResourceRoleIdentifier("identifier2");
 }});
 entities.add(new UserTaskInstanceCandidateEntity() {{
 setRoleId(3L);
 setResourceRoleIdentifier("identifier3");
 }});
 entities.add(new UserTaskInstanceCandidateEntity() {{
 setUserId(1L);
 }});
 return entities;
 }

 private List<UserTaskInstanceEntity> getUserTaskInstanceEntities() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity() {{
 setId(1L);
 setFlowInstanceId(1L);
 }});
 entities.add(new UserTaskInstanceEntity() {{
 setId(2L);
 setFlowInstanceId(1L);
 }});
 entities.add(new UserTaskInstanceEntity() {{
 setId(3L);
 setFlowInstanceId(2L);
 }});
 return entities;
 }

 private List<Role> getRoles() {
 List<Role> roles = new ArrayList<>();
 roles.add(new Role() {{
 setId(1L);
 }});
 roles.add(new Role() {{
 setId(2L);
 }});
 return roles;
 }

 private List<UserRoleEntity> getUserRoleEntities() {
 List<UserRoleEntity> entities = new ArrayList<>();
 entities.add(new UserRoleEntity() {{
 setUserId(1L);
 setRoleId(1L);
 }});
 entities.add(new UserRoleEntity() {{
 setUserId(2L);
 setRoleId(1L);
 }});
 entities.add(new UserRoleEntity() {{
 setUserId(3L);
 setRoleId(2L);
 }});
 return entities;
 }

 private List<UserEntity> getUsers() {
 List<UserEntity> users = new ArrayList<>();
 users.add(new UserEntity() {{
 setId(1L);
 }});
 users.add(new UserEntity() {{
 setId(2L);
 }});
 users.add(new UserEntity() {{
 setId(3L);
 }});
 users.add(new UserEntity() {{
 setId(4L);
 }});
 return users;
 }

 private Map<Long, Long> getApprovalInstanceIdByFlowInstanceId() {
 Map<Long, Long> map = new HashMap<>();
 map.put(1L, 1L);
 map.put(2L, 2L);
 map.put(3L, 3L);
 return map;
 }

 private List<UserResourceRoleEntity> getUserResourceRoleEntities() {
 List<UserResourceRoleEntity> entities = new ArrayList<>();
 entities.add(new UserResourceRoleEntity() {{
 setUserId(1L);
 setResourceRoleId("identifier1");
 }});
 entities.add(new UserResourceRoleEntity() {{
 setUserId(2L);
 setResourceRoleId("identifier2");
 }});
 entities.add(new UserResourceRoleEntity() {{
 setUserId(3L);
 setResourceRoleId("identifier3");
 }});
 return entities;
 }
}