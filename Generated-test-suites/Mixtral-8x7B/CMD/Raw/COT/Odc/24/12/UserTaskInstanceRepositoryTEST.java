import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setUserTaskId("testUserTaskId");
        userTaskInstanceEntity.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceEntity.setApproved(false);
        userTaskInstanceEntity.setOperatorId(1L);
        userTaskInstanceEntity.setComment("testComment");
        userTaskInstanceEntity.setExpireIntervalSeconds(300);
        userTaskInstanceEntity.setExternalFlowInstanceId("testExternalFlowInstanceId");
    }

    @Test
    public void testFindByStatus() {
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(1, entities.size());
        assertEquals(userTaskInstanceEntity.getId(), entities.get(0).getId());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, new ArrayList<>());
        assertTrue(entities.contains(userTaskInstanceEntity));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, new ArrayList<>());
        assertTrue(entities.contains(userTaskInstanceEntity));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(new ArrayList<>());
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByRoleId() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserId() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testUpdate() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        int rowsAffected = repository.update(userTaskInstanceEntity);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        int rowsAffected = repository.updateUserTaskIdById(1L, "testUserTaskId");
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testUpdateStatusById() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        int rowsAffected = repository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = repository.deleteByFlowInstanceId(1L);
        assertEquals(0, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        Optional<UserTaskInstanceEntity> entityOptional = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "testActivityId", 1L);
        assertEquals(userTaskInstanceEntity.getId(), entityOptional.get().getId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        Optional<UserTaskInstanceEntity> entityOptional = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "testName", 1L);
        assertEquals(userTaskInstanceEntity.getId(), entityOptional.get().getId());
    }

    @Test
    public void testFindConfirmById() {
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setWaitForConfirm(true);
        repository.save(userTaskInstanceEntity);
        Boolean confirmed = repository.findConfirmById(1L);
        assertTrue(confirmed);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setStatus(FlowNodeStatus.CREATED);
        repository.save(userTaskInstanceEntity);
        List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<>(), FlowNodeStatus.CREATED);
        assertEquals(1, entities.size());
        assertEquals(userTaskInstanceEntity.getId(), entities.get(0).getId());
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus_EmptyList() {
        List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(new ArrayList<>(), FlowNodeStatus.CREATED);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity savedEntity = repository.save(userTaskInstanceEntity);
        assertNotNull(savedEntity.getId());
    }

    @Test
    public void testFindById() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        Optional<UserTaskInstanceEntity> entityOptional = repository.findById(1L);
        assertTrue(entityOptional.isPresent());
        assertEquals(userTaskInstanceEntity.getId(), entityOptional.get().getId());
    }

    @Test
    public void testFindById_NotFound() {
        Optional<UserTaskInstanceEntity> entityOptional = repository.findById(1L);
        assertFalse(entityOptional.isPresent());
    }

    @Test
    public void testDeleteById() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        int rowsAffected = repository.deleteById(1L);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testDeleteAll() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(userTaskInstanceEntity);
        repository.saveAll(entities);
        repository.deleteAll();
        assertEquals(0, repository.count());
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(userTaskInstanceEntity);
        repository.saveAll(entities);
        List<UserTaskInstanceEntity> allEntities = repository.findAll();
        assertEquals(1, allEntities.size());
    }

    @Test
    public void testCount() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(userTaskInstanceEntity);
        repository.saveAll(entities);
        long count = repository.count();
        assertEquals(1, count);
    }

    @Test
    public void testExistsById() {
        userTaskInstanceEntity.setId(1L);
        repository.save(userTaskInstanceEntity);
        assertTrue(repository.existsById(1L));
    }

    @Test
    public void testExistsById_NotFound() {
        assertFalse(repository.existsById(1L));
    }

}