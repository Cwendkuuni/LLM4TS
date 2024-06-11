import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserTaskInstanceRepositoryTest {

@Autowired
private TestEntityManager entityManager;

@Autowired
private UserTaskInstanceRepository repository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
assertEquals(entities.size(), 0);
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
Long userId = 1L;
Collection<Long> roleIds = Collections.singletonList(2L);
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
assertEquals(entities.size(), 0);
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
Long userId = 1L;
Collection<String> resourceRoleIdentifiers = Collections.singletonList("role1");
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
assertEquals(entities.size(), 0);
}

@Test
public void testFindByResourceRoleIdentifierIn() {
Collection<String> resourceRoleIdentifiers = Collections.singletonList("role1");
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertEquals(entities.size(), 0);
}

@Test
public void testFindByRoleId() {
Long roleId = 1L;
List<UserTaskInstanceEntity> entities = repository.findByRoleId(roleId);
assertEquals(entities.size(), 0);
}

@Test
public void testFindByCandidateUserId() {
Long userId = 1L;
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(userId);
assertEquals(entities.size(), 0);
}

@Test
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
Long id = 1L;
int result = repository.update(entity, id);
assertEquals(result, 0);
}

@Test
public void testUpdateUserTaskIdById() {
Long id = 1L;
String userTaskId = "task1";
int result = repository.updateUserTaskIdById(id, userTaskId);
assertEquals(result, 0);
}

@Test
public void testUpdateStatusById() {
Long id = 1L;
FlowNodeStatus status = FlowNodeStatus.RUNNING;
int result = repository.updateStatusById(id, status);
assertEquals(result, 0);
}

@Test
public void testDeleteByFlowInstanceId() {
Long flowInstanceId = 1L;
int result = repository.deleteByFlowInstanceId(flowInstanceId);
assertEquals(result, 0);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
FlowNodeType instanceType = FlowNodeType.START;
String activityId = "act1";
Long flowInstanceId = 1L;
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
assertEquals(entity.isPresent(), false);
}

@Test
public void testFindByInstanceTypeAndName() {
FlowNodeType instanceType = FlowNodeType.START;
String name = "start";
Long flowInstanceId = 1L;
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
assertEquals(entity.isPresent(), false);
}

@Test
public void testFindConfirmById() {
Long id = 1L;
Boolean result = repository.findConfirmById(id);
assertEquals(result, null);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
Collection<Long> flowInstanceIds = Collections.singletonList(1L);
FlowNodeStatus status = FlowNodeStatus.RUNNING;
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
assertEquals(entities.size(), 0);
}
}