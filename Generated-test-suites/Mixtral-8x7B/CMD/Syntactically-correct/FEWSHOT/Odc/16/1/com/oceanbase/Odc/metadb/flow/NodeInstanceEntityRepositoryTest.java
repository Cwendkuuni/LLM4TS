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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.NODE, FlowableElementType.CLUSTER_NODE);
 assertEquals(2, nodeInstanceEntities.size());
 for (NodeInstanceEntity nodeInstanceEntity : nodeInstanceEntities) {
 assertTrue(nodeInstanceEntity.getInstanceId().equals(1L)
 && nodeInstanceEntity.getInstanceType().equals(FlowNodeType.NODE)
 && nodeInstanceEntity.getFlowableElementType().equals(FlowableElementType.CLUSTER_NODE));
 }
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(3, nodeInstanceEntities.size());
 for (NodeInstanceEntity nodeInstanceEntity : nodeInstanceEntities) {
 assertTrue(nodeInstanceEntity.getFlowInstanceId().equals(1L));
 }
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, nodeInstanceEntities.size());
 for (Long id : ids) {
 assertTrue(nodeInstanceEntities.stream().anyMatch(e -> e.getId().equals(id)));
 }
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(3, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.NODE);
 assertEquals(2, rowsAffected);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.NODE);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.CLUSTER_NODE);
 nodeInstanceEntity.setFlowInstanceId(1L);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertNotNull(savedNodeInstanceEntity.getId());
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> nodeInstanceEntities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 assertTrue(nodeInstanceEntities.size() > 0);
 }

 @Test
 public void testCount() {
 Long count = nodeInstanceEntityRepository.count();
 assertTrue(count > 0);
 }

 @Test
 public void testExistsById() {
 Long id = 1L;
 assertTrue(nodeInstanceEntityRepository.existsById(id));
 }

 @Test
 public void testDeleteById() {
 Long id = 1L;
 nodeInstanceEntityRepository.deleteById(id);
 assertTrue(!nodeInstanceEntityRepository.existsById(id));
 }

 @Test
 public void testDeleteAll() {
 List<NodeInstanceEntity> nodeInstanceEntities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 nodeInstanceEntityRepository.deleteAll(nodeInstanceEntities);
 assertTrue(nodeInstanceEntityRepository.count() == 0);
 }

 @Test
 public void testSaveAll() {
 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 for (int i = 0; i < 5; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.NODE);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.CLUSTER_NODE);
 nodeInstanceEntity.setFlowInstanceId(1L);
 nodeInstanceEntities.add(nodeInstanceEntity);
 }
 nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);
 assertEquals(5, nodeInstanceEntityRepository.count());
 }

}