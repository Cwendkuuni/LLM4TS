import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.TransactionSystemException;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntitySpecifications;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@ExtendWith(MockitoExtension.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private JpaRepository<NodeInstanceEntity, Long> jpaRepositoryMock;

    @Mock
    private JpaSpecificationExecutor<NodeInstanceEntity> jpaSpecificationExecutorMock;

    @InjectMocks
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private List<NodeInstanceEntity> nodeInstanceEntityList;
    private Pageable pageable;

    @BeforeEach
    public void setup() {
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setFlowInstanceId(2L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.USER_TASK);

        nodeInstanceEntityList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);

        pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START;

        List<NodeInstanceEntity> nodeInstanceEntityListMock = new ArrayList<>(this.nodeInstanceEntityList);

        Specification<NodeInstanceEntity> specification = NodeInstanceEntitySpecifications.and(
                NodeInstanceEntitySpecifications.hasInstanceId(instanceId),
                NodeInstanceEntitySpecifications.hasInstanceType(instanceType),
                NodeInstanceEntitySpecifications.hasFlowableElementType(flowableElementType));

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(specification);

        assertEquals(nodeInstanceEntityListMock, nodeInstances);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        Specification<NodeInstanceEntity> specification = NodeInstanceEntitySpecifications.hasFlowInstanceId(flowInstanceId);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(specification);

        assertEquals(nodeInstanceEntityList, nodeInstances);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        Specification<NodeInstanceEntity> specification = NodeInstanceEntitySpecifications.inIds(ids);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(specification);

        assertEquals(nodeInstanceEntityList, nodeInstances);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        Specification<NodeInstanceEntity> specification = NodeInstanceEntitySpecifications.hasFlowInstanceId(instanceId);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(specification);

        nodeInstanceEntityRepository.deleteAll(nodeInstances);

        assertThrows(TransactionSystemException.class, () -> {
            nodeInstanceEntityRepository.findAll(specification);
        });
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        Specification<NodeInstanceEntity> specification = NodeInstanceEntitySpecifications.and(
                NodeInstanceEntitySpecifications.hasInstanceId(instanceId),
                NodeInstanceEntitySpecifications.hasInstanceType(instanceType));

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(specification);

        nodeInstanceEntityRepository.deleteAll(nodeInstances);

        assertThrows(TransactionSystemException.class, () -> {
            nodeInstanceEntityRepository.findAll(specification);
        });
    }

}