import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

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
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("task1");
        entity.setStatus(FlowNodeStatus.CREATED);
        entity.setApproved(false);
        entity.setOperatorId(1L);
        entity.setComment("comment1");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("instance1");
        int result = userTaskInstanceRepository.update(entity);
        // Check if the returned result is greater than 0
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        // Check if the returned result is greater than 0
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.CREATED;
        int result = userTaskInstanceRepository.updateStatusById(id, status);
        // Check if the returned result is greater than 0
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        // Check if the returned result is greater than 0
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        // Check if the returned Optional contains a non-null value
        assertThat(entity).isPresent();
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        // Check if the returned Optional contains a non-null value
        assertThat(entity).isPresent();
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        // Check if the returned value is null
        assertThat(result).isNull();
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.CREATED;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        // Check if the returned list is not empty
        assertThat(entities).isNotEmpty();
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setUserTaskId("task1");
        entity.setStatus(FlowNodeStatus.CREATED);
        entity.setApproved(false);
        entity.setOperatorId(1L);
        entity.setComment("comment1");
        entity.setExpireIntervalSeconds(60);
        entity.setExternalFlowInstanceId("instance1");
        entityManager.persist(entity);
        entityManager.flush();
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
        // Check if the returned list contains the saved entity
        assertThat(entities).contains(entity);
    }
}