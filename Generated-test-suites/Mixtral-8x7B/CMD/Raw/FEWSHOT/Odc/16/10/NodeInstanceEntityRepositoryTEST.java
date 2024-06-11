import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

public class NodeInstanceEntityRepositoryTest {

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstances = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.TASK);
 assertEquals(1, nodeInstances.size());
 assertEquals("startTask", nodeInstances.get(0).getName());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = repository.findByFlowInstanceId(1L);
 assertEquals(3, nodeInstances.size());
 assertEquals("startTask", nodeInstances.get(0).getName());
 assertEquals("middleTask", nodeInstances.get(1).getName());
 assertEquals("endTask", nodeInstances.get(2).getName());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeInstances = repository.findByIds(ids);
 assertEquals(3, nodeInstances.size());
 assertEquals("startTask", nodeInstances.get(0).getName());
 assertEquals("middleTask", nodeInstances.get(1).getName());
 assertEquals("endTask", nodeInstances.get(2).getName());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = repository.deleteByFlowInstanceId(1L);
 assertEquals(3, rowsAffected);
 List<NodeInstanceEntity> nodeInstances = repository.findByFlowInstanceId(1L);
 assertEquals(0, nodeInstances.size());
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
 assertEquals(1, rowsAffected);
 List<NodeInstanceEntity> nodeInstances = repository.findByInstanceId(1L);
 assertEquals(2, nodeInstances.size());
 }

 @Test
 public void testPage() {
 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 Page<NodeInstanceEntity> page = repository.findAll(pageable);
 assertEquals(3, page.getTotalElements());
 assertEquals(3, page.getContent().size());
 NodeInstanceEntity first = page.getContent().get(0);
 assertEquals(1L, first.getId().longValue());
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setName("newTask");
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.TASK);
 entity.setFlowableElementType(FlowableElementType.TASK);
 NodeInstanceEntity saved = repository.save(entity);
 assertNotNull(saved.getId());
 NodeInstanceEntity found = repository.findById(saved.getId()).orElse(null);
 assertEquals(saved, found);
 }

 @Test
 public void testFindById() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setName("newTask");
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.TASK);
 entity.setFlowableElementType(FlowableElementType.TASK);
 NodeInstanceEntity saved = repository.save(entity);
 NodeInstanceEntity found = repository.findById(saved.getId()).orElse(null);
 assertEquals(saved, found);
 }

 @Test
 public void testFindByName() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setName("newTask");
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.TASK);
 entity.setFlowableElementType(FlowableElementType.TASK);
 NodeInstanceEntity saved = repository.save(entity);
 NodeInstanceEntity found = repository.findByName(saved.getName()).orElse(null);
 assertEquals(saved, found);
 }

 @Test
 public void testFindByNameNull() {
 NodeInstanceEntity found = repository.findByName(null);
 assertNull(found);
 }

 @Test
 public void testCount() {
 long count = repository.count();
 assertEquals(3, count);
 }

 @Test
 public void testExistsById() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setName("newTask");
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.TASK);
 entity.setFlowableElementType(FlowableElementType.TASK);
 NodeInstanceEntity saved = repository.save(entity);
 boolean exists = repository.existsById(saved.getId());
 assertTrue(exists);
 }

 @Test
 public void testFlush() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setName("newTask");
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.TASK);
 entity.setFlowableElementType(FlowableElementType.TASK);
 NodeInstanceEntity saved = repository.save(entity);
 repository.flush();
 NodeInstanceEntity found = repository.findById(saved.getId()).orElse(null);
 assertEquals(saved, found);
 }

}