
package com.oceanbase.odc.metadb.flow;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow. NodeInstanceEntityRepository;

@RunWith(SpringJUnit4ClassRunner.class)
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
        // assert that the result is not empty and contains the expected NodeInstanceEntity objects
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.END_EVENT;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        // assert that the result is empty
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        // assert that the result contains the expected NodeInstanceEntity objects for the given flowInstanceId
        assertEquals(5, result.size());
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        Long flowInstanceId = 5L;
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        // assert that the result is empty
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        // assert that the result contains the expected NodeInstanceEntity objects for the given ids
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByIds_EmptyResult() {
        List<Long> ids = Arrays.asList(5L, 6L, 7L);
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
        // assert that the result is empty
        assertEquals(0, result.size());
    }

    @Test
    @Rollback(true)
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        // assert that the result is equal to the number of NodeInstanceEntity objects that were deleted
        // and that the database contains no NodeInstanceEntity objects with the given instanceId
        assertEquals(5, result);
        List<NodeInstanceEntity> result2 = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertEquals(0, result2.size());
    }

    @Test
    @Rollback(true)
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        // assert that the result is equal to the number of NodeInstanceEntity objects that were deleted
        // and that the database contains no NodeInstanceEntity objects with the given instanceId and instanceType
        assertEquals(2, result);
        List<NodeInstanceEntity> result2 = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(0, result2.size());
    }
}
