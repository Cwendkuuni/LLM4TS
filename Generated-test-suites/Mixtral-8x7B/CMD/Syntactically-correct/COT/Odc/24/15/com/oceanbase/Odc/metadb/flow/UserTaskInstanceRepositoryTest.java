package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity.Builder;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {})
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.READY);
        assertNotNull(entities);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        roleIds.add(3L);

        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertNotNull(entities);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("ROLE_1");
        resourceRoleIdentifiers.add("ROLE_2");

        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId,
                resourceRoleIdentifiers);
        assertNotNull(entities);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("ROLE_1");

        List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertNotNull(entities);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> entities = repository.findByRoleId(roleId);
        assertNotNull(entities);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(userId);
        assertNotNull(entities);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = createUserTaskInstanceEntity();
        int result = repository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "TASK_1";

        int result = repository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.READY;

        int result = repository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        int result = repository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "ACTIVITY_1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(instanceType,
                activityId, flowInstanceId);
        assertFalse(entity.isEmpty());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "NAME_1";
        Long flowInstanceId = 1L;

        Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);
        assertFalse(entity.isEmpty());
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean confirm = repository.findConfirmById(id);
        assertNotNull(confirm);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        FlowNodeStatus status = FlowNodeStatus.READY;

        List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, status);
        assertNotNull(entities);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<UserTaskInstanceEntity> page = repository.findAll(pageable);
        assertEquals(10, page.getContent().size());
    }

    @Test
    public void testFindAllWithSort() {
        Sort sort = Sort.by("id").ascending();
        List<UserTaskInstanceEntity> entities = repository.findAll(sort);
        assertEquals(10, entities.size());
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        UserTaskInstanceEntity entity = repository.findById(id).orElse(null);
        assertNotNull(entity);
    }

    @Test
    public void testFindById_NotFound() {
        Long id = 100L;

        UserTaskInstanceEntity entity = repository.findById(id).orElse(null);
        assertNull(entity);
    }

    @Test
    public void testExistsById() {
        Long id = 1L;

        boolean exists = repository.existsById(id);
        assertTrue(exists);
    }

    @Test
    public void testExistsById_NotFound() {
        Long id = 100L;

        boolean exists = repository.existsById(id);
        assertFalse(exists);
    }

    @Test
    public void testCount() {
        long count = repository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        repository.deleteById(id);
        assertFalse(repository.existsById(id));
    }

    @Test
    public void testDeleteAll() {
        repository.deleteAll();
        assertTrue(repository.count() == 0);
    }

    private UserTaskInstanceEntity createUserTaskInstanceEntity() {
        return Builder.anEntity()
                .withId(1L)
                .withFlowInstanceId(1L)
                .withActivityId("ACTIVITY_1")
                .withName("NAME_1")
                .withInstanceType(FlowNodeType.APPROVAL)
                .withStatus(FlowNodeStatus.READY)
                .withUserTaskId("TASK_1")
                .withOperatorId(1L)
                .withExpireIntervalSeconds(10)
                .withExternalFlowInstanceId("EXTERNAL_INSTANCE_1")
                .build();
    }
}