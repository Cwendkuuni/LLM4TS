import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = YourApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {"spring.config.location=classpath:application-test.yml"},
        loader = AnnotationConfigContextLoader.class)
@ActiveProfiles("test")
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.INITIALIZED);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(2L, 3L));
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("role1", "role2"));
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(1L).get();
        int updatedRows = userTaskInstanceRepository.update(entity);
        assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(1L, "test-user-task-id");
        assertEquals(1, updatedRows);
    }

    @Test
    public void testUpdateStatusById() {
        int updatedRows = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(1, updatedRows);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, deletedRows);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.TASK, "test-activity-id", 1L);
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.TASK, "test-name", 1L);
        assertTrue(entityOptional.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(confirmed != null);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L), FlowNodeStatus.INITIALIZED);
        assertTrue(entities.size() > 0);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        userTaskInstanceRepository.update(entity);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeUpdateUserTaskIdById() {
        userTaskInstanceRepository.updateUserTaskIdById(1L, null);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeUpdateStatusById() {
        userTaskInstanceRepository.updateStatusById(1L, null);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeDeleteByFlowInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(-1L);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeFindByInstanceTypeAndActivityId() {
        userTaskInstanceRepository.findByInstanceTypeAndActivityId(null, "test-activity-id", 1L);
    }

    @Test(expected = RuntimeException.class)
    public void testNegativeFindByInstanceTypeAndName() {
        userTaskInstanceRepository.findByInstanceTypeAndName(null, "test-name", 1L);
    }

    @Test
    public void testFindByStatusWithEmptyList() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(null);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIdsWithEmptyLists() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(null, new ArrayList<>());
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifierWithEmptyLists() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(null, new ArrayList<>());
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByResourceRoleIdentifierInWithEmptyList() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(new ArrayList<>());
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByRoleIdWithEmptyList() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(null);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdWithEmptyList() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(null);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatusWithEmptyList() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                new ArrayList<>(), null);
        assertTrue(entities.isEmpty());
    }
}