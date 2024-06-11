
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.RUNNING);
        // assertions based on the expected outcome
        assertThat(entities).isNotNull();
    }

    // Additional test cases from Expert 1, 2, and 3

}
