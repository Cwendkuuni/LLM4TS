import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setInstanceId(instanceId);
        node1.setInstanceType(instanceType);
        node1.setFlowableElementType(flowableElementType);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setInstanceId(instanceId);
        node2.setInstanceType(instanceType);
        node2.setFlowableElementType(FlowableElementType.GATEWAY);
        entityManager.persist(node2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertThat(result.get(0).getId()).isEqualTo(node1.getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setFlowInstanceId(flowInstanceId);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setFlowInstanceId(flowInstanceId);
        entityManager.persist(node2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setId(1L);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setId(2L);
        entityManager.persist(node2);

        NodeInstanceEntity node3 = new NodeInstanceEntity();
        node3.setId(3L);
        entityManager.persist(node3);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(3, result.size());
        assertThat(result).extracting(NodeInstanceEntity::getId)
                .containsExactlyInAnyOrderElementsOf(ids);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setFlowInstanceId(instanceId);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setFlowInstanceId(instanceId);
        entityManager.persist(node2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);

        assertEquals(0, result.size());
        assertEquals(2, rowsAffected);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setInstanceId(instanceId);
        node1.setInstanceType(instanceType);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setInstanceId(instanceId);
        node2.setInstanceType(instanceType);
        entityManager.persist(node2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceId(instanceId);

        assertEquals(0, result.size());
        assertEquals(2, rowsAffected);
    }

    @Test
    public void testFindByInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setInstanceId(instanceId);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setInstanceId(instanceId);
        entityManager.persist(node2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceId(instanceId);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> entities = Arrays.asList(
                new NodeInstanceEntity(),
                new NodeInstanceEntity(),
                new NodeInstanceEntity()
        );

        entities.forEach(entityManager::persist);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

        assertEquals(3, result.size());
    }

    @Test
    public void testCount() {
        List<NodeInstanceEntity> entities = Arrays.asList(
                new NodeInstanceEntity(),
                new NodeInstanceEntity(),
                new NodeInstanceEntity()
        );

        entities.forEach(entityManager::persist);

        Long count = nodeInstanceEntityRepository.count();

        assertEquals(3L, count);
    }

    @Test
    public void testExistsById() {
        Long id = 1L;

        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setId(id);
        entityManager.persist(entity);

        boolean exists = nodeInstanceEntityRepository.existsById(id);

        assertEquals(true, exists);
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entityManager.persist(entity);

        Long id = entity.getId();

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(id).orElse(null);

        assertEquals(entity, result);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();

        NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);

        Long id = savedEntity.getId();

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(id).orElse(null);

        assertEquals(entity, result);
    }

    @Test
    public void testDeleteById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entityManager.persist(entity);

        Long id = entity.getId();

        nodeInstanceEntityRepository.deleteById(id);

        NodeInstanceEntity result = nodeInstanceEntityRepository.findById(id).orElse(null);

        assertEquals(null, result);
    }

    @Test
    public void testFindOneByInstanceIdAndInstanceTypeAndFlowableElementTypeOrderByIdDesc() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;

        NodeInstanceEntity node1 = new NodeInstanceEntity();
        node1.setInstanceId(instanceId);
        node1.setInstanceType(instanceType);
        node1.setFlowableElementType(flowableElementType);
        entityManager.persist(node1);

        NodeInstanceEntity node2 = new NodeInstanceEntity();
        node2.setInstanceId(instanceId);
        node2.setInstanceType(instanceType);
        node2.setFlowableElementType(FlowableElementType.GATEWAY);
        entityManager.persist(node2);

        NodeInstanceEntity result = nodeInstanceEntityRepository.findOneByInstanceIdAndInstanceTypeAndFlowableElementTypeOrderByIdDesc(instanceId, instanceType, flowableElementType);

        assertEquals(node2.getId(), result.getId());
    }
}