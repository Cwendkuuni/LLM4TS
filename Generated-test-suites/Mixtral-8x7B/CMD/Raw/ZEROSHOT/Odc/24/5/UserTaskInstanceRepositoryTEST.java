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
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository repository;

 @Autowired
 private TestEntityManager entityManager;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.RUNNING);
 entityManager.persist(entity);

 entities = repository.findByStatus(FlowNodeStatus.RUNNING);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, null);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setCandidateUserIds(Collections.singletonList(1L));
 entityManager.persist(entity);

 entities = repository.findByCandidateUserIdOrRoleIds(1L, null);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));

 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 entities = repository.findByCandidateUserIdOrRoleIds(null, roleIds);
 assertEquals(entities.size(), 0);

 entity.setCandidateRoleIds(roleIds);
 entityManager.merge(entity);

 entities = repository.findByCandidateUserIdOrRoleIds(null, roleIds);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setCandidateUserIds(Collections.singletonList(1L));
 entityManager.persist(entity);

 entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));

 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("testRole");
 entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(null, resourceRoleIdentifiers);
 assertEquals(entities.size(), 0);

 entity.setResourceRoleIdentifiers(resourceRoleIdentifiers);
 entityManager.merge(entity);

 entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(null, resourceRoleIdentifiers);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(null);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setResourceRoleIdentifiers(Collections.singletonList("testRole"));
 entityManager.persist(entity);

 entities = repository.findByResourceRoleIdentifierIn(null);
 assertEquals(entities.size(), 0);

 entities = repository.findByResourceRoleIdentifierIn(Collections.singletonList("testRole"));
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setCandidateRoleIds(Collections.singletonList(1L));
 entityManager.persist(entity);

 entities = repository.findByRoleId(1L);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setCandidateUserIds(Collections.singletonList(1L));
 entityManager.persist(entity);

 entities = repository.findByCandidateUserId(1L);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity));
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entityManager.persist(entity);

 int rowsAffected = repository.update(entity);
 assertEquals(rowsAffected, 1);

 entity.setUserTaskId("test");
 entity.setStatus(FlowNodeStatus.FAILED);
 entity.setApproved(true);
 entity.setOperatorId(2L);
 entity.setComment("test comment");
 entity.setExpireIntervalSeconds(100L);
 entity.setExternalFlowInstanceId("test flow instance");

 rowsAffected = repository.update(entity);
 assertEquals(rowsAffected, 1);

 entity = entityManager.find(UserTaskInstanceEntity.class, entity.getId());
 assertEquals(entity.getUserTaskId(), "test");
 assertEquals(entity.getStatus(), FlowNodeStatus.FAILED);
 assertEquals(entity.isApproved(), true);
 assertEquals(entity.getOperatorId(), 2L);
 assertEquals(entity.getComment(), "test comment");
 assertEquals(entity.getExpireIntervalSeconds(), 100L);
 assertEquals(entity.getExternalFlowInstanceId(), "test flow instance");
 }

 @Test
 public void testUpdateUserTaskIdById() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entityManager.persist(entity);

 int rowsAffected = repository.updateUserTaskIdById(1L, "test");
 assertEquals(rowsAffected, 1);

 entity = entityManager.find(UserTaskInstanceEntity.class, entity.getId());
 assertEquals(entity.getUserTaskId(), "test");
 }

 @Test
 public void testUpdateStatusById() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entityManager.persist(entity);

 int rowsAffected = repository.updateStatusById(1L, FlowNodeStatus.FAILED);
 assertEquals(rowsAffected, 1);

 entity = entityManager.find(UserTaskInstanceEntity.class, entity.getId());
 assertEquals(entity.getStatus(), FlowNodeStatus.FAILED);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
 entity1.setFlowInstanceId(1L);
 entityManager.persist(entity1);

 UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
 entity2.setFlowInstanceId(1L);
 entityManager.persist(entity2);

 int rowsAffected = repository.deleteByFlowInstanceId(1L);
 assertEquals(rowsAffected, 2);

 List<UserTaskInstanceEntity> entities = repository.findAll();
 assertEquals(entities.size(), 0);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "test", 1L);
 assertFalse(entity.isPresent());

 UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
 entity1.setInstanceType(FlowNodeType.APPROVAL);
 entity1.setActivityId("test");
 entity1.setFlowInstanceId(1L);
 entityManager.persist(entity1);

 entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "test", 1L);
 assertTrue(entity.isPresent());
 assertEquals(entity.get(), entity1);
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "test", 1L);
 assertFalse(entity.isPresent());

 UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
 entity1.setInstanceType(FlowNodeType.APPROVAL);
 entity1.setName("test");
 entity1.setFlowInstanceId(1L);
 entityManager.persist(entity1);

 entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "test", 1L);
 assertTrue(entity.isPresent());
 assertEquals(entity.get(), entity1);
 }

 @Test
 public void testFindConfirmById() {
 assertNull(repository.findConfirmById(1L));

 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setWaitForConfirm(true);
 entityManager.persist(entity);

 assertTrue(repository.findConfirmById(entity.getId()));
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collections.singletonList(1L), FlowNodeStatus.RUNNING);
 assertEquals(entities.size(), 0);

 UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
 entity1.setFlowInstanceId(1L);
 entity1.setStatus(FlowNodeStatus.RUNNING);
 entityManager.persist(entity1);

 entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collections.singletonList(1L), FlowNodeStatus.RUNNING);
 assertEquals(entities.size(), 1);
 assertTrue(entities.contains(entity1));
 }
}