import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertTrue(entities.size() > 0);
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(entities.size() > 0);
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(entities.size() == 3);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(result, 1);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(result, 1);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_EVENT);
 entity.setFlowableElementType(FlowableElementType.START_EVENT);
 entity.setFlowInstanceId(1L);

 NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
 assertNotNull(savedEntity.getId());
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> entities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 assertTrue(entities.size() > 0);
 }
}