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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setUp() {
        userTaskInstanceEntity = UserTaskInstanceEntity.builder()
                .id(1L)
                .userTaskId("userTaskId")
                .status(FlowNodeStatus.CREATED)
                .approved(false)
                .operatorId(1L)
                .comment("comment")
                .expireIntervalSeconds(10)
                .externalFlowInstanceId("externalFlowInstanceId")
                .build();
        entityManager.persist(userTaskInstanceEntity);
        entityManager.flush();
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(userTaskInstanceEntity.getId());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(userTaskInstanceEntity.getId());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                resourceRoleIdentifiers);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(userTaskInstanceEntity.getId());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                resourceRoleIdentifiers);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("newUserTaskId");
        entity.setStatus(FlowNodeStatus.APPROVED);
        entity.setApproved(true);
        entity.setOperatorId(2L);
        entity.setComment("newComment");
        entity.setExpireIntervalSeconds(20);
        entity.setExternalFlowInstanceId("newExternalFlowInstanceId");
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(result, 1);
        UserTaskInstanceEntity updatedEntity = entityManager.find(UserTaskInstanceEntity.class, 1L);
        assertEquals(updatedEntity.getUserTaskId(), "newUserTaskId");
        assertEquals(updatedEntity.getStatus(), FlowNodeStatus.APPROVED);
        assertEquals(updatedEntity.isApproved(), true);
        assertEquals(updatedEntity.getOperatorId(), 2L);
        assertEquals(updatedEntity.getComment(), "newComment");
        assertEquals(updatedEntity.getExpireIntervalSeconds(), 20);
        assertEquals(updatedEntity.getExternalFlowInstanceId(), "newExternalFlowInstanceId");
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");
        assertEquals(result, 1);
        UserTaskInstanceEntity updatedEntity = entityManager.find(UserTaskInstanceEntity.class, 1L);
        assertEquals(updatedEntity.getUserTaskId(), "newUserTaskId");
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        assertEquals(result, 1);
        UserTaskInstanceEntity updatedEntity = entityManager.find(UserTaskInstanceEntity.class, 1L);
        assertEquals(updatedEntity.getStatus(), FlowNodeStatus.APPROVED);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = new ArrayList<>();
        userTaskInstanceEntities.add(UserTaskInstanceEntity.builder().id(2L).build());
        userTaskInstanceEntities.add(UserTaskInstanceEntity.builder().id(3L).build());
        userTaskInstanceRepository.saveAll(userTaskInstanceEntities);
        entityManager.flush();
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(result, 1);
        List<UserTaskInstanceEntity> all = userTaskInstanceRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntity.builder()
                .id(2L)
                .userTaskId("userTaskId")
                .status(FlowNodeStatus.CREATED)
                .approved(false)
                .operatorId(1L)
                .comment("comment")
                .expireIntervalSeconds(10)
                .externalFlowInstanceId("externalFlowInstanceId")
                .flowInstanceId(1L)
                .build();
        entityManager.persist(userTaskInstanceEntity);
        entityManager.flush();
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.START_EVENT, "userTaskId", 1L);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(userTaskInstanceEntity.getId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntity.builder()
                .id(2L)
                .userTaskId("userTaskId")
                .status(FlowNodeStatus.CREATED)
                .approved(false)
                .operatorId(1L)
                .comment("comment")
                .expireIntervalSeconds(10)
                .externalFlowInstanceId("externalFlowInstanceId")
                .flowInstanceId(1L)
                .name("name")
                .build();
        entityManager.persist(userTaskInstanceEntity);
        entityManager.flush();
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.START_EVENT, "name", 1L);
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getId()).isEqualTo(userTaskInstanceEntity.getId());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntity.builder()
                .id(2L)
                .waitForConfirm(true)
                .userTaskId("userTaskId")
                .status(FlowNodeStatus.CREATED)
                .approved(false)
                .operatorId(1L)
                .comment("comment")
                .expireIntervalSeconds(10)
                .externalFlowInstanceId("externalFlowInstanceId")
                .flowInstanceId(1L)
                .build();
        entityManager.persist(userTaskInstanceEntity);
        entityManager.flush();
        Boolean result = userTaskInstanceRepository.findConfirmById(2L);
        assertThat(result).isTrue();
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntity.builder()
                .id(2L)
                .userTaskId("userTaskId")
                .status(FlowNodeStatus.CREATED)
                .approved(false)
                .operatorId(1L)
                .comment("comment")
                .expireIntervalSeconds(10)
                .externalFlowInstanceId("externalFlowInstanceId")
                .flowInstanceId(1L)
                .build();
        entityManager.persist(userTaskInstanceEntity);
        entityManager.flush();
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(1L), FlowNodeStatus.CREATED);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo(userTaskInstanceEntity.getId());
    }
}