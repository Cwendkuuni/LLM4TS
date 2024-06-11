import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Arrays;
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
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private TestEntityManager entityManager;

 @Autowired
 private UserTaskInstanceRepository repository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.READY);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<Long> roleIds = Arrays.asList(1L, 2L);
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2");
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
 resourceRoleIdentifiers);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<String> resourceRoleIdentifier = Arrays.asList("role1", "role2");
 List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
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
 int result = repository.updateUserTaskIdById(1L, "userTaskId");
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = repository.updateStatusById(1L, FlowNodeStatus.READY);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = repository.deleteByFlowInstanceId(1L);
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.TASK;
 String activityId = "activityId";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(instanceType,
 activityId, flowInstanceId);
 assertThat(entity).isNotNull();
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.TASK;
 String name = "name";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(instanceType, name,
 flowInstanceId);
 assertThat(entity).isNotNull();
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean confirm = repository.findConfirmById(id);
 assertThat(confirm).isNotNull();
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<Long> flowInstanceIds = Arrays.asList(1L, 2L);
 FlowNodeStatus status = FlowNodeStatus.READY;
 List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 flowInstanceIds, status);
 assertThat(entities).isNotNull();
 }

}