import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepository.class)
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
assertEquals(3, entities.size());
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
Collection<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertEquals(2, entities.size());
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
Collection<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("admin");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
resourceRoleIdentifiers);
assertEquals(2, entities.size());
}

@Test
public void testFindByResourceRoleIdentifierIn() {
Collection<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("admin");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertEquals(2, entities.size());
}

@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
assertEquals(2, entities.size());
}

@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
assertEquals(2, entities.size());
}

@Test
@Transactional
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setStatus(FlowNodeStatus.EXECUTING);
int rowsAffected = userTaskInstanceRepository.update(entity);
assertEquals(1, rowsAffected);
}

@Test
@Transactional
public void testUpdateUserTaskIdById() {
int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "123456");
assertEquals(1, rowsAffected);
}

@Test
@Transactional
public void testUpdateStatusById() {
int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.EXECUTING);
assertEquals(1, rowsAffected);
}

@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
assertEquals(2, rowsAffected);
}

@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
FlowNodeType.APPROVAL, "activity1", 1L);
assertEquals(entity.isPresent(), true);
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
FlowNodeType.APPROVAL, "name1", 1L);
assertEquals(entity.isPresent(), true);
}

@Test
public void testFindConfirmById() {
Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
assertEquals(confirm, true);
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
Collection<Long> flowInstanceIds = new ArrayList<>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
flowInstanceIds, FlowNodeStatus.CREATED);
assertEquals(2, entities.size());
}

@Test
public void testFindAll() {
Pageable pageable = PageRequest.of(0, 10);
Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(pageable);
assertEquals(page.getTotalElements(), 5);
}

@Test
public void testFindById() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
assertEquals(entity.isPresent(), true);
}

@Test
public void testSave() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setStatus(FlowNodeStatus.CREATED);
UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
assertEquals(savedEntity.getId(), 6);
}

@Test
public void testExistsById() {
boolean exists = userTaskInstanceRepository.existsById(1L);
assertEquals(exists, true);
}

@Test
public void testCount() {
long count = userTaskInstanceRepository.count();
assertEquals(count, 5);
}

@Test
public void testDeleteById() {
boolean deleted = userTaskInstanceRepository.deleteById(1L);
assertEquals(deleted, true);
}

@Test
public void testFindAllByOrderByIdDesc() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllByOrderByIdDesc();
assertEquals(entities.size(), 5);
}

}