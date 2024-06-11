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
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
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
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
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
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
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
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        entityManager.persist(nodeInstanceEntity2);
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(2, result);
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null));
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
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, result);
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId()).orElse(null));
        assertNull(nodeInstanceEntityRepository.findById(nodeInstanceEntity2.getId()).orElse(null));
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);
        NodeInstanceEntity savedNodeInstanceEntity = entityManager.find(NodeInstanceEntity.class,
                nodeInstanceEntity.getId());
        assertNotNull(savedNodeInstanceEntity);
        assertNotSame(nodeInstanceEntity, savedNodeInstanceEntity);
        assertEquals(nodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindAll() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity2);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testFindAllWithPageable() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity2);
        Pageable pageable = PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"));
        Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(1, page.getContent().size());
        assertEquals(nodeInstanceEntity1, page.getContent().get(0));
    }

    @Test
    public void testCount() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity2);
        long count = nodeInstanceEntityRepository.count();
        assertEquals(2, count);
    }

    @Test
    public void testExistsById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);
        boolean exists = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId());
        assertTrue(exists);
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId())
                .orElse(null);
        assertNotNull(savedNodeInstanceEntity);
        assertEquals(nodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindAllById() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity1);
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity2);
        List<Long> ids = Arrays.asList(nodeInstanceEntity1.getId(), nodeInstanceEntity2.getId());
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>(
                nodeInstanceEntityRepository.findAllById(ids));
        assertEquals(2, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, nodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, nodeInstanceEntities.get(1));
    }

    @Test
    public void testFlush() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        entityManager.persist(nodeInstanceEntity);
        entityManager.flush();
        NodeInstanceEntity savedNodeInstanceEntity = entityManager.find(NodeInstanceEntity.class,
                nodeInstanceEntity.getId());
        assertNotNull(savedNodeInstanceEntity);
        assertEquals(nodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(), savedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testSaveAll() {
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        List<NodeInstanceEntity> nodeInstanceEntities = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2);
        entityManager.persist(nodeInstanceEntity1);
        entityManager.persist(nodeInstanceEntity2);
        entityManager.flush();
        List<NodeInstanceEntity> savedNodeInstanceEntities = new ArrayList<>(
                nodeInstanceEntityRepository.saveAll(nodeInstanceEntities));
        assertEquals(2, savedNodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity1, savedNodeInstanceEntities.get(0));
        assertEquals(nodeInstanceEntity2, savedNodeInstanceEntities.get(1));
    }

}