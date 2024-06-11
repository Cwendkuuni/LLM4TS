import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private List<UserTaskInstanceEntity> userTaskEntities;

    @Before
    public void setUp() {
        userTaskEntities = new ArrayList<>();

        for (long i = 1; i <= 5; i++) {
            UserTaskInstanceEntity userTaskEntity = new UserTaskInstanceEntity();
            userTaskEntity.setId(i);
            userTaskEntity.setUserTaskId("userTaskId_" + i);
            userTaskEntity.setStatus(FlowNodeStatus.CREATED);
            userTaskEntity.setApproved(true);
            userTaskEntity.setOperatorId((int) i);
            userTaskEntity.setComment("comment_" + i);
            userTaskEntity.setExpireIntervalSeconds(300);
            userTaskEntity.setExternalFlowInstanceId("externalFlowInstanceId_" + i);
            userTaskEntity.setFlowInstanceId(1L);

            userTaskEntities.add(userTaskEntity);
        }

        userTaskEntities.get(0).setStatus(FlowNodeStatus.RUNNING);
        userTaskEntities.get(1).setStatus(FlowNodeStatus.FAILED);
        userTaskEntities.get(2).setStatus(FlowNodeStatus.COMPLETED);
        userTaskEntities.get(3).setStatus(FlowNodeStatus.CANCELED);
        userTaskEntities.get(4).setStatus(FlowNodeStatus.SKIPPED);

        entityManager.persist(userTaskEntities.get(0));
        entityManager.persist(userTaskEntities.get(2));
        entityManager.persist(userTaskEntities.get(4));
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertThat(result).hasSize(2).extracting(UserTaskInstanceEntity::getStatus)
                .containsOnly(FlowNodeStatus.CREATED);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Long nonExistentUserId = 100L;
        List<Long> roleIds = Collections.singletonList(1L);
        List<Long> nonExistentRoleIds = Collections.singletonList(100L);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertThat(result).hasSize(1).extracting(UserTaskInstanceEntity::getStatus)
                .containsOnly(FlowNodeStatus.CREATED);

        result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(nonExistentUserId, roleIds);
        assertThat(result).isEmpty();

        result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, nonExistentRoleIds);
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        String resourceRoleIdentifier = "resourceRoleIdentifier_1";
        String nonExistentResourceRoleIdentifier = "nonExistentResourceRoleIdentifier_1";

        List<String> resourceRoleIdentifiers = Collections.singletonList(resourceRoleIdentifier);
        List<String> nonExistentResourceRoleIdentifiers = Collections.singletonList(nonExistentResourceRoleIdentifier);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertThat(result).hasSize(1).extracting(UserTaskInstanceEntity::getStatus)
                .containsOnly(FlowNodeStatus.CREATED);

        result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                nonExistentResourceRoleIdentifiers);
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = Collections.singletonList("resourceRoleIdentifier_1");
        List<String> nonExistentResourceRoleIdentifiers = Collections.singletonList("nonExistentResourceRoleIdentifier_1");

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                resourceRoleIdentifiers);
        assertThat(result).hasSize(1).extracting(UserTaskInstanceEntity::getStatus)
                .containsOnly(FlowNodeStatus.CREATED);

        result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(nonExistentResourceRoleIdentifiers);
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        Long nonExistentRoleId = 100L;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);
        assertThat(result).hasSize(1).extracting(UserTaskInstanceEntity::getStatus)
                .containsOnly(FlowNodeStatus.CREATED);

        result = userTaskInstanceRepository.findByRoleId(nonExistentRoleId);
        assertThat(result).isEmpty();
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        Long nonExistentUserId = 100L;

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertThat(result).hasSize(1).extracting(UserTaskInstanceEntity::getStatus)
                .containsOnly(FlowNodeStatus.CREATED);

        result = userTaskInstanceRepository.findByCandidateUserId(nonExistentUserId);
        assertThat(result).isEmpty();
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("newUserTaskId_1");
        entity.setStatus(FlowNodeStatus.RUNNING);
        entity.setApproved(false);
        entity.setOperatorId(10L);
        entity.setComment("newComment_1");
        entity.setExpireIntervalSeconds(400);
        entity.setExternalFlowInstanceId("newExternalFlowInstanceId_1");

        int rowsAffected = userTaskInstanceRepository.update(entity);
        assertThat(rowsAffected).isEqualTo(1);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findById(1L);
        assertThat(result).isPresent().get().extracting(UserTaskInstanceEntity::getUserTaskId)
                .isEqualTo("newUserTaskId_1");
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId_1");
        assertThat(rowsAffected).isEqualTo(1);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findById(1L);
        assertThat(result).isPresent().get().extracting(UserTaskInstanceEntity::getUserTaskId)
                .isEqualTo("newUserTaskId_1");
    }

    @Test
    public void testUpdateStatusById() {
        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.RUNNING);
        assertThat(rowsAffected).isEqualTo(1);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findById(1L);
        assertThat(result).isPresent().get().extracting(UserTaskInstanceEntity::getStatus)
                .isEqualTo(FlowNodeStatus.RUNNING);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertThat(rowsAffected).isEqualTo(3);

        List<UserTaskInstanceEntity> allResults = userTaskInstanceRepository.findAll();
        assertThat(allResults).hasSize(2).extracting(UserTaskInstanceEntity::getFlowInstanceId)
                .containsOnly(2L, 4L);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activityId_1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        assertThat(result).isPresent().get().extracting(UserTaskInstanceEntity::getUserTaskId)
                .isEqualTo("userTaskId_1");
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name_1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertThat(result).isPresent().get().extracting(UserTaskInstanceEntity::getUserTaskId)
                .isEqualTo("userTaskId_1");
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertThat(result).isTrue();
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        FlowNodeStatus status = FlowNodeStatus.CREATED;
        Long flowInstanceId1 = 1L;
        Long flowInstanceId2 = 2L;

        List<Long> flowInstanceIds = Collections.singletonList(flowInstanceId1);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);
        assertThat(result).hasSize(2).extracting(UserTaskInstanceEntity::getFlowInstanceId)
                .containsOnly(1L);

        List<Long> multipleFlowInstanceIds = Arrays.asList(flowInstanceId1, flowInstanceId2);
        result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(multipleFlowInstanceIds, status);
        assertThat(result).hasSize(3).extracting(UserTaskInstanceEntity::getFlowInstanceId)
                .containsOnly(1L, 2L, 4L);
    }
}