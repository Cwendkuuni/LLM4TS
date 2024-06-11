
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
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

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context-test.xml")
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(pageable);
        assertEquals(page.getTotalElements(), userTaskInstanceRepository.findAll().size());
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
        entities.forEach(entity -> assertEquals(FlowNodeStatus.RUNNING, entity.getStatus()));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        entities.forEach(entity -> {
            if (entity.getCandidateUser() != null) {
                assertEquals(userId, entity.getCandidateUser().getId());
            } else if (entity.getCandidateRole() != null) {
                assertTrue(roleIds.contains(entity.getCandidateRole().getId()));
            }
        });
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("admin");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        entities.forEach(entity -> {
            if (entity.getCandidateUser() != null) {
                assertEquals(userId, entity.getCandidateUser().getId());
            } else if (entity.getResourceRole() != null) {
                assertTrue(resourceRoleIdentifiers.contains(entity.getResourceRole().getIdentifier()));
            }
        });
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("admin");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        entities.forEach(entity -> {
            if (entity.getResourceRole() != null) {
                assertTrue(resourceRoleIdentifiers.contains(entity.getResourceRole().getIdentifier()));
            }
        });
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
        entities.forEach(entity -> assertEquals(roleId, entity.getCandidateRole().getId()));
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
        entities.forEach(entity -> assertEquals(userId, entity.getCandidateUser().getId()));
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.RUNNING);
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository.findById(entity.getId());
        assertTrue(optional.isPresent());
        UserTaskInstanceEntity updatedEntity = optional.get();
        assertEquals(FlowNodeStatus.RUNNING, updatedEntity.getStatus());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";
        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository.findById(id);
        assertTrue(optional.isPresent());
        UserTaskInstanceEntity updatedEntity = optional.get();
        assertEquals(userTaskId, updatedEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.RUNNING;
        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(1, result);
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository.findById(id);
        assertTrue(optional.isPresent());
        UserTaskInstanceEntity updatedEntity = optional.get();
        assertEquals(status, updatedEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(1, result);
        assertNull(userTaskInstanceRepository.findById(flowInstanceId).orElse(null));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.START;
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(optional.isPresent());
        UserTaskInstanceEntity entity = optional.get();
        assertEquals(instanceType, entity.getInstanceType());
        assertEquals(activityId, entity.getActivityId());
        assertEquals(flowInstanceId, entity.getFlowInstanceId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.START;
        String name = "start";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> optional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertTrue(optional.isPresent());
        UserTaskInstanceEntity entity = optional.get();
        assertEquals(instanceType, entity.getInstanceType());
        assertEquals(name, entity.getName());
        assertEquals(flowInstanceId, entity.getFlowInstanceId());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = userTaskInstanceRepository.findConfirmById(id);
        assertTrue(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.RUNNING;
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        entities.forEach(entity -> {
            assertEquals(status, entity.getStatus());
            assertTrue(flowInstanceIds.contains(entity.getFlowInstanceId()));
        });
    }

}
