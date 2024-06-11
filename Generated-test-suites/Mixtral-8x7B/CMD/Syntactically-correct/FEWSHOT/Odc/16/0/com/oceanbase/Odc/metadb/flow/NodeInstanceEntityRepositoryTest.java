import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.TASK);
        assertEquals(0, nodeInstances.size());

        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setInstanceId(1L);
        nodeInstance.setInstanceType(FlowNodeType.START);
        nodeInstance.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance);

        nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.TASK);
        assertEquals(1, nodeInstances.size());
        assertEquals(nodeInstance.getId(), nodeInstances.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setInstanceId(1L);
        nodeInstance1.setInstanceType(FlowNodeType.START);
        nodeInstance1.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance1);

        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setInstanceId(1L);
        nodeInstance2.setInstanceType(FlowNodeType.END);
        nodeInstance2.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance2);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(2, nodeInstances.size());

        List<Long> ids = new ArrayList<>();
        for (NodeInstanceEntity nodeInstance : nodeInstances) {
            ids.add(nodeInstance.getId());
        }

        assertTrue(ids.contains(nodeInstance1.getId()));
        assertTrue(ids.contains(nodeInstance2.getId()));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();

        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setInstanceId(1L);
        nodeInstance1.setInstanceType(FlowNodeType.START);
        nodeInstance1.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance1);
        ids.add(nodeInstance1.getId());

        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setInstanceId(2L);
        nodeInstance2.setInstanceType(FlowNodeType.END);
        nodeInstance2.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance2);
        ids.add(nodeInstance2.getId());

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstances.size());

        List<Long> nodeIds = new ArrayList<>();
        for (NodeInstanceEntity nodeInstance : nodeInstances) {
            nodeIds.add(nodeInstance.getId());
        }

        assertTrue(nodeIds.contains(nodeInstance1.getId()));
        assertTrue(nodeIds.contains(nodeInstance2.getId()));
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setInstanceId(1L);
        nodeInstance1.setInstanceType(FlowNodeType.START);
        nodeInstance1.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance1);

        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setInstanceId(1L);
        nodeInstance2.setInstanceType(FlowNodeType.END);
        nodeInstance2.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, result);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
        nodeInstance1.setInstanceId(1L);
        nodeInstance1.setInstanceType(FlowNodeType.START);
        nodeInstance1.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance1);

        NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
        nodeInstance2.setInstanceId(1L);
        nodeInstance2.setInstanceType(FlowNodeType.END);
        nodeInstance2.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance2);

        NodeInstanceEntity nodeInstance3 = new NodeInstanceEntity();
        nodeInstance3.setInstanceId(2L);
        nodeInstance3.setInstanceType(FlowNodeType.START);
        nodeInstance3.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceRepository.save(nodeInstance3);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);

        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceId(1L);
        assertEquals(1, nodeInstances.size());
        assertEquals(nodeInstance2.getId(), nodeInstances.get(0).getId());

        nodeInstances = nodeInstanceEntityRepository.findByInstanceId(2L);
        assertEquals(1, nodeInstances.size());
        assertEquals(nodeInstance3.getId(), nodeInstances.get(0).getId());
    }
}