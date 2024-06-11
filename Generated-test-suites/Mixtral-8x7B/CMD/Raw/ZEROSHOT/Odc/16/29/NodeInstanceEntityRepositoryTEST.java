import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository jpaRepository;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        given(jpaRepository.query(anyString(), any(NodeInstanceEntity.class))).willReturn(nodeInstanceEntities);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        // Then
        assertThat(result).isEqualTo(nodeInstanceEntities);
        verify(jpaRepository, times(1)).query(anyString(), any(NodeInstanceEntity.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        // Given
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        given(jpaRepository.findByFlowInstanceId(flowInstanceId)).willReturn(nodeInstanceEntities);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Then
        assertThat(result).isEqualTo(nodeInstanceEntities);
        verify(jpaRepository, times(1)).findByFlowInstanceId(flowInstanceId);
    }

    @Test
    public void testFindByIds() {
        // Given
        Collection<Long> ids = Collections.singletonList(1L);
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        given(jpaRepository.query(anyString(), anyMap())).willReturn(nodeInstanceEntities);

        // When
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        // Then
        assertThat(result).isEqualTo(nodeInstanceEntities);
        verify(jpaRepository, times(1)).query(anyString(), anyMap());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        // Given
        Long instanceId = 1L;
        given(jpaRepository.createQuery(anyString())).willReturn(null);

        // When
        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        // Then
        verify(jpaRepository, times(1)).createQuery(anyString());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        // Given
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        given(jpaRepository.createQuery(anyString())).willReturn(null);

        // When
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        // Then
        verify(jpaRepository, times(1)).createQuery(anyString());
    }
}