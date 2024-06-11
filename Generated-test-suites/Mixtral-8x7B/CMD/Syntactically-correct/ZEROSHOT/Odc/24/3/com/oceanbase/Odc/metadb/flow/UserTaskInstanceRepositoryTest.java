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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository repository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
assertNotNull(entities);
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertNotNull(entities);
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("identifier1");
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
assertNotNull(entities);
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("identifier1");
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertNotNull(entities);
}

@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
assertNotNull(entities);
}

@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
assertNotNull(entities);
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
int result = repository.updateUserTaskIdById(1L, "userTaskId");
assertEquals(1, result);
}

@Test
public void testUpdateStatusById() {
int result = repository.updateStatusById(1L, FlowNodeStatus.RUNNING);
assertEquals(1, result);
}

@Test
public void testDeleteByFlowInstanceId() {
int result = repository.deleteByFlowInstanceId(1L);
assertEquals(1, result);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.MANUAL, "activityId", 1L);
assertFalse(entity.isEmpty());
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.MANUAL, "name", 1L);
assertFalse(entity.isEmpty());
}

@Test
public void testFindConfirmById() {
Boolean result = repository.findConfirmById(1L);
assertNotNull(result);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
FlowNodeStatus.RUNNING);
assertNotNull(entities);
}
}