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
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<FlowNodeType> instanceTypes = Collections.singletonList(FlowNodeType.START);
 List<FlowableElementType> flowableElementTypes = Collections.singletonList(FlowableElementType.TASK);
 List<NodeInstanceEntity> expected = new ArrayList<>();
 // Add some test data to the expected list based on the specific requirements
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 instanceTypes.get(0), flowableElementTypes.get(0));
 assertEquals(expected, result);
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> expected = new ArrayList<>();
 // Add some test data to the expected list based on the specific requirements
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(expected, result);
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Collections.singletonList(1L);
 List<NodeInstanceEntity> expected = new ArrayList<>();
 // Add some test data to the expected list based on the specific requirements
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(expected, result);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(expectedNumberOfRowsDeleted, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(expectedNumberOfRowsDeleted, rowsAffected);
 }

 // Add more test methods as needed
}