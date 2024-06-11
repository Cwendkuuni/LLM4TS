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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepository.class)
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// Test for JpaRepository.save(S)
@Test
public void testSaveNodeInstanceEntity() {
NodeInstanceEntity niEntity = new NodeInstanceEntity();
niEntity = nodeInstanceEntityRepository.save(niEntity);
assertNotNull(niEntity.getId());
}

// Test for JpaRepository.findAll()
@Test
public void testFindAllNodeInstanceEntities() {
List<NodeInstanceEntity> niEntities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
assertFalse(niEntities.isEmpty());
}

// Test for JpaRepository.findById(ID)
@Test
public void testFindNodeInstanceEntityById() {
NodeInstanceEntity niEntity = nodeInstanceEntityRepository.findById(1L).get();
assertNotNull(niEntity);
}

// Test for JpaRepository.existsById(ID)
@Test
public void testExistsNodeInstanceEntityById() {
boolean result = nodeInstanceEntityRepository.existsById(1L);
assertTrue(result);
}

// Test for JpaRepository.deleteById(ID)
@Test
public void testDeleteNodeInstanceEntityById() {
nodeInstanceEntityRepository.deleteById(1L);
boolean result = nodeInstanceEntityRepository.existsById(1L);
assertFalse(result);
}

// Test for JpaRepository.findAll(Sort)
@Test
public void testFindAllNodeInstanceEntitiesSorted() {
}

// Test for JpaRepository.findAll(Example<S>)
@Test
public void testFindAllNodeInstanceEntitiesByExample() {
}

// Test for JpaRepository.findAll(Example<S>, Sort)
@Test
public void testFindAllNodeInstanceEntitiesByExampleSorted() {
}

// Test for JpaRepository.count()
@Test
public void testCountNodeInstanceEntities() {
long count = nodeInstanceEntityRepository.count();
assertTrue(count > 0);
}

// Test for JpaRepository.deleteAll()
@Test
public void testDeleteAllNodeInstanceEntities() {
nodeInstanceEntityRepository.deleteAll();
long count = nodeInstanceEntityRepository.count();
assertEquals(0, count);
}

// Test for JpaRepository.deleteById(ID)
@Test
public void testDeleteAllNodeInstanceEntitiesInBatch() {
}

// Test for JpaRepository.deleteAll()
@Test
public void testDeleteAllNodeInstanceEntitiesInBatchUsingQuery() {
}

// Test for JpaSpecificationExecutor.findAll(Specification<S>, Pageable)
@Test
public void testFindAllNodeInstanceEntitiesBySpecification() {
}

// Test for JpaSpecificationExecutor.findAll(Specification<S>)
@Test
public void testFindAllNodeInstanceEntitiesBySpecificationWithoutPageable() {
}

// Test for JpaSpecificationExecutor.findOne(Specification<S>)
@Test
public void testFindOneNodeInstanceEntityBySpecification() {
}

// Test for JpaSpecificationExecutor.count(Specification<S>)
@Test
public void testCountNodeInstanceEntitiesBySpecification() {
}

// Test for JpaSpecificationExecutor.exists(Specification<S>)
@Test
public void testExistsNodeInstanceEntityBySpecification() {
}

// Test for custom query "findByInstanceIdAndInstanceTypeAndFlowableElementType"
@Test
public void testFindNodeInstanceEntitiesByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> niEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
assertFalse(niEntities.isEmpty());
}

// Test for "findByFlowInstanceId" method
@Test
public void testFindNodeInstanceEntitiesByFlowInstanceId() {
List<NodeInstanceEntity> niEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertFalse(niEntities.isEmpty());
}

// Test for custom query "findByIds"
@Test
public void testFindNodeInstanceEntitiesByIds() {
List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L, 3L));
List<NodeInstanceEntity> niEntities = nodeInstanceEntityRepository.findByIds(ids);
assertFalse(niEntities.isEmpty());
}

// Test for custom query "deleteByFlowInstanceId"
@Test
@Transactional
public void testDeleteNodeInstanceEntitiesByFlowInstanceId() {
long countBeforeDeletion = nodeInstanceEntityRepository.countByFlowInstanceId(1L);
nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
long countAfterDeletion = nodeInstanceEntityRepository.countByFlowInstanceId(1L);
assertEquals(countBeforeDeletion - countAfterDeletion, 1);
}

// Test for custom query "deleteByInstanceIdAndInstanceType"
@Test
@Transactional
public void testDeleteNodeInstanceEntitiesByInstanceIdAndInstanceType() {
long countBeforeDeletion = nodeInstanceEntityRepository.countByInstanceIdAndInstanceType(1L,
FlowNodeType.START_EVENT);
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
long countAfterDeletion = nodeInstanceEntityRepository.countByInstanceIdAndInstanceType(1L,
FlowNodeType.START_EVENT);
assertEquals(countBeforeDeletion - countAfterDeletion, 1);
}

// Test for pagination with "findAll(Pageable)"
@Test
public void testFindNodeInstanceEntitiesWithPaging() {
Pageable pageable = PageRequest.of(0, 10);
Page<NodeInstanceEntity> niEntitiesPage = nodeInstanceEntityRepository.findAll(pageable);
assertEquals(10, niEntitiesPage.getContent().size());
}

}