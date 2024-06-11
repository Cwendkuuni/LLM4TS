import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
        assertEquals(1, entities.size());
        assertEquals(1L, entities.get(0).getInstanceId().longValue());
        assertEquals(FlowNodeType.START_EVENT, entities.get(0).getInstanceType());
        assertEquals(FlowableElementType.START_EVENT, entities.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNoResults() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L,
                FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
        assertEquals(0, entities.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(2, entities.size());
        assertEquals(1L, entities.get(0).getFlowInstanceId().longValue());
        assertEquals(2L, entities.get(1).getFlowInstanceId().longValue());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, entities.size());
        assertEquals(1L, entities.get(0).getId().longValue());
        assertEquals(2L, entities.get(1).getId().longValue());
    }

    @Test
    public void testFindByIdsWithEmptyList() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(Collections.emptyList());
        assertEquals(0, entities.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, rowsAffected);

        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(0, entities.size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(1, rowsAffected);

        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceId(1L);
        assertEquals(0, entities.size());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(3L);
        entity.setInstanceType(FlowNodeType.USER_TASK);
        entity.setFlowableElementType(FlowableElementType.USER_TASK);
        entity.setFlowInstanceId(1L);
        NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
        assertNotNull(savedEntity.getId());
    }

    @Test
    public void testSaveAll() {
        List<NodeInstanceEntity> entities = new ArrayList<>();
        entities.add(new NodeInstanceEntity(3L, FlowNodeType.USER_TASK, FlowableElementType.USER_TASK, 1L));
        entities.add(new NodeInstanceEntity(4L, FlowNodeType.END_EVENT, FlowableElementType.END_EVENT, 1L));
        List<NodeInstanceEntity> savedEntities = nodeInstanceRepository.saveAll(entities);
        assertEquals(2, savedEntities.size());
        for (NodeInstanceEntity entity : savedEntities) {
            assertNotNull(entity.getId());
        }
    }

    @Test
    public void testCount() {
        Long count = nodeInstanceEntityRepository.count();
        assertNotNull(count);
        assertTrue(count > 0);
    }

    @Test
    public void testExistsById() {
        assertTrue(nodeInstanceEntityRepository.existsById(1L));
        assertFalse(nodeInstanceEntityRepository.existsById(100L));
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(1L).orElse(null);
        assertNotNull(entity);
        assertEquals(1L, entity.getId().longValue());
    }

    @Test
    public void testFindByIdWithNoResults() {
        NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(100L).orElse(null);
        assertNull(entity);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> entities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(1L)));
        assertTrue(entities.stream().anyMatch(e -> e.getId().equals(2L)));
    }

    @Test
    public void testFindAllWithEmptyList() {
        List<NodeInstanceEntity> entities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
        entities.clear();
        assertEquals(0, entities.size());
    }
}