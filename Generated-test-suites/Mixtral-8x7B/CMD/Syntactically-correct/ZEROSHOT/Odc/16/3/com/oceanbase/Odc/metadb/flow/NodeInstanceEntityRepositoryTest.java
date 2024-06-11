import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private TestEntityManager entityManager;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(1L);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(flowableElementType);
        entityManager.persist(nodeInstanceEntity2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, instanceType, flowableElementType);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        entityManager.persist(nodeInstanceEntity2);

        List<Long> ids = Arrays.asList(nodeInstanceEntity1.getId(), nodeInstanceEntity2.getId());
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity2);

        int deletedRows = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(2, deletedRows);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        entityManager.persist(nodeInstanceEntity2);

        int deletedRows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, deletedRows);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity2);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(2, nodeInstanceEntities.size());
    }

    @Test
    public void testFindAllWithPageable() {
        for (int i = 0; i < 20; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        Pageable pageable = PageRequest.of(0, 10);
        Page<NodeInstanceEntity> nodeInstanceEntityPage = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(10, nodeInstanceEntityPage.getContent().size());
        assertEquals(20, nodeInstanceEntityPage.getTotalElements());
    }

    @Test
    public void testFindAllWithSort() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        Sort sort = Sort.by("id").descending();
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(sort);
        assertEquals(10, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntities.get(0).getId(), 9L);
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);

        Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());
        assertTrue(optionalNodeInstanceEntity.isPresent());
        assertEquals(nodeInstanceEntity, optionalNodeInstanceEntity.get());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(1L);
        assertFalse(optionalNodeInstanceEntity.isPresent());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertNotNull(savedNodeInstanceEntity.getId());
        assertEquals(nodeInstanceEntity, savedNodeInstanceEntity);
    }

    @Test
    public void testSaveAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntities.add(nodeInstanceEntity);
            entityManager.persist(nodeInstanceEntity);
        }

        List<NodeInstanceEntity> savedNodeInstanceEntities = nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);
        assertEquals(10, savedNodeInstanceEntities.size());
        for (int i = 0; i < 10; i++) {
            assertNotNull(savedNodeInstanceEntities.get(i).getId());
            assertEquals(nodeInstanceEntities.get(i), savedNodeInstanceEntities.get(i));
        }
    }

    @Test
    public void testSaveAndFlush() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.saveAndFlush(nodeInstanceEntity);
        assertNotNull(savedNodeInstanceEntity.getId());

        Optional<NodeInstanceEntity> optionalSavedNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId());
        assertTrue(optionalSavedNodeInstanceEntity.isPresent());
        assertEquals(savedNodeInstanceEntity, optionalSavedNodeInstanceEntity.get());
    }

    @Test
    public void testCount() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        long count = nodeInstanceEntityRepository.count();
        assertEquals(10, count);
    }

    @Test
    public void testExistsById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);

        assertTrue(nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId()));
    }

    @Test
    public void testExistsByIdNotFound() {
        assertFalse(nodeInstanceEntityRepository.existsById(1L));
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);

        nodeInstanceEntityRepository.deleteById(nodeInstanceEntity.getId());

        Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());
        assertFalse(optionalNodeInstanceEntity.isPresent());
        assertNull(optionalNodeInstanceEntity.get());
    }

    @Test
    public void testDelete() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);

        nodeInstanceEntityRepository.delete(nodeInstanceEntity);

        Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());
        assertFalse(optionalNodeInstanceEntity.isPresent());
        assertNull(optionalNodeInstanceEntity.get());
    }

    @Test
    public void testDeleteAll() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        nodeInstanceEntityRepository.deleteAll();

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteAllWithIterable() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        nodeInstanceEntityRepository.deleteAll(nodeInstanceEntities);

        List<NodeInstanceEntity> remainingNodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, remainingNodeInstanceEntities.size());
    }

    @Test
    public void testDeleteAllWithIterableWithFlush() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        nodeInstanceEntityRepository.deleteAll(nodeInstanceEntities);
        entityManager.flush();

        List<NodeInstanceEntity> remainingNodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, remainingNodeInstanceEntities.size());
    }

    @Test
    public void testDeleteAllInBatch() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        nodeInstanceEntityRepository.deleteAllInBatch();

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteAllInBatchWithFlush() {
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            entityManager.persist(nodeInstanceEntity);
        }

        nodeInstanceEntityRepository.deleteAllInBatch();
        entityManager.flush();

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNull() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null, null);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIdsWithEmpty() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(Collections.emptyList());
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIdsWithNull() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(null);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testDeleteByFlowInstanceIdWithNull() {
        int deletedRows = nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
        assertEquals(0, deletedRows);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceTypeWithNull() {
        int deletedRows = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
        assertEquals(0, deletedRows);
    }
}