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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.TASK, FlowableElementType.USER_TASK);
 assertEquals(2, entities.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(3, entities.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, entities.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.TASK);
 assertEquals(2, rowsAffected);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(4L);
 entity.setInstanceType(FlowNodeType.GATEWAY);
 entity.setFlowableElementType(FlowableElementType.EXCLUSIVE_GATEWAY);
 NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
 assertNotNull(savedEntity.getId());
 }

 @Test
 public void testUpdate() {
 NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(1L).orElseThrow(() -> new RuntimeException("Entity not found"));
 entity.setInstanceType(FlowNodeType.EVENT);
 NodeInstanceEntity updatedEntity = nodeInstanceEntityRepository.save(entity);
 assertNotNull(updatedEntity);
 assertEquals(FlowNodeType.EVENT, updatedEntity.getInstanceType());
 }

 @Test
 public void testDeleteById() {
 nodeInstanceEntityRepository.deleteById(1L);
 NodeInstanceEntity entity = nodeInstanceEntityRepository.findById(1L).orElse(null);
 assertNull(entity);
 }

 @Test
 public void testCount() {
 Long count = nodeInstanceEntityRepository.count();
 assertNotNull(count);
 }

 @Test
 public void testExistsById() {
 boolean exists = nodeInstanceEntityRepository.existsById(1L);
 assertTrue(exists);
 }

}