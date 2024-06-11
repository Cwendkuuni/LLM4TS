import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setUp() {
        userTaskInstanceEntity = new UserTaskInstanceEntity(1L, "testUserTaskInstance", "testFlowInstance",
                "testActivityId", FlowNodeType.APPROVAL, FlowNodeStatus.INIT, null, null, null, null, null,
                null, null, null);
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findAll();
        assertFalse(userTaskInstanceEntities.isEmpty());
    }

    @Test
    public void testFindById() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository.findById(savedUserTaskInstanceEntity.getId()).orElse(null);
        assertNotNull(foundUserTaskInstanceEntity);
        assertEquals(savedUserTaskInstanceEntity, foundUserTaskInstanceEntity);
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        assertNotNull(savedUserTaskInstanceEntity.getId());
    }

    @Test
    public void testDeleteById() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        userTaskInstanceRepository.deleteById(savedUserTaskInstanceEntity.getId());
        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository.findById(savedUserTaskInstanceEntity.getId()).orElse(null);
        assertNull(foundUserTaskInstanceEntity);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.INIT);
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertEquals(FlowNodeStatus.INIT, entity.getStatus());
        }
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertTrue(entity.getCandidateUserIds().contains(1L) || entity.getCandidateRoleIds().contains(1L));
        }
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("testRoleIdentifier");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertTrue(entity.getCandidateUserIds().contains(1L) || entity.getCandidateResourceRoleIdentifiers().contains("testRoleIdentifier"));
        }
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("testRoleIdentifier");
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertTrue(entity.getCandidateResourceRoleIdentifiers().contains("testRoleIdentifier"));
        }
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByRoleId(1L);
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertTrue(entity.getCandidateRoleIds().contains(1L));
        }
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findByCandidateUserId(1L);
        for (UserTaskInstanceEntity entity : userTaskInstanceEntities) {
            assertTrue(entity.getCandidateUserIds().contains(1L));
        }
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        UserTaskInstanceEntity updateParam = new UserTaskInstanceEntity();
        updateParam.setId(savedUserTaskInstanceEntity.getId());
        updateParam.setUserTaskId("testUpdatedUserTaskId");
        updateParam.setStatus(FlowNodeStatus.APPROVED);
        updateParam.setApproved(true);
        updateParam.setOperatorId(2L);
        updateParam.setComment("testUpdatedComment");
        updateParam.setExpireIntervalSeconds(60);
        updateParam.setExternalFlowInstanceId("testUpdatedExternalFlowInstanceId");
        int rowsAffected = userTaskInstanceRepository.update(updateParam);
        assertEquals(1, rowsAffected);
        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository.findById(savedUserTaskInstanceEntity.getId()).orElse(null);
        assertEquals(updateParam.getUserTaskId(), foundUserTaskInstanceEntity.getUserTaskId());
        assertEquals(updateParam.getStatus(), foundUserTaskInstanceEntity.getStatus());
        assertEquals(updateParam.isApproved(), foundUserTaskInstanceEntity.isApproved());
        assertEquals(updateParam.getOperatorId(), foundUserTaskInstanceEntity.getOperatorId());
        assertEquals(updateParam.getComment(), foundUserTaskInstanceEntity.getComment());
        assertEquals(updateParam.getExpireIntervalSeconds(), foundUserTaskInstanceEntity.getExpireIntervalSeconds());
        assertEquals(updateParam.getExternalFlowInstanceId(), foundUserTaskInstanceEntity.getExternalFlowInstanceId());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(savedUserTaskInstanceEntity.getId(), "testUpdatedUserTaskId");
        assertEquals(1, rowsAffected);
        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository.findById(savedUserTaskInstanceEntity.getId()).orElse(null);
        assertEquals("testUpdatedUserTaskId", foundUserTaskInstanceEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        int rowsAffected = userTaskInstanceRepository.updateStatusById(savedUserTaskInstanceEntity.getId(), FlowNodeStatus.APPROVED);
        assertEquals(1, rowsAffected);
        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository.findById(savedUserTaskInstanceEntity.getId()).orElse(null);
        assertEquals(FlowNodeStatus.APPROVED, foundUserTaskInstanceEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        int rowsAffected = userTaskInstanceRepository.deleteByFlowInstanceId(savedUserTaskInstanceEntity.getFlowInstanceId());
        assertEquals(1, rowsAffected);
        UserTaskInstanceEntity foundUserTaskInstanceEntity = userTaskInstanceRepository.findById(savedUserTaskInstanceEntity.getId()).orElse(null);
        assertNull(foundUserTaskInstanceEntity);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        Optional<UserTaskInstanceEntity> foundUserTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
                savedUserTaskInstanceEntity.getActivityId(), savedUserTaskInstanceEntity.getFlowInstanceId());
        assertTrue(foundUserTaskInstanceEntityOptional.isPresent());
        assertEquals(savedUserTaskInstanceEntity, foundUserTaskInstanceEntityOptional.get());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        Optional<UserTaskInstanceEntity> foundUserTaskInstanceEntityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
                savedUserTaskInstanceEntity.getName(), savedUserTaskInstanceEntity.getFlowInstanceId());
        assertTrue(foundUserTaskInstanceEntityOptional.isPresent());
        assertEquals(savedUserTaskInstanceEntity, foundUserTaskInstanceEntityOptional.get());
    }

    @Test
    public void testFindConfirmById() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity = userTaskInstanceRepository.save(userTaskInstanceEntity);
        savedUserTaskInstanceEntity.setWaitForConfirm(true);
        userTaskInstanceRepository.save(savedUserTaskInstanceEntity);
        Boolean confirmed = userTaskInstanceRepository.findConfirmById(savedUserTaskInstanceEntity.getId());
        assertTrue(confirmed);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        UserTaskInstanceEntity savedUserTaskInstanceEntity1 = userTaskInstanceRepository.save(new UserTaskInstanceEntity(1L, "testUserTaskInstance1", "testFlowInstance1",
                "testActivityId1", FlowNodeType.APPROVAL, FlowNodeStatus.INIT, null, null, null, null, null,
                null, null, null));
        UserTaskInstanceEntity savedUserTaskInstanceEntity2 = userTaskInstanceRepository.save(new UserTaskInstanceEntity(2L, "testUserTaskInstance2", "testFlowInstance2",
                "testActivityId2", FlowNodeType.APPROVAL, FlowNodeStatus.APPROVED, null, null, null, null, null,
                null, null, null));
        List<UserTaskInstanceEntity> userTaskInstanceEntities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(savedUserTaskInstanceEntity1.getFlowInstanceId()), FlowNodeStatus.INIT);
        assertEquals(1, userTaskInstanceEntities.size());
        assertEquals(savedUserTaskInstanceEntity1, userTaskInstanceEntities.get(0));
    }

    @Test
    public void testFindAllWithSpecification() {
        Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> {
            query.orderBy(cb.asc(root.get("id")));
            return cb.conjunction();
        };
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserTaskInstanceEntity> userTaskInstanceEntityPage = userTaskInstanceRepository.findAll(spec, pageable);
        assertNotNull(userTaskInstanceEntityPage);
        assertEquals(0, userTaskInstanceEntityPage.getNumber());
        assertEquals(10, userTaskInstanceEntityPage.getSize());
        assertEquals(userTaskInstanceEntityPage.getTotalElements(), userTaskInstanceRepository.count(spec));
    }
}