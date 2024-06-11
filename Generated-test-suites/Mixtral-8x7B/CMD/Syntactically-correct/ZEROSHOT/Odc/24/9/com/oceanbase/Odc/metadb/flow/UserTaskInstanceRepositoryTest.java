import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
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
 private UserTaskInstanceRepository userTaskInstanceRepository;

 private List<UserTaskInstanceEntity> userTaskInstanceEntities;

 @Before
 public void setUp() {
 userTaskInstanceEntities = new ArrayList<>();
 UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
 userTaskInstanceEntity.setId(1L);
 userTaskInstanceEntity.setUserTaskId("userTaskId");
 userTaskInstanceEntity.setStatus(FlowNodeStatus.CREATED);
 userTaskInstanceEntity.setApproved(false);
 userTaskInstanceEntity.setOperatorId(1L);
 userTaskInstanceEntity.setComment("comment");
 userTaskInstanceEntity.setExpireIntervalSeconds(100L);
 userTaskInstanceEntity.setExternalFlowInstanceId("externalFlowInstanceId");
 userTaskInstanceEntity.setFlowInstanceId(1L);
 userTaskInstanceEntities.add(userTaskInstanceEntity);
 }

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
 Collections.singletonList(1L));
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
 Collections.singletonList("resourceRoleIdentifier"));
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
 Collections.singletonList("resourceRoleIdentifier"));
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

 @Test
 public void testUpdate() {
 int result = userTaskInstanceRepository.update(getUserTaskInstanceEntityWithId());
 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "updatedUserTaskId");
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
 FlowNodeType.APPROVAL, "activityId", 1L);
 assertTrue(result.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
 FlowNodeType.APPROVAL, "name", 1L);
 assertTrue(result.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 Boolean result = userTaskInstanceRepository.findConfirmById(1L);
 assertNull(result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collections.singletonList(1L), FlowNodeStatus.CREATED);
 assertEquals(userTaskInstanceEntities.size(), result.size());
 }

}