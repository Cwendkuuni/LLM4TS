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
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.ACTION);
 assertEquals(0, nodeEntities.size());

 NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
 nodeEntity.setInstanceId(1L);
 nodeEntity.setInstanceType(FlowNodeType.START);
 nodeEntity.setFlowableElementType(FlowableElementType.ACTION);
 nodeInstanceEntityRepository.save(nodeEntity);

 nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.ACTION);
 assertEquals(1, nodeEntities.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(0, nodeEntities.size());

 NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
 nodeEntity.setFlowInstanceId(1L);
 nodeInstanceEntityRepository.save(nodeEntity);

 nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(1, nodeEntities.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);

 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(0, nodeEntities.size());

 NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
 nodeEntity.setId(1L);
 nodeInstanceEntityRepository.save(nodeEntity);

 nodeEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(1, nodeEntities.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
 assertEquals(0, nodeEntities.size());
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, FlowableElementType.ACTION);
 assertEquals(0, nodeEntities.size());
 }
}