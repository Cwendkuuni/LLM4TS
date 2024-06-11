
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.READY);
        assertNotNull(entities);
        assertTrue(!entities.isEmpty());
        for (UserTaskInstanceEntity entity : entities) {
            assertEquals(FlowNodeStatus.READY, entity.getStatus());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
        assertNotNull(entities);
        assertTrue(!entities.isEmpty());
        for (UserTaskInstanceEntity entity : entities) {
            assertTrue(entity.getCandidateUserIds().contains(1L) || entity.getCandidateRoleIds().contains(2L) || entity.getCandidateRoleIds().contains(3L));
        }
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        ReflectionTestUtils.setField(entity, "id", 1L);
        ReflectionTestUtils.setField(entity, "status", FlowNodeStatus.READY);
        int rowsAffected = userTaskInstanceRepository.update(entity);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("identifier1", "identifier2"));
        assertNotNull(entities);
        assertTrue(!entities.isEmpty());
        for (UserTaskInstanceEntity entity : entities) {
            assertTrue(entity.getResourceRoleIdentifiers().contains("identifier1") || entity.getResourceRoleIdentifiers().contains("identifier2"));
        }
    }

    @Test
    public void testUpdateStatusById() {
        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.SERVICE_TASK, "activityId", 1L);
        assertTrue(entityOptional.isPresent());
        UserTaskInstanceEntity entity = entityOptional.get();
        assertEquals(FlowNodeType.SERVICE_TASK, entity.getInstanceType());
        assertEquals("activityId", entity.getActivityId());
        assertEquals(1L, entity.getFlowInstanceId().longValue());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.READY);
        assertNotNull(entities);
        assertTrue(!entities.isEmpty());
        for (UserTaskInstanceEntity entity : entities) {
            assertTrue(entity.getFlowInstanceId().equals(1L) || entity.getFlowInstanceId().equals(2L));
            assertEquals(FlowNodeStatus.READY, entity.getStatus());
        }
    }
}
