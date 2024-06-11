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
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository repository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.CREATED);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, Collections.emptyList());
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.emptyList());
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(Collections.emptyList());
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
 assertEquals(0, entities.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 int result = repository.update(entity);
 assertEquals(0, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int result = repository.updateUserTaskIdById(1L, "test");
 assertEquals(0, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = repository.updateStatusById(1L, FlowNodeStatus.CREATED);
 assertEquals(0, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = repository.deleteByFlowInstanceId(1L);
 assertEquals(0, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "test", 1L);
 assertEquals(false, entity.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "test", 1L);
 assertEquals(false, entity.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Boolean result = repository.findConfirmById(1L);
 assertNull(result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L),
 FlowNodeStatus.CREATED);
 assertEquals(0, entities.size());
 }

}