import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START, FlowableElementType.START_EVENT);
 assertEquals(1, nodeInstances.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(3, nodeInstances.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, nodeInstances.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(3, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
 assertEquals(1, result);
 }

 @Test
 public void testCRUD() {
 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setInstanceId(1L);
 nodeInstance.setInstanceType(FlowNodeType.START);
 nodeInstance.setFlowInstanceId(1L);
 nodeInstance.setFlowableElementType(FlowableElementType.START_EVENT);

 nodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
 assertNotNull(nodeInstance.getId());

 NodeInstanceEntity nodeInstanceDB = nodeInstanceEntityRepository.findById(nodeInstance.getId()).get();
 assertEquals(nodeInstance, nodeInstanceDB);

 nodeInstanceDB.setInstanceId(2L);
 nodeInstanceEntityRepository.save(nodeInstanceDB);
 nodeInstanceDB = nodeInstanceEntityRepository.findById(nodeInstance.getId()).get();
 assertEquals(new Long(2), nodeInstanceDB.getInstanceId());

 nodeInstanceEntityRepository.deleteById(nodeInstance.getId());
 nodeInstanceDB = nodeInstanceEntityRepository.findById(nodeInstance.getId()).get();
 assertNull(nodeInstanceDB);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullId() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.START,
 FlowableElementType.START_EVENT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullInstanceType() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null,
 FlowableElementType.START_EVENT);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NullFlowableElementType() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByFlowInstanceId_NullId() {
 nodeInstanceEntityRepository.findByFlowInstanceId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIds_NullIds() {
 nodeInstanceEntityRepository.findByIds(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByFlowInstanceId_NullId() {
 nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByInstanceIdAndInstanceType_NullId() {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.START);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByInstanceIdAndInstanceType_NullInstanceType() {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSave_NullEntity() {
 nodeInstanceEntityRepository.save(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindById_NullId() {
 nodeInstanceEntityRepository.findById(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteById_NullId() {
 nodeInstanceEntityRepository.deleteById(null);
 }

}