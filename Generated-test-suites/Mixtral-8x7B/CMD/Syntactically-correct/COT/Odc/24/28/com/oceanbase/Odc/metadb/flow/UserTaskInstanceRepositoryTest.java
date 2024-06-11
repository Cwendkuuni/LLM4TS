import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.Arrays;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserTaskInstanceEntity userTaskInstanceEntity1;
    private UserTaskInstanceEntity userTaskInstanceEntity2;
    private UserTaskInstanceEntity userTaskInstanceEntity3;

    @Before
    public void setUp() {
        userTaskInstanceEntity1 = UserTaskInstanceEntity.builder()
                .id(1L)
                .flowInstanceId(1L)
                .userTaskId("123")
                .status(FlowNodeStatus.TO_BE_EXECUTED)
                .build();

        userTaskInstanceEntity2 = UserTaskInstanceEntity.builder()
                .id(2L)
                .flowInstanceId(2L)
                .userTaskId("456")
                .status(FlowNodeStatus.EXECUTING)
                .build();

        userTaskInstanceEntity3 = UserTaskInstanceEntity.builder()
                .id(3L)
                .flowInstanceId(3L)
                .userTaskId("789")
                .status(FlowNodeStatus.EXECUTED)
                .build();

        entityManager.persist(userTaskInstanceEntity1);
        entityManager.persist(userTaskInstanceEntity2);
        entityManager.persist(userTaskInstanceEntity3);
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findAll();

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(3));
    }

    @Test
    public void testFindById() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findById(1L);

        assertThat(userTaskInstanceEntity, notNullValue());
        assertThat(userTaskInstanceEntity.isPresent(), equalTo(true));
        assertThat(userTaskInstanceEntity.get(), equalTo(userTaskInstanceEntity1));
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.TO_BE_EXECUTED);

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(1));
        assertThat(userTaskInstanceEntities.get(0), equalTo(userTaskInstanceEntity1));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Arrays.asList(2L, 3L));

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(2));
        assertThat(userTaskInstanceEntities.contains(userTaskInstanceEntity1), equalTo(true));
        assertThat(userTaskInstanceEntities.contains(userTaskInstanceEntity2), equalTo(true));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("role1", "role2"));

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(2));
        assertThat(userTaskInstanceEntities.contains(userTaskInstanceEntity1), equalTo(true));
        assertThat(userTaskInstanceEntities.contains(userTaskInstanceEntity2), equalTo(true));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Arrays.asList("role1"));

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(2));
        assertThat(userTaskInstanceEntities.contains(userTaskInstanceEntity1), equalTo(true));
        assertThat(userTaskInstanceEntities.contains(userTaskInstanceEntity2), equalTo(true));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(2L);

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(1));
        assertThat(userTaskInstanceEntities.get(0), equalTo(userTaskInstanceEntity2));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(1));
        assertThat(userTaskInstanceEntities.get(0), equalTo(userTaskInstanceEntity1));
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstanceEntity = UserTaskInstanceEntity.builder()
                .id(1L)
                .userTaskId("987")
                .status(FlowNodeStatus.EXECUTED)
                .build();

        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);

        assertThat(result, equalTo(1));
        Optional<UserTaskInstanceEntity> updatedUserTaskInstanceEntity = userTaskInstanceRepository.findById(1L);
        assertThat(updatedUserTaskInstanceEntity, notNullValue());
        assertThat(updatedUserTaskInstanceEntity.isPresent(), equalTo(true));
        assertThat(updatedUserTaskInstanceEntity.get(), equalTo(userTaskInstanceEntity));
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "456");

        assertThat(result, equalTo(1));
        Optional<UserTaskInstanceEntity> updatedUserTaskInstanceEntity = userTaskInstanceRepository.findById(1L);
        assertThat(updatedUserTaskInstanceEntity, notNullValue());
        assertThat(updatedUserTaskInstanceEntity.isPresent(), equalTo(true));
        assertThat(updatedUserTaskInstanceEntity.get().getUserTaskId(), equalTo("456"));
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.EXECUTED);

        assertThat(result, equalTo(1));
        Optional<UserTaskInstanceEntity> updatedUserTaskInstanceEntity = userTaskInstanceRepository.findById(1L);
        assertThat(updatedUserTaskInstanceEntity, notNullValue());
        assertThat(updatedUserTaskInstanceEntity.isPresent(), equalTo(true));
        assertThat(updatedUserTaskInstanceEntity.get().getStatus(), equalTo(FlowNodeStatus.EXECUTED));
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        assertThat(result, equalTo(1));
        Optional<UserTaskInstanceEntity> deletedUserTaskInstanceEntity = userTaskInstanceRepository.findById(1L);
        assertThat(deletedUserTaskInstanceEntity, notNullValue());
        assertThat(deletedUserTaskInstanceEntity.isPresent(), equalTo(false));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
                "123", 1L);

        assertThat(userTaskInstanceEntity, notNullValue());
        assertThat(userTaskInstanceEntity.isPresent(), equalTo(true));
        assertThat(userTaskInstanceEntity.get(), equalTo(userTaskInstanceEntity1));
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> userTaskInstanceEntity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
                "123", 1L);

        assertThat(userTaskInstanceEntity, notNullValue());
        assertThat(userTaskInstanceEntity.isPresent(), equalTo(true));
        assertThat(userTaskInstanceEntity.get(), equalTo(userTaskInstanceEntity1));
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);

        assertThat(confirm, notNullValue());
        assertThat(confirm, equalTo(false));
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L, 2L), FlowNodeStatus.TO_BE_EXECUTED);

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.size(), equalTo(1));
        assertThat(userTaskInstanceEntities.get(0), equalTo(userTaskInstanceEntity1));
    }

    @Test
    public void testFindAllWithPaginationAndSorting() {
        PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findAll(pageRequest);

        assertThat(userTaskInstanceEntities, notNullValue());
        assertThat(userTaskInstanceEntities.getTotalElements(), equalTo(3L));
        assertThat(userTaskInstanceEntities.getNumberOfElements(), equalTo(2));
        assertThat(userTaskInstanceEntities.getContent(), notNullValue());
        assertThat(userTaskInstanceEntities.getContent().size(), equalTo(2));
        assertThat(userTaskInstanceEntities.getContent().get(0), equalTo(userTaskInstanceEntity1));
        assertThat(userTaskInstanceEntities.getContent().get(1), equalTo(userTaskInstanceEntity2));
    }
}