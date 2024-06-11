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

import com.oceanbase.odc.metadb.flow.entity.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepositoryTestConfig.class)
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
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(2L));
        assertNotNull(entities);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role1"));
        assertNotNull(entities);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertNotNull(entities);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = repository.findByRoleId(1L);
        assertNotNull(entities);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(1L);
        assertNotNull(entities);
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        int rowsAffected = repository.update(entity);
        assertEquals(1, rowsAffected);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        int rowsAffected = repository.updateUserTaskIdById(1L, "task1");
        assertEquals(1, rowsAffected);
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        int rowsAffected = repository.updateStatusById(1L, FlowNodeStatus.READY);
        assertEquals(1, rowsAffected);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = repository.deleteByFlowInstanceId(1L);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activity1", 1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(FlowNodeType.ACTIVITY, "name1", 1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirmed = repository.findConfirmById(1L);
        assertNotNull(confirmed);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L),
                FlowNodeStatus.READY);
        assertNotNull(entities);
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> entities = repository.findAll();
        assertNotNull(entities);
    }

    @Test
    public void testFindAllByPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> page = repository.findAll(pageable);
        assertNotNull(page);
    }

    @Test
    public void testFindById() {
        Optional<UserTaskInstanceEntity> entity = repository.findById(1L);
        assertTrue(entity.isPresent());
    }

    @Test
    public void testFindById_notFound() {
        Optional<UserTaskInstanceEntity> entity = repository.findById(2L);
        assertFalse(entity.isPresent());
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        repository.save(entity);
        Optional<UserTaskInstanceEntity> savedEntity = repository.findById(1L);
        assertTrue(savedEntity.isPresent());
    }

    @Test
    public void testSaveAll() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());
        repository.saveAll(entities);
        assertEquals(2, repository.findAll().size());
    }

    @Test
    public void testCount() {
        Long count = repository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testExistsById() {
        assertTrue(repository.existsById(1L));
    }

    @Test
    public void testExistsById_notFound() {
        assertFalse(repository.existsById(2L));
    }

    @Test
    public void testDeleteById() {
        repository.deleteById(1L);
        assertNull(repository.findById(1L));
    }

    @Test
    public void testDeleteAll() {
        repository.deleteAll();
        assertTrue(repository.findAll().isEmpty());
    }

}

class UserTaskInstanceRepositoryTestConfig {
    // Add any necessary configuration here
}