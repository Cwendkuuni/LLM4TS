import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertTrue(nodeInstanceEntities.size() > 0);
    }

    // Test for edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullParameter() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    // Test for findByFlowInstanceId

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertTrue(nodeInstanceEntities.size() > 0);
    }

    // Test for edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testFindByFlowInstanceId_NullParameter() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    // Test for findByIds

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertTrue(nodeInstanceEntities.size() == ids.size());
    }

    // Test for edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testFindByIds_NullParameter() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    @Test
    public void testFindByIds_EmptyList() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(new ArrayList<>());
        assertTrue(nodeInstanceEntities.isEmpty());
    }

    // Test for deleteByFlowInstanceId

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertTrue(rowsAffected > 0);
    }

    // Test for edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testDeleteByFlowInstanceId_NullParameter() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    // Test for deleteByInstanceIdAndInstanceType

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertTrue(rowsAffected > 0);
    }

    // Test for edge cases and exceptions

    @Test(expected = NullPointerException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullParameter() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
    }
}