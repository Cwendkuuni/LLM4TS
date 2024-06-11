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
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

@Autowired
private TestEntityManager entityManager;

@Autowired
private UserTaskInstanceRepository repository;

// Test for findByStatus(FlowNodeStatus status)
@Test
public void testFindByStatus() {
FlowNodeStatus status = FlowNodeStatus.OPEN;
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setStatus(status);
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findByStatus(status);
assertTrue(result.size() > 0);
}

// Test for findByCandidateUserIdOrRoleIds(Long userId, Collection<Long> roleIds)
@Test
public void testFindByCandidateUserIdOrRoleIds() {
Long userId = 1L;
Collection<Long> roleIds = new ArrayList<>();
roleIds.add(2L);

UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity();

userTaskInstanceEntity1.setCandidateUserIds(Collections.singletonList(userId));
entityManager.persist(userTaskInstanceEntity1);
entityManager.flush();

userTaskInstanceEntity2.setCandidateRoleIds(roleIds);
entityManager.persist(userTaskInstanceEntity2);
entityManager.flush();

userTaskInstanceEntity3.setCandidateUserIds(Collections.singletonList(3L));
entityManager.persist(userTaskInstanceEntity3);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
assertTrue(result.size() == 2);
}

// Test for findByCandidateUserIdOrResourceRoleIdentifier(Long userId, Collection<String> resourceRoleIdentifiers)
@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
Long userId = 1L;
Collection<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role1");

UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity();

userTaskInstanceEntity1.setCandidateUserIds(Collections.singletonList(userId));
entityManager.persist(userTaskInstanceEntity1);
entityManager.flush();

userTaskInstanceEntity2.setResourceRoleIdentifiers(resourceRoleIdentifiers);
entityManager.persist(userTaskInstanceEntity2);
entityManager.flush();

userTaskInstanceEntity3.setCandidateUserIds(Collections.singletonList(3L));
entityManager.persist(userTaskInstanceEntity3);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
assertTrue(result.size() == 2);
}

// Test for findByResourceRoleIdentifierIn(Collection<String> resourceRoleIdentifier)
@Test
public void testFindByResourceRoleIdentifierIn() {
Collection<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role1");

UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();

userTaskInstanceEntity1.setResourceRoleIdentifiers(resourceRoleIdentifiers);
entityManager.persist(userTaskInstanceEntity1);
entityManager.flush();

userTaskInstanceEntity2.setResourceRoleIdentifiers(resourceRoleIdentifiers);
entityManager.persist(userTaskInstanceEntity2);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertTrue(result.size() == 2);
}

// Test for findByRoleId(Long roleId)
@Test
public void testFindByRoleId() {
Long roleId = 1L;
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setCandidateRoleIds(Collections.singletonList(roleId));
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findByRoleId(roleId);
assertTrue(result.size() > 0);
}

// Test for findByCandidateUserId(Long userId)
@Test
public void testFindByCandidateUserId() {
Long userId = 1L;
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setCandidateUserIds(Collections.singletonList(userId));
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(userId);
assertTrue(result.size() > 0);
}

// Test for update(UserTaskInstanceEntity entity)
@Test
public void testUpdate() {
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

int result = repository.update(userTaskInstanceEntity);
assertEquals(1, result);
}

// Test for updateUserTaskIdById(Long id, String userTaskId)
@Test
public void testUpdateUserTaskIdById() {
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

int result = repository.updateUserTaskIdById(userTaskInstanceEntity.getId(), "task1");
assertEquals(1, result);
}

// Test for updateStatusById(Long id, FlowNodeStatus status)
@Test
public void testUpdateStatusById() {
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

int result = repository.updateStatusById(userTaskInstanceEntity.getId(), FlowNodeStatus.CLOSED);
assertEquals(1, result);
}

// Test for deleteByFlowInstanceId(Long instanceId)
@Test
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;
UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();

userTaskInstanceEntity1.setFlowInstanceId(instanceId);
userTaskInstanceEntity2.setFlowInstanceId(instanceId);

entityManager.persist(userTaskInstanceEntity1);
entityManager.persist(userTaskInstanceEntity2);
entityManager.flush();

int result = repository.deleteByFlowInstanceId(instanceId);
assertEquals(2, result);
}

// Test for findByInstanceTypeAndActivityId(FlowNodeType instanceType, String activityId, Long flowInstanceId)
@Test
public void testFindByInstanceTypeAndActivityId() {
FlowNodeType instanceType = FlowNodeType.TASK;
String activityId = "activity1";
Long flowInstanceId = 1L;

UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setInstanceType(instanceType);
userTaskInstanceEntity.setActivityId(activityId);
userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
assertTrue(result.isPresent());
}

// Test for findByInstanceTypeAndName(FlowNodeType instanceType, String name, Long flowInstanceId)
@Test
public void testFindByInstanceTypeAndName() {
FlowNodeType instanceType = FlowNodeType.TASK;
String name = "name1";
Long flowInstanceId = 1L;

UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setInstanceType(instanceType);
userTaskInstanceEntity.setName(name);
userTaskInstanceEntity.setFlowInstanceId(flowInstanceId);

entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
assertTrue(result.isPresent());
}

// Test for findConfirmById(Long id)
@Test
public void testFindConfirmById() {
UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
userTaskInstanceEntity.setWaitForConfirm(true);
entityManager.persist(userTaskInstanceEntity);
entityManager.flush();

Boolean result = repository.findConfirmById(userTaskInstanceEntity.getId());
assertTrue(result);
}

// Test for findApprovalInstanceIdByFlowInstanceIdAndStatus(Collection<Long> flowInstanceIds, FlowNodeStatus status)
@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
Collection<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);

UserTaskInstanceEntity userTaskInstanceEntity1 = new UserTaskInstanceEntity();
userTaskInstanceEntity1.setFlowInstanceId(1L);
userTaskInstanceEntity1.setStatus(FlowNodeStatus.OPEN);

UserTaskInstanceEntity userTaskInstanceEntity2 = new UserTaskInstanceEntity();
userTaskInstanceEntity2.setFlowInstanceId(2L);
userTaskInstanceEntity2.setStatus(FlowNodeStatus.OPEN);

UserTaskInstanceEntity userTaskInstanceEntity3 = new UserTaskInstanceEntity();
userTaskInstanceEntity3.setFlowInstanceId(3L);
userTaskInstanceEntity3.setStatus(FlowNodeStatus.CLOSED);

entityManager.persist(userTaskInstanceEntity1);
entityManager.persist(userTaskInstanceEntity2);
entityManager.persist(userTaskInstanceEntity3);
entityManager.flush();

List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.OPEN);
assertTrue(result.size() == 2);
}
}