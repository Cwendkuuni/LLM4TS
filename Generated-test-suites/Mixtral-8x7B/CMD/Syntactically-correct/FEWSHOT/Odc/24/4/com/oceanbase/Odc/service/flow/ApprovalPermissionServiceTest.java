//Corresponding JUnit 4 Test Cases:
package com.oceanbase.odc.service.flow;

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
import com.oceanbase.odc.metadb.iam.UserResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.UserResourceRoleRepository;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRole;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRoleEntity;
import com.oceanbase.odc.metadb.iam.resourcerole.ResourceRoleRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.iam.ResourceRoleService;
import com.oceanbase.odc.service.iam.UserService;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.iam.model.Role;
import com.oceanbase.odc.service.iam.model.User;

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

 @Mock
 private ResourceRoleRepository resourceRoleRepository;

 @InjectMocks
 private ApprovalPermissionService approvalPermissionService;

 private UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity1;
 private UserTaskInstanceCandidateEntity userTaskInstanceCandidateEntity2;
 private UserTaskInstanceEntity userTaskInstanceEntity1;
 private UserTaskInstanceEntity userTaskInstanceEntity2;
 private User user1;
 private User user2;
 private Role role1;
 private Role role2;
 private UserRoleEntity userRoleEntity1;
 private UserRoleEntity userRoleEntity2;
 private UserResourceRoleEntity userResourceRoleEntity;
 private ResourceRole resourceRole1;
 private ResourceRole resourceRole2;
 private Map<String, Set<Long>> candidates;
 private Map<String, Set<Long>> candidatesEmpty;
 private Set<Long> roleIds;
 private Set<Long> roleIdsEmpty;
 private Set<Long> userIds;
 private Set<Long> userIdsEmpty;
 private Set<Long> userIdsAndRoleIds;
 private Set<String> resourceRoleIdentifiers;
 private Set<String> resourceRoleIdentifiersEmpty;
 private Collection<Long> approvalInstanceIds;
 private Collection<Long> approvalInstanceIdsEmpty;
 private Map<Long, Set<Long>> instanceId2CandidateRoleIds;
 private Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds;
 private Set<Long> candidateUserIds;
 private List<Role> candidateRoles;
 private List<UserTaskInstanceEntity> approvableApprovalInstances;
 private List<UserTaskInstanceEntity> approvableApprovalInstancesEmpty;

 @Before
 public void setUp() {
 userTaskInstanceCandidateEntity1 = new UserTaskInstanceCandidateEntity();
 userTaskInstanceCandidateEntity1.setUserId(1L);
 userTaskInstanceCandidateEntity1.setRoleId(1L);
 userTaskInstanceCandidateEntity1.setResourceRoleIdentifier("identifier1");
 userTaskInstanceCandidateEntity2 = new UserTaskInstanceCandidateEntity();
 userTaskInstanceCandidateEntity2.setUserId(2L);
 userTaskInstanceCandidateEntity2.setRoleId(2L);
 userTaskInstanceCandidateEntity2.setResourceRoleIdentifier("identifier2");
 userTaskInstanceEntity1 = new UserTaskInstanceEntity();
 userTaskInstanceEntity1.setId(1L);
 userTaskInstanceEntity1.setFlowInstanceId(1L);
 userTaskInstanceEntity1.setStatus(FlowNodeStatus.EXECUTING);
 userTaskInstanceEntity2 = new UserTaskInstanceEntity();
 userTaskInstanceEntity2.setId(2L);
 userTaskInstanceEntity2.setFlowInstanceId(2L);
 userTaskInstanceEntity2.setStatus(FlowNodeStatus.EXECUTING);
 user1 = new User();
 user1.setId(1L);
 user2 = new User();
 user2.setId(2L);
 role1 = new Role();
 role1.setId(1L);
 role1.setEnabled(true);
 role2 = new Role();
 role2.setId(2L);
 role2.setEnabled(false);
 userRoleEntity1 = new UserRoleEntity();
 userRoleEntity1.setUserId(1L);
 userRoleEntity1.setRoleId(1L);
 userRoleEntity2 = new UserRoleEntity();
 userRoleEntity2.setUserId(2L);
 userRoleEntity2.setRoleId(2L);
 userResourceRoleEntity = new UserResourceRoleEntity();
 userResourceRoleEntity.setResourceId("resourceId");
 userResourceRoleEntity.setResourceRoleId("resourceRoleId");
 userResourceRoleEntity.setUserId(1L);
 resourceRole1 = new ResourceRole();
 resourceRole1.setIdentifier("identifier1");
 resourceRole1.setEnabled(true);
 resourceRole2 = new ResourceRole();
 resourceRole2.setIdentifier("identifier2");
 resourceRole2.setEnabled(false);
 candidates = new HashMap<>();
 candidates.put("ROLE", new HashSet<>(Collections.singletonList(1L)));
 candidates.put("USER", new HashSet<>(Collections.singletonList(2L)));
 candidatesEmpty = new HashMap<>();
 roleIds = new HashSet<>(Collections.singletonList(1L));
 roleIdsEmpty = new HashSet<>();
 userIds = new HashSet<>(Collections.singletonList(1L));
 userIdsEmpty = new HashSet<>();
 userIdsAndRoleIds = new HashSet<>();
 userIdsAndRoleIds.addAll(userIds);
 userIdsAndRoleIds.addAll(roleIds);
 resourceRoleIdentifiers = new HashSet<>(Collections.singletonList("identifier1"));
 resourceRoleIdentifiersEmpty = new HashSet<>();
 approvalInstanceIds = new ArrayList<>();
 approvalInstanceIds.add(1L);
 approvalInstanceIdsEmpty = new ArrayList<>();
 instanceId2CandidateRoleIds = new HashMap<>();
 instanceId2CandidateRoleIds.put(1L, roleIds);
 instanceId2CandidateResourceRoleIdentifierIds = new HashMap<>();
 instanceId2CandidateResourceRoleIdentifierIds.put(1L, resourceRoleIdentifiers);
 candidateUserIds = new HashSet<>(Collections.singletonList(1L));
 candidateRoles = new ArrayList<>();
 candidateRoles.add(new Role(role1));
 approvableApprovalInstances = new ArrayList<>();
 approvableApprovalInstances.add(userTaskInstanceEntity1);
 approvableApprovalInstancesEmpty = new ArrayList<>();
 when(userService.getUserRoleIds(1L)).thenReturn(roleIds);
 when(userService.getCurrentUserResourceRoleIdentifiers()).thenReturn(resourceRoleIdentifiers);
 when(userRepository.findByUserIdsAndEnabled(userIds, true)).thenReturn(Collections.singletonList(user1));
 when(userRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(Collections.singletonList(user2));
 when(userRepository.findByUserIdsOrRoleIds(userIds, roleIds, true))
 .thenReturn(Collections.singletonList(user1));
 when(roleRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(Collections.singletonList(role1));
 when(roleRepository.findByRoleIdsAndEnabled(roleIdsEmpty, true)).thenReturn(Collections.emptyList());
 when(userRoleRepository.findByRoleIdIn(roleIds)).thenReturn(Collections.singletonList(userRoleEntity1));
 when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(resourceRoleIdentifiers))
 .thenReturn(Collections.singletonList(userResourceRoleEntity));
 when(resourceRoleRepository.findByIdentifierIn(resourceRoleIdentifiers)).thenReturn(
 Collections.singletonList(resourceRole1));
 when(userTaskCandidateRepository.findByApprovalInstanceId(1L))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity1));
 when(userTaskCandidateRepository.findByApprovalInstanceIds(approvalInstanceIds))
 .thenReturn(Collections.singletonList(userTaskInstanceCandidateEntity1));
 when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers))
 .thenReturn(Collections.singletonList(userTaskInstanceEntity1));
 when(userTaskInstanceRepository.findByCandidateUserId(1L))
 .thenReturn(Collections.singletonList(userTaskInstanceEntity1));
 when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers))
 .thenReturn(
 Collections.singletonList(userTaskInstanceEntity1));
 when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds))
 .thenReturn(Collections.singletonList(userTaskInstanceEntity1));
 when(userTaskInstanceRepository.findByStatus(FlowNodeStatus.EXECUTING))
 .thenReturn(approvableApprovalInstances);
 when(authenticationFacade.currentUserId()).thenReturn(1L);
 }

 @Test
 public void testGetCandidates() {
 assertEquals(2, approvalPermissionService.getCandidates(1L, true).size());
 verify(userRepository, times(1)).findByRoleIdsAndEnabled(roleIds, true);
 }

 @Test
 public void testGetCandidatesEmpty() {
 assertEquals(0, approvalPermissionService.getCandidates(1L, false).size());
 verify(userRepository, times(1)).findByRoleIdsAndEnabled(roleIdsEmpty, false);
 }

 @Test
 public void testGetInstanceId2CandidateRoleIds() {
 assertEquals(roleIds, instanceId2CandidateRoleIds.get(1L));
 }

 @Test
 public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
 assertEquals(resourceRoleIdentifiers, instanceId2CandidateResourceRoleIdentifierIds.get(1L));
 }

 @Test
 public void testGetCandidateUserIds() {
 assertEquals(candidateUserIds, approvalPermissionService.getCandidateUserIds(approvalInstanceIds));
 }

 @Test
 public void testGetCandidateRoles() {
 assertEquals(candidateRoles, approvalPermissionService.getCandidateRoles(1L, true));
 }

 @Test
 public void testIsApprovable() {
 assertTrue(approvalPermissionService.isApprovable(1L));
 }

 @Test
 public void testIsApprovableFalse() {
 assertFalse(approvalPermissionService.isApprovable(2L));
 }

 @Test
 public void testGetApprovableApprovalInstances() {
 assertEquals(approvableApprovalInstances, approvalPermissionService.getApprovableApprovalInstances());
 }

 @Test
 public void testGetApprovableApprovalInstancesEmpty() {
 assertEquals(approvableApprovalInstancesEmpty,
 approvalPermissionService.getApprovableApprovalInstances(2L));
 }

 @Test
 public void testSetCandidateResourceRole() {
 approvalPermissionService.setCandidateResourceRole(1L, "identifier1");
 verify(userTaskCandidateRepository, times(1)).save(userTaskInstanceCandidateEntity1);
 }

 @Test
 public void testListApprovableExternalInstances() {
 assertTrue(approvalPermissionService.listApprovableExternalInstances().isEmpty());
 }

 @Test
 public void testGetCandidatesByFlowInstanceIds() {
 assertTrue(approvalPermissionService.getCandidatesByFlowInstanceIds(Collections.singleton(1L)).isEmpty());
 }
}