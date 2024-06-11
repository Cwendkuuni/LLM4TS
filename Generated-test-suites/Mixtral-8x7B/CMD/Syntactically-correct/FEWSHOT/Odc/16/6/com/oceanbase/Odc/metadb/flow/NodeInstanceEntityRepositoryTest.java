import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, nodeInstances.size());
 assertEquals(FlowNodeType.START_EVENT, nodeInstances.get(0).getInstanceType());
 assertEquals(FlowableElementType.START_EVENT, nodeInstances.get(0).getFlowableElementType());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(nodeInstances.size() > 0);
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
 long result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 long result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(1, result);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertNotNull(savedNodeInstanceEntity.getId());
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll();
 assertTrue(nodeInstances.size() > 0);
 }

 @Test
 public void testFindById() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 NodeInstanceEntity foundNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
 .orElse(null);
 assertEquals(savedNodeInstanceEntity, foundNodeInstanceEntity);
 }

 @Test
 public void testCount() {
 long count = nodeInstanceEntityRepository.count();
 assertTrue(count > 0);
 }

 @Test
 public void testExistsById() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertTrue(nodeInstanceEntityRepository.existsById(savedNodeInstanceEntity.getId()));
 }

 @Test
 public void testDeleteById() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntityRepository.deleteById(savedNodeInstanceEntity.getId());
 assertTrue(!nodeInstanceEntityRepository.existsById(savedNodeInstanceEntity.getId()));
 }

 @Test
 public void testDeleteAll() {
 List<NodeInstanceEntity> nodeInstances = new ArrayList<>();
 for (long i = 0; i < 10; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(i);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 nodeInstances.add(nodeInstanceEntity);
 }
 nodeInstanceEntityRepository.saveAll(nodeInstances);
 nodeInstanceEntityRepository.deleteAll();
 assertTrue(nodeInstanceEntityRepository.count() == 0);
 }

 @Test
 public void testSaveAll() {
 List<NodeInstanceEntity> nodeInstances = new ArrayList<>();
 for (long i = 0; i < 10; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(i);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 nodeInstances.add(nodeInstanceEntity);
 }
 List<NodeInstanceEntity> savedNodeInstances = nodeInstanceEntityRepository.saveAll(nodeInstances);
 assertEquals(10, savedNodeInstances.size());
 }

 @Test
 public void testFlush() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntityRepository.flush();
 NodeInstanceEntity foundNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
 .orElse(null);
 assertEquals(savedNodeInstanceEntity, foundNodeInstanceEntity);
 }

 @Test
 public void testSaveAndFlush() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstanceEntity.setVersion(0L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.saveAndFlush(nodeInstanceEntity);
 NodeInstanceEntity foundNodeInstanceEntity = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId())
 .orElse(null);
 assertEquals(savedNodeInstanceEntity, foundNodeInstanceEntity);
 }

}