import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:test-application-context.xml")
@Transactional
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository repository;

@Test
public void testFindAll() {
List<UserTaskInstanceEntity> entities = repository.findAll();
assertNotNull(entities);
assertTrue(entities.size() > 0);
}

@Test
public void testFindById() {
Long id = 1L;
UserTaskInstanceEntity entity = repository.findById(id).orElse(null);
assertNotNull(entity);
assertEquals(id, entity.getId());
}

@Test
public void testFindByStatus() {
FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
List<UserTaskInstanceEntity> entities = repository.findByStatus(status);
assertNotNull(entities);
assertTrue(entities.size() > 0);
for (UserTaskInstanceEntity entity : entities) {
assertEquals(status, entity.getStatus());
}
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
Long userId = 1L;
List<Long> roleIds = Arrays.asList(2L, 3L);
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
assertNotNull(entities);
assertTrue(entities.size() > 0);
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
Long userId = 1L;
List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
assertNotNull(entities);
assertTrue(entities.size() > 0);
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
assertNotNull(entities);
assertTrue(entities.size() > 0);
}

@Test
public void testFindByRoleId() {
Long roleId = 1L;
List<UserTaskInstanceEntity> entities = repository.findByRoleId(roleId);
assertNotNull(entities);
assertTrue(entities.size() > 0);
}

@Test
public void testFindByCandidateUserId() {
Long userId = 1L;
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(userId);
assertNotNull(entities);
assertTrue(entities.size() > 0);
}

@Test
public void testUpdate() {
Long id = 1L;
UserTaskInstanceEntity entity = repository.findById(id).orElse(null);
assertNotNull(entity);
assertEquals(id, entity.getId());

entity.setStatus(FlowNodeStatus.COMPLETED);
repository.update(entity);
assertNotNull(repository.findById(id).orElse(null));
assertEquals(FlowNodeStatus.COMPLETED, repository.findById(id).orElse(null).getStatus());
}

@Test
public void testUpdateUserTaskIdById() {
Long id = 1L;
String userTaskId = "task1";
repository.updateUserTaskIdById(id, userTaskId);
assertEquals(userTaskId, repository.findById(id).orElse(null).getUserTaskId());
}

@Test
public void testUpdateStatusById() {
Long id = 1L;
FlowNodeStatus status = FlowNodeStatus.COMPLETED;
repository.updateStatusById(id, status);
assertEquals(status, repository.findById(id).orElse(null).getStatus());
}

@Test
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;
repository.deleteByFlowInstanceId(instanceId);
assertEquals(0, repository.findAll().size());
}

@Test
public void testFindByInstanceTypeAndActivityId() {
FlowNodeType instanceType = FlowNodeType.ACTIVITY;
String activityId = "activity1";
Long flowInstanceId = 1L;
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
assertTrue(entity.isPresent());
assertEquals(instanceType, entity.get().getInstanceType());
assertEquals(activityId, entity.get().getActivityId());
assertEquals(flowInstanceId, entity.get().getFlowInstanceId());
}

@Test
public void testFindByInstanceTypeAndName() {
FlowNodeType instanceType = FlowNodeType.ACTIVITY;
String name = "activity1";
Long flowInstanceId = 1L;
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
assertTrue(entity.isPresent());
assertEquals(instanceType, entity.get().getInstanceType());
assertEquals(name, entity.get().getName());
assertEquals(flowInstanceId, entity.get().getFlowInstanceId());
}

@Test
public void testFindConfirmById() {
Long id = 1L;
Boolean confirm = repository.findConfirmById(id);
assertFalse(confirm);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = Arrays.asList(1L, 2L);
FlowNodeStatus status = FlowNodeStatus.IN_PROGRESS;
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
assertNotNull(entities);
assertTrue(entities.size() > 0);
for (UserTaskInstanceEntity entity : entities) {
assertEquals(status, entity.getStatus());
assertTrue(flowInstanceIds.contains(entity.getFlowInstanceId()));
}
}

}