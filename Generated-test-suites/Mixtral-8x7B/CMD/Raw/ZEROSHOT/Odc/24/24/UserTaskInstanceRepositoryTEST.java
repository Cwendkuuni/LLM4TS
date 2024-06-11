import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

 @Autowired
 private UserTaskInstanceRepository userTaskInstanceRepository;

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("identifier1");
 resourceRoleIdentifiers.add("identifier2");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
 resourceRoleIdentifiers);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("identifier1");
 resourceRoleIdentifiers.add("identifier2");
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 entity.setStatus(FlowNodeStatus.RUNNING);
 int result = userTaskInstanceRepository.update(entity);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.RUNNING);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
 FlowNodeType.APPROVAL, "activityId", 1L);
 assertThat(entity).isNotNull();
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
 "name", 1L);
 assertThat(entity).isNotNull();
 }

 @Test
 public void testFindConfirmById() {
 Boolean result = userTaskInstanceRepository.findConfirmById(1L);
 assertThat(result).isNotNull();
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 flowInstanceIds.add(2L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 flowInstanceIds, FlowNodeStatus.RUNNING);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindAll() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll(pageable);
 assertThat(entities).isNotNull();
 }

 @Test
 public void testFindById() {
 Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
 assertThat(entity).isNotNull();
 }

 @Test
 public void testSave() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setStatus(FlowNodeStatus.RUNNING);
 UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
 assertThat(savedEntity).isNotNull();
 }

 @Test
 public void testExistsById() {
 boolean result = userTaskInstanceRepository.existsById(1L);
 assertThat(result).isTrue();
 }

 @Test
 public void testCount() {
 long result = userTaskInstanceRepository.count();
 assertThat(result).isNotNull();
 }

 @Test
 public void testDeleteById() {
 boolean result = userTaskInstanceRepository.deleteById(1L);
 assertThat(result).isTrue();
 }

 @Test
 public void testFindAllById() {
 Collection<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllById(ids);
 assertThat(entities).isNotNull();
 }
}