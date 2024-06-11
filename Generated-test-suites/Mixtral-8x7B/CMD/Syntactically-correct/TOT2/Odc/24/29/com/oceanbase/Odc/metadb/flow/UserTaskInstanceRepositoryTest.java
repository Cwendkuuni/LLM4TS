import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {}, loader = AnnotationConfigContextLoader.class)
public class UserTaskInstanceRepositoryTest {

@Autowired
private UserTaskInstanceRepository userTaskInstanceRepository;

/*
Test for findByStatus method
*/
@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
assertEquals(3, entities.size());
}

/*
Test for findByCandidateUserIdOrRoleIds method with valid userId and roleIds
*/
@Test
public void testFindByCandidateUserIdOrRoleIdsValid() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
assertEquals(2, entities.size());
}

/*
Test for findByCandidateUserIdOrRoleIds method with invalid userId and roleIds
*/
@Test
public void testFindByCandidateUserIdOrRoleIdsInvalid() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(3L, roleIds);
assertEquals(0, entities.size());
}

/*
Test for findByCandidateUserIdOrResourceRoleIdentifier method with valid userId and roleIdentifiers
*/
@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifierValid() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role_1");
resourceRoleIdentifiers.add("role_2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
assertEquals(2, entities.size());
}

/*
Test for findByCandidateUserIdOrResourceRoleIdentifier method with invalid userId and roleIdentifiers
*/
@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifierInvalid() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role_1");
resourceRoleIdentifiers.add("role_2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(3L, resourceRoleIdentifiers);
assertEquals(0, entities.size());
}

/*
Test for findByResourceRoleIdentifierIn method with valid roleIdentifiers
*/
@Test
public void testFindByResourceRoleIdentifierInValid() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role_1");
resourceRoleIdentifiers.add("role_2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
assertEquals(2, entities.size());
}

/*
Test for findByResourceRoleIdentifierIn method with invalid roleIdentifiers
*/
@Test
public void testFindByResourceRoleIdentifierInInvalid() {
List<String> resourceRoleIdentifiers = new ArrayList<>();
resourceRoleIdentifiers.add("role_1");
resourceRoleIdentifiers.add("role_2");
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.emptyList());
assertEquals(0, entities.size());
}

/*
Test for findByRoleId method with valid roleId
*/
@Test
public void testFindByRoleIdValid() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
assertEquals(1, entities.size());
}

/*
Test for findByRoleId method with invalid roleId
*/
@Test
public void testFindByRoleIdInvalid() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(3L);
assertEquals(0, entities.size());
}

/*
Test for findByCandidateUserId method with valid userId
*/
@Test
public void testFindByCandidateUserIdValid() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
assertEquals(1, entities.size());
}

/*
Test for findByCandidateUserId method with invalid userId
*/
@Test
public void testFindByCandidateUserIdInvalid() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(3L);
assertEquals(0, entities.size());
}

/*
Test for update method with valid UserTaskInstanceEntity object
*/
@Test
public void testUpdateValid() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setUserTaskId("task_1");
entity.setStatus(FlowNodeStatus.RUNNING);
entity.setApproved(true);
entity.setOperatorId(1L);
entity.setComment("comment_1");
entity.setExpireIntervalSeconds(300);
entity.setExternalFlowInstanceId("external_id_1");
int rowsAffected = userTaskInstanceRepository.update(entity);
assertEquals(1, rowsAffected);
}

/*
Test for updateUserTaskIdById method with valid id and userTaskId
*/
@Test
public void testUpdateUserTaskIdByIdValid() {
int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "user_task_1");
assertEquals(1, rowsAffected);
}

/*
Test for updateUserTaskIdById method with invalid id
*/
@Test
public void testUpdateUserTaskIdByIdInvalid() {
int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(3L, "user_task_1");
assertEquals(0, rowsAffected);
}

/*
Test for updateStatusById method with valid id and status
*/
@Test
public void testUpdateStatusByIdValid() {
int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
assertEquals(1, rowsAffected);
}

