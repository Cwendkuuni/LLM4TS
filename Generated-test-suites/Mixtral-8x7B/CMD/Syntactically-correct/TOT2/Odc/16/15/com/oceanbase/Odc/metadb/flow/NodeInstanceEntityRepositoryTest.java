import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepositoryTest.TestNodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepositoryMock;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutorMock;

    @InjectMocks
    private TestNodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(NodeInstanceEntity.builder().build());

        when(jpaSpecificationExecutorMock.query(any(Specification.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(nodeInstanceEntities));

        Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.SERVICE_TASK);

        assertEquals(nodeInstanceEntities, result.getContent());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(NodeInstanceEntity.builder().build());

        when(jpaRepositoryMock.findByFlowInstanceId(1L)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(nodeInstanceEntities, result);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Collections.singletonList(1L);
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        nodeInstanceEntities.add(NodeInstanceEntity.builder().build());

        when(jpaRepositoryMock.findByIdIn(ids)).thenReturn(nodeInstanceEntities);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(nodeInstanceEntities, result);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        doNothing().when(jpaRepositoryMock).deleteAll(any());

        nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        verify(jpaRepositoryMock, times(1)).deleteAll(any());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        doNothing().when(jpaRepositoryMock).deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class));

        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);

        verify(jpaRepositoryMock, times(1))
                .deleteByInstanceIdAndInstanceType(anyLong(), any(FlowNodeType.class));
    }

    static class TestNodeInstanceEntityRepository extends NodeInstanceEntityRepository {

        public TestNodeInstanceEntityRepository(JpaRepository<NodeInstanceEntity, Long> jpaRepository,
                JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutor) {
            super(jpaRepository, jpaSpecificationExecutor);
        }

        @Override
        public Page<NodeInstanceEntity> findAll(Specification<NodeInstanceEntity> spec, PageRequest pageRequest) {
            return super.findAll(spec, pageRequest);
        }

        @Override
        public long count(Specification<NodeInstanceEntity> spec) {
            return super.count(spec);
        }

    }

}