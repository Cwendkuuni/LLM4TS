import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
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

// Test for JpaRepository's findAll() method
@Test
public void testFindAll() {
List<UserTaskInstanceEntity> entities = repository.findAll();
assertNotNull(entities);
}

// Test for JpaRepository's findById() method
@Test
public void testFindById() {
Optional<UserTaskInstanceEntity> entity = repository.findById(1L);
assertTrue(entity.isPresent());
entity = repository.findById(2L);
assertFalse(entity.isPresent());
}

// Test for JpaRepository's save() method
@Test
public void testSave() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity = repository.save(entity);
assertNotNull(entity.getId());
}

// Test for findByStatus() method
@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.CREATED);
assertEquals(3, entities.size());
}

// Test for findByCandidateUserIdOrRoleIds() method
@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(2L));
assertEquals(2, entities.size());
}

// Test for findByCandidateUserIdOrResourceRoleIdentifier() method
@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("3"));
assertEquals(1, entities.size());
}

// Test for findByResourceRoleIdentifierIn() method
@Test
public void testFindByResourceRoleIdentifierIn() {
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(Collections.singletonList("3"));
assertEquals(1, entities.size());
}

// Test for findByRoleId() method
@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = repository.findByRoleId(2L);
assertEquals(2, entities.size());
}

// Test for findByCandidateUserId() method
@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
assertEquals(3, entities.size());
}

// Test for update() method
@Test
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
int result = repository.update(entity);
assertEquals(1, result);
}

// Test for updateUserTaskIdById() method
@Test
public void testUpdateUserTaskIdById() {
int result = repository.updateUserTaskIdById(1L, "testUserTaskId");
assertEquals(1, result);
}

// Test for updateStatusById() method
@Test
public void testUpdateStatusById() {
int result = repository.updateStatusById(1L, FlowNodeStatus.APPROVED);
assertEquals(1, result);
}

// Test for deleteByFlowInstanceId() method
@Test
public void testDeleteByFlowInstanceId() {
int result = repository.deleteByFlowInstanceId(1L);
assertEquals(1, result);
}

// Test for findByInstanceTypeAndActivityId() method
@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "testActivityId", 1L);
assertTrue(entity.isPresent());
}

// Test for findByInstanceTypeAndName() method
@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "testName", 1L);
assertTrue(entity.isPresent());
}

// Test for findConfirmById() method
@Test
public void testFindConfirmById() {
Boolean confirm = repository.findConfirmById(1L);
assertTrue(confirm);
}

// Test for findApprovalInstanceIdByFlowInstanceIdAndStatus() method
@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.CREATED);
assertEquals(3, entities.size());
}
}