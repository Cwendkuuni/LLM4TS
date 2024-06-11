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

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertEquals(0, nodes.size());

        NodeInstanceEntity node = new NodeInstanceEntity();
        node.setInstanceId(instanceId);
        node.setInstanceType(instanceType);
        node.setFlowableElementType(flowableElementType);

        nodeInstanceEntityRepository.save(node);

        nodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);

        assertEquals(1, nodes.size());
        assertEquals(node, nodes.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(0, nodes.size());

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setFlowInstanceId(flowInstanceId);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setFlowInstanceId(flowInstanceId);

        nodeInstanceEntityRepository.save(node1);
        nodeInstanceEntityRepository.save(node2);

        nodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, nodes.size());
        assertEquals(node1, nodes.get(0));
        assertEquals(node2, nodes.get(1));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        List<Long> ids = new ArrayList<>();
        ids.add(id1);
        ids.add(id2);

        List<NodeInstanceEntity> nodes = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(0, nodes.size());

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setId(id1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setId(id2);

        nodeInstanceEntityRepository.save(node1);
        nodeInstanceEntityRepository.save(node2);

        nodes = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, nodes.size());
        assertEquals(node1, nodes.get(0));
        assertEquals(node2, nodes.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setFlowInstanceId(instanceId);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setFlowInstanceId(instanceId);

        nodeInstanceEntityRepository.save(node1);
        nodeInstanceEntityRepository.save(node2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(2, result);
        assertNull(nodeInstanceEntityRepository.findById(node1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(node2.getId()).orElse(null));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setInstanceId(instanceId);
        node1.setInstanceType(instanceType);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setInstanceId(instanceId);
        node2.setInstanceType(instanceType);

        nodeInstanceEntityRepository.save(node1);
        nodeInstanceEntityRepository.save(node2);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(2, result);
        assertNull(nodeInstanceEntityRepository.findById(node1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(node2.getId()).orElse(null));
    }
}