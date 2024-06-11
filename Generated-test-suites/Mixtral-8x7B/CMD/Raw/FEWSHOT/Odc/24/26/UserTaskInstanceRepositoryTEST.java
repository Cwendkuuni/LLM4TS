import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.data.jpa.repository.query.QueryUtils;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
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
        entities.add(new UserTaskInstanceEntity());

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<UserTaskInstanceEntity> page = new PageImpl<>(entities, pageRequest, entities.size());

        when(userTaskInstanceRepository.findAll(specification, pageRequest)).thenReturn(page);

        Page<UserTaskInstanceEntity> resultPage = userTaskInstanceRepository.findAll(specification, pageRequest);

        assertEquals(entities.size(), resultPage.getTotalElements());
        verify(userTaskInstanceRepository).findAll(specification, pageRequest);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());

        Long userId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        when(userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);

        assertEquals(entities.size(), result.size());
        verify(userTaskInstanceRepository).findByCandidateUserIdOrRoleIds(userId, roleIds);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());

        Long userId = 1L;
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("identifier1");
        resourceRoleIdentifiers.add("identifier2");

        when(userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers))
                .thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);

        assertEquals(entities.size(), result.size());
        verify(userTaskInstanceRepository)
                .findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());

        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("identifier1");
        resourceRoleIdentifiers.add("identifier2");

        when(userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);

        assertEquals(entities.size(), result.size());
        verify(userTaskInstanceRepository).findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());

        Long roleId = 1L;

        when(userTaskInstanceRepository.findByRoleId(roleId)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(roleId);

        assertEquals(entities.size(), result.size());
        verify(userTaskInstanceRepository).findByRoleId(roleId);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());

        Long userId = 1L;

        when(userTaskInstanceRepository.findByCandidateUserId(userId)).thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(userId);

        assertEquals(entities.size(), result.size());
        verify(userTaskInstanceRepository).findByCandidateUserId(userId);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();

        int result = userTaskInstanceRepository.update(entity);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).update(entity);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateUserTaskIdById(id, userTaskId);
    }

    @Test
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        int result = userTaskInstanceRepository.updateStatusById(id, status);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).updateStatusById(id, status);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(1, result);
        verify(userTaskInstanceRepository).deleteByFlowInstanceId(instanceId);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long flowInstanceId = 1L;
        String activityId = "activityId";
        FlowNodeType instanceType = FlowNodeType.APPROVAL;

        Optional<UserTaskInstanceEntity> optionalEntity = Optional.of(new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId))
                .thenReturn(optionalEntity);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);

        assertTrue(result.isPresent());
        verify(userTaskInstanceRepository).findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long flowInstanceId = 1L;
        String name = "name";
        FlowNodeType instanceType = FlowNodeType.APPROVAL;

        Optional<UserTaskInstanceEntity> optionalEntity = Optional.of(new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId))
                .thenReturn(optionalEntity);

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name,
                flowInstanceId);

        assertTrue(result.isPresent());
        verify(userTaskInstanceRepository).findByInstanceTypeAndName(instanceType, name, flowInstanceId);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;

        Boolean result = userTaskInstanceRepository.findConfirmById(id);

        assertNull(result);
        verify(userTaskInstanceRepository).findConfirmById(id);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId1 = 1L;
        Long flowInstanceId2 = 2L;
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(flowInstanceId1);
        flowInstanceIds.add(flowInstanceId2);
        FlowNodeStatus status = FlowNodeStatus.APPROVED;

        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        entities.add(new UserTaskInstanceEntity());

        when(userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status))
                .thenReturn(entities);

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);

        assertEquals(entities.size(), result.size());
        verify(userTaskInstanceRepository).findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
    }

}