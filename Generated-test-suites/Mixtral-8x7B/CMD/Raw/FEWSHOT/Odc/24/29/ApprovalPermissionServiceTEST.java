package com.oceanbase.odc.service.flow;

import static org.junit.Assert.*;

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

	@Test
	public void testGetCandidates() {
		Long approvalInstanceId = 1L;
		Boolean enabled = true;
		Set<User> expected = new HashSet<>();
		when(userRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
			.thenReturn(Collections.singletonList(new UserEntity()));
		assertEquals(expected, approvalPermissionService.getCandidates(approvalInstanceId, enabled));
	}

	@Test
	public void testGetInstanceId2CandidateRoleIds() {
		Collection<Long> approvalInstanceIds = new ArrayList<>();
		Map<Long, Set<Long>> expected = new HashMap<>();
		when(userTaskCandidateRepository.findByApprovalInstanceIds(anyCollection()))
			.thenReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity()));
		assertEquals(expected, approvalPermissionService.getInstanceId2CandidateRoleIds(approvalInstanceIds));
	}

	@Test
	public void testGetInstanceId2CandidateResourceRoleIdentifierIds() {
		Collection<Long> approvalInstanceIds = new ArrayList<>();
		Map<Long, Set<String>> expected = new HashMap<>();
		when(userTaskCandidateRepository.findByApprovalInstanceIds(anyCollection()))
			.thenReturn(Collections.singletonList(new UserTaskInstanceCandidateEntity()));
		assertEquals(expected, approvalPermissionService.getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceIds));
	}

	@Test
	public void testGetCandidateUserIds() {
		Collection<Long> approvalInstanceIds = new ArrayList<>();
		Set<Long> expected = new HashSet<>();
		when(userRoleRepository.findByRoleIdIn(anySet()))
			.thenReturn(Collections.singletonList(new UserRoleEntity()));
		when(userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(anySet(), anySet()))
			.thenReturn(Collections.singletonList(new UserResourceRoleEntity()));
		assertEquals(expected, approvalPermissionService.getCandidateUserIds(approvalInstanceIds));
	}

	@Test
	public void testGetCandidateRoles() {
		Long approvalInstanceId = 1L;
		Boolean enabled = true;
		List<Role> expected = new ArrayList<>();
		when(roleRepository.findByRoleIdsAndEnabled(anySet(), anyBoolean()))
			.thenReturn(Collections.singletonList(new RoleEntity()));
		assertEquals(expected, approvalPermissionService.getCandidateRoles(approvalInstanceId, enabled));
	}

	@Test
	public void testIsApprovable() {
		Long approvalInstanceId = 1L;
		Set<Long> expected = new HashSet<>();
		when(getApprovableApprovalInstances().stream()
			.map(UserTaskInstanceEntity::getId)
			.collect(Collectors.toSet()))
			.thenReturn(expected);
		assertTrue(approvalPermissionService.isApprovable(approvalInstanceId));
	}

	@Test
	public void testGetApprovableApprovalInstances() {
		Long userId = 1L;
		Set<Long> roleIds = new HashSet<>();
		Set<String> resourceRoleIdentifiers = new HashSet<>();
		List<UserTaskInstanceEntity> expected = new ArrayList<>();

		when(userService.getUserRoleIds(userId)).thenReturn(roleIds);
		when(resourceRoleService.getResourceRoleIdentifiersByUserId(userId))
			.thenReturn(resourceRoleIdentifiers);

		when(userTaskInstanceRepository.findByCandidateUserId(userId))
			.thenReturn(expected);
		when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
			.thenReturn(expected);
		when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds))
			.thenReturn(expected);

		assertEquals(expected, approvalPermissionService.getApprovableApprovalInstances(userId));
	}

	@Test
	public void testSetCandidateResourceRole() {
		Long approvalInstanceId = 1L;
		String resourceRoleIdentifier = "resourceRoleIdentifier";
		UserTaskInstanceCandidateEntity expected = new UserTaskInstanceCandidateEntity();
		expected.setApprovalInstanceId(approvalInstanceId);
		expected.setResourceRoleIdentifier(resourceRoleIdentifier);
		when(userTaskCandidateRepository.save(any()))
			.thenReturn(expected);
		assertEquals(expected, approvalPermissionService.setCandidateResourceRole(approvalInstanceId, resourceRoleIdentifier));
	}

	@Test
	public void testListApprovableExternalInstances() {
		List<UserTaskInstanceEntity> expected = new ArrayList<>();
		when(userTaskInstanceRepository.findByStatus(any()))
			.thenReturn(expected);
		assertEquals(expected, approvalPermissionService.listApprovableExternalInstances());
	}

	@Test
	public void testGetCandidatesByFlowInstanceIds() {
		Collection<Long> flowInstanceIds = new ArrayList<>();
		Map<Long, Set<UserEntity>> expected = new HashMap<>();

		Map<Long, Long> approvalInstanceId2FlowInstanceId =
				userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
						flowInstanceIds,
						FlowNodeStatus.EXECUTING).stream().collect(
						Collectors.toMap(UserTaskInstanceEntity::getId,
								UserTaskInstanceEntity::getFlowInstanceId));

		Map<Long, Set<String>> instanceId2CandidateResourceRoleIdentifierIds =
				getInstanceId2CandidateResourceRoleIdentifierIds(approvalInstanceId2FlowInstanceId.keySet());

		Set<String> resourceRoleIdentifiers =
				instanceId2CandidateResourceRoleIdentifierIds.values().stream().flatMap(Set::stream).collect(
				Collectors.toSet());

		Map<String, Set<Long>> identifier2UserIds = userResourceRoleRepository.findByResourceIdsAndResourceRoleIdsIn(
				resourceRoleIdentifiers).stream()
				.collect(Collectors.groupingBy(o -> String.format("%s:%s", o.getResourceId(), o.getResourceRoleId())))
				.entrySet()
				.stream().collect(Collectors.toMap(entry -> entry.getKey(),
						entry -> entry.getValue().stream().map(UserResourceRoleEntity::getUserId).collect(
						Collectors.toSet())));

		Map<Long, Set<Long>> instanceId2UserIds =
				instanceId2CandidateResourceRoleIdentifierIds.entrySet().stream().collect(
				Collectors.toMap(Entry::getKey,
						entry -> entry.getValue().stream()
								.filter(i -> CollectionUtils.isNotEmpty(identifier2UserIds.get(i)))
								.map(identifier2UserIds::get).flatMap(Set::stream)
								.collect(Collectors.toSet())));

		Set<Long> approvalUserIds =
				identifier2UserIds.values().stream().flatMap(Set::stream).collect(Collectors.toSet());

		Map<Long, UserEntity> userId2User = userRepository.findByUserIds(approvalUserIds).stream()
				.collect(Collectors.toMap(UserEntity::getId, userEntity -> userEntity));

		Map<Long, Set<UserEntity>> instanceId2Candidates = instanceId2UserIds.entrySet().stream().collect(
				Collectors.toMap(Entry::getKey, entry -> entry.getValue().stream().map(userId2User::get).collect(
						Collectors.toSet())));

		Map<Long, Set<UserEntity>> instanceId2CandidatesExpected = instanceId2UserIds.entrySet().stream().collect(
				Collectors.toMap(entry -> approvalInstanceId2FlowInstanceId.get(entry.getKey()),
						entry -> instanceId2Candidates.get(entry.getKey())));

		assertEquals(instanceId2CandidatesExpected, approvalPermissionService.getCandidatesByFlowInstanceIds(flowInstanceIds));
	}

}