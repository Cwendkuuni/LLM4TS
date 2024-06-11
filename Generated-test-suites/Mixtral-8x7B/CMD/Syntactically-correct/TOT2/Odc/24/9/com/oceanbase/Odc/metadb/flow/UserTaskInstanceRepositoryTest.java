import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
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
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertThat(entities).isNotNull();
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("resource1");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
assertThat(entities).isNotNull();
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("resource1");
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
@Transactional
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setUserTaskId("userTaskId");
int rowsAffected = userTaskInstanceRepository.update(entity);
assertThat(rowsAffected).isEqualTo(1);
}

@Test
@Transactional
public void testUpdateUserTaskIdById() {
int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
assertThat(rowsAffected).isEqualTo(1);
}

@Test
@Transactional
public void testUpdateStatusById() {
int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.RUNNING);
assertThat(rowsAffected).isEqualTo(1);
}

@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
assertThat(rowsAffected).isEqualTo(1);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository
.findByInstanceTypeAndActivityId(FlowNodeType.TASK, "activityId", 1L);
assertFalse(entityOptional.isEmpty());
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository
.findByInstanceTypeAndName(FlowNodeType.TASK, "name", 1L);
assertFalse(entityOptional.isEmpty());
}

@Test
public void testFindConfirmById() {
Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
assertFalse(confirm);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.RUNNING);
assertThat(entities).isNotNull();
}

}