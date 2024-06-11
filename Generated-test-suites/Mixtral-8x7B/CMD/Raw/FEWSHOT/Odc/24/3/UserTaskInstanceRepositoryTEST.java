import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.STARTED);
 assertEquals(3, result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 Long userId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(2L);
 roleIds.add(3L);
 List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 Long userId = 1L;
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 resourceRoleIdentifiers.add("role2");
 List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
 resourceRoleIdentifiers);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 Collection<String> resourceRoleIdentifiers = new ArrayList<>();
 resourceRoleIdentifiers.add("role1");
 List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByRoleId() {
 Long roleId = 2L;
 List<UserTaskInstanceEntity> result = repository.findByRoleId(roleId);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 Long userId = 1L;
 List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(userId);
 assertEquals(2, result.size());
 }

 @Test
 public void testUpdate() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 entity.setId(1L);
 int result = repository.update(entity);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 Long id = 1L;
 String userTaskId = "userTask1";
 int result = repository.updateUserTaskIdById(id, userTaskId);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 Long id = 1L;
 FlowNodeStatus status = FlowNodeStatus.COMPLETED;
 int result = repository.updateStatusById(id, status);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long flowInstanceId = 1L;
 int result = repository.deleteByFlowInstanceId(flowInstanceId);
 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String activityId = "activity1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(instanceType, activityId,
 flowInstanceId);
 assertEquals(Optional.of(new UserTaskInstanceEntity()), result);
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 FlowNodeType instanceType = FlowNodeType.APPROVAL;
 String name = "name1";
 Long flowInstanceId = 1L;
 Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 assertEquals(Optional.of(new UserTaskInstanceEntity()), result);
 }

 @Test
 public void testFindConfirmById() {
 Long id = 1L;
 Boolean result = repository.findConfirmById(id);
 assertEquals(true, result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 Collection<Long> flowInstanceIds = new ArrayList<>();
 flowInstanceIds.add(1L);
 FlowNodeStatus status = FlowNodeStatus.STARTED;
 List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
 status);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindAll() {
 List<UserTaskInstanceEntity> result = repository.findAll();
 assertEquals(5, result.size());
 }

 @Test
 public void testFindAllSort() {
 List<UserTaskInstanceEntity> result = repository.findAll(Sort.by(Sort.Direction.ASC, "id"));
 assertEquals(5, result.size());
 }

 @Test
 public void testFindAllPage() {
 Pageable pageable = PageRequest.of(0, 2);
 Page<UserTaskInstanceEntity> result = repository.findAll(pageable);
 assertEquals(2, result.getContent().size());
 }

 @Test
 public void testFindOne() {
 Long id = 1L;
 UserTaskInstanceEntity result = repository.findOne(id);
 assertEquals(new UserTaskInstanceEntity(), result);
 }

 @Test
 public void testExistsById() {
 Long id = 1L;
 boolean result = repository.existsById(id);
 assertEquals(true, result);
 }

 @Test
 public void testCount() {
 Long result = repository.count();
 assertEquals(5L, result);
 }

 @Test
 public void testDeleteById() {
 Long id = 1L;
 repository.deleteById(id);
 }

 @Test
 public void testDeleteAll() {
 repository.deleteAll();
 }

 @Test
 public void testFindAllById() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<UserTaskInstanceEntity> result = repository.findAllById(ids);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindById() {
 Long id = 1L;
 UserTaskInstanceEntity result = repository.findById(id).orElse(null);
 assertEquals(new UserTaskInstanceEntity(), result);
 }

 @Test
 public void testSave() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 UserTaskInstanceEntity result = repository.save(entity);
 assertEquals(new UserTaskInstanceEntity(), result);
 }

 @Test
 public void testSaveAll() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 List<UserTaskInstanceEntity> result = repository.saveAll(entities);
 assertEquals(2, result.size());
 }

 @Test
 public void testFindAllBySpecification() {
 Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
 FlowNodeStatus.STARTED);
 List<UserTaskInstanceEntity> result = repository.findAll(spec);
 assertEquals(3, result.size());
 }

 @Test
 public void testFindOneBySpecification() {
 Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
 FlowNodeStatus.STARTED);
 UserTaskInstanceEntity result = repository.findOne(spec).orElse(null);
 assertEquals(new UserTaskInstanceEntity(), result);
 }

 @Test
 public void testCountBySpecification() {
 Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
 FlowNodeStatus.STARTED);
 Long result = repository.count(spec);
 assertEquals(3L, result);
 }

 @Test
 public void testExistsBySpecification() {
 Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
 FlowNodeStatus.STARTED);
 boolean result = repository.exists(spec);
 assertEquals(true, result);
 }

 @Test
 public void testFlush() {
 repository.save(new UserTaskInstanceEntity());
 repository.flush();
 }

 @Test
 public void testDetach() {
 UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
 repository.save(entity);
 Long id = entity.getId();
 UserTaskInstanceEntity detachedEntity = repository.getOne(id);
 repository.detach(detachedEntity);
 }

 @Test
 public void testFindAllBySpecificationAndPage() {
 Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
 FlowNodeStatus.STARTED);
 Pageable pageable = PageRequest.of(0, 2);
 Page<UserTaskInstanceEntity> result = repository.findAll(spec, pageable);
 assertEquals(2, result.getContent().size());
 }
}