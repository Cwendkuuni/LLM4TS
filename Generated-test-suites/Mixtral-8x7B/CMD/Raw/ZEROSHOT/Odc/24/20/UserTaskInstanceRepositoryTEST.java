import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.repository.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private JpaRepository<UserTaskInstanceEntity, Long> mockJpaRepository;

    @Mock
    private JpaSpecificationExecutor<UserTaskInstanceEntity> mockJpaSpecificationExecutor;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private UserTaskInstanceEntity userTaskInstanceEntity;

    @Before
    public void setup() {
        userTaskInstanceEntity = new UserTaskInstanceEntity();
        userTaskInstanceEntity.setId(1L);
        userTaskInstanceEntity.setFlowInstanceId(2L);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        doReturn(list).when(mockJpaRepository).findByStatus(any(FlowNodeStatus.class));

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);

        Assert.assertEquals(list, result);
        verify(mockJpaRepository, times(1)).findByStatus(any(FlowNodeStatus.class));
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        doReturn(list).when(mockJpaRepository).findByCandidateUserIdOrRoleIds(anyLong(), any());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L,
                new ArrayList<>());

        Assert.assertEquals(list, result);
        verify(mockJpaRepository, times(1)).findByCandidateUserIdOrRoleIds(anyLong(), any());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        doReturn(list).when(mockJpaRepository)
                .findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), any());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
                1L, new ArrayList<>());

        Assert.assertEquals(list, result);
        verify(mockJpaRepository, times(1))
                .findByCandidateUserIdOrResourceRoleIdentifier(anyLong(), any());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        doReturn(list).when(mockJpaRepository).findByResourceRoleIdentifierIn(any());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                new ArrayList<>());

        Assert.assertEquals(list, result);
        verify(mockJpaRepository, times(1)).findByResourceRoleIdentifierIn(any());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        doReturn(list).when(mockJpaRepository).findByRoleId(anyLong());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);

        Assert.assertEquals(list, result);
        verify(mockJpaRepository, times(1)).findByRoleId(anyLong());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        doReturn(list).when(mockJpaRepository).findByCandidateUserId(anyLong());

        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);

        Assert.assertEquals(list, result);
        verify(mockJpaRepository, times(1)).findByCandidateUserId(anyLong());
    }

    @Test
    @Transactional
    public void testUpdate() {
        doReturn(1).when(mockJpaRepository).update(any(UserTaskInstanceEntity.class));

        int result = userTaskInstanceRepository.update(userTaskInstanceEntity);

        Assert.assertEquals(1, result);
        verify(mockJpaRepository, times(1)).update(any(UserTaskInstanceEntity.class));
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        doReturn(1).when(mockJpaRepository).updateUserTaskIdById(anyLong(), any());

        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "test");

        Assert.assertEquals(1, result);
        verify(mockJpaRepository, times(1)).updateUserTaskIdById(anyLong(), any());
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        doReturn(1).when(mockJpaRepository).updateStatusById(anyLong(), any());

        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.CREATED);

        Assert.assertEquals(1, result);
        verify(mockJpaRepository, times(1)).updateStatusById(anyLong(), any());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        doReturn(1).when(mockJpaRepository).deleteByFlowInstanceId(anyLong());

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);

        Assert.assertEquals(1, result);
        verify(mockJpaRepository, times(1)).deleteByFlowInstanceId(anyLong());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        doReturn(Optional.of(userTaskInstanceEntity)).when(mockJpaRepository)
                .findByInstanceTypeAndActivityId(any(FlowNodeType.class), any(), anyLong());

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.ACTIVITY, "test", 1L);

        Assert.assertEquals(Optional.of(userTaskInstanceEntity), result);
        verify(mockJpaRepository, times(1))
                .findByInstanceTypeAndActivityId(any(FlowNodeType.class), any(), anyLong());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        UserTaskInstanceEntity userTaskInstanceEntity = new UserTaskInstanceEntity();
        doReturn(Optional.of(userTaskInstanceEntity)).when(mockJpaRepository)
                .findByInstanceTypeAndName(any(FlowNodeType.class), any(), anyLong());

        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.ACTIVITY, "test", 1L);

        Assert.assertEquals(Optional.of(userTaskInstanceEntity), result);
        verify(mockJpaRepository, times(1))
                .findByInstanceTypeAndName(any(FlowNodeType.class), any(), anyLong());
    }

    @Test
    public void testFindConfirmById() {
        doReturn(true).when(mockJpaRepository).findConfirmById(anyLong());

        Boolean result = userTaskInstanceRepository.findConfirmById(1L);

        Assert.assertEquals(true, result);
        verify(mockJpaRepository, times(1)).findConfirmById(anyLong());
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> list = new ArrayList<>();
        list.add(userTaskInstanceEntity);
        Pageable pageable = PageRequest.of(0, 10);
        doReturn(new PageImpl<>(list, pageable, list.size())).when(mockJpaSpecificationExecutor)
                .findAll(any(Specification.class), any(Pageable.class));

        Page<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                new ArrayList<>(), FlowNodeStatus.CREATED, pageable);

        Assert.assertEquals(list, result);
        verify(mockJpaSpecificationExecutor, times(1))
                .findAll(any(Specification.class), any(Pageable.class));
    }
}