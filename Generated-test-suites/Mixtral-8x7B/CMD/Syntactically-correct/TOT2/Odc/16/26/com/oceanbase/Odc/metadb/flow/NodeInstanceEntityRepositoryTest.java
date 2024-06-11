import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 // Test for JpaRepository.save(S s)
 @Test
 public void testSaveNodeInstanceEntity() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 assertNotNull(nodeInstanceEntity.getId());
 }

 // Test for JpaRepository.findOne(ID id)
 @Test
 public void testFindNodeInstanceEntity() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.findOne(nodeInstanceEntity.getId());
 assertEquals(nodeInstanceEntity, savedNodeInstanceEntity);
 }

 // Test for JpaRepository.findAll()
 @Test
 public void testFindAllNodeInstanceEntities() {
 Iterable<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
 assertFalse(nodeInstanceEntities.iterator().hasNext());
 }

 // Test for JpaRepository.count()
 @Test
 public void testCountNodeInstanceEntities() {
 long count = nodeInstanceEntityRepository.count();
 assertEquals(0, count);
 }

 // Test for JpaRepository.delete(ID id)
 @Test
 public void testDeleteNodeInstanceEntity() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntityRepository.delete(nodeInstanceEntity.getId());
 long count = nodeInstanceEntityRepository.count();
 assertEquals(0, count);
 }

 // Test for JpaRepository.exists(ID id)
 @Test
 public void testExistsNodeInstanceEntity() {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
 boolean exists = nodeInstanceEntityRepository.exists(nodeInstanceEntity.getId());
 assertTrue(exists);
 }

 // Test for JpaSpecificationExecutor.findAll(Specification<T> spec)
 @Test
 public void testFindAllNodeInstanceEntitiesWithSpecification() {
 }

 // Test for JpaSpecificationExecutor.find(Specification<T> spec, Pageable pageable)
 @Test
 public void testFindNodeInstanceEntitiesWithSpecificationAndPageable() {
 }

 // Test for JpaSpecificationExecutor.count(Specification<T> spec)
 @Test
 public void testCountNodeInstanceEntitiesWithSpecification() {
 }

 // Test for JpaSpecificationExecutor.findOne(Specification<T> spec)
 @Test
 public void testFindNodeInstanceEntityWithSpecification() {
 }

 // Test for custom findByInstanceIdAndInstanceTypeAndFlowableElementType method
 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(instanceId);
 nodeInstanceEntity.setInstanceType(instanceType.name());
 nodeInstanceEntity.setFlowableElementType(flowableElementType.name());
 nodeInstanceEntityRepository.save(nodeInstanceEntity);
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository
 .findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
 assertEquals(1, nodeInstanceEntities.size());
 assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
 }

 // Test for findByFlowInstanceId method
 @Test
 public void testFindByFlowInstanceId() {
 Long flowInstanceId = 1L;
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
 assertEquals(1, nodeInstanceEntities.size());
 assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
 }

 // Test for findByIds method
 @Test
 public void testFindByIds() {
 List<Long> ids = new ArrayList<>();
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setId(1L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity1);
 ids.add(1L);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setId(2L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity2);
 ids.add(2L);
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(2, nodeInstanceEntities.size());
 assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity1));
 assertTrue(nodeInstanceEntities.contains(nodeInstanceEntity2));
 }

 // Test for deleteByFlowInstanceId method
 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setFlowInstanceId(instanceId);
 nodeInstanceEntityRepository.save(nodeInstanceEntity1);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setFlowInstanceId(instanceId);
 nodeInstanceEntityRepository.save(nodeInstanceEntity2);
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
 assertEquals(2, rowsAffected);
 long count = nodeInstanceEntityRepository.count();
 assertEquals(0, count);
 }

 // Test for deleteByInstanceIdAndInstanceType method
 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setInstanceId(instanceId);
 nodeInstanceEntity1.setInstanceType(instanceType.name());
 nodeInstanceEntityRepository.save(nodeInstanceEntity1);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setInstanceId(instanceId);
 nodeInstanceEntity2.setInstanceType(instanceType.name());
 nodeInstanceEntityRepository.save(nodeInstanceEntity2);
 int rowsAffected = nodeInstanceEntityRepository
 .deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertEquals(2, rowsAffected);
 long count = nodeInstanceEntityRepository.count();
 assertEquals(0, count);
 }

}