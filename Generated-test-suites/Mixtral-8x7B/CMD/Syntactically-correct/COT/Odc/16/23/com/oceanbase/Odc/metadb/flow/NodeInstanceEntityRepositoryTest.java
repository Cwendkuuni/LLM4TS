import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor;

    private NodeInstanceEntity nodeInstanceEntity;

    private List<NodeInstanceEntity> nodeInstanceEntities;

    @Before
    public void setUp() {
        nodeInstanceEntity = NodeInstanceEntity.builder()
                .id(1L)
                .instanceId(2L)
                .instanceType(FlowNodeType.START_EVENT)
                .flowableElementType(FlowableElementType.TASK)
                .build();

        nodeInstanceEntities = new ArrayList<>(Arrays.asList(nodeInstanceEntity));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);
        given(jpaSpecificationExecutor.findAll(any(Specification.class)))
                .willReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                2L, FlowNodeType.START_EVENT, FlowableElementType.TASK);

        assertThat(result).hasSize(1).containsExactly(nodeInstanceEntity);
        verify(jpaSpecificationExecutor, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);
        given(jpaRepository.findByFlowInstanceId(2L))
                .willReturn(expectedNodes);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(2L);

        assertThat(result).hasSize(1).containsExactly(nodeInstanceEntity);
        verify(jpaRepository, times(1)).findByFlowInstanceId(2L);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Collections.singletonList(1L);
        List<NodeInstanceEntity> expectedNodes = Arrays.asList(nodeInstanceEntity);
        given(jpaRepository.findByIdIn(ids))
                .willReturn(expectedNodes);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertThat(result).hasSize(1).containsExactly(nodeInstanceEntity);
        verify(jpaRepository, times(1)).findByIdIn(ids);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        given(jpaRepository.deleteByFlowInstanceId(2L))
                .willReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(2L);

        assertThat(result).isEqualTo(1);
        verify(jpaRepository, times(1)).deleteByFlowInstanceId(2L);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        given(jpaRepository.deleteByInstanceIdAndInstanceType(2L,
                FlowNodeType.START_EVENT))
                .willReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L,
                FlowNodeType.START_EVENT);

        assertThat(result).isEqualTo(1);
        verify(jpaRepository, times(1)).deleteByInstanceIdAndInstanceType(2L,
                FlowNodeType.START_EVENT);
    }
}