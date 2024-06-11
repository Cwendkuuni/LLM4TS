import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        entity1.setInstanceType(FlowNodeType.ACTIVITY);
        entity1.setFlowableElementType(FlowableElementType.TASK);
        entity1.setFlowInstanceId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(1L);
        entity2.setInstanceType(FlowNodeType.ACTIVITY);
        entity2.setFlowableElementType(FlowableElementType.GATEWAY);
        entity2.setFlowInstanceId(1L);
        entityManager.persist(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.ACTIVITY, FlowableElementType.TASK);
        assertEquals(1, result.size());
        assertEquals(entity1, result.get(0));

        result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.ACTIVITY, FlowableElementType.GATEWAY);
        assertEquals(1, result.size());
        assertEquals(entity2, result.get(0));

        result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                2L, FlowNodeType.ACTIVITY, FlowableElementType.TASK);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        entity1.setInstanceType(FlowNodeType.ACTIVITY);
        entity1.setFlowableElementType(FlowableElementType.TASK);
        entity1.setFlowInstanceId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(2L);
        entity2.setInstanceType(FlowNodeType.ACTIVITY);
        entity2.setFlowableElementType(FlowableElementType.TASK);
        entity2.setFlowInstanceId(1L);
        entityManager.persist(entity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(2, result.size());

        result = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        entity1.setInstanceType(FlowNodeType.ACTIVITY);
        entity1.setFlowableElementType(FlowableElementType.TASK);
        entity1.setFlowInstanceId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(2L);
        entity2.setInstanceType(FlowNodeType.ACTIVITY);
        entity2.setFlowableElementType(FlowableElementType.TASK);
        entity2.setFlowInstanceId(2L);
        entityManager.persist(entity2);

        List<Long> ids = Arrays.asList(1L, 2L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, result.size());

        ids = Collections.singletonList(3L);
        result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(0, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        entity1.setInstanceType(FlowNodeType.ACTIVITY);
        entity1.setFlowableElementType(FlowableElementType.TASK);
        entity1.setFlowInstanceId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(2L);
        entity2.setInstanceType(FlowNodeType.ACTIVITY);
        entity2.setFlowableElementType(FlowableElementType.TASK);
        entity2.setFlowInstanceId(1L);
        entityManager.persist(entity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, result);

        result = nodeInstanceEntityRepository.deleteByFlowInstanceId(2L);
        assertEquals(0, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity entity1 = new NodeInstanceEntity();
        entity1.setInstanceId(1L);
        entity1.setInstanceType(FlowNodeType.ACTIVITY);
        entity1.setFlowableElementType(FlowableElementType.TASK);
        entity1.setFlowInstanceId(1L);
        entityManager.persist(entity1);

        NodeInstanceEntity entity2 = new NodeInstanceEntity();
        entity2.setInstanceId(1L);
        entity2.setInstanceType(FlowNodeType.GATEWAY);
        entity2.setFlowableElementType(FlowableElementType.TASK);
        entity2.setFlowInstanceId(2L);
        entityManager.persist(entity2);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.ACTIVITY);
        assertEquals(1, result);

        result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.GATEWAY);
        assertEquals(1, result);

        result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.ACTIVITY);
        assertEquals(0, result);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.ACTIVITY);
        entity.setFlowableElementType(FlowableElementType.TASK);
        entity.setFlowInstanceId(1L);
        NodeInstanceEntity savedEntity = entityManager.persist(entity);
        assertNotNull(savedEntity.getId());

        NodeInstanceEntity loadedEntity = entityManager.find(NodeInstanceEntity.class, savedEntity.getId());
        assertEquals(savedEntity, loadedEntity);
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(1L);
        entity.setInstanceType(FlowNodeType.ACTIVITY);
        entity.setFlowableElementType(FlowableElementType.TASK);
        entity.setFlowInstanceId(1L);
        NodeInstanceEntity savedEntity = entityManager.persist(entity);
        assertNotNull(savedEntity.getId());

        NodeInstanceEntity loadedEntity = nodeInstanceEntityRepository.findById(savedEntity.getId()).orElse(null);
        assertEquals(savedEntity, loadedEntity);

        loadedEntity = nodeInstanceEntityRepository.findById(2L).orElse(null);
        assertNull(loadedEntity);
    }
}