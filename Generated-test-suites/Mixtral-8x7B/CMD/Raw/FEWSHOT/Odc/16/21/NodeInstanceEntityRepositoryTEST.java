import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START, FlowableElementType.SERVICE);
 assertEquals(1, nodeInstances.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(!nodeInstances.isEmpty());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(!nodeInstances.isEmpty());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(1, result);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setInstanceId(1L);
 nodeInstance.setInstanceType(FlowNodeType.START);
 nodeInstance.setFlowableElementType(FlowableElementType.SERVICE);
 nodeInstance.setFlowInstanceId(1L);
 NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
 assertNotNull(savedNodeInstance.getId());
 }

}