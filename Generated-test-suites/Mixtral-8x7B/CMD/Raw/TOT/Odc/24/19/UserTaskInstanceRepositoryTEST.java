import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
        assertTrue(result.size() > 0);
        for (UserTaskInstanceEntity entity : result) {
            assertEquals(FlowNodeStatus.RUNNING, entity.getStatus());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = Arrays.asList(2L, 3L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertTrue(result.size() > 0);
        for (UserTaskInstanceEntity entity : result) {
            assertTrue(entity.getCandidates().stream().anyMatch(c -> c.getUserId().equals(userId) || roleIds.contains(c.getRoleId())));
        }
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "test-user-task-id";
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(id);
        assertTrue(entityOptional.isPresent());
        UserTaskInstanceEntity entity = entityOptional.get();
        assertEquals(userTaskId, entity.getUserTaskId());
    }
}