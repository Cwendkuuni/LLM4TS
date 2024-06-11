import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

    @Mock
    private JpaRepository jpaRepository;

    @Mock
    private JpaSpecificationExecutor jpaSpecificationExecutor;

    @InjectMocks
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaSpecificationExecutor.findAll(any(Specification.class))).thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(entities, result);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), anyLong(), any(Collection.class), any(Collection.class)))
                .thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Collections.singletonList(2L));
        assertEquals(entities, result);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), anyLong(), any(Collection.class), any(Collection.class)))
                .thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Collections.singletonList("role1"));
        assertEquals(entities, result);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), anyCollection(), any(Collection.class)))
                .thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(Collections.singletonList("role1"));
        assertEquals(entities, result);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), anyLong(), any(Collection.class)))
                .thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(entities, result);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), anyLong(), any(Collection.class)))
                .thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(entities, result);
    }

    @Test
    public void testUpdate() {
        int result = userTaskInstanceRepository.update(new UserTaskInstanceEntity());
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.CREATED);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START_EVENT, "activityId", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START_EVENT, "name", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = new ArrayList<>();
        entities.add(new UserTaskInstanceEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), anyCollection(), any(Collection.class)))
                .thenReturn(entities);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(Collections.singletonList(1L), FlowNodeStatus.CREATED);
        assertEquals(entities, result);
    }
}