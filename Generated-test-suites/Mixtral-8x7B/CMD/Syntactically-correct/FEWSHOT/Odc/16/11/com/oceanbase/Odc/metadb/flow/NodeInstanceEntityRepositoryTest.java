import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> expected = new ArrayList<>();
 // Populate the 'expected' list with mock NodeInstanceEntity objects based on the given parameters
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.START_EVENT;
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 assertEquals(expected, result);
 }

 @Test
 public void testFindByFlowInstanceId() {
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expected = new ArrayList<>();
 // Populate the 'expected' list with mock NodeInstanceEntity objects based on the given 'flowInstanceId'
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 assertEquals(expected, result);
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Collections.singletonList(1L);
 List<NodeInstanceEntity> expected = new ArrayList<>();
 // Populate the 'expected' list with mock NodeInstanceEntity objects based on the given 'ids'
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(expected, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int expected = 0;
 // Set the expected value based on the actual data in your test environment
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(expected, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 int expected = 0;
 // Set the expected value based on the actual data in your test environment
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(expected, result);
 }
}