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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private TestEntityManager entityManager;

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("test");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
 1L, resourceRoleIdentifiers);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("test");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertEquals(0, entities.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.CREATED);
 int result = userTaskInstanceRepository.update(entity);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "test");
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.CREATED);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 assertEquals(0, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
 FlowNodeType.APPROVAL, "test", 1L);
 assertEquals(false, entity.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
 FlowNodeType.APPROVAL, "test", 1L);
 assertEquals(false, entity.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Boolean result = userTaskInstanceRepository.findConfirmById(1L);
 assertNull(result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 flowInstanceIds, FlowNodeStatus.CREATED);
 assertEquals(0, entities.size());
 }

 @Test
 public void testSave() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entity = userTaskInstanceRepository.save(entity);
 assertNotNull(entity.getId());
 }

 @Test
 public void testFindById() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entityManager.persist(entity);
 entityManager.flush();
 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findById(1L);
 assertTrue(result.isPresent());
 assertEquals(FlowNodeStatus.CREATED, result.get().getStatus());
 }

 @Test
 public void testFindAll() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entityManager.persist(entity);
 entityManager.flush();
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
 assertEquals(1, entities.size());
 }

 @Test
 public void testExistsById() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entityManager.persist(entity);
 entityManager.flush();
 assertTrue(userTaskInstanceRepository.existsById(1L));
 }

 @Test
 public void testCount() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entityManager.persist(entity);
 entityManager.flush();
 long count = userTaskInstanceRepository.count();
 assertEquals(1, count);
 }

 @Test
 public void testDeleteById() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entityManager.persist(entity);
 entityManager.flush();
 userTaskInstanceRepository.deleteById(1L);
 assertFalse(userTaskInstanceRepository.existsById(1L));
 }

 @Test
 public void testFindAllById() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.CREATED);
 entity.setFlowInstanceId(1L);
 entity.setActivityId("test");
 entity.setInstanceType(FlowNodeType.APPROVAL);
 entity.setUserTaskId("test");
 entityManager.persist(entity);
 entityManager.flush();
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllById(ids);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindAllByStatusIn() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository
 .findAllByStatusIn(Collections.singletonList(FlowNodeStatus.CREATED));
 assertEquals(0, entities.size());
 }
}