import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NodeInstanceEntityRepositoryTest {

    @Mock
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @InjectMocks
    private NodeInstanceEntityRepositoryTest nodeInstanceEntityRepositoryTest;

    private NodeInstanceEntity nodeInstanceEntity;

    @Before
    public void setup() {
        nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setVersion(1);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setCreateTime(new Date());
        nodeInstanceEntity.setLastUpdateTime(new Date());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setVersion(1);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setCreateTime(new Date());
        nodeInstanceEntity.setLastUpdateTime(new Date());
        when(nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType))
                .thenReturn(Arrays.asList(nodeInstanceEntity));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(result.size(), 1);
        assertEquals(result.get(0).getId(), nodeInstanceEntity.getId());
        assertEquals(result.get(0).getVersion(), nodeInstanceEntity.getVersion());
        assertEquals(result.get(0).getFlowInstanceId(), nodeInstanceEntity.getFlowInstanceId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.USER_TASK);
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntity1.setVersion(1);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity1.setCreateTime(new Date());
        nodeInstanceEntity1.setLastUpdateTime(new Date());
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.USER_TASK);
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntity2.setVersion(1);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntity2.setCreateTime(new Date());
        nodeInstanceEntity2.setLastUpdateTime(new Date());
        when(nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId))
                .thenReturn(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(result.size(), 2);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setVersion(1);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setCreateTime(new Date());
        nodeInstanceEntity.setLastUpdateTime(new Date());
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(result, 1);
        assertTrue(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).isEmpty());
    }
}