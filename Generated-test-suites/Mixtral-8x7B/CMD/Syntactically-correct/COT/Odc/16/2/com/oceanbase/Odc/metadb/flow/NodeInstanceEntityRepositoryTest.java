import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.SERVICE_TASK);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(0, nodeInstances.size());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(0, result);
    }

    @Test
    public void testFindAll() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(pageRequest);
        assertEquals(0, nodeInstanceEntities.getContent().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNull() {
        nodeInstanceEntityRepository.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeNullParams() {
        nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByFlowInstanceIdNull() {
        nodeInstanceEntityRepository.findByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdsNull() {
        nodeInstanceEntityRepository.findByIds(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByFlowInstanceIdNull() {
        nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByInstanceIdAndInstanceTypeNull() {
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findOne(1L);
        assertEquals(null, nodeInstanceEntity);
    }

    @Test
    public void testExistsById() {
        boolean result = nodeInstanceEntityRepository.existsById(1L);
        assertEquals(false, result);
    }

    @Test
    public void testCount() {
        long result = nodeInstanceEntityRepository.count();
        assertEquals(0, result);
    }

    @Test
    public void testFindAllOrderByIdAsc() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>(
                nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindAllOrderByIdDesc() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>(
                nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "id")));
        assertEquals(0, nodeInstanceEntities.size());
    }
}