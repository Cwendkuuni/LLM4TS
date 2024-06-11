import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepositoryTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    // Helper method to create a UserTaskInstanceEntity for testing
    private UserTaskInstanceEntity createUserTaskInstanceEntity() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("userTaskId");
        entity.setStatus(FlowNodeStatus.RUNNING);
        entity.setApproved(true);
        entity.setOperatorId(1L);
        entity.setComment("comment");
        entity.setExpireIntervalSeconds(3600);
        entity.setExternalFlowInstanceId("externalFlowInstanceId");
        return entity;
    }

    // Test for JpaRepository<UserTaskInstanceEntity, Long>

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindById() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(1L);
        assertTrue(entityOptional.isPresent());

        entityOptional = userTaskInstanceRepository.findById(100L);
        assertFalse(entityOptional.isPresent());
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity entity = createUserTaskInstanceEntity();
        UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.save(entity);
        assertEquals(entity, savedEntity);
        assertEquals(entity.getId(), savedEntity.getId());
    }

    @Test
    public void testDeleteById() {
        userTaskInstanceRepository.deleteById(1L);
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(1L);
        assertFalse(entityOptional.isPresent());
    }

    // Test for JpaSpecificationExecutor<UserTaskInstanceEntity>

    @Test
    public void testFindAllBySpecification() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll((root, query, cb) -> {
            query.orderBy(cb.asc(root.get("id")));
            return null;
        });
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindOneBySpecification() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findOne((root, query, cb) -> {
            query.where(cb.equal(root.get("id"), 1L));
            return null;
        });
        assertTrue(entityOptional.isPresent());

        entityOptional = userTaskInstanceRepository.findOne((root, query, cb) -> {
            query.where(cb.equal(root.get("id"), 100L));
            return null;
        });
        assertFalse(entityOptional.isPresent());
    }

    // Test for custom methods

    // findByStatus

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
        for (UserTaskInstanceEntity entity : entities) {
            assertEquals(FlowNodeStatus.RUNNING, entity.getStatus());
        }
    }

    // findByCandidateUserIdOrRoleIds

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        for (UserTaskInstanceEntity entity : entities) {
            assertEquals(1L, entity.getCandidateUserId().longValue());
        }

        entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(100L, roleIds);
        assertTrue(entities.isEmpty());

        List<UserTaskInstanceEntity> entitiesWithRoleIds = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections
                .singletonList(2L));
        for (UserTaskInstanceEntity entity : entitiesWithRoleIds) {
            assertTrue(entity.getCandidateRoleIds().contains(2L));
        }
    }

    // findByCandidateUserIdOrResourceRoleIdentifier

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                resourceRoleIdentifiers);
        for (UserTaskInstanceEntity entity : entities) {
            assertEquals(1L, entity.getCandidateUserId().longValue());
        }

        entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(100L, resourceRoleIdentifiers);
        assertTrue(entities.isEmpty());

        List<UserTaskInstanceEntity> entitiesWithResourceRoleIdentifier = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                Collections.singletonList("resourceRoleIdentifier2"));
        for (UserTaskInstanceEntity entity : entitiesWithResourceRoleIdentifier) {
            assertTrue(entity.getCandidateResourceRoleIdentifiers().contains("resourceRoleIdentifier2"));
        }
    }

    // findByResourceRoleIdentifierIn

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        for (UserTaskInstanceEntity entity : entities) {
            assertTrue(entity.getCandidateResourceRoleIdentifiers().contains("resourceRoleIdentifier1"));
        }

        List<String> emptyResourceRoleIdentifiers = new ArrayList<>();
        entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(emptyResourceRoleIdentifiers);
        assertTrue(entities.isEmpty());
    }

    // findByRoleId

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        for (UserTaskInstanceEntity entity : entities) {
            assertEquals(1L, entity.getCandidateRoleIds().stream().filter(roleId -> roleId == 1L).findFirst().get().longValue());
        }

        entities = userTaskInstanceRepository.findByRoleId(100L);
        assertTrue(entities.isEmpty());
    }

    // findByCandidateUserId

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        for (UserTaskInstanceEntity entity : entities) {
            assertEquals(1L, entity.getCandidateUserId().longValue());
        }

        entities = userTaskInstanceRepository.findByCandidateUserId(100L);
        assertTrue(entities.isEmpty());
    }

    // update

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = createUserTaskInstanceEntity();
        int rowsAffected = userTaskInstanceRepository.update(entity);
        assertEquals(1, rowsAffected);

        Optional<UserTaskInstanceEntity> updatedEntityOptional = userTaskInstanceRepository.findById(1L);
        assertTrue(updatedEntityOptional.isPresent());
        UserTaskInstanceEntity updatedEntity = updatedEntityOptional.get();
        assertEquals(entity, updatedEntity);
    }

    // updateUserTaskIdById

    @Test
    public void testUpdateUserTaskIdById() {
        String newUserTaskId = "newUserTaskId";
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(1L, newUserTaskId);
        assertEquals(1, rowsAffected);

        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(1L);
        assertTrue(entityOptional.isPresent());
        UserTaskInstanceEntity entity = entityOptional.get();
        assertEquals(newUserTaskId, entity.getUserTaskId());
    }

    // updateStatusById

    @Test
    public void testUpdateStatusById() {
        FlowNodeStatus newStatus = FlowNodeStatus.FAILED;
        int rowsAffected = userTaskInstanceRepository.updateStatusById(1L, newStatus);
        assertEquals(1, rowsAffected);

        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(1L);
        assertTrue(entityOptional.isPresent());
        UserTaskInstanceEntity entity = entityOptional.get();
        assertEquals(newStatus, entity.getStatus());
    }

    // deleteByFlowInstanceId

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, rowsAffected);

        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(1L);
        assertFalse(entityOptional.isPresent());
    }

    // findByInstanceTypeAndActivityId

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 1L);
        assertTrue(entityOptional.isPresent());

        entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId", 100L);
        assertFalse(entityOptional.isPresent());
    }

    // findByInstanceTypeAndName

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 1L);
        assertTrue(entityOptional.isPresent());

        entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name", 100L);
        assertFalse(entityOptional.isPresent());
    }

    // findConfirmById

    @Test
    public void testFindConfirmById() {
        Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(confirmed);

        confirmed = userTaskInstanceRepository.findConfirmById(100L);
        assertFalse(confirmed);
    }

    // findApprovalInstanceIdByFlowInstanceIdAndStatus

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, FlowNodeStatus.RUNNING);
        for (UserTaskInstanceEntity entity : entities) {
            assertTrue(flowInstanceIds.contains(entity.getFlowInstanceId()));
            assertEquals(FlowNodeStatus.RUNNING, entity.getStatus());
        }

        List<Long> emptyFlowInstanceIds = new ArrayList<>();
        entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(emptyFlowInstanceIds, FlowNodeStatus.RUNNING);
        assertTrue(entities.isEmpty());
    }
}

class UserTaskInstanceRepositoryTestConfig {

    // Add any necessary configuration if needed

}