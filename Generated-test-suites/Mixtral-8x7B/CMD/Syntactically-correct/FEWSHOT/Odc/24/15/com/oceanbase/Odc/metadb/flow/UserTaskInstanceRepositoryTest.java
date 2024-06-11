import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

public class UserTaskInstanceRepositoryTest {
private UserTaskInstanceRepository repository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.CREATED);
assertEquals(3, entities.size());
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertEquals(2, entities.size());
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role1");
resourceRoleIdentifiers.add("role2");
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
resourceRoleIdentifiers);
assertEquals(1, entities.size());
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role1");
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertEquals(1, entities.size());
}

@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
assertEquals(2, entities.size());
}

@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
assertEquals(3, entities.size());
}

@Test
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setStatus(FlowNodeStatus.CREATED);
int result = repository.update(entity);
assertEquals(1, result);
}

@Test
public void testUpdateUserTaskIdById() {
int result = repository.updateUserTaskIdById(1L, "userTaskId1");
assertEquals(1, result);
}

@Test
public void testUpdateStatusById() {
int result = repository.updateStatusById(1L, FlowNodeStatus.CREATED);
assertEquals(1, result);
}

@Test
public void testDeleteByFlowInstanceId() {
int result = repository.deleteByFlowInstanceId(1L);
assertEquals(2, result);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> optional = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
"activity1", 1L);
assertEquals("entity1", optional.get().getUserTaskId());
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> optional = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name1", 1L);
assertEquals("entity2", optional.get().getUserTaskId());
}

@Test
public void testFindConfirmById() {
Boolean confirm = repository.findConfirmById(1L);
assertEquals(true, confirm);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
FlowNodeStatus.CREATED);
assertEquals(2, entities.size());
}

@Test
public void testFindAll() {
Pageable pageable = PageRequest.of(0, 10);
Page<UserTaskInstanceEntity> page = repository.findAll(pageable);
assertEquals(5, page.getTotalElements());
}

@Test
public void testFindAllBySpecification() {
Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
FlowNodeStatus.CREATED);
Pageable pageable = PageRequest.of(0, 10);
Page<UserTaskInstanceEntity> page = repository.findAll(spec, pageable);
assertEquals(3, page.getTotalElements());
}

@Test
public void testSave() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setUserTaskId("userTaskId1");
entity.setStatus(FlowNodeStatus.CREATED);
UserTaskInstanceEntity savedEntity = repository.save(entity);
assertEquals("userTaskId1", savedEntity.getUserTaskId());
}

@Test
public void testFindById() {
Optional<UserTaskInstanceEntity> optional = repository.findById(1L);
assertEquals("entity1", optional.get().getUserTaskId());
}

@Test
public void testFindByInstanceId() {
UserTaskInstanceEntity entity = repository.findByInstanceId(1L);
assertEquals("entity1", entity.getUserTaskId());
}

@Test
public void testExistsById() {
boolean exists = repository.existsById(1L);
assertEquals(true, exists);
}

@Test
public void testCount() {
long count = repository.count();
assertEquals(5, count);
}

@Test
public void testDeleteById() {
repository.deleteById(1L);
assertNull(repository.findById(1L).orElse(null));
}

@Test
public void testDeleteAll() {
repository.deleteAll();
long count = repository.count();
assertEquals(0, count);
}
}