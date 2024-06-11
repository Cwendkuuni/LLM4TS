import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(flowableElementType);
        entityManager.persist(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertEquals(2, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
        assertEquals(nodeInstanceEntity2, result.get(1));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
        assertEquals(nodeInstanceEntity2, result.get(1));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(id1);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(id2);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
        assertEquals(nodeInstanceEntity2, result.get(1));
    }

    @Test
    public void testFindByIds_emptyList() {
        List<Long> ids = Collections.emptyList();
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(0, result.size());
    }

    @Test
    public void testFindByIds_null() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(null);

        assertEquals(0, result.size());
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

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(2, rowsAffected);
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(2, rowsAffected);
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null));
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();

        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotNull(nodeInstanceEntity.getId());
    }

    @Test
    public void testSave_withFields() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotNull(savedNodeInstanceEntity.getId());
        assertEquals(instanceId, savedNodeInstanceEntity.getInstanceId());
        assertEquals(instanceType, savedNodeInstanceEntity.getInstanceType());
        assertEquals(flowableElementType, savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testSave_updateExistingEntity() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity existingNodeInstanceEntity = new NodeInstanceEntity();
        existingNodeInstanceEntity.setInstanceId(instanceId);
        existingNodeInstanceEntity.setInstanceType(instanceType);
        existingNodeInstanceEntity.setFlowableElementType(flowableElementType);
        entityManager.persist(existingNodeInstanceEntity);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertEquals(existingNodeInstanceEntity.getId(), savedNodeInstanceEntity.getId());
        assertEquals(existingNodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(existingNodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(existingNodeInstanceEntity.getFlowableElementType(),
                savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testSave_updateExistingEntity_withNewId() {
        Long existingId = 1L;
        Long newId = 2L;
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity existingNodeInstanceEntity = new NodeInstanceEntity();
        existingNodeInstanceEntity.setId(existingId);
        existingNodeInstanceEntity.setInstanceId(instanceId);
        existingNodeInstanceEntity.setInstanceType(instanceType);
        existingNodeInstanceEntity.setFlowableElementType(flowableElementType);
        entityManager.persist(existingNodeInstanceEntity);

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(newId);
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotSame(existingId, savedNodeInstanceEntity.getId());
        assertEquals(newId, savedNodeInstanceEntity.getId());
        assertEquals(instanceId, savedNodeInstanceEntity.getInstanceId());
        assertEquals(instanceType, savedNodeInstanceEntity.getInstanceType());
        assertEquals(flowableElementType, savedNodeInstanceEntity.getFlowableElementType());
    }

}