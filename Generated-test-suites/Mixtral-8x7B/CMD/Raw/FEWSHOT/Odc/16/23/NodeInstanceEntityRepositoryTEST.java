import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
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
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.NODE, FlowableElementType.START_EVENT);
 assertEquals(0, nodeInstances.size());

 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setInstanceId(1L);
 nodeInstance.setInstanceType(FlowNodeType.NODE);
 nodeInstance.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceRepository.save(nodeInstance);

 nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.NODE, FlowableElementType.START_EVENT);
 assertEquals(1, nodeInstances.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(0, nodeInstances.size());

 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setFlowInstanceId(1L);
 nodeInstanceRepository.save(nodeInstance);

 nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(1, nodeInstances.size());
 }

 @Test
 public void testFindByIds() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(new ArrayList<Long>() {{
 add(1L);
 add(2L);
 }});
 assertEquals(0, nodeInstances.size());

 NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
 nodeInstance1.setId(1L);
 nodeInstanceRepository.save(nodeInstance1);

 NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
 nodeInstance2.setId(2L);
 nodeInstanceRepository.save(nodeInstance2);

 nodeInstances = nodeInstanceEntityRepository.findByIds(new ArrayList<Long>() {{
 add(1L);
 add(2L);
 }});
 assertEquals(2, nodeInstances.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;

 NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
 nodeInstance1.setFlowInstanceId(instanceId);
 nodeInstanceRepository.save(nodeInstance1);

 NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
 nodeInstance2.setFlowInstanceId(instanceId);
 nodeInstanceRepository.save(nodeInstance2);

 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(2, result);

 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
 assertEquals(0, nodeInstances.size());
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.NODE;

 NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
 nodeInstance1.setInstanceId(instanceId);
 nodeInstance1.setInstanceType(instanceType);
 nodeInstanceRepository.save(nodeInstance1);

 NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
 nodeInstance2.setInstanceId(instanceId);
 nodeInstance2.setInstanceType(instanceType);
 nodeInstanceRepository.save(nodeInstance2);

 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(2, result);

 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceId(instanceId);
 assertEquals(0, nodeInstances.size());
 }
}