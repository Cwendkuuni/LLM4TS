import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

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
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.INTERMEDIATE_THROW_EVENT;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertTrue(nodeInstanceEntities.size() > 0);
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertTrue(nodeInstanceEntities.size() > 0);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);

        assertTrue(nodeInstanceEntities.size() == ids.size());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(result, 1);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(result, 1);
    }

    @Test
    public void testFindAll() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        PageRequest pageRequest = PageRequest.of(0, 10, sort);

        Page<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(pageRequest);

        assertTrue(nodeInstanceEntities.getTotalElements() > 0);
    }

    @Test
    public void testFindById() {
        Long id = 1L;

        Optional<NodeInstanceEntity> nodeInstanceEntity = nodeInstanceEntityRepository.findById(id);

        assertTrue(nodeInstanceEntity.isPresent());
    }

    @Test
    @Transactional
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(1L);
        nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity.setFlowableElementType(FlowableElementType.INTERMEDIATE_THROW_EVENT);

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);

        assertNotNull(savedNodeInstanceEntity.getId());
    }

    @Test
    @Transactional
    public void testDeleteById() {
        Long id = 1L;

        nodeInstanceEntityRepository.deleteById(id);

        Optional<NodeInstanceEntity> nodeInstanceEntity = nodeInstanceEntityRepository.findById(id);

        assertFalse(nodeInstanceEntity.isPresent());
    }
}