import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

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
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, entities.size());
 assertEquals(1L, entities.get(0).getId().longValue());
 entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.ACTIVITY, FlowableElementType.ACTIVITY);
 assertEquals(2, entities.size());
 entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
 assertEquals(1, entities.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(4, entities.size());
 }

 @Test
 public void testFindByIds() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(Arrays.asList(1L, 2L, 3L));
 assertEquals(3, entities.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(4L);
 assertEquals(2, entities.size());
 nodeInstanceEntityRepository.deleteByFlowInstanceId(4L);
 entities = nodeInstanceEntityRepository.findByFlowInstanceId(4L);
 assertTrue(entities.isEmpty());
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(1L,
 FlowNodeType.START_EVENT);
 assertEquals(1, entities.size());
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertTrue(entities.isEmpty());
 }

 @Test
 @Transactional
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(5L);
 entity.setInstanceType(FlowNodeType.BOUNDARY_EVENT);
 entity.setFlowableElementType(FlowableElementType.BOUNDARY_EVENT);
 entity = nodeInstanceEntityRepository.save(entity);
 assertNotNull(entity.getId());
 NodeInstanceEntity entity2 = new NodeInstanceEntity();
 entity2.setInstanceId(5L);
 entity2.setInstanceType(FlowNodeType.BOUNDARY_EVENT);
 entity2.setFlowableElementType(FlowableElementType.BOUNDARY_EVENT);
 entity2 = nodeInstanceEntityRepository.save(entity2);
 assertNotSame(entity.getId(), entity2.getId());
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> entities = new ArrayList<>(nodeInstanceEntityRepository.findAll());
 assertTrue(entities.size() > 0);
 }
}