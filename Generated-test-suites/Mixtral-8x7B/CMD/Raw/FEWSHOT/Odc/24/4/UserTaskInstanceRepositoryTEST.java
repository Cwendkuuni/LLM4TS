import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
 assertEquals(3, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
 assertEquals(2, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role_1");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role_1");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
 assertEquals(2, entities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertEquals(3, entities.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 int rowsAffected = userTaskInstanceRepository.update(entity);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "task_1");
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testUpdateStatusById() {
 int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 assertEquals(3, rowsAffected);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.MANUAL, "activity_1", 1L);
 assertEquals("task_1", entity.get().getUserTaskId());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.MANUAL, "name_1", 1L);
 assertEquals("task_1", entity.get().getUserTaskId());
 }

 @Test
 public void testFindConfirmById() {
 Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
 assertEquals(true, confirmed);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.CREATED);
 assertEquals(3, entities.size());
 }
}