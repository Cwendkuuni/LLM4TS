import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.SERVICE_TASK);
        assertEquals(2, entities.size());
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("ServiceTask1")));
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("ServiceTask2")));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(3, entities.size());
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("StartEvent")));
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("ServiceTask1")));
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("EndEvent")));
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, entities.size());
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("StartEvent")));
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("ServiceTask1")));
        assertTrue(entities.stream().anyMatch(e -> e.getNodeName().equals("EndEvent")));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity entity = new NodeInstanceEntity();
        entity.setInstanceId(4L);
        entity.setInstanceType(FlowNodeType.START);
        entity.setFlowInstanceId(1L);
        entity.setNodeName("TestStartEvent");
        entity.setFlowableElementType(FlowableElementType.START_EVENT);
        NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
        assertNotNull(savedEntity.getId());
        assertEquals("TestStartEvent", savedEntity.getNodeName());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> entities = new ArrayList<>(nodeInstanceEntityRepository.findAll());
        assertEquals(4, entities.size());
    }

}