/*
Test for updateStatusById method with invalid id
*/
@Test
public void testUpdateStatusByIdInvalid() {
int rowsAffected = userTaskInstanceRepository.updateStatusById(3L, FlowNodeStatus.COMPLETED);
assertEquals(0, rowsAffected);
}

/*
Test for deleteByFlowInstanceId method with valid flowInstanceId
*/
@Test
public void testDeleteByFlowInstanceIdValid() {
int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
assertEquals(1, rowsAffected);
}

/*
Test for deleteByFlowInstanceId method with invalid flowInstanceId
*/
@Test
public void testDeleteByFlowInstanceIdInvalid() {
int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(3L);
assertEquals(0, rowsAffected);
}

/*
Test for findByInstanceTypeAndActivityId method with valid instanceType, activityId and flowInstanceId
*/
@Test
public void testFindByInstanceTypeAndActivityIdValid() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activity_1", 1L);
assertEquals(1L, entity.get().getId().longValue());
}

/*
Test for findByInstanceTypeAndActivityId method with invalid instanceType, activityId and flowInstanceId
*/
@Test
public void testFindByInstanceTypeAndActivityIdInvalid() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activity_3", 1L);
assertEquals(Optional.empty(), entity);
}

/*
Test for findByInstanceTypeAndName method with valid instanceType, name and flowInstanceId
*/
@Test
public void testFindByInstanceTypeAndNameValid() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name_1", 1L);
assertEquals(1L, entity.get().getId().longValue());
}

/*
Test for findByInstanceTypeAndName method with invalid instanceType, name and flowInstanceId
*/
@Test
public void testFindByInstanceTypeAndNameInvalid() {
Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name_3", 1L);
assertEquals(Optional.empty(), entity);
}

/*
Test for findConfirmById method with valid id
*/
@Test
public void testFindConfirmByIdValid() {
Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
assertEquals(true, confirmed);
}

/*
Test for findConfirmById method with invalid id
*/
@Test
public void testFindConfirmByIdInvalid() {
Boolean confirmed = userTaskInstanceRepository.findConfirmById(3L);
assertEquals(false, confirmed);
}

/*
Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method with valid flowInstanceIds and status
*/
@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusValid() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.RUNNING);
assertEquals(2, entities.size());
}

/*
Test for findApprovalInstanceIdByFlowInstanceIdAndStatus method with invalid flowInstanceIds and status
*/
@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusInvalid() {
List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(3L, 4L), FlowNodeStatus.RUNNING);
assertEquals(0, entities.size());
}

/*
Test for findAll method with Sorting
*/
@Test
public void testFindAllWithSorting() {
Sort sort = Sort.by(Sort.Direction.ASC, "id");
Page<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll(PageRequest.of(0, 10, sort));
assertEquals(3, entities.getTotalElements());
}

/*
Test for findAll method with Pagination
*/
@Test
public void testFindAllWithPagination() {
PageRequest pageable = PageRequest.of(0, 2);
Page<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll(pageable);
assertEquals(2, entities.getTotalElements());
}

/*
Test for save method with new UserTaskInstanceEntity object
*/
@Test
public void testSaveNew() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setUserTaskId("task_1");
entity.setStatus(FlowNodeStatus.RUNNING);
entity.setApproved(true);
entity.setOperatorId(1L);
entity.setComment("comment_1");
entity.setExpireIntervalSeconds(300);
entity.setExternalFlowInstanceId("external_id_1");
UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
assertEquals(4L, savedEntity.getId().longValue());
}

/*
Test for save method with existing UserTaskInstanceEntity object
*/
@Test
public void testSaveExisting() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
entity.setId(1L);
entity.setUserTaskId("task_1_updated");
entity.setStatus(FlowNodeStatus.RUNNING);
entity.setApproved(true);
entity.setOperatorId(1L);
entity.setComment("comment_1");
entity.setExpireIntervalSeconds(300);
entity.setExternalFlowInstanceId("external_id_1");
UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
assertEquals(1L, savedEntity.getId().longValue());
assertEquals("task_1_updated", savedEntity.getUserTaskId());
}
}