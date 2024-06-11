import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
        assertFalse(entities.isEmpty());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, result);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(1, result);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(10, page.getContent().size());
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(1L).orElse(null);
        assertNotNull(entity);
    }

    @Test
    @Transactional
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.START_EVENT);
        entity.setFlowableElementType(FlowableElementType.START_EVENT);
        entity.setFlowInstanceId(1L);
        NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
        assertNotNull(savedEntity.getId());
    }

    @Test
    @Transactional
    public void testDelete() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.START_EVENT);
        entity.setFlowableElementType(FlowableElementType.START_EVENT);
        entity.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(entity);
        nodeInstanceEntityRepository.delete(entity);
        NodeInstanceEntity deletedEntity = nodeInstanceEntityRepository.findById(entity.getId()).orElse(null);
        assertNull(deletedEntity);
    }

    @Test
    public void testExistsById() {
        assertTrue(nodeInstanceEntityRepository.existsById(1L));
        assertFalse(nodeInstanceEntityRepository.existsById(100L));
    }

    @Test
    public void testCount() {
        long count = nodeInstanceEntityRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testFindAllOrderByIdDesc() {
        Pageable pageable = PageRequest.of(0, 10, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "id"));
        Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(10, page.getContent().size());
        for (int i = 0; i < page.getContent().size(); i++) {
            assertEquals(page.getContent().size() - i, page.getContent().get(i).getId().longValue());
        }
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNoResults() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(100L,
                FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceIdWithNoResults() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(100L);
        assertTrue(entities.isEmpty());
    }

    @Test
    public void testFindByIdsWithNullCollection() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(null);
        assertTrue(entities.isEmpty());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceIdWithNoResults() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(100L);
        assertEquals(0, result);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceTypeWithNoResults() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(100L, FlowNodeType.START_EVENT);
        assertEquals(0, result);
    }

    @Test
    public void testFindAllWithEmptyList() {
        Pageable pageable = PageRequest.of(10, 10);
        Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(0, page.getContent().size());
    }

    @Test
    public void testFindOneWithNull() {
        NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(100L).orElse(null);
        assertNull(entity);
    }

    @Test
    @Transactional
    public void testSaveWithNull() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(100L);
        entity.setInstanceType(FlowNodeType.START_EVENT);
        entity.setFlowableElementType(FlowableElementType.START_EVENT);
        entity.setFlowInstanceId(100L);
        NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
        assertNotNull(savedEntity.getId());
        nodeInstanceEntityRepository.delete(savedEntity);
    }
}

class NodeInstanceEntityRepositoryTestConfig {

}