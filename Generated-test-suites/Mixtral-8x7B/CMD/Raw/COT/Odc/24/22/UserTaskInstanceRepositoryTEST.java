import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UserTaskInstanceRepositoryTest.Config.class}, loader = AnnotationConfigContextLoader.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        // Test with existing status
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.READY);
        assertFalse(entities.isEmpty());

        // Test with non-existent status
        entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.fromValue("non-existent-status"));
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Test with existing userId and roleIds
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertFalse(entities.isEmpty());

        // Test with non-existent userId and existing roleIds
        entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(3L, roleIds);
        assertTrue(entities.isEmpty());

        // Test with existing userId and non-existent roleIds
        roleIds.clear();
        roleIds.add(3L);
        entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertFalse(entities.isEmpty());

        // Test with non-existent userId and non-existent roleIds
        entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(3L, roleIds);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Test with existing userId and resourceRoleIdentifiers
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        resourceRoleIdentifiers.add("role2");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        assertFalse(entities.isEmpty());

        // Test with non-existent userId and existing resourceRoleIdentifiers
        entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(3L, resourceRoleIdentifiers);
        assertTrue(entities.isEmpty());

        // Test with existing userId and non-existent resourceRoleIdentifiers
        resourceRoleIdentifiers.clear();
        resourceRoleIdentifiers.add("role3");
        entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        assertFalse(entities.isEmpty());

        // Test with non-existent userId and non-existent resourceRoleIdentifiers
        entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(3L, resourceRoleIdentifiers);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Test with existing resourceRoleIdentifiers
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        resourceRoleIdentifiers.add("role2");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertFalse(entities.isEmpty());

        // Test with non-existent resourceRoleIdentifiers
        entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("non-existent-role"));
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByRoleId() {
        // Test with existing roleId
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        assertFalse(entities.isEmpty());

        // Test with non-existent roleId
        entities = userTaskInstanceRepository.findByRoleId(3L);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserId() {
        // Test with existing userId
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertFalse(entities.isEmpty());

        // Test with non-existent userId
        entities = userTaskInstanceRepository.findByCandidateUserId(3L);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testUpdate() {
        // Test with an existing entity
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        // Set necessary fields
        int rowsAffected = userTaskInstanceRepository.update(entity);
        assertEquals(1, rowsAffected);

        // Test with a non-existent entity
        entity.setId(3L);
        rowsAffected = userTaskInstanceRepository.update(entity);
        assertEquals(0, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        // Test with an existing id
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "test-user-task-id");
        assertEquals(1, rowsAffected);

        // Test with a non-existent id
        rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(3L, "test-user-task-id");
        assertEquals(0, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        // Test with an existing id and status
        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(1, rowsAffected);

        // Test with a non-existent id and existing status
        rowsAffected = userTaskInstanceRepository.updateStatusById(3L, FlowNodeStatus.COMPLETED);
        assertEquals(0, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Test with an existing flowInstanceId
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, rowsAffected);

        // Test with a non-existent flowInstanceId
        rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(3L);
        assertEquals(0, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        // Test with existing instanceType, activityId, and flowInstanceId
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activity1", 1L);
        assertTrue(entityOptional.isPresent());

        // Test with non-existent instanceType, activityId, and flowInstanceId
        entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.fromValue("non-existent-type"), "non-existent-activity", 3L);
        assertTrue(entityOptional.isEmpty());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        // Test with existing instanceType, name, and flowInstanceId
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name1", 1L);
        assertTrue(entityOptional.isPresent());

        // Test with non-existent instanceType, name, and flowInstanceId
        entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.fromValue("non-existent-type"), "non-existent-name", 3L);
        assertTrue(entityOptional.isEmpty());
    }

    @Test
    public void testFindConfirmById() {
        // Test with existing id
        Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(confirmed);

        // Test with non-existent id
        confirmed = userTaskInstanceRepository.findConfirmById(3L);
        assertNull(confirmed);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        // Test with existing flowInstanceIds and status
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.READY);
        assertFalse(entities.isEmpty());

        // Test with non-existent flowInstanceIds and existing status
        entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(3L), FlowNodeStatus.READY);
        assertTrue(entities.isEmpty());

        // Test with existing flowInstanceIds and non-existent status
        entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.fromValue("non-existent-status"));
        assertTrue(entities.isEmpty());
    }

    // Other test methods for pagination and other methods

    // ...

    @org.springframework.context.annotation.Configuration
    static class Config {
        // Register any required beans for testing
    }
}