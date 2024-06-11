import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        entity1.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);
        entity2.setFlowableElementType(FlowableElementType.GATEWAY);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(id1);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(id2);
        nodeInstanceEntityRepository.save(entity2);

        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, result.size());
        assertEquals(entity1, result.get(0));
        assertEquals(entity2, result.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(entity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(2, result);

        List<NodeInstanceEntity> foundEntities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertEquals(Collections.emptyList(), foundEntities);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(entity2);

        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        entity3.setInstanceId(instanceId);
        entity3.setInstanceType(FlowNodeType.END_EVENT);
        nodeInstanceEntityRepository.save(entity3);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, result);

        List<NodeInstanceEntity> foundEntities = nodeInstanceEntityRepository.findByInstanceId(instanceId);
        assertEquals(1, foundEntities.size());
        assertEquals(entity3, foundEntities.get(0));
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        assertNotNull(entity.getId());
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        Long id = entity.getId();
        NodeInstanceEntity foundEntity = nodeInstanceEntityRepository.findById(id).orElse(null);
        assertNotNull(foundEntity);
        assertEquals(entity, foundEntity);
    }

    @Test
    public void testFindByInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceId(instanceId);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(FlowNodeType.END_EVENT);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId,
                instanceType);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        Long id = entity.getId();
        nodeInstanceEntityRepository.deleteById(id);

        NodeInstanceEntity deletedEntity = nodeInstanceEntityRepository.findById(id).orElse(null);
        assertNull(deletedEntity);
    }
}