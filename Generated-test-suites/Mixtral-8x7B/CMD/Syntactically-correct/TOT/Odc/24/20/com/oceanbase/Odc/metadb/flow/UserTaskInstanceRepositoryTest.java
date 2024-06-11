import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository repository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
entities.add(createEntity(1L, FlowNodeStatus.WAITING));
entities.add(createEntity(2L, FlowNodeStatus.RUNNING));
entities.add(createEntity(3L, FlowNodeStatus.WAITING));
entities.add(createEntity(4L, FlowNodeStatus.COMPLETED));
repository.saveAll(entities);

List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.WAITING);
assertEquals(2, result.size());
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
entities.add(createEntity(1L, FlowNodeStatus.WAITING, 1L, null));
entities.add(createEntity(2L, FlowNodeStatus.RUNNING, 2L, 3L));
entities.add(createEntity(3L, FlowNodeStatus.WAITING, 1L, null));
entities.add(createEntity(4L, FlowNodeStatus.COMPLETED, null, 3L));
repository.saveAll(entities);

List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
assertEquals(2, result.size());
}

@Test
public void testUpdate() {
UserTaskInstanceEntity entity = createEntity(1L, FlowNodeStatus.WAITING);
repository.save(entity);

UserTaskInstanceEntity updatedEntity = new UserTaskInstanceEntity();
updatedEntity.setId(1L);
updatedEntity.setStatus(FlowNodeStatus.COMPLETED);
updatedEntity.setUserTaskId("123456");
repository.update(updatedEntity);

UserTaskInstanceEntity result = repository.findById(1L).orElse(null);
assertNotNull(result);
assertEquals(FlowNodeStatus.COMPLETED, result.getStatus());
assertEquals("123456", result.getUserTaskId());
}

private UserTaskInstanceEntity createEntity(Long id, FlowNodeStatus status, Long userId, Long... roleIds) {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(id);
entity.setStatus(status);
entity.setUserTaskId("task" + id);
entity.setFlowInstanceId(id);
entity.setUserTaskDefId(id);
entity.setFlowInstanceNodeId(id);
entity.setFlowInstanceNodeApprovalId(id);
entity.setActivityId("activity" + id);
entity.setInstanceType(FlowNodeType.APPROVAL);
entity.setCandidateUserId(userId);
entity.setRoleIds(Arrays.asList(roleIds));
return entity;
}

}