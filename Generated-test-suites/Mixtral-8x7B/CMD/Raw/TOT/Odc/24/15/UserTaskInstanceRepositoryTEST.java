import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
        assertThat(entities).isNotEmpty();
        entities.forEach(entity -> assertThat(entity.getStatus()).isEqualTo(FlowNodeStatus.RUNNING));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(3L, roleIds);
        assertThat(entities).isNotEmpty();
        entities.forEach(entity -> {
            assertThat(entity.getCandidateUserIds()).contains(3L);
            or(entity.getCandidateRoleIds()).containsAnyOf(1L, 2L);
        });
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "test-user-task-id";
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertThat(rowsAffected).isEqualTo(1);
        UserTaskInstanceEntity entity = userTaskInstanceRepository.findById(id).orElse(null);
        assertThat(entity).isNotNull();
        assertThat(entity.getUserTaskId()).isEqualTo(userTaskId);
    }

    // Add more test cases for other methods

}