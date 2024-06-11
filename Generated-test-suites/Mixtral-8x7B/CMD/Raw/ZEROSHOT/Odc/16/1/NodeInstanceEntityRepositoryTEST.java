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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        entity1.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);
        entity2.setFlowableElementType(FlowableElementType.START_EVENT);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
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

        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        Long differentFlowInstanceId = 2L;
        entity3.setFlowInstanceId(differentFlowInstanceId);
        nodeInstanceEntityRepository.save(entity3);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        NodeInstanceEntity entity3 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity3);

        List<Long> ids = Arrays.asList(entity1.getId(), entity3.getId());

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity3.getId(), result.get(1).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(entity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(2, rowsAffected);
        assertNull(nodeInstanceEntityRepository.findById(entity1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(entity2.getId()).orElse(null));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(FlowNodeType.EVENT);
        nodeInstanceEntityRepository.save(entity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, rowsAffected);
        assertNull(nodeInstanceEntityRepository.findById(entity1.getId()).orElse(null));
        assertNotNull(nodeInstanceEntityRepository.findById(entity2.getId()).orElse(null));
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
        assertEquals(entity.getId(), result.get().getId());
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = new ArrayList<>(nodeInstanceEntityRepository.findAll());

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    public void testCount() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        Long count = nodeInstanceEntityRepository.count();

        assertEquals(2L, count);
    }

    @Test
    public void testExistsById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        boolean exists = nodeInstanceEntityRepository.existsById(entity.getId());

        assertTrue(exists);
    }

    @Test
    public void testFindAllById() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllById(Arrays.asList(entity1.getId(),
                entity2.getId()));

        assertEquals(2, result.size());
        assertEquals(entity1.getId(), result.get(0).getId());
        assertEquals(entity2.getId(), result.get(1).getId());
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity);

        nodeInstanceEntityRepository.deleteById(entity.getId());

        assertNull(nodeInstanceEntityRepository.findById(entity.getId()).orElse(null));
    }

    @Test
    public void testDeleteAllById() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        nodeInstanceEntityRepository.deleteAllById(Arrays.asList(entity1.getId(), entity2.getId()));

        assertNull(nodeInstanceEntityRepository.findById(entity1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(entity2.getId()).orElse(null));
    }

    @Test
    public void testDeleteAll() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(entity2);

        nodeInstanceEntityRepository.deleteAll();

        assertEquals(0, nodeInstanceEntityRepository.findAll().size());
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

        assertEquals(2, nodeInstanceEntityRepository.findAll().size());
    }

    @Test
    public void testFindAllOrderByFlowInstanceIdAsc() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(2L);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(
                SortUtil.sortAsc(NodeInstanceEntity.Fields.FLOW_INSTANCE_ID));

        assertEquals(2, result.size());
        assertEquals(entity1.getFlowInstanceId(), result.get(0).getFlowInstanceId());
        assertEquals(entity2.getFlowInstanceId(), result.get(1).getFlowInstanceId());
    }

    @Test
    public void testFindAllOrderByFlowInstanceIdDesc() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(2L);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(
                SortUtil.sortDesc(NodeInstanceEntity.Fields.FLOW_INSTANCE_ID));

        assertEquals(2, result.size());
        assertEquals(entity2.getFlowInstanceId(), result.get(0).getFlowInstanceId());
        assertEquals(entity1.getFlowInstanceId(), result.get(1).getFlowInstanceId());
    }

    @Test
    public void testFindAllOrderByFlowInstanceIdAndInstanceIdAsc() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(1L);
        entity1.setInstanceId(1L);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(2L);
        entity2.setInstanceId(2L);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(
                SortUtil.sortAsc(NodeInstanceEntity.Fields.FLOW_INSTANCE_ID, NodeInstanceEntity.Fields.INSTANCE_ID));

        assertEquals(2, result.size());
        assertEquals(entity1.getFlowInstanceId(), result.get(0).getFlowInstanceId());
        assertEquals(entity1.getInstanceId(), result.get(0).getInstanceId());
        assertEquals(entity2.getFlowInstanceId(), result.get(1).getFlowInstanceId());
        assertEquals(entity2.getInstanceId(), result.get(1).getInstanceId());
    }

    @Test
    public void testFindAllOrderByFlowInstanceIdAndInstanceIdDesc() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(1L);
        entity1.setInstanceId(1L);
        nodeInstanceEntityRepository.save(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(2L);
        entity2.setInstanceId(2L);
        nodeInstanceEntityRepository.save(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(
                SortUtil.sortDesc(NodeInstanceEntity.Fields.FLOW_INSTANCE_ID, NodeInstanceEntity.Fields.INSTANCE_ID));

        assertEquals(2, result.size());
        assertEquals(entity2.getFlowInstanceId(), result.get(0).getFlowInstanceId());
        assertEquals(entity2.getInstanceId(), result.get(0).getInstanceId());
        assertEquals(entity1.getFlowInstanceId(), result.get(1).getFlowInstanceId());
        assertEquals(entity1.getInstanceId(), result.get(1).getInstanceId());
    }
}