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

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START, FlowableElementType.NODE);
 assertEquals(0, entities.size());

 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START);
 entity.setFlowableElementType(FlowableElementType.NODE);
 nodeInstanceEntityRepository.save(entity);

 entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START, FlowableElementType.NODE);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(0, entities.size());

 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setFlowInstanceId(1L);
 nodeInstanceEntityRepository.save(entity);

 entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByIds() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
 assertEquals(0, entities.size());

 NodeInstanceEntity entity1 = new NodeInstanceEntity();
 entity1.setId(1L);
 nodeInstanceEntityRepository.save(entity1);

 NodeInstanceEntity entity2 = new NodeInstanceEntity();
 entity2.setId(2L);
 nodeInstanceEntityRepository.save(entity2);

 List<NodeInstanceEntity> expectedEntities = Arrays.asList(entity1, entity2);
 entities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L));
 assertEquals(expectedEntities, entities);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
 assertEquals(0, entities.size());
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceId(instanceId);
 assertEquals(0, entities.size());
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setFlowInstanceId(1L);
 nodeInstanceEntityRepository.save(entity);
 assertNotNull(entity.getId());
 }

 @Test
 public void testUpdate() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(1L);
 entity.setFlowInstanceId(1L);
 nodeInstanceEntityRepository.save(entity);

 entity.setFlowInstanceId(2L);
 nodeInstanceEntityRepository.save(entity);
 assertEquals(Long.valueOf(2), nodeInstanceEntityRepository.findById(1L).get().getFlowInstanceId());
 }

 @Test
 public void testFindById() {
 Long id = 1L;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(id);
 nodeInstanceEntityRepository.save(entity);

 NodeInstanceEntity foundEntity = nodeInstanceEntityRepository.findById(id).get();
 assertEquals(entity, foundEntity);
 }

 @Test
 public void testFindByInstanceId() {
 Long instanceId = 1L;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(instanceId);
 nodeInstanceEntityRepository.save(entity);

 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceId(instanceId);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(instanceId);
 entity.setInstanceType(instanceType);
 nodeInstanceEntityRepository.save(entity);

 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(instanceId,
 instanceType);
 assertEquals(1, entities.size());
 }

 @Test
 public void testCount() {
 Long count = 1L;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(1L);
 nodeInstanceEntityRepository.save(entity);

 Long actualCount = nodeInstanceEntityRepository.count();
 assertEquals(count, actualCount);
 }

 @Test
 public void testExistsById() {
 Long id = 1L;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(id);
 nodeInstanceEntityRepository.save(entity);

 boolean exists = nodeInstanceEntityRepository.existsById(id);
 assertTrue(exists);
 }

 @Test
 public void testExistsByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(instanceId);
 entity.setInstanceType(instanceType);
 nodeInstanceEntityRepository.save(entity);

 boolean exists = nodeInstanceEntityRepository.existsByInstanceIdAndInstanceType(instanceId, instanceType);
 assertTrue(exists);
 }

 @Test
 public void testDeleteById() {
 Long id = 1L;
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(id);
 nodeInstanceEntityRepository.save(entity);

 nodeInstanceEntityRepository.deleteById(id);
 assertNull(nodeInstanceEntityRepository.findById(id).orElse(null));
 }

 @Test
 public void testDeleteAll() {
 List<NodeInstanceEntity> entities = new ArrayList<>();
 for (long i = 1; i <= 3; i++) {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setId(i);
 entities.add(entity);
 nodeInstanceEntityRepository.save(entity);
 }

 nodeInstanceEntityRepository.deleteAll();
 assertEquals(0, nodeInstanceEntityRepository.count());
 }

}