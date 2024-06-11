import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jpa.TestEntityManager;
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
    private TestEntityManager entityManager;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        FlowNodeType instanceType = FlowNodeType.FLOW_NODE;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setInstanceId(1L);
        niEntity1.setInstanceType(instanceType);
        niEntity1.setFlowableElementType(flowableElementType);
        entityManager.persist(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setInstanceId(1L);
        niEntity2.setInstanceType(instanceType);
        niEntity2.setFlowableElementType(FlowableElementType.END_EVENT);
        entityManager.persist(niEntity2);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, instanceType,
                flowableElementType);

        assertEquals(1, resultList.size());
        assertEquals(niEntity1.getId(), resultList.get(0).getId());
    }

    @Test
    public void testFindByFlowInstanceId() {
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setFlowInstanceId(1L);
        entityManager.persist(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setFlowInstanceId(2L);
        entityManager.persist(niEntity2);

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(1, resultList.size());
        assertEquals(niEntity1.getId(), resultList.get(0).getId());
    }

    @Test
    public void testFindByIds() {
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setInstanceId(1L);
        entityManager.persist(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setInstanceId(2L);
        entityManager.persist(niEntity2);

        List<Long> ids = new ArrayList<>();
        ids.add(niEntity1.getId());
        ids.add(niEntity2.getId());

        List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, resultList.size());
        assertEquals(niEntity1.getId(), resultList.get(0).getId());
        assertEquals(niEntity2.getId(), resultList.get(1).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        NodeInstanceEntity niEntity = new NodeInstanceEntity();
        niEntity.setFlowInstanceId(1L);
        entityManager.persist(niEntity);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(1, rowsAffected);
        assertNull(entityManager.find(NodeInstanceEntity.class, niEntity.getId()));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setInstanceId(1L);
        niEntity1.setInstanceType(FlowNodeType.FLOW_NODE);
        entityManager.persist(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setInstanceId(1L);
        niEntity2.setInstanceType(FlowNodeType.TASK);
        entityManager.persist(niEntity2);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.FLOW_NODE);

        assertEquals(1, rowsAffected);
        assertNull(entityManager.find(NodeInstanceEntity.class, niEntity1.getId()));
        assertNotNull(entityManager.find(NodeInstanceEntity.class, niEntity2.getId()));
    }
}