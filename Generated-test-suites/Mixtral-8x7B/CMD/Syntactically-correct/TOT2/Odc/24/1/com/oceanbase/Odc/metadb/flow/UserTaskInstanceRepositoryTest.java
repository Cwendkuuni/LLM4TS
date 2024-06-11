
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.config.TestConfig;
import com.oceanbase.odc.metadb.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindById() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
        assertTrue(entity.isPresent());

        entity = userTaskInstanceRepository.findById(9999L);
        assertFalse(entity.isPresent());
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(1L);
        entity.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceRepository.save(entity);

        Optional<UserTaskInstanceEntity> savedEntity = userTaskInstanceRepository.findById(entity.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals(entity.getFlowInstanceId(), savedEntity.get().getFlowInstanceId());
        assertEquals(entity.getStatus(), savedEntity.get().getStatus());
    }

    @Test
    public void testDeleteById() {
        userTaskInstanceRepository.deleteById(1L);
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
        assertFalse(entity.isPresent());
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                Collections.singletonList(2L));
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
                1L, Collections.singletonList("admin"));
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                Collections.singletonList("admin"));
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setFlowInstanceId(1L);
        entity.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceRepository.update(entity);

        Optional<UserTaskInstanceEntity> savedEntity = userTaskInstanceRepository.findById(entity.getId());
        assertTrue(savedEntity.isPresent());
        assertEquals(entity.getFlowInstanceId(), savedEntity.get().getFlowInstanceId());
        assertEquals(entity.getStatus(), savedEntity.get().getStatus());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        userTaskInstanceRepository.updateUserTaskIdById(1L, "test-user-task-id");

        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
        assertTrue(entity.isPresent());
        assertEquals("test-user-task-id", entity.get().getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);

        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
        assertTrue(entity.isPresent());
        assertEquals(FlowNodeStatus.COMPLETED, entity.get().getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByFlowInstanceId(1L);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.START_EVENT, "test-activity-id", 1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.USER_TASK, "test-name", 1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirm = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                Collections.singletonList(1L), FlowNodeStatus.CREATED);
        assertNotNull(entities);
        assertTrue(entities.size() > 0);
    }
}
