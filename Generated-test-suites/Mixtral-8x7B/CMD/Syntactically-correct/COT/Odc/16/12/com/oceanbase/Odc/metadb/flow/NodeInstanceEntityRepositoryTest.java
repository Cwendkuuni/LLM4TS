import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;
        List<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(id2);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);

        nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).orElse(null);
        assertNull(nodeInstanceEntity);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, result);

        nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).orElse(null);
        assertNull(nodeInstanceEntity);
    }
}