import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntity1.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntity2.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setId(2L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
        nodeInstanceEntity3.setId(3L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity3);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(3, result.size());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(2, result);

        List<NodeInstanceEntity> findResult = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertEquals(0, findResult.size());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(instanceId);
        nodeInstanceEntity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(instanceId);
        nodeInstanceEntity2.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(2, result);

        List<NodeInstanceEntity> findResult = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, FlowableElementType.TASK);
        assertEquals(0, findResult.size());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceEntity.setFlowInstanceId(1L);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotNull(savedNodeInstanceEntity.getId());
        assertEquals(nodeInstanceEntity.getInstanceId(), savedNodeInstanceEntity.getInstanceId());
        assertEquals(nodeInstanceEntity.getInstanceType(), savedNodeInstanceEntity.getInstanceType());
        assertEquals(nodeInstanceEntity.getFlowableElementType(),
                savedNodeInstanceEntity.getFlowableElementType());
        assertEquals(nodeInstanceEntity.getFlowInstanceId(), savedNodeInstanceEntity.getFlowInstanceId());
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceEntity.setFlowInstanceId(1L);
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        NodeInstanceEntity findNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
                .orElse(null);

        assertNotNull(findNodeInstanceEntity);
        assertEquals(savedNodeInstanceEntity.getInstanceId(), findNodeInstanceEntity.getInstanceId());
        assertEquals(savedNodeInstanceEntity.getInstanceType(), findNodeInstanceEntity.getInstanceType());
        assertEquals(savedNodeInstanceEntity.getFlowableElementType(),
                findNodeInstanceEntity.getFlowableElementType());
        assertEquals(savedNodeInstanceEntity.getFlowInstanceId(), findNodeInstanceEntity.getFlowInstanceId());
    }

    @Test
    @Transactional
    public void testDeleteById() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.TASK);
        nodeInstanceEntity.setFlowInstanceId(1L);
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntityRepository.deleteById(savedNodeInstanceEntity.getId());

        NodeInstanceEntity findNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
                .orElse(null);
        assertNull(findNodeInstanceEntity);
    }

}