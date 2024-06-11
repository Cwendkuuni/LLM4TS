import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepository.class)
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
 assertEquals(3, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(2L);
 roleIds.add(3L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 assertEquals(2, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
 resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 2L;
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
 assertEquals(2, entities.size());
 }

 @Test
 @Transactional
 public void testUpdate() {
 Long id = 1L;
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(id);
 entity.setUserTaskId("task1");
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setApproved(true);
 entity.setOperatorId(1L);
 entity.setComment("comment1");
 entity.setExpireIntervalSeconds(60);
 entity.setExternalFlowInstanceId("external1");
 int rowsAffected = userTaskInstanceRepository.update(entity);
 assertEquals(1, rowsAffected);
 }

 @Test
 @Transactional
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "task2";
 int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, rowsAffected);
 }

 @Test
 @Transactional
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.APPROVED;
 int rowsAffected = userTaskInstanceRepository.updateStatusById(id, status);
 assertEquals(1, rowsAffected);
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 Long flowInstanceId = 1L;
 int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository
 .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 assertNotNull(entity);
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
 flowInstanceId);
 assertNotNull(entity);
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 assertEquals(true, confirm);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 FlowNodeStatus status = FlowNodeStatus.CREATED;
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository
 .findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 assertEquals(1, entities.size());
 }

}