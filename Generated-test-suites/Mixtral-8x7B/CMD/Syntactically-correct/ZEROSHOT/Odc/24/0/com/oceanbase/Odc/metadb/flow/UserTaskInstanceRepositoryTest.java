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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

@Autowired
private TestEntityManager entityManager;

@Autowired
private UserTaskInstanceRepository repository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
assertThat(entities).isNotNull();
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertThat(entities).isNotNull();
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role1");
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
assertThat(entities).isNotNull();
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role1");
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertThat(entities).isNotNull();
}

@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
assertThat(entities).isNotNull();
}

@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
assertThat(entities).isNotNull();
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
int result = repository.updateUserTaskIdById(1L, "task1");
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
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activity1", 1L);
assertThat(entity).isNotNull();
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name1", 1L);
assertThat(entity).isNotNull();
}

@Test
public void testFindConfirmById() {
Boolean result = repository.findConfirmById(1L);
assertThat(result).isNotNull();
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.RUNNING);
assertThat(entities).isNotNull();
}

}