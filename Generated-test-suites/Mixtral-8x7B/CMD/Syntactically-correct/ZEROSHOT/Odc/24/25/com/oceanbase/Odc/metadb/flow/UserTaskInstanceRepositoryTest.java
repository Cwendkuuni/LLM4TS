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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository repository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.WAITING_APPROVAL);
 assertEquals(3, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
 assertEquals(2, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("admin");
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("admin");
 List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
 assertEquals(2, entities.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 int result = repository.update(entity);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int result = repository.updateUserTaskIdById(1L, "test");
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = repository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = repository.deleteByFlowInstanceId(1L);
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "test", 1L);
 assertTrue(entity.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "test", 1L);
 assertTrue(entity.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Boolean result = repository.findConfirmById(1L);
 assertTrue(result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
FlowNodeStatus.WAITING_APPROVAL);
assertEquals(1, entities.size());
}

 @Test
 public void testSave() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setStatus(FlowNodeStatus.WAITING_APPROVAL);
 UserTaskInstanceEntity savedEntity = repository.save(entity);
 assertNotNull(savedEntity.getId());
 }

 @Test
 public void testFindById() {
 Long id = 1L;
 Optional<UserTaskInstanceEntity> entity = repository.findById(id);
 assertTrue(entity.isPresent());
 assertEquals(id, entity.get().getId());
 }

 @Test
 public void testFindById_notFound() {
 Optional<UserTaskInstanceEntity> entity = repository.findById(100L);
 assertFalse(entity.isPresent());
 }
}