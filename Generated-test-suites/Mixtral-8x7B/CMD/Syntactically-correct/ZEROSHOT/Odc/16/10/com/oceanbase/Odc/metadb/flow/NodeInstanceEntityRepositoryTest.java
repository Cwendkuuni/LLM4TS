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
import org.springframework.boot.test.autoconfigure.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.END_EVENT);
        entityManager.persist(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);
        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity1.getId(), result.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        entityManager.persist(nodeInstanceEntity2);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);
        entityManager.persist(nodeInstanceEntity3);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(instanceId);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(instanceId);
        entityManager.persist(nodeInstanceEntity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(2, result);

        Optional<NodeInstanceEntity> findResult1 = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId());
        assertFalse(findResult1.isPresent());

        Optional<NodeInstanceEntity> findResult2 = nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId());
        assertFalse(findResult2.isPresent());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        entityManager.persist(nodeInstanceEntity2);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setInstanceId(instanceId);
        nodeInstanceEntity3.setInstanceType(FlowNodeType.BOUNDARY_EVENT);
        entityManager.persist(nodeInstanceEntity3);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, result);

        Optional<NodeInstanceEntity> findResult1 = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId());
        assertFalse(findResult1.isPresent());

        Optional<NodeInstanceEntity> findResult2 = nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId());
        assertFalse(findResult2.isPresent());

        Optional<NodeInstanceEntity> findResult3 = nodeInstanceEntityRepository.findById(nodeInstanceEntity3.getId());
        assertTrue(findResult3.isPresent());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertNotNull(savedNodeInstanceEntity.getId());

        Optional<NodeInstanceEntity> findResult = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId());
        assertTrue(findResult.isPresent());
        assertEquals(savedNodeInstanceEntity, findResult.get());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        for (long i = 1; i <= 5; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntity.setInstanceId(i);
            nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
            nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
            nodeInstanceEntities.add(nodeInstanceEntity);
            entityManager.persist(nodeInstanceEntity);
        }

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();
        assertEquals(5, result.size());
    }

    @Test
    public void testCount() {
        for (long i = 1; i <= 5; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntity.setInstanceId(i);
            nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
            nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
            entityManager.persist(nodeInstanceEntity);
        }

        long result = nodeInstanceEntityRepository.count();
        assertEquals(5, result);
    }

    @Test
    public void testExistsById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        entityManager.persist(nodeInstanceEntity);

        boolean result1 = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId());
        assertTrue(result1);

        boolean result2 = nodeInstanceEntityRepository.existsById(-1L);
        assertFalse(result2);
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        entityManager.persist(nodeInstanceEntity);

        Optional<NodeInstanceEntity> result1 = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());
        assertTrue(result1.isPresent());
        assertEquals(nodeInstanceEntity, result1.get());

        Optional<NodeInstanceEntity> result2 = nodeInstanceEntityRepository.findById(-1L);
        assertFalse(result2.isPresent());
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        entityManager.persist(nodeInstanceEntity);

        nodeInstanceEntityRepository.deleteById(nodeInstanceEntity.getId());
        Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());
        assertFalse(result.isPresent());
    }

}