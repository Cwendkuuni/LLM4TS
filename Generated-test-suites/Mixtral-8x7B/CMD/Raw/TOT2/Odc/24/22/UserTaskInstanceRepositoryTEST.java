import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

@Autowired
private TestEntityManager entityManager;

@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;

// Test for findByStatus
@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
assertFalse(entities.isEmpty());
}

// Test for findByCandidateUserIdOrRoleIds
@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertFalse(entities.isEmpty());
}

// Test for findByCandidateUserIdOrResourceRoleIdentifier
@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("resourceRole1");
resourceRoleIdentifiers.add("resourceRole2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
assertFalse(entities.isEmpty());
}

// Test for findByResourceRoleIdentifierIn
@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("resourceRole1");
resourceRoleIdentifiers.add("resourceRole2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertFalse(entities.isEmpty());
}

// Test for findByRoleId
@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
assertFalse(entities.isEmpty());
}

// Test for findByCandidateUserId
@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
assertFalse(entities.isEmpty());
}

// Test for update
@Test
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setUserTaskId("task1");
entity.setStatus(FlowNodeStatus.CREATED);
entity.setApproved(true);
entity.setOperatorId(1L);
entity.setComment("test");
entity.setExpireIntervalSeconds(60);
entity.setExternalFlowInstanceId("instance1");

int rowsAffected = userTaskInstanceRepository.update(entity);
assertEquals(1, rowsAffected);
}

// Test for updateUserTaskIdById
@Test
public void testUpdateUserTaskIdById() {
int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "task1");
assertEquals(1, rowsAffected);
}

// Test for updateStatusById
@Test
public void testUpdateStatusById() {
int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
assertEquals(1, rowsAffected);
}

// Test for deleteByFlowInstanceId
@Test
public void testDeleteByFlowInstanceId() {
int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
assertEquals(1, rowsAffected);
}

// Test for findByInstanceTypeAndActivityId
@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START_EVENT, "activity1", 1L);
assertTrue(entity.isPresent());
}

// Test for findByInstanceTypeAndName
@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.USER_TASK, "task1", 1L);
assertTrue(entity.isPresent());
}

// Test for findConfirmById
@Test
public void testFindConfirmById() {
Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
assertNotNull(confirm);
}

// Test for findApprovalInstanceIdByFlowInstanceIdAndStatus
@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
flowInstanceIds.add(2L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.CREATED);
assertFalse(entities.isEmpty());
}

// Test for save
@Test
public void testSave() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setUserTaskId("task1");
entity.setStatus(FlowNodeStatus.CREATED);
entity.setApproved(true);
entity.setOperatorId(1L);
entity.setComment("test");
entity.setExpireIntervalSeconds(60);
entity.setExternalFlowInstanceId("instance1");

UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
assertNotNull(savedEntity.getId());
}
}