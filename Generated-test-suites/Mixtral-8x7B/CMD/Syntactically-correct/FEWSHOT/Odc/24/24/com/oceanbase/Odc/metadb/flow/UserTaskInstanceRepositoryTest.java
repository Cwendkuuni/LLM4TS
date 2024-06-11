import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
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
resourceRoleIdentifiers.add("ROLE1");
resourceRoleIdentifiers.add("ROLE2");
List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
resourceRoleIdentifiers);
assertEquals(2, entities.size());
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("ROLE1");
List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertEquals(1, entities.size());
}

@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
assertEquals(1, entities.size());
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
entity.setStatus(FlowNodeStatus.COMPLETED);
int result = repository.update(entity);
assertEquals(1, result);
}

@Test
public void testUpdateUserTaskIdById() {
int result = repository.updateUserTaskIdById(1L, "TASK1");
assertEquals(1, result);
}

@Test
public void testUpdateStatusById() {
int result = repository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
assertEquals(1, result);
}

@Test
public void testDeleteByFlowInstanceId() {
int result = repository.deleteByFlowInstanceId(1L);
assertEquals(1, result);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
"ACT1", 1L);
assertNotNull(entity);
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "NAME1", 1L);
assertNotNull(entity);
}

@Test
public void testFindConfirmById() {
Boolean confirm = repository.findConfirmById(1L);
assertNull(confirm);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
FlowNodeStatus.RUNNING);
assertEquals(1, entities.size());
}

@Test
public void testFindAll() {
Pageable pageable = PageRequest.of(0, 10);
Page<UserTaskInstanceEntity> page = repository.findAll(pageable);
assertEquals(10, page.getContent().size());
}

@Test
public void testFindAllWithSpecification() {
Pageable pageable = PageRequest.of(0, 10);
Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
FlowNodeStatus.RUNNING);
Page<UserTaskInstanceEntity> page = repository.findAll(spec, pageable);
assertEquals(3, page.getContent().size());
}
}