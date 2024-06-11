import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, nodeInstances.size());
 assertEquals(1L, nodeInstances.get(0).getInstanceId().longValue());
 assertEquals(FlowNodeType.START_EVENT, nodeInstances.get(0).getInstanceType());
 assertEquals(FlowableElementType.START_EVENT, nodeInstances.get(0).getFlowableElementType());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(3, nodeInstances.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, nodeInstances.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(3, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.USER_TASK);
 assertEquals(2, rowsAffected);
 assertNull(nodeInstanceEntityRepository.findById(nodeInstances.get(0).getId()).orElse(null));
 assertNull(nodeInstanceEntityRepository.findById(nodeInstances.get(1).getId()).orElse(null));
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(4L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.END_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.END_EVENT);

 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertNotNull(savedNodeInstanceEntity.getId());
 }
}