import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import com.oceanbase.odc.metadb.flow.FlowableElementType;
import com.oceanbase.odc.metadb.flow.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<NodeInstanceEntity>();
 nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
 assertNotNull(nodeInstanceEntities);
 assertTrue(!nodeInstanceEntities.isEmpty());
 }

 @Test
 public void testFindAllWithSorting() {
 List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<NodeInstanceEntity>();
 nodeInstanceEntities = nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
 assertNotNull(nodeInstanceEntities);
 assertTrue(!nodeInstanceEntities.isEmpty());
 }

 @Test
 public void testFindAllWithPagination() {
 PageRequest pageRequest = PageRequest.of(0, 10);
 Page<NodeInstanceEntity> nodeInstanceEntityPage = nodeInstanceEntityRepository.findAll(pageRequest);
 assertNotNull(nodeInstanceEntityPage);
 assertTrue(nodeInstanceEntityPage.hasContent());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 1L, FlowNodeType.START, FlowableElementType.NODE);
 assertNotNull(nodeInstanceEntities);
 assertTrue(nodeInstanceEntities.size() > 0);
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertNotNull(nodeInstanceEntities);
 assertTrue(nodeInstanceEntities.size() > 0);
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
 assertNotNull(nodeInstanceEntities);
 assertTrue(nodeInstanceEntities.size() == 3);
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(rowsAffected, 1);
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
 assertEquals(rowsAffected, 1);
 }
}