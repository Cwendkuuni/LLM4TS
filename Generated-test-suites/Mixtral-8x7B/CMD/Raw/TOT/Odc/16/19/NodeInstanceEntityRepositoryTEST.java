import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(instanceId, result.get(0).getInstanceId());
        Assert.assertEquals(instanceType.name(), result.get(0).getInstanceType());
        Assert.assertEquals(flowableElementType.name(), result.get(0).getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(flowInstanceId, result.get(0).getFlowInstanceId());
        Assert.assertEquals(flowInstanceId, result.get(1).getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.contains(nodeInstanceEntityRepository.findOne(1L)));
        Assert.assertTrue(result.contains(nodeInstanceEntityRepository.findOne(2L)));
        Assert.assertTrue(result.contains(nodeInstanceEntityRepository.findOne(3L)));
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        Assert.assertEquals(2, result);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertEquals(1, result);
    }
}