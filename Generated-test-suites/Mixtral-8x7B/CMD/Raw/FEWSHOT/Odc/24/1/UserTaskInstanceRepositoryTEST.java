import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

 @Test
 public void testFindByStatus() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());

 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findByStatus(FlowNodeStatus status) {
 return entities;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findByStatus(FlowNodeStatus.CREATED);

 assertEquals(3, result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrRoleIds() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());

 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findByCandidateUserIdOrRoleIds(Long userId, Collection<Long> roleIds) {
 return entities;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(2L));

 assertEquals(3, result.size());
 }

 @Test
 public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());

 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findByCandidateUserIdOrResourceRoleIdentifier(Long userId,
 Collection<String> resourceRoleIdentifiers) {
 return entities;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findByCandidateUserIdOrResourceRoleIdentifier(1L,
 Collections.singletonList("resourceRoleIdentifier"));

 assertEquals(3, result.size());
 }

 @Test
 public void testFindByResourceRoleIdentifierIn() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());

 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findByResourceRoleIdentifierIn(Collection<String> resourceRoleIdentifier) {
 return entities;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findByResourceRoleIdentifierIn(
 Collections.singletonList("resourceRoleIdentifier"));

 assertEquals(3, result.size());
 }

 @Test
 public void testFindByRoleId() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());

 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findByRoleId(Long roleId) {
 return entities;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findByRoleId(1L);

 assertEquals(3, result.size());
 }

 @Test
 public void testFindByCandidateUserId() {
 List<UserTaskInstanceEntity> entities = new ArrayList<>();
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());
 entities.add(new UserTaskInstanceEntity());

 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findByCandidateUserId(Long userId) {
 return entities;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findByCandidateUserId(1L);

 assertEquals(3, result.size());
 }

 @Test
 public void testUpdate() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public int update(UserTaskInstanceEntity entity) {
 return 1;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 int result = testClass.update(new UserTaskInstanceEntity());

 assertEquals(1, result);
 }

 @Test
 public void testUpdateUserTaskIdById() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public int updateUserTaskIdById(Long id, String userTaskId) {
 return 1;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 int result = testClass.updateUserTaskIdById(1L, "userTaskId");

 assertEquals(1, result);
 }

 @Test
 public void testUpdateStatusById() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public int updateStatusById(Long id, FlowNodeStatus status) {
 return 1;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 int result = testClass.updateStatusById(1L, FlowNodeStatus.CREATED);

 assertEquals(1, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public int deleteByFlowInstanceId(Long flowInstanceId) {
 return 1;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 int result = testClass.deleteByFlowInstanceId(1L);

 assertEquals(1, result);
 }

 @Test
 public void testFindByInstanceTypeAndActivityId() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public Optional<UserTaskInstanceEntity> findByInstanceTypeAndActivityId(FlowNodeType instanceType, String activityId,
 Long flowInstanceId) {
 return Optional.of(new UserTaskInstanceEntity());
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 Optional<UserTaskInstanceEntity> result = testClass.findByInstanceTypeAndActivityId(FlowNodeType.ASSIGN, "activityId",
 1L);

 assertEquals(true, result.isPresent());
 }

 @Test
 public void testFindByInstanceTypeAndName() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public Optional<UserTaskInstanceEntity> findByInstanceTypeAndName(FlowNodeType instanceType, String name,
 Long flowInstanceId) {
 return Optional.of(new UserTaskInstanceEntity());
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 Optional<UserTaskInstanceEntity> result = testClass.findByInstanceTypeAndName(FlowNodeType.ASSIGN, "name", 1L);

 assertEquals(true, result.isPresent());
 }

 @Test
 public void testFindConfirmById() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public Boolean findConfirmById(Long id) {
 return true;
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 Boolean result = testClass.findConfirmById(1L);

 assertEquals(true, result);
 }

 @Test
 public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
 JpaRepository<UserTaskInstanceEntity, Long> repository = new UserTaskInstanceRepository() {
 @Override
 public List<UserTaskInstanceEntity> findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collection<Long> flowInstanceIds, FlowNodeStatus status) {
 return new ArrayList<>();
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 List<UserTaskInstanceEntity> result = testClass.findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collections.singletonList(1L), FlowNodeStatus.CREATED);

 assertEquals(0, result.size());
 }

 // Specification tests

 @Test
 public void testFindAllBySpecification() {
 UserTaskInstanceRepository repository = new UserTaskInstanceRepository() {
 @Override
 public Page<UserTaskInstanceEntity> findAll(Specification<UserTaskInstanceEntity> spec, Pageable pageable) {
 return new PageImpl<>(Collections.singletonList(new UserTaskInstanceEntity()));
 }
 };

 UserTaskInstanceRepository testClass = new UserTaskInstanceRepositoryTest().new UserTaskInstanceRepositoryStub(repository);

 Page<UserTaskInstanceEntity> result = testClass.findAll(null, PageRequest.of(0, 10));

 assertEquals(1, result.getTotalElements());
 }

 class UserTaskInstanceRepositoryStub extends UserTaskInstanceRepository {
 private JpaRepository<UserTaskInstanceEntity, Long> repository;

 UserTaskInstanceRepositoryStub(JpaRepository<UserTaskInstanceEntity, Long> repository) {
 this.repository = repository;
 }

 @Override
 public List<UserTaskInstanceEntity> findByStatus(FlowNodeStatus status) {
 return repository.findAll();
 }

 @Override
 public List<UserTaskInstanceEntity> findByCandidateUserIdOrRoleIds(Long userId, Collection<Long> roleIds) {
 return repository.findAll();
 }

 @Override
 public List<UserTaskInstanceEntity> findByCandidateUserIdOrResourceRoleIdentifier(Long userId,
 Collection<String> resourceRoleIdentifiers) {
 return repository.findAll();
 }

 @Override
 public List<UserTaskInstanceEntity> findByResourceRoleIdentifierIn(Collection<String> resourceRoleIdentifier) {
 return repository.findAll();
 }

 @Override
 public List<UserTaskInstanceEntity> findByRoleId(Long roleId) {
 return repository.findAll();
 }

 @Override
 public List<UserTaskInstanceEntity> findByCandidateUserId(Long userId) {
 return repository.findAll();
 }

 @Override
 public int update(UserTaskInstanceEntity entity) {
 return repository.save(entity).getId().intValue();
 }

 @Override
 public int updateUserTaskIdById(Long id, String userTaskId) {
 return repository.updateUserTaskIdById(id, userTaskId);
 }

 @Override
 public int updateStatusById(Long id, FlowNodeStatus status) {
 return repository.updateStatusById(id, status);
 }

 @Override
 public int deleteByFlowInstanceId(Long flowInstanceId) {
 return repository.deleteByFlowInstanceId(flowInstanceId);
 }

 @Override
 public Optional<UserTaskInstanceEntity> findByInstanceTypeAndActivityId(FlowNodeType instanceType, String activityId,
 Long flowInstanceId) {
 return repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
 }

 @Override
 public Optional<UserTaskInstanceEntity> findByInstanceTypeAndName(FlowNodeType instanceType, String name,
 Long flowInstanceId) {
 return repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
 }

 @Override
 public Boolean findConfirmById(Long id) {
 return repository.findConfirmById(id);
 }

 @Override
 public List<UserTaskInstanceEntity> findApprovalInstanceIdByFlowInstanceIdAndStatus(
 Collection<Long> flowInstanceIds, FlowNodeStatus status) {
 return repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
 }

 @Override
 public Page<UserTaskInstanceEntity> findAll(Specification<UserTaskInstanceEntity> spec, Pageable pageable) {
 return repository.findAll(spec, pageable);
 }
 }
}