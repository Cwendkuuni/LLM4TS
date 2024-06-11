import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START, FlowableElementType.TASK);
        assertEquals(1, nodeInstances.size());
        NodeInstanceEntity nodeInstance = nodeInstances.get(0);
        assertEquals(1L, nodeInstance.getInstanceId().longValue());
        assertEquals(FlowNodeType.START, nodeInstance.getInstanceType());
        assertEquals(FlowableElementType.TASK, nodeInstance.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(3, nodeInstances.size());
        NodeInstanceEntity nodeInstance1 = nodeInstances.get(0);
        assertEquals(1L, nodeInstance1.getFlowInstanceId().longValue());
        assertEquals(FlowNodeType.START, nodeInstance1.getInstanceType());
        assertEquals(FlowableElementType.TASK, nodeInstance1.getFlowableElementType());
        NodeInstanceEntity nodeInstance2 = nodeInstances.get(1);
        assertEquals(1L, nodeInstance2.getFlowInstanceId().longValue());
        assertEquals(FlowNodeType.END, nodeInstance2.getInstanceType());
        assertEquals(FlowableElementType.TASK, nodeInstance2.getFlowableElementType());
        NodeInstanceEntity nodeInstance3 = nodeInstances.get(2);
        assertEquals(1L, nodeInstance3.getFlowInstanceId().longValue());
        assertEquals(FlowNodeType.USER, nodeInstance3.getInstanceType());
        assertEquals(FlowableElementType.GATEWAY, nodeInstance3.getFlowableElementType());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, nodeInstances.size());
        assertEquals(1L, nodeInstances.get(0).getId().longValue());
        assertEquals(2L, nodeInstances.get(1).getId().longValue());
        assertEquals(3L, nodeInstances.get(2).getId().longValue());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(1, result);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START, FlowableElementType.TASK);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setInstanceId(1L);
        nodeInstance.setInstanceType(FlowNodeType.START);
        nodeInstance.setFlowableElementType(FlowableElementType.TASK);
        nodeInstance.setFlowInstanceId(1L);
        nodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
        assertNotNull(nodeInstance.getId());
        NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.findById(nodeInstance.getId()).get();
        assertEquals(nodeInstance, savedNodeInstance);
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll();
        assertEquals(3, nodeInstances.size());
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity nodeInstance = nodeInstanceEntityRepository.findById(1L).get();
        assertNotNull(nodeInstance);
        assertEquals(1L, nodeInstance.getId().longValue());
        assertEquals(FlowNodeType.START, nodeInstance.getInstanceType());
        assertEquals(FlowableElementType.TASK, nodeInstance.getFlowableElementType());
        assertEquals(1L, nodeInstance.getFlowInstanceId().longValue());
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity nodeInstance = nodeInstanceEntityRepository.findOne((long) 1);
        assertNotNull(nodeInstance);
        assertEquals(1L, nodeInstance.getId().longValue());
        assertEquals(FlowNodeType.START, nodeInstance.getInstanceType());
        assertEquals(FlowableElementType.TASK, nodeInstance.getFlowableElementType());
        assertEquals(1L, nodeInstance.getFlowInstanceId().longValue());
    }

    @Test
    public void testCount() {
        long count = nodeInstanceEntityRepository.count();
        assertEquals(3, count);
    }

    @Test
    public void testExistsById() {
        assertTrue(nodeInstanceEntityRepository.existsById(1L));
        assertFalse(nodeInstanceEntityRepository.existsById(4L));
    }

    @Test
    public void testDeleteById() {
        nodeInstanceEntityRepository.deleteById(1L);
        assertNull(nodeInstanceEntityRepository.findById(1L));
    }

    @Test
    public void testDeleteAll() {
        nodeInstanceEntityRepository.deleteAll();
        assertEquals(0, nodeInstanceEntityRepository.count());
    }

    @Test
    public void testFlush() {
        NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
        nodeInstance.setInstanceId(1L);
        nodeInstance.setInstanceType(FlowNodeType.START);
        nodeInstance.setFlowableElementType(FlowableElementType.TASK);
        nodeInstance.setFlowInstanceId(1L);
        nodeInstanceEntityRepository.save(nodeInstance);
        nodeInstanceEntityRepository.flush();
        NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.findById(nodeInstance.getId()).get();
        assertEquals(nodeInstance, savedNodeInstance);
    }

    @Test
    public void testSaveAll() {
        List<NodeInstanceEntity> nodeInstances = Arrays.asList(
                new NodeInstanceEntity(1L, FlowNodeType.START, FlowableElementType.TASK, 1L),
                new NodeInstanceEntity(2L, FlowNodeType.END, FlowableElementType.TASK, 1L),
                new NodeInstanceEntity(3L, FlowNodeType.USER, FlowableElementType.GATEWAY, 1L));
        nodeInstanceEntityRepository.saveAll(nodeInstances);
        nodeInstanceEntityRepository.flush();
        List<NodeInstanceEntity> savedNodeInstances = nodeInstanceEntityRepository.findAll();
        assertEquals(3, savedNodeInstances.size());
        assertEquals(nodeInstances, savedNodeInstances);
    }

    @Test
    public void testFindAllById() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAllById(ids);
        assertEquals(3, nodeInstances.size());
        assertEquals(1L, nodeInstances.get(0).getId().longValue());
        assertEquals(2L, nodeInstances.get(1).getId().longValue());
        assertEquals(3L, nodeInstances.get(2).getId().longValue());
    }

    @Test
    public void testCountById() {
        long count1 = nodeInstanceEntityRepository.countById(1L);
        long count2 = nodeInstanceEntityRepository.countById(4L);
        assertEquals(1, count1);
        assertEquals(0, count2);
    }

    @Test
    public void testExistsByIdNull() {
        assertFalse(nodeInstanceEntityRepository.existsById(null));
    }

    @Test
    public void testDeleteByIdNull() {
        nodeInstanceEntityRepository.deleteById(null);
    }

    @Test
    public void testFindAllNull() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll((Iterable<Long>) null);
        assertEquals(3, nodeInstances.size());
    }

    @Test
    public void testFindAllByIdNull() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAllById(null);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testFindAllNullEmpty() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Collections.emptyList());
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testFindAllNullEmptyList() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll((Iterable<Long>) Collections.emptyList());
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testFindAllByIdEmpty() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAllById(Collections.emptyList());
        assertEquals(0, nodeInstances.size());
    }

}