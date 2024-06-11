import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository repository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.STANDARD;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        Assert.assertFalse(nodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByFlowInstanceId(flowInstanceId);
        Assert.assertFalse(nodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> nodeInstanceEntities = repository.findByIds(ids);
        Assert.assertFalse(nodeInstanceEntities.isEmpty());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowsAffected = repository.deleteByFlowInstanceId(instanceId);
        Assert.assertTrue(rowsAffected > 0);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.STANDARD;

        int rowsAffected = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        Assert.assertTrue(rowsAffected > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.STANDARD;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;
        FlowableElementType flowableElementType = FlowableElementType.START_EVENT;

        repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.STANDARD;
        FlowableElementType flowableElementType = null;

        repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceId_NullFlowInstanceId() {
        Long flowInstanceId = null;

        repository.findByFlowInstanceId(flowInstanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIds_NullIds() {
        Collection<Long> ids = null;

        repository.findByIds(ids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceId_NullInstanceId() {
        Long instanceId = null;

        repository.deleteByFlowInstanceId(instanceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullInstanceId() {
        Long instanceId = null;
        FlowNodeType instanceType = FlowNodeType.STANDARD;

        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceType_NullInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = null;

        repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntity.setFlowInstanceId(1L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.STANDARD);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);

        NodeInstanceEntity savedNodeInstanceEntity = repository.save(nodeInstanceEntity);
        Assert.assertNotNull(savedNodeInstanceEntity.getId());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>(repository.findAll());
        Assert.assertFalse(nodeInstanceEntities.isEmpty());
    }

    @Test
    public void testFindOne() {
        Long id = 1L;
        NodeInstanceEntity nodeInstanceEntity = repository.findOne(id);
        Assert.assertNotNull(nodeInstanceEntity);
    }

    @Test
    public void testExists() {
        Long id = 1L;
        boolean exists = repository.exists(id);
        Assert.assertTrue(exists);
    }

    @Test
    public void testCount() {
        long count = repository.count();
        Assert.assertTrue(count > 0);
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        repository.delete(id);
    }

    @Test
    public void testFindAllByInstanceId() {
        Long instanceId = 1L;
        List<NodeInstanceEntity> nodeInstanceEntities = repository.findAllByInstanceId(instanceId);
        Assert.assertFalse(nodeInstanceEntities.isEmpty());
    }
}