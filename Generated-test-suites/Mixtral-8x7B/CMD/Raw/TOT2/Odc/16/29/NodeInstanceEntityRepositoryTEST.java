import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 private List<NodeInstanceEntity> nodeEntities;

 @Before
 public void setup() {
 nodeEntities = new ArrayList<>();
 for (int i = 0; i < 10; i++) {
 NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
 nodeEntity.setInstanceId(i);
 nodeEntity.setInstanceType(FlowNodeType.START_EVENT);
 nodeEntity.setFlowableElementType(FlowableElementType.USER_TASK);
 nodeEntity.setFlowInstanceId(i);
 nodeEntities.add(nodeEntity);
 }
 nodeInstanceEntityRepository.saveAll(nodeEntities);
 }

 @After
 public void teardown() {
 nodeInstanceEntityRepository.deleteAll(nodeEntities);
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(5L,
 FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);
 assertEquals(1, result.size());
 assertEquals(5L, result.get(0).getInstanceId().longValue());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullValues() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
 null, null);
 assertEquals(nodeEntities.size(), result.size());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(5L);
 assertEquals(1, result.size());
 assertEquals(5L, result.get(0).getInstanceId().longValue());
 }

 @Test
 public void testFindByFlowInstanceIdWithNullValue() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(null);
 assertEquals(0, result.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 5L, 9L);
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, result.size());
 }

 @Test
 public void testFindByIdsWithEmptyCollection() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(new ArrayList<>());
 assertEquals(0, result.size());
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(5L);
 assertEquals(1, rowsAffected);
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceIdWithEmptyValues() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
 assertEquals(0, rowsAffected);
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(5L,
 FlowNodeType.START_EVENT);
 assertEquals(1, rowsAffected);
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceTypeWithEmptyValues() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
 assertEquals(0, rowsAffected);
 }

 @Test
 public void testFindAll() {
 Pageable pageable = PageRequest.of(0, 5);
 Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(pageable);
 assertEquals(5, result.getContent().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllWithNegativePageSize() {
 Pageable pageable = PageRequest.of(0, -5);
 nodeInstanceEntityRepository.findAll(pageable);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
 nodeEntity.setInstanceId(11L);
 nodeEntity.setInstanceType(FlowNodeType.END_EVENT);
 nodeEntity.setFlowableElementType(FlowableElementType.SERVICE_TASK);
 nodeEntity.setFlowInstanceId(11L);
 nodeInstanceEntityRepository.save(nodeEntity);
 NodeInstanceEntity savedNodeEntity = nodeInstanceEntityRepository.findByInstanceId(11L).get(0);
 assertEquals(nodeEntity.getInstanceId(), savedNodeEntity.getInstanceId());
 assertEquals(nodeEntity.getInstanceType(), savedNodeEntity.getInstanceType());
 assertEquals(nodeEntity.getFlowableElementType(), savedNodeEntity.getFlowableElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveWithNullEntity() {
 nodeInstanceEntityRepository.save(null);
 }

 @Test
 public void testSaveAll() {
 List<NodeInstanceEntity> entities = new ArrayList<>();
 entities.add(new NodeInstanceEntity(12L, FlowNodeType.GATEWAY, FlowableElementType.USER_TASK, 12L));
 entities.add(new NodeInstanceEntity(13L, FlowNodeType.BOUNDARY_EVENT, FlowableElementType.SERVICE_TASK, 13L));
 nodeInstanceEntityRepository.saveAll(entities);
 for (NodeInstanceEntity entity : entities) {
 NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.findByInstanceId(entity.getInstanceId()).get(0);
 assertEquals(entity.getInstanceId(), savedEntity.getInstanceId());
 assertEquals(entity.getInstanceType(), savedEntity.getInstanceType());
 assertEquals(entity.getFlowableElementType(), savedEntity.getFlowableElementType());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveAllWithNullCollection() {
 nodeInstanceEntityRepository.saveAll(null);
 }

}

class NodeInstanceEntityRepositoryTestConfig {
 // Nothing to configure in this case
}