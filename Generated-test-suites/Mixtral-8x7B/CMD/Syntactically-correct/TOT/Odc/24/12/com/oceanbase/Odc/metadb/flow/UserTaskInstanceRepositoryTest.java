
package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Autowired
    private TestEntityManager entityManager;

    private UserTaskInstanceEntity entity;

    @Before
    public void setUp() {
        entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setUserTaskId("test_user_task_id");
        entity.setStatus(FlowNodeStatus.CREATED);
        entity.setCandidateUserIds(Arrays.asList(1L));
        entity.setCandidateRoleIds(Arrays.asList(2L, 3L));
        entityManager.persist(entity);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertTrue(entities.size() > 0);
        entities.forEach(entity -> assertEquals(FlowNodeStatus.CREATED, entity.getStatus()));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
        assertTrue(entities.size() > 0);
        entities.forEach(entity -> {
            assertTrue(entity.getCandidateUserIds().contains(1L) || entity.getCandidateRoleIds().contains(2L) || entity.getCandidateRoleIds().contains(3L));
        });
    }

    @Test
    public void testUpdateUserTaskIdById() {
        long id = 1L;
        String userTaskId = "test_user_task_id";
        int rowsAffected = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, rowsAffected);
        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findById(id);
        assertTrue(entityOptional.isPresent());
        assertEquals(userTaskId, entityOptional.get().getUserTaskId());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testUpdateUserTaskIdById_WithInvalidId_ThrowsDataIntegrityViolationException() {
        long id = -1L;
        String userTaskId = "test_user_task_id";
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
    }

    @Test(expected = EmptyResultDataAccessException.class)
    public void testUpdateUserTaskIdById_WithNonExistentId_ThrowsEmptyResultDataAccessException() {
        long id = Long.MAX_VALUE;
        String userTaskId = "test_user_task_id";
        userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
    }
}
