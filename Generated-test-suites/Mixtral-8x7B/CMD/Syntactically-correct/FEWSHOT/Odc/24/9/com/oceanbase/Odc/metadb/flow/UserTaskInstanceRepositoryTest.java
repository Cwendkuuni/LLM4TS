import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

public class UserTaskInstanceRepositoryTest {
 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
 assertEquals(3, entities.size());
 }
 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<Long>() {{ add(2L); add(3L); }};
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 assertEquals(2, entities.size());
 }
 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = new ArrayList<String>() {{ add("role:4"); add("role:5"); }};
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }
 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifiers = new ArrayList<String>() {{ add("role:4"); }};
 List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }
 @Test
 public void testFindByRoleId() {
 Long roleId = 2L;
 List<UserTaskInstanceEntity> entities = repository.findByRoleId(roleId);
 assertEquals(1, entities.size());
 }
 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(userId);
 assertEquals(2, entities.size());
 }
 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 int rowsAffected = repository.update(entity);
 assertEquals(1, rowsAffected);
 }
 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "task-123";
 int rowsAffected = repository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, rowsAffected);
 }
 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.RUNNING;
 int rowsAffected = repository.updateStatusById(id, status);
 assertEquals(1, rowsAffected);
 }
 @Test
 public void testDeleteByFlowInstanceId() {
 Long flowInstanceId = 1L;
 int rowsAffected = repository.deleteByFlowInstanceId(flowInstanceId);
 assertEquals(1, rowsAffected);
 }
 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity-123";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(instanceType, activityId,
 flowInstanceId);
 assertEquals(entity.get().getActivityId(), activityId);
 }
 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "approval-123";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 assertEquals(entity.get().getName(), name);
 }
 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = repository.findConfirmById(id);
 assertEquals(confirm, true);
 }
 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<Long>() {{ add(1L); add(2L); }};
 FlowNodeStatus status = FlowNodeStatus.RUNNING;
 List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
 status);
 assertEquals(2, entities.size());
 }
}