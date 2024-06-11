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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                instanceId, instanceType, flowableElementType);

        // Add your assertion logic here
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        // Add your assertion logic here
    }

    @Test
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByIds(ids);

        // Add your assertion logic here
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

        assertEquals(rowsAffected, 1); // Update this assertion according to your expectation
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

        assertEquals(rowsAffected, 1); // Update this assertion according to your expectation
    }

    // Add more test methods for other methods if required
}

@Configuration
class NodeInstanceEntityRepositoryTestConfig {

    // Add your configuration here, such as @Bean definitions
}