import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);
assertEquals(1, nodeInstances.size());
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(3, nodeInstances.size());
}

@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(3, nodeInstances.size());
}

@Test
public void testDeleteByFlowInstanceId() {
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(3, rowsAffected);
}

@Test
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
assertEquals(1, rowsAffected);
}

@Test
@Transactional
public void testSave() {
NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
nodeInstance.setInstanceId(1L);
nodeInstance.setInstanceType(FlowNodeType.START);
nodeInstance.setFlowableElementType(FlowableElementType.TASK);
nodeInstance.setFlowInstanceId(1L);

NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
assertNotNull(savedNodeInstance.getId());

Optional<NodeInstanceEntity> optionalNodeInstance = nodeInstanceEntityRepository.findById(savedNodeInstance.getId());
assertTrue(optionalNodeInstance.isPresent());
assertEquals(savedNodeInstance, optionalNodeInstance.get());
}

@Test(expected = IllegalArgumentException.class)
public void testSaveNull() {
nodeInstanceEntityRepository.save(null);
}

@Test(expected = org.springframework.dao.DataIntegrityViolationException.class)
public void testSaveDuplicate() {
NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
nodeInstance.setInstanceId(1L);
nodeInstance.setInstanceType(FlowNodeType.START);
nodeInstance.setFlowableElementType(FlowableElementType.TASK);
nodeInstance.setFlowInstanceId(1L);

NodeInstanceEntity savedNodeInstance1 = nodeInstanceEntityRepository.save(nodeInstance);
NodeInstanceEntity savedNodeInstance2 = nodeInstanceEntityRepository.save(nodeInstance);
assertNotEquals(savedNodeInstance1.getId(), savedNodeInstance2.getId());
}

@Test
@Transactional
public void testUpdate() {
NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
nodeInstance.setInstanceId(1L);
nodeInstance.setInstanceType(FlowNodeType.START);
nodeInstance.setFlowableElementType(FlowableElementType.TASK);
nodeInstance.setFlowInstanceId(1L);

NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
assertNotNull(savedNodeInstance.getId());

savedNodeInstance.setInstanceId(2L);
savedNodeInstance.setInstanceType(FlowNodeType.END);

NodeInstanceEntity updatedNodeInstance = nodeInstanceEntityRepository.save(savedNodeInstance);
assertNotNull(updatedNodeInstance.getId());
assertEquals(savedNodeInstance, updatedNodeInstance);
}

@Test
@Transactional
public void testDelete() {
NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
nodeInstance.setInstanceId(1L);
nodeInstance.setInstanceType(FlowNodeType.START);
nodeInstance.setFlowableElementType(FlowableElementType.TASK);
nodeInstance.setFlowInstanceId(1L);

NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
assertNotNull(savedNodeInstance.getId());

nodeInstanceEntityRepository.deleteById(savedNodeInstance.getId());

Optional<NodeInstanceEntity> optionalNodeInstance = nodeInstanceEntityRepository.findById(savedNodeInstance.getId());
assertFalse(optionalNodeInstance.isPresent());
}

@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
public void testDeleteNonExistent() {
nodeInstanceEntityRepository.deleteById(1L);
}

@Test
public void testCount() {
long count = nodeInstanceEntityRepository.count();
assertTrue(count > 0);
}

@Test
public void testExistsById() {
Long id = 1L;
assertTrue(nodeInstanceEntityRepository.existsById(id));
}

@Test
public void testFindById() {
Optional<NodeInstanceEntity> optionalNodeInstance = nodeInstanceEntityRepository.findById(1L);
assertTrue(optionalNodeInstance.isPresent());
}

@Test
public void testFindByIdNotFound() {
Optional<NodeInstanceEntity> optionalNodeInstance = nodeInstanceEntityRepository.findById(100L);
assertFalse(optionalNodeInstance.isPresent());
}

}