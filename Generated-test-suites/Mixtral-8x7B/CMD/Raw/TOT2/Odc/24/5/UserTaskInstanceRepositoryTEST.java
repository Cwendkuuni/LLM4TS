import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {UserTaskInstanceRepositoryTest.Config.class}, loader = AnnotationConfigContextLoader.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(pageable);
        assertEquals(10, page.getSize());
        assertNotNull(page.getContent());
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.OPEN);
        assertEquals(3, entities.size());
        entities.forEach(entity -> assertEquals(FlowNodeStatus.OPEN, entity.getStatus()));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
        assertEquals(2, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("r1", "r2"));
        assertEquals(1, entities.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Arrays.asList("r1", "r2"));
        assertEquals(1, entities.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(2L);
        assertEquals(2, entities.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(3, entities.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.CLOSED);

        int updatedRows = userTaskInstanceRepository.update(entity);
        assertEquals(1, updatedRows);

        Optional<UserTaskInstanceEntity> optionalEntity = userTaskInstanceRepository.findById(1L);
        assertThat(optionalEntity).isPresent();
        UserTaskInstanceEntity updatedEntity = optionalEntity.get();
        assertEquals(FlowNodeStatus.CLOSED, updatedEntity.getStatus());
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int updatedRows = userTaskInstanceRepository.updateUserTaskIdById(1L, "new_user_task_id");
        assertEquals(1, updatedRows);

        Optional<UserTaskInstanceEntity> optionalEntity = userTaskInstanceRepository.findById(1L);
        assertThat(optionalEntity).isPresent();
        UserTaskInstanceEntity updatedEntity = optionalEntity.get();
        assertEquals("new_user_task_id", updatedEntity.getUserTaskId());
    }

    @Test
    public void testUpdateStatusById() {
        int updatedRows = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.CLOSED);
        assertEquals(1, updatedRows);

        Optional<UserTaskInstanceEntity> optionalEntity = userTaskInstanceRepository.findById(1L);
        assertThat(optionalEntity).isPresent();
        UserTaskInstanceEntity updatedEntity = optionalEntity.get();
        assertEquals(FlowNodeStatus.CLOSED, updatedEntity.getStatus());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int deletedRows = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, deletedRows);

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
        assertEquals(7, entities.size());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> optionalEntity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "a1", 1L);
        assertThat(optionalEntity).isPresent();
        UserTaskInstanceEntity entity = optionalEntity.get();
        assertEquals(1L, entity.getId().longValue());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> optionalEntity = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL, "name3", 1L);
        assertThat(optionalEntity).isPresent();
        UserTaskInstanceEntity entity = optionalEntity.get();
        assertEquals(3L, entity.getId().longValue());
    }

    @Test
    public void testFindConfirmById() {
        Boolean confirmed = userTaskInstanceRepository.findConfirmById(1L);
        assertEquals(true, confirmed);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Arrays.asList(1L, 2L), FlowNodeStatus.OPEN);
        assertEquals(2, entities.size());
    }

    @Configuration
    static class Config {
    }

}