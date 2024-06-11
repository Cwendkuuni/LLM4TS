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
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
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
        // Create some UserTaskInstanceEntity with different statuses
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        task1.setStatus(FlowNodeStatus.CREATED);
        entityManager.persist(task1);

        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        task2.setStatus(FlowNodeStatus.APPROVED);
        entityManager.persist(task2);

        UserTaskInstanceEntity task3 = new UserTaskInstanceEntity();
        task3.setStatus(FlowNodeStatus.CREATED);
        entityManager.persist(task3);

        // Test the method
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);

        // Assert the results
        assertThat(results).hasSize(2).contains(task1, task3);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        // Create some UserTaskInstanceEntity with different candidate user IDs and role IDs
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        entityManager.persist(task1);

        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        entityManager.persist(task2);

        UserTaskInstanceEntity task3 = new UserTaskInstanceEntity();
        task3.setCandidateUserId(1L);
        entityManager.persist(task3);

        UserTaskInstanceEntity task4 = new UserTaskInstanceEntity();
        task4.setCandidateRoleIds(Arrays.asList(1L, 2L));
        entityManager.persist(task4);

        UserTaskInstanceEntity task5 = new UserTaskInstanceEntity();
        task5.setCandidateUserId(2L);
        entityManager.persist(task5);

        // Test the method
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(1L, 3L));

        // Assert the results
        assertThat(results).hasSize(3).contains(task1, task3, task4);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        // Create some UserTaskInstanceEntity with different candidate user IDs and resource role identifiers
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        entityManager.persist(task1);

        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        entityManager.persist(task2);

        UserTaskInstanceEntity task3 = new UserTaskInstanceEntity();
        task3.setCandidateUserId(1L);
        entityManager.persist(task3);

        UserTaskInstanceEntity task4 = new UserTaskInstanceEntity();
        task4.setResourceRoleIdentifiers(Arrays.asList("role1", "role2"));
        entityManager.persist(task4);

        UserTaskInstanceEntity task5 = new UserTaskInstanceEntity();
        task5.setCandidateUserId(2L);
        entityManager.persist(task5);

        // Test the method
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
                1L, Arrays.asList("role1", "role3"));

        // Assert the results
        assertThat(results).hasSize(3).contains(task1, task3, task4);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        // Create some UserTaskInstanceEntity with different resource role identifiers
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        entityManager.persist(task1);

        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        entityManager.persist(task2);

        UserTaskInstanceEntity task3 = new UserTaskInstanceEntity();
        task3.setResourceRoleIdentifiers(Arrays.asList("role1"));
        entityManager.persist(task3);

        UserTaskInstanceEntity task4 = new UserTaskInstanceEntity();
        task4.setResourceRoleIdentifiers(Arrays.asList("role2"));
        entityManager.persist(task4);

        UserTaskInstanceEntity task5 = new UserTaskInstanceEntity();
        task5.setResourceRoleIdentifiers(Arrays.asList("role1", "role3"));
        entityManager.persist(task5);

        // Test the method
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1", "role4"));

        // Assert the results
        assertThat(results).hasSize(3).contains(task3, task5);
    }

    @Test
    public void testFindByRoleId() {
        // Create some UserTaskInstanceEntity with different role IDs
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        entityManager.persist(task1);

        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        entityManager.persist(task2);

        UserTaskInstanceEntity task3 = new UserTaskInstanceEntity();
        task3.setCandidateRoleIds(Arrays.asList(1L));
        entityManager.persist(task3);

        UserTaskInstanceEntity task4 = new UserTaskInstanceEntity();
        task4.setCandidateRoleIds(Arrays.asList(2L));
        entityManager.persist(task4);

        // Test the method
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByRoleId(1L);

        // Assert the results
        assertThat(results).hasSize(2).contains(task1, task3);
    }

    @Test
    public void testFindByCandidateUserId() {
        // Create some UserTaskInstanceEntity with different candidate user IDs
        UserTaskInstanceEntity task1 = new UserTaskInstanceEntity();
        entityManager.persist(task1);

        UserTaskInstanceEntity task2 = new UserTaskInstanceEntity();
        entityManager.persist(task2);

        UserTaskInstanceEntity task3 = new UserTaskInstanceEntity();
        task3.setCandidateUserId(1L);
        entityManager.persist(task3);

        UserTaskInstanceEntity task4 = new UserTaskInstanceEntity();
        task4.setCandidateUserId(2L);
        entityManager.persist(task4);

        // Test the method
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserId(1L);

        // Assert the results
        assertThat(results).hasSize(2).contains(task1, task3);
    }

    // Add test cases for other methods and edge cases here

    // Helper method to clean up the database after each test
    @Transactional
    void truncateTables() {
        JdbcTestUtils.deleteFromTables(entityManager.getJdbcTemplate(), "flow_instance_node_approval",
                "flow_instance_node");
    }

}