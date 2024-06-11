
package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.LongStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    @DisplayName("Test findByInstanceIdAndInstanceTypeAndFlowableElementType")
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.USER_TASK;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

        assertEquals(1, result.size());
        assertEquals(instanceId, result.get(0).getInstanceId());
        assertEquals(instanceType.name(), result.get(0).getInstanceType());
        assertEquals(flowableElementType.name(), result.get(0).getFlowableElementType());
    }

    @Test
    @DisplayName("Test findByFlowInstanceId")
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(ni -> ni.getInstanceId().equals(1L)));
        assertTrue(result.stream().anyMatch(ni -> ni.getInstanceId().equals(2L)));
    }

    @Test
    @DisplayName("Test findByIds")
    public void testFindByIds() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(3, result.size());
        assertTrue(result.stream().anyMatch(ni -> ni.getId().equals(1L)));
        assertTrue(result.stream().anyMatch(ni -> ni.getId().equals(2L)));
        assertTrue(result.stream().anyMatch(ni -> ni.getId().equals(3L)));
    }

    // Additional test cases for other methods

}
