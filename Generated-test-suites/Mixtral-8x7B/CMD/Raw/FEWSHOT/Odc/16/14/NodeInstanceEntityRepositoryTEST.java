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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, nodeEntities.size());
 assertEquals(FlowNodeType.START_EVENT, nodeEntities.get(0).getInstanceType());
 assertEquals(FlowableElementType.START_EVENT, nodeEntities.get(0).getFlowableElementType());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(!nodeEntities.isEmpty());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(!nodeEntities.isEmpty());
 assertEquals(3, nodeEntities.size());
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int count = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(3, count);
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int count = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(1, count);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
 nodeEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeEntity.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeEntity.setFlowInstanceId(1L);
 nodeEntity.setInstanceId(1L);

 NodeInstanceEntity savedNodeEntity = nodeInstanceEntityRepository.save(nodeEntity);
 assertNotNull(savedNodeEntity.getId());
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> nodeEntities = new ArrayList<>(nodeInstanceEntityRepository.findAll());
 assertTrue(!nodeEntities.isEmpty());
 }
}