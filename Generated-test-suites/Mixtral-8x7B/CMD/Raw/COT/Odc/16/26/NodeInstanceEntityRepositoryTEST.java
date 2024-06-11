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
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        entity1.setFlowableElementType(flowableElementType);
        entity1.setId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);
        entity2.setFlowableElementType(flowableElementType);
        entity2.setId(2L);
        entityManager.persist(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertEquals(2, result.size());
        assertEquals(entity1, result.get(0));
        assertEquals(entity2, result.get(1));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(flowInstanceId);
        entity1.setId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(flowInstanceId);
        entity2.setId(2L);
        entityManager.persist(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        assertEquals(entity1, result.get(0));
        assertEquals(entity2, result.get(1));
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(id1);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(id2);
        entityManager.persist(entity2);

        List<Long> ids = Arrays.asList(id1, id2);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, result.size());
        assertEquals(entity1, result.get(0));
        assertEquals(entity2, result.get(1));
    }

    @Test
    public void testFindByIds_EmptyList() {
        List<Long> ids = new ArrayList<>();
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(0, result.size());
    }

    @Test
    public void testFindByIds_Null() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(null);

        assertEquals(0, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setFlowInstanceId(instanceId);
        entity1.setId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setFlowInstanceId(instanceId);
        entity2.setId(2L);
        entityManager.persist(entity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(2, rowsAffected);
        assertNull(entityManager.find(NodeInstanceEntity.class, 1L));
        assertNull(entityManager.find(NodeInstanceEntity.class, 2L));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(instanceId);
        entity1.setInstanceType(instanceType);
        entity1.setId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(instanceId);
        entity2.setInstanceType(instanceType);
        entity2.setId(2L);
        entityManager.persist(entity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(2, rowsAffected);
        assertNull(entityManager.find(NodeInstanceEntity.class, 1L));
        assertNull(entityManager.find(NodeInstanceEntity.class, 2L));
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(2L);
        entityManager.persist(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

        assertEquals(2, result.size());
        assertEquals(entity1, result.get(0));
        assertEquals(entity2, result.get(1));
    }

    @Test
    public void testFindAll_EmptyResult() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

        assertEquals(0, result.size());
    }

    @Test
    public void testExistsById() {
        Long id = 1L;
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(id);
        entityManager.persist(entity);

        boolean exists = nodeInstanceEntityRepository.existsById(id);

        assertTrue(exists);
    }

    @Test
    public void testExistsById_NonExistingId() {
        Long id = 1L;

        boolean exists = nodeInstanceEntityRepository.existsById(id);

        assertFalse(exists);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(id);
        entityManager.persist(entity);

        Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    public void testFindById_NonExistingId() {
        Long id = 1L;

        Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(id);

        assertFalse(result.isPresent());
    }

    @Test
    public void testCount() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setId(2L);
        entityManager.persist(entity2);

        long count = nodeInstanceEntityRepository.count();

        assertEquals(2, count);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(id);
        entityManager.persist(entity);

        nodeInstanceEntityRepository.deleteById(id);

        assertNull(entityManager.find(NodeInstanceEntity.class, id));
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entityManager.persist(entity);

        Long id = entity.getId();

        NodeInstanceEntity savedEntity = new NodeInstanceEntity();
        entityManager.persist(savedEntity);

        nodeInstanceEntityRepository.save(savedEntity);

        assertNotNull(entityManager.find(NodeInstanceEntity.class, id));
        assertEquals(savedEntity, entityManager.find(NodeInstanceEntity.class, savedEntity.getId()));
    }

    @Test
    public void testFindAllByPageable() {
        for (int i = 0; i < 20; i++) {
            NodeInstanceEntity entity = new NodeInstanceEntity();
            entityManager.persist(entity);
        }

        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(pageRequest);

        assertEquals(20, result.getTotalElements());
        assertEquals(10, result.getNumberOfElements());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    public void testSaveAll() {
        List<NodeInstanceEntity> entities = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            NodeInstanceEntity entity = new NodeInstanceEntity();
            entities.add(entity);
            entityManager.persist(entity);
        }

        nodeInstanceEntityRepository.saveAll(entities);

        for (NodeInstanceEntity entity : entities) {
            assertNotNull(entityManager.find(NodeInstanceEntity.class, entity.getId()));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullInstanceId() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.START_EVENT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullInstanceType() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType_NoMatchingRows() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(0, rowsAffected);
    }

    @Test
    public void testDeleteByFlowInstanceId_NoMatchingRows() {
        Long instanceId = 1L;

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(0, rowsAffected);
    }
}