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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        assertNotNull(nodeEntities);
        assertEquals(1, nodeEntities.size());

        NodeInstanceEntity nodeEntity = nodeEntities.get(0);
        assertEquals(instanceId, nodeEntity.getInstanceId());
        assertEquals(instanceType.name(), nodeEntity.getInstanceType());
        assertEquals(flowableElementType.name(), nodeEntity.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertNotNull(nodeEntities);
        assertEquals(2, nodeEntities.size());

        for (NodeInstanceEntity nodeEntity : nodeEntities) {
            assertEquals(flowInstanceId, nodeEntity.getFlowInstanceId());
        }
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        Collection<Long> ids = Arrays.asList(id1, id2);

        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertNotNull(nodeEntities);
        assertEquals(2, nodeEntities.size());

        for (NodeInstanceEntity nodeEntity : nodeEntities) {
            assertTrue(id1.equals(nodeEntity.getId()) || id2.equals(nodeEntity.getId()));
        }
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int initialCount = (int) nodeInstanceEntityRepository.count();

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(2, rowsAffected);
        assertEquals(initialCount - 2, (int) nodeInstanceEntityRepository.count());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        int initialCount = (int) nodeInstanceEntityRepository.count();

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(1, rowsAffected);
        assertEquals(initialCount - 1, (int) nodeInstanceEntityRepository.count());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findAll();

        assertNotNull(nodeEntities);
        assertEquals(2, nodeEntities.size());
    }

    @Test
    public void testFindAll_Page() {
        int page = 0;
        int size = 1;
        Pageable pageable = PageRequest.of(page, size);

        Page<NodeInstanceEntity> nodeEntitiesPage = nodeInstanceEntityRepository.findAll(pageable);

        assertNotNull(nodeEntitiesPage);
        assertEquals(1, nodeEntitiesPage.getContent().size());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
        nodeEntity.setInstanceId(3L);
        nodeEntity.setInstanceType(FlowNodeType.TASK.name());
        nodeEntity.setFlowableElementType(FlowableElementType.USER_TASK.name());

        NodeInstanceEntity savedNodeEntity = nodeInstanceEntityRepository.save(nodeEntity);

        assertNotNull(savedNodeEntity);
        assertNotSame(nodeEntity.getId(), savedNodeEntity.getId());

        NodeInstanceEntity foundNodeEntity = nodeInstanceEntityRepository.findById(savedNodeEntity.getId())
                .orElse(null);

        assertNotNull(foundNodeEntity);
        assertEquals(nodeEntity.getInstanceId(), foundNodeEntity.getInstanceId());
        assertEquals(nodeEntity.getInstanceType(), foundNodeEntity.getInstanceType());
        assertEquals(nodeEntity.getFlowableElementType(), foundNodeEntity.getFlowableElementType());
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        NodeInstanceEntity nodeEntity = nodeInstanceEntityRepository.findById(id).orElse(null);

        assertNotNull(nodeEntity);
        assertEquals(id, nodeEntity.getId());
    }

    @Test
    public void testFindById_NotFound() {
        Long id = 3L;

        NodeInstanceEntity nodeEntity = nodeInstanceEntityRepository.findById(id).orElse(null);

        assertNull(nodeEntity);
    }
}