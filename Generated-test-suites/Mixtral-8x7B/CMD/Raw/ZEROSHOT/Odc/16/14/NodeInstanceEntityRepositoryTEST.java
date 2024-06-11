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
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        entity1.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);
        entity2.setFlowableElementType(FlowableElementType.SERVICE_TASK);
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

        Collection<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(entity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(2, result);

        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, entities.size());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

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
        entity3.setInstanceType(FlowNodeType.END);
        nodeInstanceEntityRepository.save(entity3);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, result);

        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findAll();
        assertEquals(1, entities.size());
        assertEquals(entity3, entities.get(0));
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        assertNotNull(entity.getId());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, entities.size());

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        entities = nodeInstanceEntityRepository.findAll();
        assertEquals(2, entities.size());
        assertTrue(entities.contains(entity1));
        assertTrue(entities.contains(entity2));
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        Long id = entity.getId();
        assertNotNull(id);

        NodeInstanceEntity result = nodeInstanceEntityRepository.findOne(id);
        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        Long id = entity.getId();
        assertNotNull(id);

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(id).orElse(null);
        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    public void testExists() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        Long id = entity.getId();
        assertNotNull(id);

        assertTrue(nodeInstanceEntityRepository.exists(id));
        assertFalse(nodeInstanceEntityRepository.exists(id + 1));
    }

    @Test
    public void testCount() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        long count = nodeInstanceEntityRepository.count();
        assertEquals(2, count);
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        Long id = entity.getId();
        assertNotNull(id);

        nodeInstanceEntityRepository.deleteById(id);

        assertNull(nodeInstanceEntityRepository.findById(id).orElse(null));
    }

    @Test
    @Transactional
    public void testDeleteAll() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        nodeInstanceEntityRepository.deleteAll();

        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByIdIsIn() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        List<Long> ids = new ArrayList<>();
        ids.add(entity1.getId());
        ids.add(entity2.getId());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIdIsIn(ids);
        assertEquals(2, result.size());
        assertTrue(result.contains(entity1));
        assertTrue(result.contains(entity2));
    }
}