import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
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

 private NodeInstanceEntity nodeInstanceEntity1;
 private NodeInstanceEntity nodeInstanceEntity2;
 private NodeInstanceEntity nodeInstanceEntity3;
 private List<NodeInstanceEntity> nodeInstanceEntityList;

 @Before
 public void setUp() {
 nodeInstanceEntity1 = new NodeInstanceEntity();
 nodeInstanceEntity1.setFlowInstanceId(1L);
 nodeInstanceEntity1.setInstanceId(1L);
 nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstanceEntity1.setFlowableElementType(FlowableElementType.START);

 nodeInstanceEntity2 = new NodeInstanceEntity();
 nodeInstanceEntity2.setFlowInstanceId(1L);
 nodeInstanceEntity2.setInstanceId(2L);
 nodeInstanceEntity2.setInstanceType(FlowNodeType.INTERMEDIATE_EVENT);
 nodeInstanceEntity2.setFlowableElementType(FlowableElementType.INTERRUPT);

 nodeInstanceEntity3 = new NodeInstanceEntity();
 nodeInstanceEntity3.setFlowInstanceId(2L);
 nodeInstanceEntity3.setInstanceId(1L);
 nodeInstanceEntity3.setInstanceType(FlowNodeType.END_EVENT);
 nodeInstanceEntity3.setFlowableElementType(FlowableElementType.END);

 nodeInstanceEntityList = Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3);
 nodeInstanceEntityRepository.saveAll(nodeInstanceEntityList);
 }

 @After
 public void tearDown() {
 nodeInstanceEntityRepository.deleteAll();
 }

 @Test
 public void testFindAll() {
 List<NodeInstanceEntity> nodeInstanceEntityListFromDB = nodeInstanceEntityRepository.findAll();
 assertEquals(nodeInstanceEntityList, nodeInstanceEntityListFromDB);
 }

 @Test
 public void testFindById() {
 NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(nodeInstanceEntity1.getId())
 .orElseThrow(RuntimeException::new);
 assertEquals(nodeInstanceEntity1, nodeInstanceEntity);
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstanceEntityListFromDB = nodeInstanceEntityRepository
 .findByFlowInstanceId(nodeInstanceEntity1.getFlowInstanceId());
 assertTrue(nodeInstanceEntityListFromDB.containsAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2)));
 }

 @Test
 public void testFindByInstanceIdAndInstanceType() {
 List<NodeInstanceEntity> nodeInstanceEntityListFromDB = nodeInstanceEntityRepository
 .findByInstanceIdAndInstanceType(nodeInstanceEntity3.getInstanceId(), nodeInstanceEntity3.getInstanceType());
 assertTrue(nodeInstanceEntityListFromDB.contains(nodeInstanceEntity3));
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstanceEntityListFromDB = nodeInstanceEntityRepository
 .findByInstanceIdAndInstanceTypeAndFlowableElementType(nodeInstanceEntity1.getInstanceId(),
 nodeInstanceEntity1.getInstanceType(),
 nodeInstanceEntity1.getFlowableElementType());
 assertTrue(nodeInstanceEntityListFromDB.contains(nodeInstanceEntity1));
 }

 @Test
 public void testFindByIds() {
 Collection<Long> ids = new ArrayList<>();
 ids.add(nodeInstanceEntity1.getId());
 ids.add(nodeInstanceEntity2.getId());
 List<NodeInstanceEntity> nodeInstanceEntityListFromDB = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(nodeInstanceEntityListFromDB.containsAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2)));
 }

 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(nodeInstanceEntity1.getFlowInstanceId());
 assertEquals(2, rowsAffected);
 }

 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(nodeInstanceEntity3.getInstanceId(),
 nodeInstanceEntity3.getInstanceType());
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testFindAllSort() {
 Sort sort = Sort.by(Sort.Direction.ASC, "flowInstanceId", "instanceId", "instanceType", "flowableElementType");
 List<NodeInstanceEntity> nodeInstanceEntityListFromDB = nodeInstanceEntityRepository.findAll(sort);
 assertEquals(nodeInstanceEntityList, nodeInstanceEntityListFromDB);
 }

 @Test
 public void testFindAllPage() {
 PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "flowInstanceId"));
 Page<NodeInstanceEntity> nodeInstanceEntityPage = nodeInstanceEntityRepository.findAll(pageRequest);
 assertEquals(2, nodeInstanceEntityPage.getNumberOfElements());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_WrongParam() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
 nodeInstanceEntity1.getInstanceType(),
 nodeInstanceEntity1.getFlowableElementType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceIdAndInstanceType_WrongParam() {
 nodeInstanceEntityRepository.findByInstanceIdAndInstanceType(null, nodeInstanceEntity1.getInstanceType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByInstanceId_WrongParam() {
 nodeInstanceEntityRepository.findByInstanceId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByFlowInstanceId_WrongParam() {
 nodeInstanceEntityRepository.findByFlowInstanceId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindById_WrongParam() {
 nodeInstanceEntityRepository.findById(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIds_WrongParam() {
 nodeInstanceEntityRepository.findByIds(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByFlowInstanceId_WrongParam() {
 nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByInstanceIdAndInstanceType_WrongParam() {
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, nodeInstanceEntity3.getInstanceType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllPage_WrongParam() {
 nodeInstanceEntityRepository.findAll(PageRequest.of(0, -1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllSort_WrongParam() {
 nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.DESC, "flowInstanceId"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllSort_WrongField() {
 nodeInstanceEntityRepository.findAll(Sort.by(Sort.Direction.ASC, "wrongField"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllPage_WrongField() {
 PageRequest pageRequest = PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "wrongField"));
 nodeInstanceEntityRepository.findAll(pageRequest);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllPage_WrongPage() {
 PageRequest pageRequest = PageRequest.of(-1, 2, Sort.by(Sort.Direction.ASC, "flowInstanceId"));
 nodeInstanceEntityRepository.findAll(pageRequest);
 }
}