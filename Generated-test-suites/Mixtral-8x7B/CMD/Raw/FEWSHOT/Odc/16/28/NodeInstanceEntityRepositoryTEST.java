import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.NODE);
assertEquals(0, nodeEntities.size());

nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L, FlowNodeType.END, FlowableElementType.NODE);
assertEquals(1, nodeEntities.size());

nodeEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(3L, FlowNodeType.DECISION, FlowableElementType.GATEWAY);
assertEquals(1, nodeEntities.size());
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(0, nodeEntities.size());

nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
assertEquals(3, nodeEntities.size());

nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(3L);
assertEquals(2, nodeEntities.size());
}

@Test
public void testFindByIds() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
ids.add(3L);
List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(3, nodeEntities.size());

ids.clear();
ids.add(4L);
nodeEntities = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(0, nodeEntities.size());
}

@Test
public void testDeleteByFlowInstanceId() {
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(0, rowsAffected);

rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(2L);
assertEquals(3, rowsAffected);

rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(3L);
assertEquals(2, rowsAffected);
}

@Test
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
assertEquals(0, rowsAffected);

rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.END);
assertEquals(1, rowsAffected);

rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(3L, FlowNodeType.DECISION);
assertEquals(1, rowsAffected);
}

@Test
public void testFindAll() {
List<NodeInstanceEntity> nodeEntities = (List<NodeInstanceEntity>) nodeInstanceEntityRepository.findAll();
assertEquals(5, nodeEntities.size());
}

@Test
public void testFindAllOrderByInstanceIdAsc() {
PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "instanceId"));
Page<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findAll(pageRequest);
assertEquals(5, nodeEntities.getTotalElements());

List<NodeInstanceEntity> content = nodeEntities.getContent();
assertEquals(FlowNodeType.START, content.get(0).getInstanceType());
assertEquals(FlowNodeType.END, content.get(1).getInstanceType());
assertEquals(FlowNodeType.DECISION, content.get(2).getInstanceType());
assertEquals(FlowNodeType.TASK, content.get(3).getInstanceType());
assertEquals(FlowNodeType.GATEWAY, content.get(4).getInstanceType());
}

@Test
public void testFindAllOrderByInstanceIdDesc() {
PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "instanceId"));
Page<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findAll(pageRequest);
assertEquals(5, nodeEntities.getTotalElements());

List<NodeInstanceEntity> content = nodeEntities.getContent();
assertEquals(FlowNodeType.GATEWAY, content.get(0).getInstanceType());
assertEquals(FlowNodeType.TASK, content.get(1).getInstanceType());
assertEquals(FlowNodeType.DECISION, content.get(2).getInstanceType());
assertEquals(FlowNodeType.END, content.get(3).getInstanceType());
assertEquals(FlowNodeType.START, content.get(4).getInstanceType());
}

@Test
public void testSave() {
NodeInstanceEntity nodeEntity = new NodeInstanceEntity();
nodeEntity.setInstanceId(6L);
nodeEntity.setInstanceType(FlowNodeType.TASK);
nodeEntity.setFlowableElementType(FlowableElementType.NODE);
nodeEntity.setFlowInstanceId(2L);

nodeInstanceEntityRepository.save(nodeEntity);

List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
assertEquals(4, nodeEntities.size());
}

@Test
public void testUpdate() {
List<NodeInstanceEntity> nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
assertEquals(3, nodeEntities.size());

NodeInstanceEntity nodeEntity = nodeEntities.get(0);
nodeEntity.setInstanceType(FlowNodeType.END);

nodeInstanceEntityRepository.save(nodeEntity);

nodeEntities = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
assertEquals(3, nodeEntities.size());

assertEquals(FlowNodeType.END, nodeEntities.get(0).getInstanceType());
}

@Test
public void testCount() {
long count = nodeInstanceEntityRepository.count();
assertEquals(5, count);
}

@Test
public void testExistsById() {
assertTrue(nodeInstanceEntityRepository.existsById(1L));
assertFalse(nodeInstanceEntityRepository.existsById(6L));
}

@Test
public void testFindById() {
NodeInstanceEntity nodeEntity = nodeInstanceEntityRepository.findById(1L).orElse(null);
assertNull(nodeEntity);

nodeEntity = nodeInstanceEntityRepository.findById(2L).orElse(null);
assertNotNull(nodeEntity);
}
}