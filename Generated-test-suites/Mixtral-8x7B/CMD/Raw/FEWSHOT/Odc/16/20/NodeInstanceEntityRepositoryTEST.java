import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START, FlowableElementType.START_EVENT);
 assertEquals(1, entities.size());
 assertEquals(1L, entities.get(0).getInstanceId().longValue());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(3, entities.size());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, entities.size());
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(2, result);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.END);
 assertEquals(1, result);
 }

 @Test
 public void testSave() {
 NodeInstanceEntity entity = new NodeInstanceEntity();
 entity.setInstanceId(3L);
 entity.setInstanceType(FlowNodeType.END);
 entity.setFlowableElementType(FlowableElementType.END_EVENT);
 NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
 assertNotNull(savedEntity);
 assertNotSame(entity, savedEntity);
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> entities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
 assertTrue(entities.size() > 0);
 }

 @Test
 public void testPage() {
 Pageable pageable = PageRequest.of(0, 2);
 Page<NodeInstanceEntity> page = nodeInstanceEntityRepository.findAll(pageable);
 assertEquals(2, page.getSize());
 }

 @Test
 public void testQueryAnnotationSupport() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(1L,
 FlowNodeType.START);
 assertEquals(1, entities.size());
 }

 @Test
 public void testQueryAnnotationWithNamedParameterSupport() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeNamedQuery(1L,
 FlowNodeType.START);
 assertEquals(1, entities.size());
 }

 @Test
 public void testQueryAnnotationWithNativeQuerySupport() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdNativeQuery(1L);
 assertEquals(1, entities.size());
 }

 @Test
 public void testQueryAnnotationWithNamedNativeQuerySupport() {
 List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceIdNamedNativeQuery(1L);
 assertEquals(1, entities.size());
 }

 @Test
 public void testQueryAnnotationWithCountQuerySupport() {
 Long count = nodeInstanceEntityRepository.countByInstanceId(1L);
 assertEquals(1L, count);
 }

 @Test
 public void testQueryAnnotationWithNamedCountQuerySupport() {
 Long count = nodeInstanceEntityRepository.countByInstanceIdNamedQuery(1L);
 assertEquals(1L, count);
 }
}