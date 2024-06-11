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
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 // Test for JpaRepository#save(S entity)
 @Test
 public void testSave() {
 NodeInstanceEntity ni = new NodeInstanceEntity();
 ni.setInstanceId(1L);
 ni.setInstanceType(FlowNodeType.START_EVENT);
 ni.setFlowableElementType(FlowableElementType.USER_TASK);

 NodeInstanceEntity savedNi = nodeInstanceEntityRepository.save(ni);
 assertNotNull(savedNi.getId());
 assertEquals(ni.getInstanceId(), savedNi.getInstanceId());
 assertEquals(ni.getInstanceType(), savedNi.getInstanceType());
 assertEquals(ni.getFlowableElementType(), savedNi.getFlowableElementType());
 }

 // Test for JpaRepository#findAll()
 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> niList = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 assertTrue(niList.size() > 0);
 }

 // Test for JpaRepository#findAll(Sort sort)
 @Test
 public void testFindAllWithSort() {
 Sort sort = Sort.by(Sort.Direction.ASC, "id");
 List<NodeInstanceEntity> niList = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll(sort);
 assertTrue(niList.size() > 0);
 }

 // Test for JpaRepository#findAll(Pageable pageable)
 @Test
 public void testFindAllWithPage() {
 PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 Page<NodeInstanceEntity> niPage = nodeInstanceEntityRepository.findAll(pageRequest);
 assertTrue(niPage.getContent().size() <= 10);
 }

 // Test for JpaRepository#findById(ID id)
 @Test
 public void testFindById() {
 NodeInstanceEntity ni = nodeInstanceEntityRepository.findById(1L).orElse(null);
 assertNotNull(ni);
 }

 // Test for JpaRepository#existsById(ID id)
 @Test
 public void testExistsById() {
 assertTrue(nodeInstanceEntityRepository.existsById(1L));
 assertFalse(nodeInstanceEntityRepository.existsById(9999L));
 }

 // Test for JpaRepository#deleteById(ID id)
 @Test
 @Transactional
 public void testDeleteById() {
 nodeInstanceEntityRepository.deleteById(1L);
 assertFalse(nodeInstanceEntityRepository.existsById(1L));
 }

 // Test for JpaRepository#count()
 @Test
 public void testCount() {
 long count = nodeInstanceEntityRepository.count();
 assertTrue(count > 0);
 }

 // Test for custom query: findByInstanceIdAndInstanceTypeAndFlowableElementType
 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> niList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 1L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);
 assertTrue(niList.size() > 0);
 }

 // Test for JpaSpecificationExecutor#findAll(Specification<S> spec)
 @Test
 public void testFindAllWithSpecification() {
 // Implementation depends on your business logic
 }

 // Test for JpaSpecificationExecutor#findOne(Specification<S> spec)
 @Test
 public void testFindOneWithSpecification() {
 // Implementation depends on your business logic
 }

 // Test for JpaSpecificationExecutor#count(Specification<S> spec)
 @Test
 public void testCountWithSpecification() {
 // Implementation depends on your business logic
 }

 // Test for JpaSpecificationExecutor#exists(Specification<S> spec)
 @Test
 public void testExistsWithSpecification() {
 // Implementation depends on your business logic
 }

 // Test for JpaRepository#findAll() with empty result
 @Test
 public void testFindAllWithEmptyResult() {
 List<NodeInstanceEntity> niList = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 NodeInstanceEntity ni = new NodeInstanceEntity();
 ni.setInstanceId(9999L);
 niList.removeIf(n -> n.getInstanceId().equals(ni.getInstanceId()));
 nodeInstanceEntityRepository.deleteAll(niList);
 List<NodeInstanceEntity> emptyNiList = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 assertTrue(emptyNiList.isEmpty());
 }

 // Test for JpaRepository#findAll(Sort sort) with empty result
 @Test
 public void testFindAllWithSortAndEmptyResult() {
 Sort sort = Sort.by(Sort.Direction.ASC, "id");
 List<NodeInstanceEntity> emptyNiList = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll(sort);
 assertTrue(emptyNiList.isEmpty());
 }

 // Test for JpaRepository#findAll(Pageable pageable) with empty result
 @Test
 public void testFindAllWithPageAndEmptyResult() {
 PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 Page<NodeInstanceEntity> emptyNiPage = nodeInstanceEntityRepository.findAll(pageRequest);
 assertTrue(emptyNiPage.getContent().isEmpty());
 }

 // Test for custom query: findByFlowInstanceId
 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> niList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(niList.size() > 0);
 }

 // Test for custom query: findByIds
 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> niList = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(niList.size() == ids.size());
 }

 // Test for custom query: deleteByFlowInstanceId
 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertTrue(rowsAffected > 0);
 }

 // Test for custom query: deleteByInstanceIdAndInstanceType
 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertTrue(rowsAffected > 0);
 }

}

class NodeInstanceEntityRepositoryTestConfig {
 // Add your test configuration here if needed
}