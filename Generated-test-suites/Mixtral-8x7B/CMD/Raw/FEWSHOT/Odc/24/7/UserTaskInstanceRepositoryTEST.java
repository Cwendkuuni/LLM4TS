import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.metadb.flow.specification.UserTaskInstanceSpecification;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private Specification<UserTaskInstanceEntity> specification;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        UserTaskInstanceSpecification.byStatus(FlowNodeStatus.RUNNING).toPredicate(null, null, specification);
        assertEquals(page, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(1L));
        assertEquals(page, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("test"));
        assertEquals(page, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("test"));
        assertEquals(page, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(page, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, PageRequest.of(0, 10), entities.size());
        userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(page, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testUpdate() {
        int result = userTaskInstanceRepository.update(new UserTaskInstanceEntity());
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "test");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.SUCCEEDED);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.TASK, "test", 1L);
        assertEquals(Optional.of(new UserTaskInstanceEntity()), entity);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.TASK, "test", 1L);
        assertEquals(Optional.of(new UserTaskInstanceEntity()), entity);
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertEquals(true, result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L),
                FlowNodeStatus.RUNNING);
        assertEquals(entities, userTaskInstanceRepository.findAll(specification));
    }

    @Test
    public void testFindByInstanceTypeAndActivityId_EmptyResult() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.TASK, "nonexistent", 1L);
        assertEquals(Optional.empty(), entity);
    }

    @Test
    public void testFindByInstanceTypeAndName_EmptyResult() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.TASK, "nonexistent", 1L);
        assertEquals(Optional.empty(), entity);
    }
}