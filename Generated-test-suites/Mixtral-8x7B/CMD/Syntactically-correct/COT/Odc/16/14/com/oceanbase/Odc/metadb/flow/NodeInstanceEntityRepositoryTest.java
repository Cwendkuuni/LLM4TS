import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.UnexpectedRollbackException;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
                FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
        assertEquals(1, nodeInstances.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(3, nodeInstances.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstances.size());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(3, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
        assertEquals(1, result);
    }

    @Test
    public void testFindAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(3, page.getContent().size());
    }

    @Test
    public void testFindById() {
        NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(1L).orElse(null);
        assertNotNull(nodeInstanceEntity);
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(4L);
        nodeInstanceEntity.setFlowInstanceId(2L);
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
        assertEquals(4, savedNodeInstanceEntity.getId().longValue());
    }

    @Test
    public void testDeleteById() {
        nodeInstanceEntityRepository.deleteById(1L);
        assertThrows(UnexpectedRollbackException.class, () -> nodeInstanceEntityRepository.findById(1L).orElse(null));
    }

    @Test
    public void testCount() {
        Long count = nodeInstanceEntityRepository.count();
        assertEquals(3, count.longValue());
    }

    @Test
    public void testExistsById() {
        boolean exists = nodeInstanceEntityRepository.existsById(1L);
        assertEquals(true, exists);
    }

    @Test
    public void testFindAllOrderByFlowInstanceIdAsc() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.ASC, "flowInstanceId"));
        assertEquals(3, nodeInstances.size());
        assertEquals(1L, nodeInstances.get(0).getFlowInstanceId().longValue());
    }

    @Test
    public void testFindAllOrderByFlowInstanceIdDesc() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "flowInstanceId"));
        assertEquals(3, nodeInstances.size());
        assertEquals(3L, nodeInstances.get(0).getFlowInstanceId().longValue());
    }

    @Test
    public void testFindAllOrderByInstanceIdAsc() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.ASC, "instanceId"));
        assertEquals(3, nodeInstances.size());
        assertEquals(1L, nodeInstances.get(0).getInstanceId().longValue());
    }

    @Test
    public void testFindAllOrderByInstanceIdDesc() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "instanceId"));
        assertEquals(3, nodeInstances.size());
        assertEquals(3L, nodeInstances.get(0).getInstanceId().longValue());
    }

    @Test
    public void testFindAllOrderByFlowableElementTypeAsc() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.ASC, "flowableElementType"));
        assertEquals(3, nodeInstances.size());
        assertEquals(FlowableElementType.END_EVENT, nodeInstances.get(0).getFlowableElementType());
    }

    @Test
    public void testFindAllOrderByFlowableElementTypeDesc() {
        List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "flowableElementType"));
        assertEquals(3, nodeInstances.size());
        assertEquals(FlowableElementType.START_EVENT, nodeInstances.get(0).getFlowableElementType());
    }
}

class NodeInstanceEntityRepositoryTestConfig {

    @Bean
    public NodeInstanceEntityRepository nodeInstanceEntityRepository() {
        return new NodeInstanceEntityRepository() {
            // Add any necessary mock implementations for the methods in the interface
        };
    }

}