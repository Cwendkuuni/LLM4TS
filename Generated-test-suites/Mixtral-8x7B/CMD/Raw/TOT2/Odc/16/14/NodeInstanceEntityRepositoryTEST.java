import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
 // Arrange
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.BOUNDARY_EVENT;

 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(instanceId);
 nodeInstanceEntity.setInstanceType(instanceType);
 nodeInstanceEntity.setFlowableElementType(flowableElementType);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Act
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);

 // Assert
 assertEquals(1, result.size());
 assertEquals(instanceId, result.get(0).getInstanceId());
 assertEquals(instanceType, result.get(0).getInstanceType());
 assertEquals(flowableElementType, result.get(0).getFlowableElementType());
 }

 @Test
 public void testFindByFlowInstanceId() {
 // Arrange
 Long flowInstanceId = 1L;
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setFlowInstanceId(flowInstanceId);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setFlowInstanceId(flowInstanceId);
 nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

 // Act
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

 // Assert
 assertEquals(2, result.size());
 }

 @Test
 public void testFindByIds() {
 // Arrange
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setId(1L);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setId(2L);
 NodeInstanceEntity nodeInstanceEntity3 = new NodeInstanceEntity();
 nodeInstanceEntity3.setId(3L);
 nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));

 // Act
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

 // Assert
 assertEquals(3, result.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Arrange
 Long instanceId = 1L;
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setFlowInstanceId(instanceId);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setFlowInstanceId(instanceId);
 nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

 // Act
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

 // Assert
 assertEquals(2, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 // Arrange
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setInstanceId(instanceId);
 nodeInstanceEntity1.setInstanceType(instanceType);
 NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setInstanceId(instanceId);
 nodeInstanceEntity2.setInstanceType(instanceType);
 nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));

 // Act
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

 // Assert
 assertEquals(2, result);
 }

 @Test
 public void testSave() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();

 // Act
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Assert
 Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());
 assertTrue(optionalNodeInstanceEntity.isPresent());
 }

 @Test
 public void testUpdate() {
 // Arrange
 Long id = 1L;
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setId(id);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 nodeInstanceEntity.setInstanceId(2L);

 // Act
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Assert
 Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(id);
 assertTrue(optionalNodeInstanceEntity.isPresent());
 assertEquals(Long.valueOf(2), optionalNodeInstanceEntity.get().getInstanceId());
 }

 @Test
 public void testFindAll() {
 // Arrange
 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 for (long i = 0; i < 10; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(i);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntities.add(nodeInstanceEntity);
 }

 // Act
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll();

 // Assert
 assertEquals(10, result.size());
 }

 @Test
 public void testFindById() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Act
 Optional<NodeInstanceEntity> optionalNodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId());

 // Assert
 assertTrue(optionalNodeInstanceEntity.isPresent());
 }

 @Test
 public void testCount() {
 // Arrange
 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 for (long i = 0; i < 10; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(i);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntities.add(nodeInstanceEntity);
 }

 // Act
 long result = nodeInstanceEntityRepository.count();

 // Assert
 assertEquals(10, result);
 }

 @Test
 public void testExistsById() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Act
 boolean result = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId());

 // Assert
 assertTrue(result);
 }

 @Test
 public void testDeleteById() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Act
 nodeInstanceEntityRepository.deleteById(nodeInstanceEntity.getId());

 // Assert
 boolean result = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId());
 assertFalse(result);
 }

 @Test
 public void testDeleteAll() {
 // Arrange
 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
 for (long i = 0; i < 10; i++) {
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(i);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);
 nodeInstanceEntities.add(nodeInstanceEntity);
 }

 // Act
 nodeInstanceEntityRepository.deleteAll(nodeInstanceEntities);

 // Assert
 assertEquals(0, nodeInstanceEntityRepository.count());
 }

 @Test
 public void testFlush() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Act
 nodeInstanceEntityRepository.flush();

 // Assert
 boolean result = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId());
 assertTrue(result);
 }

 @Test
 public void testDetach() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
 nodeInstanceEntity.setInstanceId(1L);
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Act
 nodeInstanceEntityRepository.detach(nodeInstanceEntity);

 // Assert
 boolean result = nodeInstanceEntityRepository.existsById(nodeInstanceEntity.getId());
 assertFalse(result);
 }
}