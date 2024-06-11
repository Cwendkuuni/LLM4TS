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

import com.oceanbase.odc.metadb.flow.entity.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.entity.FlowNodeType;
import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;

public class UserTaskInstanceRepositoryTest {

    private UserTaskInstanceRepository userTaskInstanceRepository;

    public UserTaskInstanceRepositoryTest(UserTaskInstanceRepository userTaskInstanceRepository) {
        this.userTaskInstanceRepository = userTaskInstanceRepository;
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.READY);
        assertEquals(3, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, List.of(2L, 3L));
        assertEquals(2, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                List.of("role1", "role2"));
        assertEquals(1, entities.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(List.of("role1"));
        assertEquals(1, entities.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(2L);
        assertEquals(1, entities.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(2, entities.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.FINISHED);

        int rowsAffected = userTaskInstanceRepository.update(entity);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "new_user_task_id");
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.FINISHED);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "activity1", 1L);
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "name1", 1L);
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(confirmed);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                List.of(1L), FlowNodeStatus.READY);
        assertEquals(2, entities.size());
    }

    // Add more test methods for other repository methods
}