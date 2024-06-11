import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
assertThat(entities).isNotEmpty();
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
Long userId = 1L;
List<Long> roleIds = Collections.singletonList(2L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
assertThat(entities).isNotEmpty();
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
Long userId = 1L;
List<String> resourceRoleIdentifiers = Collections.singletonList("role1");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
assertThat(entities).isNotEmpty();
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifier = Collections.singletonList("role1");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
assertThat(entities).isNotEmpty();
}

@Test
public void testFindByRoleId() {
Long roleId = 1L;
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
assertThat(entities).isNotEmpty();
}

@Test
public void testFindByCandidateUserId() {
Long userId = 1L;
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
assertThat(entities).isNotEmpty();
}

@Test
@Transactional
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
int result = userTaskInstanceRepository.update(entity);
assertEquals(result, 1);
}

@Test
@Transactional
public void testUpdateUserTaskIdById() {
Long id = 1L;
String userTaskId = "task1";
int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
assertEquals(result, 1);
}

@Test
@Transactional
public void testUpdateStatusById() {
Long id = 1L;
FlowNodeStatus status = FlowNodeStatus.CREATED;
int result = userTaskInstanceRepository.updateStatusById(id, status);
assertEquals(result, 1);
}

@Test
@Transactional
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;
int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);
assertEquals(result, 1);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
FlowNodeType instanceType = FlowNodeType.APPROVAL;
String activityId = "activity1";
Long flowInstanceId = 1L;
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
assertFalse(entity.isEmpty());
}

@Test
public void testFindByInstanceTypeAndName() {
FlowNodeType instanceType = FlowNodeType.APPROVAL;
String name = "name1";

Long flowInstanceId = 1L;
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
assertFalse(entity.isEmpty());
}

@Test
public void testFindConfirmById() {
Long id = 1L;
Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
assertThat(confirm).isNotNull();
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = Collections.singletonList(1L);
FlowNodeStatus status = FlowNodeStatus.CREATED;
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
assertThat(entities).isNotEmpty();
}
}