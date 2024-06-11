import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;

// Test for List<UserTaskInstanceEntity> findByStatus(FlowNodeStatus status)
@Test
public void testFindByStatus() {
FlowNodeStatus status = FlowNodeStatus.WAITING;
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(status);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}

// Test for List<UserTaskInstanceEntity> findByCandidateUserIdOrRoleIds(@Param("userId") Long userId, @Param("roleIds") Collection<Long> roleIds)
@Test
public void testFindByCandidateUserIdOrRoleIds() {
Long userId = 1L;
List<Long> roleIds = Arrays.asList(2L, 3L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}

// Test for List<UserTaskInstanceEntity> findByCandidateUserIdOrResourceRoleIdentifier(@Param("userId") Long userId, @Param("resourceRoleIdentifiers") Collection<String> resourceRoleIdentifiers)
@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
Long userId = 1L;
List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}

// Test for List<UserTaskInstanceEntity> findByResourceRoleIdentifierIn(@Param("resourceRoleIdentifier") Collection<String> resourceRoleIdentifier)
@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}

// Test for List<UserTaskInstanceEntity> findByRoleId(@Param("roleId") Long roleId)
@Test
public void testFindByRoleId() {
Long roleId = 1L;
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}

// Test for List<UserTaskInstanceEntity> findByCandidateUserId(@Param("userId") Long userId)
@Test
public void testFindByCandidateUserId() {
Long userId = 1L;
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}

// Test for int update(@Param("param") UserTaskInstanceEntity entity)
@Test
@Transactional
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setUserTaskId("task1");
entity.setStatus(FlowNodeStatus.WAITING);
entity.setApproved(false);
entity.setOperatorId(1L);
entity.setComment("comment1");
entity.setExpireIntervalSeconds(100L);
entity.setExternalFlowInstanceId("instance1");

int rowsAffected = userTaskInstanceRepository.update(entity);
// Assert that rows affected is greater than 0
assertEquals(rowsAffected, greaterThan(0));
}

// Test for int updateUserTaskIdById(@Param("id") Long id, @Param("userTaskId") String userTaskId)
@Test
@Transactional
public void testUpdateUserTaskIdById() {
Long id = 1L;
String userTaskId = "task1";

int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
// Assert that rows affected is greater than 0
assertEquals(rowsAffected, greaterThan(0));
}

// Test for int updateStatusById(@Param("id") Long id, @Param("status") FlowNodeStatus status)
@Test
@Transactional
public void testUpdateStatusById() {
Long id = 1L;
FlowNodeStatus status = FlowNodeStatus.WAITING;

int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
// Assert that rows affected is greater than 0
assertEquals(rowsAffected, greaterThan(0));
}

// Test for int deleteByFlowInstanceId(@Param("instanceId") Long instanceId)
@Test
@Transactional
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;

int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
// Assert that rows affected is greater than 0
assertEquals(rowsAffected, greaterThan(0));
}

// Test for Optional<UserTaskInstanceEntity> findByInstanceTypeAndActivityId(@Param("instanceType") FlowNodeType instanceType, @Param("activityId") String activityId, @Param("flowInstanceId") Long flowInstanceId)
@Test
public void testFindByInstanceTypeAndActivityId() {
FlowNodeType instanceType = FlowNodeType.TASK;
String activityId = "task1";
Long flowInstanceId = 1L;

Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
// Assert that entity returned is not empty
assertEquals(entity, isPresent());
}

// Test for Optional<UserTaskInstanceEntity> findByInstanceTypeAndName(@Param("instanceType") FlowNodeType instanceType, @Param("name") String name, @Param("flowInstanceId") Long flowInstanceId)
@Test
public void testFindByInstanceTypeAndName() {
FlowNodeType instanceType = FlowNodeType.TASK;
String name = "task1";
Long flowInstanceId = 1L;

Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
// Assert that entity returned is not empty
assertEquals(entity, isPresent());
}

// Test for Boolean findConfirmById(@Param("id") Long id)
@Test
public void testFindConfirmById() {
Long id = 1L;

Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
// Assert that confirm is not null
assertEquals(confirm, isNotNull());
}

// Test for List<UserTaskInstanceEntity> findApprovalInstanceIdByFlowInstanceIdAndStatus(@Param("flowInstanceIds") Collection<Long> flowInstanceIds, @Param("status") FlowNodeStatus status)
@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
Collection<Long> flowInstanceIds = Arrays.asList(1L, 2L);
FlowNodeStatus status = FlowNodeStatus.WAITING;

List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
// Assert that entities returned are not null
assertEquals(entities, isNotEmpty());
}
}