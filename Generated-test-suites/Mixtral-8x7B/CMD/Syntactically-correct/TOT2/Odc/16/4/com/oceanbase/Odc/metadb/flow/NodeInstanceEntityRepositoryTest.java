import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private TestEntityManager entityManager;

 @Autowired
 private NodeInstanceEntityRepository repository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> entities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertTrue(entities.size() > 0);
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = repository.findByFlowInstanceId(1L);
 assertTrue(entities.size() > 0);
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 Collection<NodeInstanceEntity> entities = repository.findByIds(ids);
 assertTrue(entities.size() == 3);
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 Long instanceId = 1L;
 int result = repository.deleteByFlowInstanceId(instanceId);
 assertTrue(result > 0);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 Long instanceId = 1L;
 FlowNodeType instanceType = FlowNodeType.START_EVENT;
 int result = repository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
 assertTrue(result > 0);
 }

 @Test
 public void testPage() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<NodeInstanceEntity> page = repository.findAll(pageable);
 assertTrue(page.getTotalElements() > 0);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_EVENT);
 entity.setFlowableElementType(FlowableElementType.START_EVENT);
 entity.setFlowInstanceId(1L);
 entity.setTenantId(1L);
 entityManager.persist(entity);
 entityManager.flush();

 NodeInstanceEntity savedEntity = repository.findById(entity.getId()).get();
 assertEquals(savedEntity.getInstanceId(), entity.getInstanceId());
 assertEquals(savedEntity.getInstanceType(), entity.getInstanceType());
 assertEquals(savedEntity.getFlowableElementType(), entity.getFlowableElementType());
 assertEquals(savedEntity.getFlowInstanceId(), entity.getFlowInstanceId());
 assertEquals(savedEntity.getTenantId(), entity.getTenantId());
 }

 @Test
 public void testUpdate() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_EVENT);
 entity.setFlowableElementType(FlowableElementType.START_EVENT);
 entity.setFlowInstanceId(1L);
 entity.setTenantId(1L);
 entityManager.persist(entity);
 entityManager.flush();

 entity.setInstanceId(2L);
 entity.setInstanceType(FlowNodeType.END_EVENT);
 entity.setFlowableElementType(FlowableElementType.END_EVENT);
 entity.setFlowInstanceId(2L);
 entity.setTenantId(2L);
 entityManager.merge(entity);
 entityManager.flush();

 NodeInstanceEntity savedEntity = entityManager.find(NodeInstanceEntity.class, entity.getId());
 assertEquals(savedEntity.getInstanceId(), entity.getInstanceId());
 assertEquals(savedEntity.getInstanceType(), entity.getInstanceType());
 assertEquals(savedEntity.getFlowableElementType(), entity.getFlowableElementType());
 assertEquals(savedEntity.getFlowInstanceId(), entity.getFlowInstanceId());
 assertEquals(savedEntity.getTenantId(), entity.getTenantId());
 }

 @Test
 public void testDelete() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(1L);
 entity.setInstanceType(FlowNodeType.START_EVENT);
 entity.setFlowableElementType(FlowableElementType.START_EVENT);
 entity.setFlowInstanceId(1L);
 entity.setTenantId(1L);
 entityManager.persist(entity);
 entityManager.flush();

 repository.delete(entity);
 entityManager.flush();

 assertNull(entityManager.find(NodeInstanceEntity.class, entity.getId()));
 }

}