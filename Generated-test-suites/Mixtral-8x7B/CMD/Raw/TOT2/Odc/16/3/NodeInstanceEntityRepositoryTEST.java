import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.core.flow.model.FlowNodeType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository repository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 // Prepare test data
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;
 NodeInstanceEntity testEntity = new NodeInstanceEntity();
 testEntity.setInstanceId(instanceId);
 testEntity.setInstanceType(instanceType);
 testEntity.setFlowableElementType(flowableElementType);
 repository.save(testEntity);

 // Execute method under test
 List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
 instanceType, flowableElementType);

 // Assert results
 assertEquals(1, result.size());
 assertEquals(testEntity, result.get(0));
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_NoResults() {
 // Prepare test data (no entities with given parameters)
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.SERVICE_TASK;

 // Execute method under test
 List<NodeInstanceEntity> result = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId,
 instanceType, flowableElementType);

 // Assert results
 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindByFlowInstanceId() {
 // Prepare test data
 Long flowInstanceId = 1L;
 NodeInstanceEntity testEntity1 = new NodeInstanceEntity();
 testEntity1.setFlowInstanceId(flowInstanceId);
 NodeInstanceEntity testEntity2 = new NodeInstanceEntity();
 testEntity2.setFlowInstanceId(flowInstanceId);
 repository.save(testEntity1);
 repository.save(testEntity2);

 // Execute method under test
 List<NodeInstanceEntity> result = repository.findByFlowInstanceId(flowInstanceId);

 // Assert results
 assertEquals(2, result.size());
 }

 @Test
 public void testFindByIds() {
 // Prepare test data
 Long id1 = 1L;
 Long id2 = 2L;
 NodeInstanceEntity testEntity1 = new NodeInstanceEntity();
 testEntity1.setId(id1);
 NodeInstanceEntity testEntity2 = new NodeInstanceEntity();
 testEntity2.setId(id2);
 repository.save(testEntity1);
 repository.save(testEntity2);

 List<Long> ids = new ArrayList<>();
 ids.add(id1);
 ids.add(id2);

 // Execute method under test
 List<NodeInstanceEntity> result = repository.findByIds(ids);

 // Assert results
 assertEquals(2, result.size());
 assertTrue(result.contains(testEntity1));
 assertTrue(result.contains(testEntity2));
 }

 @Test
 public void testFindByIds_NoResults() {
 // Prepare test data (no entities with given IDs)
 Long id1 = 1L;
 Long id2 = 2L;
 List<Long> ids = new ArrayList<>();
 ids.add(id1);
 ids.add(id2);

 // Execute method under test
 List<NodeInstanceEntity> result = repository.findByIds(ids);

 // Assert results
 assertTrue(result.isEmpty());
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 // Prepare test data
 Long flowInstanceId = 1L;
 NodeInstanceEntity testEntity1 = new NodeInstanceEntity();
 testEntity1.setFlowInstanceId(flowInstanceId);
 NodeInstanceEntity testEntity2 = new NodeInstanceEntity();
 testEntity2.setFlowInstanceId(flowInstanceId);
 repository.save(testEntity1);
 repository.save(testEntity2);

 int rowsAffected = repository.deleteByFlowInstanceId(flowInstanceId);

 // Assert results
 assertEquals(2, rowsAffected);
 assertNull(repository.findById(testEntity1.getId()).orElse(null));
 assertNull(repository.findById(testEntity2.getId()).orElse(null));
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 // Prepare test data
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 NodeInstanceEntity testEntity1 = new NodeInstanceEntity();
 testEntity1.setInstanceId(instanceId);
 testEntity1.setInstanceType(instanceType);
 NodeInstanceEntity testEntity2 = new NodeInstanceEntity();
 testEntity2.setInstanceId(instanceId);
 testEntity2.setInstanceType(instanceType);
 repository.save(testEntity1);
 repository.save(testEntity2);

 int rowsAffected = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

 // Assert results
 assertEquals(2, rowsAffected);
 assertNull(repository.findById(testEntity1.getId()).orElse(null));
 assertNull(repository.findById(testEntity2.getId()).orElse(null));
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType_NoResults() {
 // Prepare test data (no entities with given ID and type)
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;

 int rowsAffected = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

 // Assert results
 assertEquals(0, rowsAffected);
 }

 @Test
 public void testSave() {
 // Prepare test data
 NodeInstanceEntity testEntity = new NodeInstanceEntity();

 // Execute method under test
 NodeInstanceEntity savedEntity = repository.save(testEntity);
 assertNotNull(savedEntity.getId());
 }

 @Test
 public void testFindAll() {
 // Execute method under test
 List<NodeInstanceEntity> result = repository.findAll();

 // Assert results
 assertFalse(result.isEmpty());
 }

 @Test
 public void testFindAll_Empty() {
 repository.deleteAll();

 // Execute method under test
 List<NodeInstanceEntity> result = repository.findAll();

 // Assert results
 assertTrue(result.isEmpty());
 }

 @Test
 public void testCount() {
 // Prepare test data
 NodeInstanceEntity testEntity1 = new NodeInstanceEntity();
 NodeInstanceEntity testEntity2 = new NodeInstanceEntity();
 repository.save(testEntity1);
 repository.save(testEntity2);

 long count = repository.count();

 // Assert results
 assertEquals(2, count);
 }

 @Test
 public void testExistsById() {
 // Prepare test data
 NodeInstanceEntity testEntity = new NodeInstanceEntity();
 repository.save(testEntity);

 boolean exists = repository.existsById(testEntity.getId());

 // Assert results
 assertTrue(exists);
 }

 @Test
 public void testExistsById_NotExists() {
 assertFalse(repository.existsById(1L));
 }

 @Test
 public void testDeleteById() {
 // Prepare test data
 NodeInstanceEntity testEntity = new NodeInstanceEntity();
 repository.save(testEntity);

 repository.deleteById(testEntity.getId());

 assertNull(repository.findById(testEntity.getId()).orElse(null));
 }

 @Test
 public void testDeleteAll() {
 // Prepare test data
 NodeInstanceEntity testEntity1 = new NodeInstanceEntity();
 NodeInstanceEntity testEntity2 = new NodeInstanceEntity();
 repository.save(testEntity1);
 repository.save(testEntity2);

 repository.deleteAll();

 assertTrue(repository.findAll().isEmpty());
 }

 @Test
 public void testFindById() {
 // Prepare test data
 NodeInstanceEntity testEntity = new NodeInstanceEntity();
 repository.save(testEntity);

 NodeInstanceEntity result = repository.findById(testEntity.getId()).orElse(null);

 // Assert results
 assertEquals(testEntity, result);
 }

 @Test
 public void testFindById_NotExists() {
 assertNull(repository.findById(1L).orElse(null));
 }

}