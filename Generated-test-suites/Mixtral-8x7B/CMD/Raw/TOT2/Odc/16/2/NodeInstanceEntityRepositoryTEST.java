import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// Test for findByInstanceIdAndInstanceTypeAndFlowableElementType()
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
assertEquals(0, result.size());

// Add test data
NodeInstanceEntity node1 = new NodeInstanceEntity();
node1.setInstanceId(1L);
node1.setInstanceType(FlowNodeType.START_EVENT);
node1.setFlowableElementType(FlowableElementType.START_EVENT);

NodeInstanceEntity node2 = new NodeInstanceEntity();
node2.setInstanceId(1L);
node2.setInstanceType(FlowNodeType.START_EVENT);
node2.setFlowableElementType(FlowableElementType.BOUNDARY_EVENT);

nodeInstanceEntityRepository.save(node1);
nodeInstanceEntityRepository.save(node2);

result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
assertEquals(1, result.size());
}

// Test for findByFlowInstanceId()
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(0, result.size());

// Add test data
NodeInstanceEntity node1 = new NodeInstanceEntity();
node1.setFlowInstanceId(1L);
node1.setInstanceType(FlowNodeType.START_EVENT);
node1.setFlowableElementType(FlowableElementType.START_EVENT);

NodeInstanceEntity node2 = new NodeInstanceEntity();
node2.setFlowInstanceId(1L);
node2.setInstanceType(FlowNodeType.INTERMEDIATE_EVENT);
node2.setFlowableElementType(FlowableElementType.INTERMEDIATE_EVENT);

nodeInstanceEntityRepository.save(node1);
nodeInstanceEntityRepository.save(node2);

result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(2, result.size());
}

// Test for findByIds()
@Test
public void testFindByIds() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(0, result.size());

// Add test data
NodeInstanceEntity node1 = new NodeInstanceEntity();
node1.setId(1L);
node1.setFlowInstanceId(1L);
node1.setInstanceType(FlowNodeType.START_EVENT);
node1.setFlowableElementType(FlowableElementType.START_EVENT);

NodeInstanceEntity node2 = new NodeInstanceEntity();
node2.setId(2L);
node2.setFlowInstanceId(2L);
node2.setInstanceType(FlowNodeType.INTERMEDIATE_EVENT);
node2.setFlowableElementType(FlowableElementType.INTERMEDIATE_EVENT);

nodeInstanceEntityRepository.save(node1);
nodeInstanceEntityRepository.save(node2);

result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(2, result.size());
}

// Test for deleteByFlowInstanceId()
@Test
public void testDeleteByFlowInstanceId() {
long countBefore = nodeInstanceEntityRepository.count();

nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

long countAfter = nodeInstanceEntityRepository.count();
assertEquals(countBefore, countAfter + 1);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(0, result.size());
}

// Test for deleteByInstanceIdAndInstanceType()
@Test
public void testDeleteByInstanceIdAndInstanceType() {
long countBefore = nodeInstanceEntityRepository.count();

nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

long countAfter = nodeInstanceEntityRepository.count();
assertEquals(countBefore, countAfter + 1);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceId(1L);
assertEquals(0, result.size());
}
}