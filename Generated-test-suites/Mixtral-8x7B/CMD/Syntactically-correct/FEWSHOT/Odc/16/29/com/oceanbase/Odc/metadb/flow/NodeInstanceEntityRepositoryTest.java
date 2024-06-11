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
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_NODE, FlowableElementType.START_EVENT);
 assertEquals(1, entities.size());
 assertEquals(1L, entities.get(0).getInstanceId().longValue());
 assertEquals(FlowNodeType.START_NODE, entities.get(0).getInstanceType());
 assertEquals(FlowableElementType.START_EVENT, entities.get(0).getFlowableElementType());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(!entities.isEmpty());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(!entities.isEmpty());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_NODE;
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_NODE);
 entity.setFlowableElementType(FlowableElementType.START_EVENT);
 entity = nodeInstanceEntityRepository.save(entity);
 assertNotNull(entity.getId());
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findAll();
 assertTrue(!entities.isEmpty());
 }

 @Test
 public void testCount() {
 Long count = nodeInstanceEntityRepository.count();
 assertTrue(count > 0);
 }

 @Test
 public void testExistsById() {
 Long id = 1L;
 boolean exists = nodeInstanceEntityRepository.existsById(id);
 assertTrue(exists);
 }

 @Test
 public void testFindById() {
 Long id = 1L;
 NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(id).get();
 assertNotNull(entity);
 }

 @Test
 public void testSaveAll() {
 List<NodeInstanceEntity> entities = new ArrayList<>();
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_NODE);
 entity.setFlowableElementType(FlowableElementType.START_EVENT);
 entities.add(entity);
 nodeInstanceEntityRepository.saveAll(entities);
 assertTrue(!entities.isEmpty());
 }

 @Test
 public void testDeleteById() {
 Long id = 1L;
 nodeInstanceEntityRepository.deleteById(id);
 }

 @Test
 public void testDeleteAll() {
 nodeInstanceEntityRepository.deleteAll();
 Long count = nodeInstanceEntityRepository.count();
 assertTrue(count == 0);
 }
}