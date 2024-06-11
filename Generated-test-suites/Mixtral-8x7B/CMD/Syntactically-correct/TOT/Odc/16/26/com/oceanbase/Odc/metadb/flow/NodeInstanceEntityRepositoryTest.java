
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertFalse(result.isEmpty());
        assertEquals(instanceId, result.get(0).getInstanceId());
        assertEquals(instanceType.name(), result.get(0).getInstanceType());
        assertEquals(flowableElementType.name(), result.get(0).getFlowableElementType());
    }

    @Test
    @DisplayName("Test findByFlowInstanceId")
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertFalse(result.isEmpty());
        assertEquals(flowInstanceId, result.get(0).getFlowInstanceId());
    }

    @Test
    @DisplayName("Test deleteByFlowInstanceId")
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(flowInstanceId);

        assertEquals(3, result); // assuming there were 3 rows with the given flowInstanceId
    }
}
