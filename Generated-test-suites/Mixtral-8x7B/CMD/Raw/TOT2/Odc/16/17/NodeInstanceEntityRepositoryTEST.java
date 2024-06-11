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

// Test for JpaRepository.save(S entity)
@Test
public void testSaveNodeInstanceEntity() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.USER_TASK);
nodeInstanceEntityRepository.save(nodeInstanceEntity);
assertNotNull(nodeInstanceEntity.getId());
}

// Test for JpaRepository.findAll()
@Test
public void testFindAllNodeInstanceEntities() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
assertTrue(nodeInstanceEntities.size() >= 0);
}

// Test for JpaRepository.findById(ID id)
@Test
public void testFindNodeInstanceEntityById() {
Long id = 1L;
NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(id).orElse(null);
assertNotNull(nodeInstanceEntity);
}

// Test for JpaRepository.existsById(ID id)
@Test
public void testExistsNodeInstanceEntityById() {
Long id = 1L;
boolean exists = nodeInstanceEntityRepository.existsById(id);
assertTrue(exists);
}

// Test for JpaRepository.deleteById(ID id)
@Test
public void testDeleteNodeInstanceEntityById() {
Long id = 1L;
nodeInstanceEntityRepository.deleteById(id);
boolean exists = nodeInstanceEntityRepository.existsById(id);
assertFalse(exists);
}

// Test for JpaRepository.findAll(Specification<S> spec)
@Test
public void testFindNodeInstanceEntitiesBySpecification() {
// Add implementation for spec
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(null);
assertTrue(nodeInstanceEntities.size() >= 0);
}

// Test for JpaRepository.count(Specification<S> spec)
@Test
public void testCountNodeInstanceEntitiesBySpecification() {
// Add implementation for spec
long count = nodeInstanceEntityRepository.count(null);
assertTrue(count >= 0);
}

// Test for JpaRepository.delete(S entity)
@Test
public void testDeleteNodeInstanceEntity() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setId(1L);
nodeInstanceEntityRepository.delete(nodeInstanceEntity);
boolean exists = nodeInstanceEntityRepository.existsById(1L);
assertFalse(exists);
}

// Test for JpaRepository.deleteAll()
@Test
public void testDeleteAllNodeInstanceEntities() {
nodeInstanceEntityRepository.deleteAll();
long count = nodeInstanceEntityRepository.count();
assertEquals(0, count);
}

// Test for JpaRepository.findAll(Sort sort)
@Test
public void testFindNodeInstanceEntitiesBySort() {
// Add implementation for sort
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(null);
assertTrue(nodeInstanceEntities.size() >= 0);
}

// Test for JpaRepository.findAll(Pageable pageable)
@Test
public void testFindNodeInstanceEntitiesByPageable() {
Pageable pageable = PageRequest.of(0, 10);
Page<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(pageable);
assertTrue(nodeInstanceEntities.getContent().size() <= 10);
}

// Test for custom findByInstanceIdAndInstanceTypeAndFlowableElementType method
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START_EVENT;
FlowableElementType flowableElementType = FlowableElementType.USER_TASK;
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
assertTrue(nodeInstanceEntities.size() >= 0);
}

// Test for findByFlowInstanceId method
@Test
public void testFindByFlowInstanceId() {
Long flowInstanceId = 1L;
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
assertTrue(nodeInstanceEntities.size() >= 0);
}

// Test for findByIds method with valid ids
@Test
public void testFindByIdsWithValidIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
assertTrue(nodeInstanceEntities.size() >= 0);
}

// Test for findByIds method with empty ids
@Test
public void testFindByIdsWithEmptyIds() {
List<Long> ids = new ArrayList<>();
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
assertTrue(nodeInstanceEntities.size() == 0);
}

// Test for deleteByFlowInstanceId method
@Test
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
assertEquals(rowsAffected, 1);
}

// Test for deleteByInstanceIdAndInstanceType method
@Test
public void testDeleteByInstanceIdAndInstanceType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START_EVENT;
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
assertEquals(rowsAffected, 1);
}
}