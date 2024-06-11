
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
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    @Transactional
    public void testFindByStatus() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.RUNNING);
        entityManager.persist(entity1);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.FAILED);
        entityManager.persist(entity2);
        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.RUNNING);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    @Transactional
    public void testFindByCandidateUserIdOrRoleIds() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setCandidateUserIds(Arrays.asList(1L));
        entityManager.persist(entity1);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setCandidateRoleIds(Arrays.asList(2L));
        entityManager.persist(entity2);
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L));
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    @Transactional
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setCandidateUserIds(Arrays.asList(1L));
        entityManager.persist(entity1);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setCandidateResourceRoleIdentifiers(Arrays.asList("2"));
        entityManager.persist(entity2);
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Arrays.asList("2"));
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    @Transactional
    public void testFindByResourceRoleIdentifierIn() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setCandidateResourceRoleIdentifiers(Arrays.asList("1", "2"));
        entityManager.persist(entity1);
        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(
                Arrays.asList("1", "2"));
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    @Transactional
    public void testFindByRoleId() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setCandidateRoleIds(Arrays.asList(1L));
        entityManager.persist(entity1);
        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    @Transactional
    public void testFindByCandidateUserId() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setCandidateUserIds(Arrays.asList(1L));
        entityManager.persist(entity1);
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entityManager.persist(entity);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setId(entity.getId());
        entity2.setUserTaskId("123");
        entity2.setStatus(FlowNodeStatus.FAILED);
        entity2.setApproved(true);
        entity2.setOperatorId(2L);
        entity2.setComment("test");
        entity2.setExpireIntervalSeconds(300);
        entity2.setExternalFlowInstanceId("456");
        int result = repository.update(entity2);
        assertEquals(1, result);
        UserTaskInstanceEntity resultEntity = repository.findById(entity.getId()).get();
        assertEquals(entity2, resultEntity);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entityManager.persist(entity);
        int result = repository.updateUserTaskIdById(entity.getId(), "123");
        assertEquals(1, result);
        UserTaskInstanceEntity resultEntity = repository.findById(entity.getId()).get();
        assertEquals("123", resultEntity.getUserTaskId());
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entityManager.persist(entity);
        int result = repository.updateStatusById(entity.getId(), FlowNodeStatus.FAILED);
        assertEquals(1, result);
        UserTaskInstanceEntity resultEntity = repository.findById(entity.getId()).get();
        assertEquals(FlowNodeStatus.FAILED, resultEntity.getStatus());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entityManager.persist(entity1);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entityManager.persist(entity2);
        int result = repository.deleteByFlowInstanceId(entity1.getFlowInstanceId());
        assertEquals(2, result);
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @Transactional
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setInstanceType(FlowNodeType.START);
        entity.setActivityId("123");
        entity.setFlowInstanceId(456L);
        entityManager.persist(entity);
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(
                FlowNodeType.START, "123", 456L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    @Transactional
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setInstanceType(FlowNodeType.START);
        entity.setName("test");
        entity.setFlowInstanceId(456L);
        entityManager.persist(entity);
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(
                FlowNodeType.START, "test", 456L);
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    @Transactional
    public void testFindConfirmById() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setWaitForConfirm(true);
        entityManager.persist(entity);
        Boolean result = repository.findConfirmById(entity.getId());
        assertTrue(result);
    }

    @Test
    @Transactional
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setFlowInstanceId(1L);
        entity1.setStatus(FlowNodeStatus.RUNNING);
        entityManager.persist(entity1);
        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setFlowInstanceId(1L);
        entity2.setStatus(FlowNodeStatus.FAILED);
        entityManager.persist(entity2);
        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Arrays.asList(1L), FlowNodeStatus.RUNNING);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }
}
