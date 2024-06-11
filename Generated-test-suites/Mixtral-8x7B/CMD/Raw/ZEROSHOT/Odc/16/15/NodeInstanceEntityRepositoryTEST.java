import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;
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
        entity2.setFlowInstanceId(flowInstanceId + 1);
        nodeInstanceEntityRepository.save(entity2);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(ids.get(0));
        nodeInstanceEntityRepository.save(entity1);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(ids.get(1));
        nodeInstanceEntityRepository.save(entity2);
        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        entity3.setId(ids.get(2));
        nodeInstanceEntityRepository.save(entity3);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, result.size());
        assertEquals(entity1, result.get(0));
        assertEquals(entity2, result.get(1));
        assertEquals(entity3, result.get(2));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(entity1);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(instanceId + 1);
        nodeInstanceEntityRepository.save(entity2);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, result);
        assertEquals(0, nodeInstanceEntityRepository.findAll().size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(entity1);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId + 1);
        entity2.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(entity2);
        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        entity3.setInstanceId(instanceId + 1);
        entity3.setInstanceType(FlowNodeType.END);
        nodeInstanceEntityRepository.save(entity3);
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, result);
        assertEquals(1, nodeInstanceEntityRepository.findAll().size());
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
        Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(entity.getId());
        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);
        List<NodeInstanceEntity> result = new ArrayList<>(nodeInstanceEntityRepository.findAll());
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    public void testFindAllEmpty() {
        List<NodeInstanceEntity> result = new ArrayList<>(nodeInstanceEntityRepository.findAll());
        assertTrue(result.isEmpty());
    }

    @Test
    public void testCount() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);
        long result = nodeInstanceEntityRepository.count();
        assertEquals(2, result);
    }

    @Test
    public void testExistsById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);
        boolean result = nodeInstanceEntityRepository.existsById(entity.getId());
        assertTrue(result);
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);
        nodeInstanceEntityRepository.deleteById(entity.getId());
        assertNull(nodeInstanceEntityRepository.findById(entity.getId()).orElse(null));
    }

    @Test
    public void testDeleteAll() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);
        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);
        nodeInstanceEntityRepository.deleteAll();
        assertTrue(nodeInstanceEntityRepository.findAll().isEmpty());
    }

    @Test
    public void testFlush() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);
        nodeInstanceEntityRepository.flush();
        assertNotNull(entity.getId());
    }

    @Test
    public void testSaveAll() {
        List<NodeInstanceEntity> entities = Arrays.asList(new NodeInstanceEntity(), new NodeInstanceEntity());
        nodeInstanceEntityRepository.saveAll(entities);
        List<NodeInstanceEntity> result = new ArrayList<>(nodeInstanceEntityRepository.findAll());
        assertEquals(2, result.size());
        assertTrue(result.containsAll(entities));
    }

    @Test
    public void testFindAllById() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> entities = new ArrayList<>();
        for (long id : ids) {
            NodeInstanceEntity entity = new NodeInstanceEntity();
            entity.setId(id);
            entities.add(entity);
            nodeInstanceEntityRepository.save(entity);
        }
        List<NodeInstanceEntity> result = new ArrayList<>(nodeInstanceEntityRepository.findAllById(ids));
        assertEquals(3, result.size());
        assertTrue(result.containsAll(entities));
    }

    @Test
    public void testFindAllByIdEmpty() {
        List<NodeInstanceEntity> result = new ArrayList<>(nodeInstanceEntityRepository.findAllById(Collections.emptyList()));
        assertTrue(result.isEmpty());
    }

}