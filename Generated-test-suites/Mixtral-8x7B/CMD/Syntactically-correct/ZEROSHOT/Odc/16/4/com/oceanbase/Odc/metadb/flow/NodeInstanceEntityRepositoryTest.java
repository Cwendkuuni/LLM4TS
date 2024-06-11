import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType.name());
        nodeInstanceEntity.setFlowableElementType(flowableElementType.name());
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity.getId(), nodeInstanceEntities.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(0, nodeInstanceEntities.size());

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(0, nodeInstanceEntities.size());

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(2, result);

        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId()).orElse(null);
        assertNull(nodeInstanceEntity);

        nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null);
        assertNull(nodeInstanceEntity);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType.name());
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType.name());
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, result);

        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId()).orElse(null);
        assertNull(nodeInstanceEntity);

        nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null);
        assertNull(nodeInstanceEntity);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType("START");
        nodeInstanceEntity.setFlowableElementType("TASK");
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).orElse(null);
        assertNotNull(savedNodeInstanceEntity);
        assertEquals(nodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType("START");
        nodeInstanceEntity1.setFlowableElementType("TASK");
        nodeInstanceEntities.add(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType("END");
        nodeInstanceEntity2.setFlowableElementType("TASK");
        nodeInstanceEntities.add(nodeInstanceEntity2);

        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        List<NodeInstanceEntity> allNodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(2, allNodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1.getInstanceId(), allNodeInstanceEntities.get(0).getInstanceId());
        assertEquals(nodeInstanceEntity2.getInstanceId(), allNodeInstanceEntities.get(1).getInstanceId());
    }

}