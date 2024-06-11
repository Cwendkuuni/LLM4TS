import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertEquals(0, nodeInstanceEntities.size());

        // Add a test NodeInstanceEntity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(0, nodeInstanceEntities.size());

        // Add a test NodeInstanceEntity
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

        // Add test NodeInstanceEntities
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(id2);

        nodeInstanceEntityRepository.saveAll(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntitiesBeforeDeletion = nodeInstanceEntityRepository.findByInstanceId(instanceId);
        assertEquals(0, nodeInstanceEntitiesBeforeDeletion.size());

        // Add test NodeInstanceEntities
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);

        nodeInstanceEntityRepository.saveAll(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceId(instanceId);

        assertEquals(2, rowsAffected);

        List<NodeInstanceEntity> nodeInstanceEntitiesAfterDeletion = nodeInstanceEntityRepository.findByInstanceId(instanceId);

        assertEquals(0, nodeInstanceEntitiesAfterDeletion.size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        List<NodeInstanceEntity> nodeInstanceEntitiesBeforeDeletion = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(0, nodeInstanceEntitiesBeforeDeletion.size());

        // Add test NodeInstanceEntities
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);

        nodeInstanceEntityRepository.saveAll(List.of(nodeInstanceEntity1, nodeInstanceEntity2));

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(2, rowsAffected);

        List<NodeInstanceEntity> nodeInstanceEntitiesAfterDeletion = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, nodeInstanceEntitiesAfterDeletion.size());
    }
}