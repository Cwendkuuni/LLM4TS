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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);
        assertNotNull(result);
        assertEquals(result.size(), 0);
        
        NodeInstanceEntity niEntity = new NodeInstanceEntity();
        niEntity.setInstanceId(instanceId);
        niEntity.setInstanceType(instanceType);
        niEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(niEntity);

        result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
                instanceType, flowableElementType);
        assertNotNull(result);
        assertEquals(result.size(), 1);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(niEntity2);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertNotNull(result);
        assertEquals(result.size(), 2);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Collection<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        assertNotNull(result);
        assertEquals(result.size(), 0);

        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setId(1L);
        nodeInstanceEntityRepository.save(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setId(2L);
        nodeInstanceEntityRepository.save(niEntity2);

        NodeInstanceEntity niEntity3 = new NodeInstanceEntity();
        niEntity3.setId(3L);
        nodeInstanceEntityRepository.save(niEntity3);

        result = nodeInstanceRepository.findByIds(ids);
        assertNotNull(result);
        assertEquals(result.size(), 3);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(niEntity2);

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(result, 2);

        NodeInstanceEntity niEntity3 = nodeInstanceEntityRepository.findById(1L).orElse(null);
        assertNull(niEntity3);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.TASK;
        NodeInstanceEntity niEntity1 = new NodeInstanceEntity();
        niEntity1.setInstanceId(instanceId);
        niEntity1.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(niEntity1);

        NodeInstanceEntity niEntity2 = new NodeInstanceEntity();
        niEntity2.setInstanceId(instanceId);
        niEntity2.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(niEntity2);

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(result, 2);

        NodeInstanceEntity niEntity3 = nodeInstanceEntityRepository.findById(1L).orElse(null);
        assertNull(niEntity3);
    }

}