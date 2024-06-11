import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.PROCESS_NODE, FlowableElementType.START_EVENT);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, result.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS_NODE);
        assertEquals(2, result);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS_NODE);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        nodeInstanceEntity.setFlowInstanceId(1L);
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertEquals(1L, savedNodeInstanceEntity.getId().longValue());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();
        assertEquals(5, result.size());
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity result = nodeInstanceEntityRepository.findOne(1L);
        assertEquals(FlowNodeType.PROCESS_NODE, result.getInstanceType());
    }

    @Test
    public void testCount() {
        long result = nodeInstanceEntityRepository.count();
        assertEquals(5, result);
    }

    @Test
    public void testExists() {
        boolean result = nodeInstanceEntityRepository.exists(1L);
        assertEquals(true, result);
    }

    @Test
    public void testDelete() {
        nodeInstanceEntityRepository.delete(1L);
        long result = nodeInstanceEntityRepository.count();
        assertEquals(4, result);
    }

    @Test
    public void testDeleteAll() {
        nodeInstanceEntityRepository.deleteAll();
        long result = nodeInstanceEntityRepository.count();
        assertEquals(0, result);
    }

    @Test
    public void testFindAllByOrderByIdAsc() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(Collections.emptySort());
        assertEquals(5, result.size());
    }
}