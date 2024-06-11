import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.TASK, FlowableElementType.USER_TASK);
        assertTrue(nodeInstances.size() > 0);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertTrue(nodeInstances.size() > 0);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(nodeInstances.size() == 2);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(rowsAffected, 1);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
        assertEquals(rowsAffected, 1);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.TASK);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertNotNull(savedNodeInstanceEntity.getId());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstances = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
        assertTrue(nodeInstances.size() > 0);
    }

    @Test
    public void testFindAllOrderByInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Collections.orderBy("instanceId"));
        assertTrue(nodeInstances.size() > 0);
    }

    @Test
    public void testCount() {
        Long count = nodeInstanceEntityRepository.count();
        assertTrue(count > 0);
    }
}