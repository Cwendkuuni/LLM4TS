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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 // Arrange
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
 List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
 expectedNodes.add(new NodeInstanceEntity());

 // Act
 List<NodeInstanceEntity> resultNodes = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 instanceId, instanceType, flowableElementType);

 // Assert
 assertEquals(expectedNodes, resultNodes);
 }

 @Test
 public void testFindByFlowInstanceId() {
 // Arrange
 Long flowInstanceId = 1L;
 List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
 expectedNodes.add(new NodeInstanceEntity());

 // Act
 List<NodeInstanceEntity> resultNodes = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);

 // Assert
 assertEquals(expectedNodes, resultNodes);
 }

 @Test
 public void testFindByIds() {
 // Arrange
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
 expectedNodes.add(new NodeInstanceEntity());

 // Act
 List<NodeInstanceEntity> resultNodes = nodeInstanceEntityRepository.findByIds(ids);

 // Assert
 assertEquals(expectedNodes, resultNodes);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 // Arrange
 Long instanceId = 1L;

 // Act
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);

 // Assert
 assertEquals(1, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 // Arrange
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;

 // Act
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);

 // Assert
 assertEquals(1, result);
 }

 @Test
 public void testFindAll() {
 // Arrange
 List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
 expectedNodes.add(new NodeInstanceEntity());

 // Act
 List<NodeInstanceEntity> resultNodes = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();

 // Assert
 assertEquals(expectedNodes, resultNodes);
 }

 @Test
 public void testFindAllWithPageable() {
 // Arrange
 Pageable pageable = PageRequest.of(0, 10);
 List<NodeInstanceEntity> expectedNodes = new ArrayList<>();
 expectedNodes.add(new NodeInstanceEntity());

 // Act
 Page<NodeInstanceEntity> resultNodes = nodeInstanceEntityRepository.findAll(pageable);

 // Assert
 assertEquals(expectedNodes, resultNodes.getContent());
 }

 @Test
 public void testExistsById() {
 // Arrange
 Long id = 1L;

 // Act
 boolean result = nodeInstanceEntityRepository.existsById(id);

 // Assert
 assertTrue(result);
 }

 @Test
 public void testCount() {
 // Arrange

 // Act
 long result = nodeInstanceEntityRepository.count();

 // Assert
 assertEquals(0, result);
 }

 @Test
 public void testSave() {
 // Arrange
 NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();

 // Act
 nodeInstanceEntityRepository.save(nodeInstanceEntity);

 // Assert
 assertNotNull(nodeInstanceEntity.getId());
 }

 @Test
 public void testFindById() {
 // Arrange
 Long id = 1L;
 NodeInstanceEntity expectedNode = new NodeInstanceEntity();

 // Act
 NodeInstanceEntity resultNode = nodeInstanceEntityRepository.findById(id).orElse(null);

 // Assert
 assertEquals(expectedNode, resultNode);
 }
}