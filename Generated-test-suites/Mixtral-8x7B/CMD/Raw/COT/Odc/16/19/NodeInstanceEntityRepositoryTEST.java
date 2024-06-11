import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        // Add your assertions here
    }

    // Test for findByFlowInstanceId method
    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        // Add your assertions here
    }

    // Test for findByIds method
    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        // Add your assertions here
    }

    // Test for deleteByFlowInstanceId method
    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        // Add your assertions here
    }

    // Test for deleteByInstanceIdAndInstanceType method
    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        // Add your assertions here
    }

    // Helper method to create a NodeInstanceEntity list for pagination testing
    private List<NodeInstanceEntity> createNodeInstanceEntityList(int size) {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntity.setId(Long.valueOf(i));
            nodeInstanceEntities.add(nodeInstanceEntity);
        }
        return nodeInstanceEntities;
    }

    // Test for findAll method with pagination
    @Test
    public void testFindAllWithPagination() {
        int listSize = 10;
        List<NodeInstanceEntity> nodeInstanceEntities = createNodeInstanceEntityList(listSize);
        nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);

        Pageable pageable = PageRequest.of(0, 5);
        Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);

        List<NodeInstanceEntity> content = page.getContent();
        assertEquals(5, content.size());

        // Add more assertions for other pageable values
    }

    // Test for findAll method with Specification
    @Test
    public void testFindAllWithSpecification() {
        // Define your specification here

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(specification);
        // Add your assertions here
    }
}