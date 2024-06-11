import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 private List<NodeInstanceEntity> nodeInstanceEntities;

 @Before
 public void setup() {
 nodeInstanceEntities = new ArrayList<>();
 for (long i = 0; i < 10; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setFlowInstanceId(i);
 nodeInstanceEntity.setInstanceId(i + 1);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntities.add(nodeInstanceEntity);
 }
 }

 @After
 public void teardown() {
 for (NodeInstanceEntity nodeInstanceEntity : nodeInstanceEntities) {
 nodeInstanceEntityRepository.delete(nodeInstanceEntity);
 }
 nodeInstanceEntities.clear();
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> nodeInstanceEntities = this.nodeInstanceEntityRepository.findAll();
 assertEquals(10, nodeInstanceEntities.size());
 }

 @Test
 public void testFindById() {
 NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(1L).get();
 assertEquals(NodeInstanceEntityRepositoryTestConfig.FLOW_INSTANCE_ID_1, nodeInstanceEntity.getFlowInstanceId());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(1, nodeInstanceEntities.size());
 }

 @Test
 public void testFindByInstanceIdAndInstanceType() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository
 .findByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(1, nodeInstanceEntities.size());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 1L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);
 assertEquals(1, nodeInstanceEntities.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, nodeInstanceEntities.size());
 }

 @Test
 public void testCount() {
 long count = nodeInstanceEntityRepository.count();
 assertEquals(10, count);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L,
 FlowNodeType.START_EVENT);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testFindAllWithSort() {
 Sort sort = Sort.by(Sort.Direction.ASC, "flowInstanceId");
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(sort);
 for (int i = 0; i < nodeInstanceEntities.size(); i++) {
 NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntities.get(i);
 assertEquals(i, nodeInstanceEntity.getFlowInstanceId().longValue());
 }
 }

 @Test
 public void testFindAllWithPageRequest() {
 PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "flowInstanceId"));
 Page<NodeInstanceEntity> nodeInstanceEntitiesPage = nodeInstanceEntityRepository.findAll(pageRequest);
 assertEquals(5, nodeInstanceEntitiesPage.getContent().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeWithNullInstanceType() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(1L, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullInstanceType() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null,
 FlowableElementType.USER_TASK);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullFlowableElementType() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByInstanceIdAndInstanceTypeWithNullInstanceType() {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
 }

 @Test
 @Transactional
 public void testSave() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setFlowInstanceId(11L);
 nodeInstanceEntity.setInstanceId(12L);
 nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertEquals(11, savedNodeInstanceEntity.getFlowInstanceId().longValue());
 assertEquals(12, savedNodeInstanceEntity.getInstanceId().longValue());
 }

}