import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

private NodeInstanceEntity nodeInstanceEntity1;
private NodeInstanceEntity nodeInstanceEntity2;
private NodeInstanceEntity nodeInstanceEntity3;

@Before
public void setup() {
nodeInstanceEntity1 = new NodeInstanceEntity();
nodeInstanceEntity1.setFlowInstanceId(1L);
nodeInstanceEntity1.setInstanceId(1L);
nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
nodeInstanceEntity1.setFlowableElementType(FlowableElementType.START_EVENT);

nodeInstanceEntity2 = new NodeInstanceEntity();
nodeInstanceEntity2.setFlowInstanceId(1L);
nodeInstanceEntity2.setInstanceId(2L);
nodeInstanceEntity2.setInstanceType(FlowNodeType.USER_TASK);
nodeInstanceEntity2.setFlowableElementType(FlowableElementType.USER_TASK);

nodeInstanceEntity3 = new NodeInstanceEntity();
nodeInstanceEntity3.setFlowInstanceId(2L);
nodeInstanceEntity3.setInstanceId(3L);
nodeInstanceEntity3.setInstanceType(FlowNodeType.END_EVENT);
nodeInstanceEntity3.setFlowableElementType(FlowableElementType.END_EVENT);

nodeInstanceEntityRepository.save(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
}

@After
public void teardown() {
nodeInstanceEntityRepository.deleteAll();
}

// Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
@Test
@Transactional
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
assertEquals(1, result.size());
assertEquals(nodeInstanceEntity1, result.get(0));

result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
assertEquals(1, result.size());
assertEquals(nodeInstanceEntity2, result.get(0));

result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L,
FlowNodeType.END_EVENT, FlowableElementType.END_EVENT);
assertEquals(1, result.size());
assertEquals(nodeInstanceEntity3, result.get(0));
}

// Test for findByFlowInstanceId method
@Test
@Transactional
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(2, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2), result);

result = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
assertEquals(1, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity3), result);
}

// Test for findByIds method
@Test
@Transactional
public void testFindByIds() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(3L);
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(2, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity3), result);
}

// Test for deleteByFlowInstanceId method
@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(2, rowsAffected);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(0, result.size());
}

// Test for deleteByInstanceIdAndInstanceType method
@Test
@Transactional
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = nodeInstanceEntityRepository
.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
assertEquals(1, rowsAffected);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(1L,
FlowNodeType.START_EVENT);
assertEquals(0, result.size());

result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(1, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity2), result);
}

// Test for save method
@Test
@Transactional
public void testSave() {
NodeInstanceEntity newNodeInstanceEntity = new NodeInstanceEntity();
newNodeInstanceEntity.setFlowInstanceId(1L);
newNodeInstanceEntity.setInstanceId(4L);
newNodeInstanceEntity.setInstanceType(FlowNodeType.SERVICE_TASK);
newNodeInstanceEntity.setFlowableElementType(FlowableElementType.SERVICE_TASK);

NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(newNodeInstanceEntity);

Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(savedNodeInstanceEntity.getId());
assertTrue(result.isPresent());
assertEquals(savedNodeInstanceEntity, result.get());
}

// Test for findById method
@Test
public void testFindById() {
Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId());
assertTrue(result.isPresent());
assertEquals(nodeInstanceEntity1, result.get());
}

// Test for findAll method
@Test
public void testFindAll() {
List<NodeInstanceEntity> result = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
assertEquals(3, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3), result);
}

// Test for deleteById method
@Test
@Transactional
public void testDeleteById() {
nodeInstanceEntityRepository.deleteById(nodeInstanceEntity1.getId());

Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId());
assertFalse(result.isPresent());
}

// Test for delete method
@Test
@Transactional
public void testDelete() {
nodeInstanceEntityRepository.delete(nodeInstanceEntity1);

Optional<NodeInstanceEntity> result = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId());
assertFalse(result.isPresent());
}

// Test for existsById method
@Test
public void testExistsById() {
assertTrue(nodeInstanceEntityRepository.existsById(nodeInstanceEntity1.getId()));
assertFalse(nodeInstanceEntityRepository.existsById(-1L));
}

// Test for count method
@Test
public void testCount() {
long count = nodeInstanceEntityRepository.count();
assertEquals(3, count);
}
}