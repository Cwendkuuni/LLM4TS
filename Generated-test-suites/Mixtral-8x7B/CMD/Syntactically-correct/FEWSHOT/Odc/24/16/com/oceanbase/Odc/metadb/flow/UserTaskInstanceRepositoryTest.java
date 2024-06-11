package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeType;

public class UserTaskInstanceRepositoryTest {

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = repository.findByStatus(FlowNodeStatus.RUNNING);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = Collections.singletonList(1L);
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = Collections.singletonList("admin");
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                resourceRoleIdentifiers);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifier = Collections.singletonList("admin");
        List<UserTaskInstanceEntity> result = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = repository.findByRoleId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = repository.findByCandidateUserId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.FINISHED);
        int result = repository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = repository.updateUserTaskIdById(1L, "test-user-task-id");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = repository.updateStatusById(1L, FlowNodeStatus.FINISHED);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = repository.deleteByFlowInstanceId(1L);
        assertEquals(2, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL,
                "test-activity-id", 1L);
        assertEquals(Optional.of(new UserTaskInstanceEntity()), result);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = repository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
                "test-name", 1L);
        assertEquals(Optional.of(new UserTaskInstanceEntity()), result);
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = repository.findConfirmById(1L);
        assertEquals(true, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = Collections.singletonList(1L);
        List<UserTaskInstanceEntity> result = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds,
                FlowNodeStatus.RUNNING);
        assertEquals(2, result.size());
    }

    private UserTaskInstanceRepository repository = mock(UserTaskInstanceRepository.class);

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> result = repository.findAll(pageable);
        assertEquals(3, result.getContent().size());
    }

    @Test
    public void testFindAllBySpec() {
        Specification<UserTaskInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("status"),
                FlowNodeStatus.RUNNING);
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> result = repository.findAll(spec, pageable);
        assertEquals(2, result.getContent().size());
    }
}