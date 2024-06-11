import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntitySpecifications;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepository.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository jpaRepository;

    @Mock
    private JpaSpecificationExecutor jpaSpecificationExecutor;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity;

    private List<NodeInstanceEntity> nodeInstanceEntityList;

    private Pageable pageable;

    private Specification<NodeInstanceEntity> spec;

    @Before
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);

        nodeInstanceEntityList = new ArrayList<>();
        nodeInstanceEntityList.add(nodeInstanceEntity);

        pageable = PageRequest.of(0, 10, Sort.Direction.ASC, "id");

        spec = Specification.where(NodeInstanceEntitySpecifications.instanceIdEq(1L));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        when(jpaSpecificationExecutor.findAll(spec, pageable)).thenReturn(Page.empty());

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.TASK);

        verify(jpaSpecificationExecutor).findAll(spec, pageable);
        assertEquals(resultList, Collections.emptyList());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_WithResult() {
        when(jpaSpecificationExecutor.findAll(spec, pageable)).thenReturn(Page.of(nodeInstanceEntityList));

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.TASK);

        verify(jpaSpecificationExecutor).findAll(spec, pageable);
        assertEquals(resultList, nodeInstanceEntityList);
    }

    @Test
    public void testFindByFlowInstanceId() {
        when(jpaRepository.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntityList);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        verify(jpaRepository).findByFlowInstanceId(1L);
        assertEquals(resultList, nodeInstanceEntityList);
    }

    @Test
    public void testFindByIds() {
        when(jpaRepository.findByIds(Collections.singletonList(1L))).thenReturn(nodeInstanceEntityList);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByIds(Collections.singletonList(1L));

        verify(jpaRepository).findByIds(Collections.singletonList(1L));
        assertEquals(resultList, nodeInstanceEntityList);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        when(jpaRepository.deleteByFlowInstanceId(1L)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        verify(jpaRepository).deleteByFlowInstanceId(1L);
        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        when(jpaRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START)).thenReturn(1);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);

        verify(jpaRepository).deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(result, 1);
    }

}