import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 private List<NodeInstanceEntity> nodeInstances;

 @Before
 public void setUp() {
 nodeInstances = new ArrayList<>();

 NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
 nodeInstance1.setInstanceId(1L);
 nodeInstance1.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstance1.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstance1.setFlowInstanceId(1L);
 nodeInstanceEntities.add(nodeInstance1);

 NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
 nodeInstance2.setInstanceId(2L);
 nodeInstance2.setInstanceType(FlowNodeType.END_EVENT);
 nodeInstance2.setFlowableElementType(FlowableElementType.END_EVENT);
 nodeInstance2.setFlowInstanceId(1L);
 nodeInstances.add(nodeInstance2);

 NodeInstanceEntity nodeInstance3 = new NodeInstanceEntity();
 nodeInstance3.setInstanceId(3L);
 nodeInstance3.setInstanceType(FlowNodeType.USER_TASK);
 nodeInstance3.setFlowableElementType(FlowableElementType.USER_TASK);
 nodeInstance3.setFlowInstanceId(2L);
 nodeInstances.add(nodeInstance3);

 nodeInstanceEntityRepository.saveAll(nodeInstances);
 }

 @After
 public void tearDown() {
 nodeInstanceEntityRepository.deleteAll();
 }

 // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);

 assertEquals(1, result.size());
 assertEquals(nodeInstances.get(0), result.get(0));
 }

 // Test for edge case: findByInstanceIdAndInstanceTypeAndFlowableElementType with non-existing values
 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeEdgeCase() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 99L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);

 assertEquals(0, result.size());
 }

 // Test for findByFlowInstanceId method
 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

 assertEquals(2, result.size());
 assertEquals(nodeInstances.subList(0, 2), result);
 }

 // Test for edge case: findByFlowInstanceId with non-existing instance id
 @Test
 public void testFindByFlowInstanceIdEdgeCase() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(99L);

 assertEquals(0, result.size());
 }

 // Test for findByIds method
 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L);
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

 assertEquals(3, result.size());
 assertEquals(nodeInstances, result);
 }

 // Test for edge case: findByIds with empty collection
 @Test
 public void testFindByIdsEdgeCase() {
 List<Long> ids = new ArrayList<>();
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

 assertEquals(0, result.size());
 }

 // Test for deleteByFlowInstanceId method
 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

 assertEquals(2, result);
 }

 // Test for edge case: deleteByFlowInstanceId with non-existing instance id
 @Test
 @Transactional
 public void testDeleteByFlowInstanceIdEdgeCase() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(99L);

 assertEquals(0, result);
 }

 // Test for deleteByInstanceIdAndInstanceType method
 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

 assertEquals(1, result);
 }

 // Test for edge case: deleteByInstanceIdAndInstanceType with non-existing values
 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceTypeEdgeCase() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(99L, FlowNodeType.START_EVENT);

 assertEquals(0, result);
 }
}