import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { UserTaskInstanceRepository.class })
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;

@Test
@Transactional
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.WAITING);
assertNotNull(entities);
}

@Test
@Transactional
public void testFindByCandidateUserIdOrRoleIds() {
List<Long> roleIds = new ArrayList<Long>();
roleIds.add(1L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertNotNull(entities);
}

@Test
@Transactional
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<String> resourceRoleIdentifiers = new ArrayList<String>();
resourceRoleIdentifiers.add("admin");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
resourceRoleIdentifiers);
assertNotNull(entities);
}

@Test
@Transactional
public void testFindByResourceRoleIdentifierIn() {
List<String> resourceRoleIdentifiers = new ArrayList<String>();
resourceRoleIdentifiers.add("admin");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertNotNull(entities);
}

@Test
@Transactional
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
assertNotNull(entities);
}

@Test
@Transactional
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
assertNotNull(entities);
}

@Test
@Transactional
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setStatus(FlowNodeStatus.COMPLETED);
int result = userTaskInstanceRepository.update(entity);
assertEquals(1, result);
}

@Test
@Transactional
public void testUpdateUserTaskIdById() {
int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "123456");
assertEquals(1, result);
}

@Test
@Transactional
public void testUpdateStatusById() {
int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
assertEquals(1, result);
}

@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
assertEquals(1, result);
}

@Test
@Transactional
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
FlowNodeType.APPROVAL, "123456", 1L);
assertNotNull(entity);
}

@Test
@Transactional
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
FlowNodeType.APPROVAL, "name", 1L);
assertNotNull(entity);
}

@Test
@Transactional
public void testFindConfirmById() {
Boolean result = userTaskInstanceRepository.findConfirmById(1L);
assertNotNull(result);
}

@Test
@Transactional
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<Long> flowInstanceIds = new ArrayList<Long>();
flowInstanceIds.add(1L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
flowInstanceIds, FlowNodeStatus.WAITING);
assertNotNull(entities);
}

@Test
public void testFindAll() {
Pageable pageable = PageRequest.of(0, 10);
Page<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll(pageable);
assertNotNull(entities);
}

@Test
public void testFindById() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
assertNotNull(entity);
}

@Test
public void testExistsById() {
boolean result = userTaskInstanceRepository.existsById(1L);
assertEquals(true, result);
}

@Test
public void testCount() {
long result = userTaskInstanceRepository.count();
assertNotNull(result);
}

@Test
public void testDeleteById() {
boolean result = userTaskInstanceRepository.deleteById(1L);
assertEquals(true, result);
}

@Test
public void testFindAllById() {
List<Long> ids = new ArrayList<Long>();
ids.add(1L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllById(ids);
assertNotNull(entities);
}

@Test
public void testSave() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setStatus(FlowNodeStatus.COMPLETED);
UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
assertNotNull(savedEntity);
}

@Test
public void testFindAllByStatusIn() {
List<FlowNodeStatus> statuses = new ArrayList<FlowNodeStatus>();
statuses.add(FlowNodeStatus.WAITING);
statuses.add(FlowNodeStatus.COMPLETED);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllByStatusIn(statuses);
assertNotNull(entities);
}

}