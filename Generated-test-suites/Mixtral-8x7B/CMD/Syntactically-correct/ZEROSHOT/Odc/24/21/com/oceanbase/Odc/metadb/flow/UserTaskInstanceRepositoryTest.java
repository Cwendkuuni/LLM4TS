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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAIT_APPROVAL);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.APPROVED);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.REJECTED);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.COMPLETED);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAIT_FOR_CONFIRM);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByStatus(null);
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, null);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, Collections.singletonList(1L));
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, null);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, Collections.singletonList("test"));
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("test"));
 assertEquals(0, entities.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(null);
 assertEquals(0, entities.size());
 entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("test"));
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
 int result = userTaskInstanceRepository.update(entity);
 assertEquals(0, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "test");
 assertEquals(0, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.WAIT_APPROVAL);
 assertEquals(0, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 assertEquals(0, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
 "test", 1L);
 assertEquals(false, entity.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
 "test", 1L);
 assertEquals(false, entity.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Boolean result = userTaskInstanceRepository.findConfirmById(1L);
 assertEquals(null, result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L),
 FlowNodeStatus.WAIT_APPROVAL);
 assertEquals(0, entities.size());
 }
}