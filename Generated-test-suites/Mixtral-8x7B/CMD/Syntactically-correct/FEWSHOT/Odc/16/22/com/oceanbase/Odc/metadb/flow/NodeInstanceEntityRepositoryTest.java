import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, nodeInstances.size());
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
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(2, nodeInstances.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(3, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(1, result);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertNotNull(savedNodeInstanceEntity.getId());
 }

 @Test
 public void testUpdate() {
 NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(1L).get();
 nodeInstanceEntity.setInstanceType(FlowNodeType.END_EVENT);
 NodeInstanceEntity updatedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertEquals(FlowNodeType.END_EVENT, updatedNodeInstanceEntity.getInstanceType());
 }
}