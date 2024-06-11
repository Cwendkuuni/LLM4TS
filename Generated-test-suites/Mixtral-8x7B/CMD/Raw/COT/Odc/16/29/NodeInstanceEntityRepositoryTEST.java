import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.EVENT);
        assertNotNull(nodeInstances);
        assertEquals(1, nodeInstances.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertNotNull(nodeInstances);
        assertTrue(nodeInstances.size() > 0);
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
        assertNotNull(nodeInstances);
        assertEquals(3, nodeInstances.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, rowsAffected);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(1, rowsAffected);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.EVENT);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertNotNull(savedNodeInstanceEntity);
        assertNotSame(0, savedNodeInstanceEntity.getId());

        NodeInstanceEntity foundNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
                .orElse(null);
        assertNotNull(foundNodeInstanceEntity);
        assertEquals(savedNodeInstanceEntity, foundNodeInstanceEntity);

        nodeInstanceEntityRepository.deleteById(savedNodeInstanceEntity.getId());
        NodeInstanceEntity deletedNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
                .orElse(null);
        assertNull(deletedNodeInstanceEntity);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstances = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
        assertNotNull(nodeInstances);
        assertTrue(nodeInstances.size() > 0);
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(1L).orElse(null);
        assertNotNull(nodeInstanceEntity);

        nodeInstanceEntity = nodeInstanceEntityRepository.findOne(1L);
        assertNotNull(nodeInstanceEntity);
    }

    @Test
    public void testExists() {
        assertTrue(nodeInstanceEntityRepository.existsById(1L));
        assertFalse(nodeInstanceEntityRepository.existsById(999L));
    }

    @Test
    public void testCount() {
        long count = nodeInstanceEntityRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testDeleteAll() {
        List<NodeInstanceEntity> nodeInstances = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
        assertNotNull(nodeInstances);
        assertTrue(nodeInstances.size() > 0);

        nodeInstanceEntityRepository.deleteAll();
        assertEquals(0, nodeInstanceEntityRepository.count());
    }

    @Test
    public void testFlush() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.EVENT);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertNotNull(savedNodeInstanceEntity);
        assertNotSame(0, savedNodeInstanceEntity.getId());

        nodeInstanceEntityRepository.flush();

        NodeInstanceEntity foundNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
                .orElse(null);
        assertNotNull(foundNodeInstanceEntity);
        assertEquals(savedNodeInstanceEntity, foundNodeInstanceEntity);

        nodeInstanceEntityRepository.deleteById(savedNodeInstanceEntity.getId());
        NodeInstanceEntity deletedNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
                .orElse(null);
        assertNull(deletedNodeInstanceEntity);
    }

    @Test
    public void testSaveAll() {
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, FlowNodeType.START_EVENT, FlowableElementType.EVENT, 1L),
                new NodeInstanceEntity(2L, FlowNodeType.END_EVENT, FlowableElementType.EVENT, 1L),
                new NodeInstanceEntity(3L, FlowNodeType.USER_TASK, FlowableElementType.TASK, 1L));

        List<NodeInstanceEntity> savedNodeInstances = (List<NodeInstanceEntity>) nodeInstanceEntityRepository
                .saveAll(nodeInstances);
        assertNotNull(savedNodeInstances);
        assertEquals(3, savedNodeInstances.size());

        for (NodeInstanceEntity nodeInstanceEntity : savedNodeInstances) {
            assertNotSame(0, nodeInstanceEntity.getId());
        }

        for (NodeInstanceEntity nodeInstanceEntity : savedNodeInstances) {
            NodeInstanceEntity foundNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId())
                    .orElse(null);
            assertNotNull(foundNodeInstanceEntity);
            assertEquals(nodeInstanceEntity, foundNodeInstanceEntity);
        }

        nodeInstanceEntityRepository.deleteAll(savedNodeInstances);
        assertEquals(0, nodeInstanceEntityRepository.count());
    }

    @Test
    public void testFindAllById() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstances = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAllById(ids);
        assertNotNull(nodeInstances);
        assertEquals(3, nodeInstances.size());
    }

    @Test
    public void testFindAllByIdNullSafe() {
        List<NodeInstanceEntity> nodeInstances = (List<NodeInstanceEntity>) nodeInstanceEntityRepository
                .findAllById(Collections.emptyList());
        assertTrue(nodeInstances.isEmpty());
    }
}