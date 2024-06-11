
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertFalse(entities.isEmpty());
        entities.forEach(entity -> assertEquals(FlowNodeStatus.CREATED, entity.getStatus()));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Arrays.asList(2L, 3L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertTrue(entities.size() > 0);
        entities.forEach(entity -> {
            if (entity.getCandidateUserId().equals(userId))
                assertTrue(entity.getCandidateUserId().equals(userId));
            else
                assertTrue(entity.getCandidateRoleIds().containsAll(roleIds));
        });
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = Arrays.asList("ROLE1", "ROLE2");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertTrue(entities.size() > 0);
        entities.forEach(entity -> {
            if (entity.getCandidateUserId().equals(userId))
                assertTrue(entity.getCandidateUserId().equals(userId));
            else
                assertTrue(entity.getCandidateResourceRoleIdentifiers().containsAll(resourceRoleIdentifiers));
        });
    }
}
