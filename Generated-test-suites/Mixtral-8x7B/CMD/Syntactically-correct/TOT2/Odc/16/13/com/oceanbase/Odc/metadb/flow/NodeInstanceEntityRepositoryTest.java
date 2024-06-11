import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// Test for JpaRepository.save(S entity)
@Test
public void testSaveNodeInstanceEntity() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
assertNotNull(nodeInstanceEntity.getId());
}

// Test for JpaRepository.findOne(ID id)
@Test
public void testFindNodeInstanceEntity() {
Long id = 1L;
NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findOne(id);
assertNotNull(nodeInstanceEntity);
assertEquals(id, nodeInstanceEntity.getId());
}

// Test for JpaRepository.findAll()
@Test
public void testFindAllNodeInstanceEntities() {
List<NodeInstanceEntity> nodeInstanceEntities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
assertTrue(nodeInstanceEntities.size() > 0);
}

// Test for JpaRepository.findAll(ID... ids)
@Test
public void testFindNodeInstanceEntitiesByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> nodeInstanceEntities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll(ids);
assertTrue(nodeInstanceEntities.size() == 3);
}

// Test for JpaRepository.exists(ID id)
@Test
public void testExistsNodeInstanceEntity() {
Long id = 1L;
assertTrue(nodeInstanceEntityRepository.exists(id));
}

// Test for JpaRepository.count()
@Test
public void testCountNodeInstanceEntities() {
long count = nodeInstanceEntityRepository.count();
assertTrue(count > 0);
}

// Test for JpaRepository.delete(ID id)
@Test
public void testDeleteNodeInstanceEntity() {
Long id = 1L;
nodeInstanceEntityRepository.delete(id);
assertFalse(nodeInstanceEntityRepository.exists(id));
}

// Test for JpaRepository.delete(S entity)
@Test
public void testDeleteNodeInstanceEntityInstance() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
nodeInstanceEntityRepository.delete(nodeInstanceEntity);
assertFalse(nodeInstanceEntityRepository.exists(nodeInstanceEntity.getId()));
}

// Test for JpaRepository.deleteAll()
@Test
public void testDeleteAllNodeInstanceEntities() {
Collection<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
nodeInstanceEntityRepository.deleteAll(nodeInstanceEntities);
assertTrue(nodeInstanceEntityRepository.count() == 0);
}

// Test for JpaSpecificationExecutor.findAll(Specification<S> spec)
@Test
public void testFindNodeInstanceEntitiesBySpecification() {
// Implement test here
}

// Test for JpaSpecificationExecutor.findAll(Specification<S> spec, Pageable pageable)
@Test
public void testFindNodeInstanceEntitiesBySpecificationAndPageable() {
// Implement test here
}

// Test for JpaSpecificationExecutor.findOne(Specification<S> spec)
@Test
public void testFindOneNodeInstanceEntityBySpecification() {
// Implement test here
}

// Test for JpaSpecificationExecutor.count(Specification<S> spec)
@Test
public void testCountNodeInstanceEntitiesBySpecification() {
// Implement test here
}

// Test for JpaSpecificationExecutor.exists(Specification<S> spec)
@Test
public void testExistsNodeInstanceEntityBySpecification() {
// Implement test here
}

// Test for custom findByInstanceIdAndInstanceTypeAndFlowableElementType method
@Test
public void testFindNodeInstanceEntitiesByInstanceIdAndInstanceTypeAndFlowableElementType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START_EVENT;
FlowableElementType flowableElementType = FlowableElementType.ACTIVITY;
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
assertTrue(nodeInstanceEntities.size() > 0);
}

// Test for findByFlowInstanceId method
@Test
public void testFindNodeInstanceEntitiesByFlowInstanceId() {
Long flowInstanceId = 1L;
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
assertTrue(nodeInstanceEntities.size() > 0);
}

// Test for findByIds method
@Test
public void testFindNodeInstanceEntitiesByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
assertTrue(nodeInstanceEntities.size() == 3);
}

// Test for deleteByFlowInstanceId method
@Test
public void testDeleteNodeInstanceEntitiesByFlowInstanceId() {
Long instanceId = 1L;
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
assertEquals(rowsAffected, 1);
}

// Test for deleteByInstanceIdAndInstanceType method
@Test
public void testDeleteNodeInstanceEntitiesByInstanceIdAndInstanceType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START_EVENT;
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
assertEquals(rowsAffected, 1);
}

}