import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(2L);
 roleIds.add(3L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 1L;
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 int result = userTaskInstanceRepository.update(entity);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "task1";
 int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.RUNNING;
 int result = userTaskInstanceRepository.updateStatusById(id, status);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long flowInstanceId = 1L;
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 assertTrue(entity.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 assertTrue(entity.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
 assertTrue(confirm != null);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 flowInstanceIds.add(2L);
 FlowNodeStatus status = FlowNodeStatus.RUNNING;
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindAll() {
 List<UserTaskInstanceEntity> entities = (List<UserTaskInstanceEntity>) userTaskInstanceRepository.findAll();
 assertFalse(entities.isEmpty());
 }

 @Test
 public void testFindById() {
 Long id = 1L;
 UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(id).orElse(null);
 assertNotNull(entity);
 }

 @Test
 public void testSave() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
 assertNotNull(savedEntity);
 }

 @Test
 public void testCount() {
 Long count = userTaskInstanceRepository.count();
 assertTrue(count > 0);
 }

 @Test
 public void testExistsById() {
 Long id = 1L;
 boolean exists = userTaskInstanceRepository.existsById(id);
 assertTrue(exists);
 }

 @Test
 public void testDeleteById() {
 Long id = 1L;
 userTaskInstanceRepository.deleteById(id);
 }

 @Test
 public void testFindAllOrderByField() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll(null, "id", "asc");
 assertFalse(entities.isEmpty());
 }

}