import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotNull(savedNodeInstanceEntity.getId());
        assertEquals(nodeInstanceEntity.getFlowInstanceId(), savedNodeInstanceEntity.getFlowInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START_EVENT, FlowableElementType.TASK);

        assertNotNull(nodeInstanceEntities);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(savedNodeInstanceEntity.getId(), nodeInstanceEntities.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.USER_TASK);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.TASK);

        nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertNotNull(nodeInstanceEntities);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1.getId(), nodeInstanceEntities.get(0).getId());
        assertEquals(nodeInstanceEntity2.getId(), nodeInstanceEntities.get(1).getId());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.TASK);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(2L);
        nodeInstanceEntity2.setInstanceId(1L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.USER_TASK);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.TASK);

        nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

        Collection<Long> ids = new ArrayList<>();
        ids.add(nodeInstanceEntity1.getId());
        ids.add(nodeInstanceEntity2.getId());

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(nodeInstanceEntities);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1.getId(), nodeInstanceEntities.get(0).getId());
        assertEquals(nodeInstanceEntity2.getId(), nodeInstanceEntities.get(1).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, rowsAffected);
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId()).orElse(null));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.USER_TASK);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.TASK);

        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertEquals(1, rowsAffected);
        assertNull(nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId()).orElse(null));
        assertNotNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null));
    }

    @Test
    public void testUpdate() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        savedNodeInstanceEntity.setInstanceId(2L);
        NodeInstanceEntity updatedNodeInstanceEntity = nodeInstanceEntityRepository.save(savedNodeInstanceEntity);

        assertNotSame(savedNodeInstanceEntity, updatedNodeInstanceEntity);
        assertEquals(savedNodeInstanceEntity.getId(), updatedNodeInstanceEntity.getId());
        assertEquals(savedNodeInstanceEntity.getFlowInstanceId(), updatedNodeInstanceEntity.getFlowInstanceId());
        assertEquals(2L, updatedNodeInstanceEntity.getInstanceId());
        assertEquals(savedNodeInstanceEntity.getInstanceType(), updatedNodeInstanceEntity.getInstanceType());
        assertEquals(savedNodeInstanceEntity.getFlowableElementType(), updatedNodeInstanceEntity.getFlowableElementType());
    }
}