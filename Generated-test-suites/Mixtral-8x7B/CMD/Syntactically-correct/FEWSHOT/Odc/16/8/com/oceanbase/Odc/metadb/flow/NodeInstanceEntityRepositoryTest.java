import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
List<NodeInstanceEntity> expected = new ArrayList<>();
NodeInstanceEntity entity1 = new NodeInstanceEntity();
entity1.setId(1L);
entity1.setInstanceId(1L);
entity1.setInstanceType(FlowNodeType.START_EVENT);
entity1.setFlowableElementType(FlowableElementType.START_EVENT);
expected.add(entity1);

NodeInstanceEntity entity2 = new NodeInstanceEntity();
entity2.setId(2L);
entity2.setInstanceId(1L);
entity2.setInstanceType(FlowNodeType.USER_TASK);
entity2.setFlowableElementType(FlowableElementType.USER_TASK);
expected.add(entity2);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
assertEquals(expected, result);

result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.USER_TASK, FlowableElementType.USER_TASK);
assertEquals(expected.get(1), result.get(0));
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> expected = new ArrayList<>();
NodeInstanceEntity entity1 = new NodeInstanceEntity();
entity1.setId(1L);
entity1.setInstanceId(1L);
entity1.setInstanceType(FlowNodeType.START_EVENT);
entity1.setFlowableElementType(FlowableElementType.START_EVENT);
expected.add(entity1);

NodeInstanceEntity entity2 = new NodeInstanceEntity();
entity2.setId(2L);
entity2.setInstanceId(1L);
entity2.setInstanceType(FlowNodeType.USER_TASK);
entity2.setFlowableElementType(FlowableElementType.USER_TASK);
expected.add(entity2);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(expected, result);
}

@Test
public void testFindByIds() {
List<NodeInstanceEntity> expected = new ArrayList<>();
NodeInstanceEntity entity1 = new NodeInstanceEntity();
entity1.setId(1L);
entity1.setInstanceId(1L);
entity1.setInstanceType(FlowNodeType.START_EVENT);
entity1.setFlowableElementType(FlowableElementType.START_EVENT);
expected.add(entity1);

NodeInstanceEntity entity2 = new NodeInstanceEntity();
entity2.setId(2L);
entity2.setInstanceId(2L);
entity2.setInstanceType(FlowNodeType.USER_TASK);
entity2.setFlowableElementType(FlowableElementType.USER_TASK);
expected.add(entity2);

List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(expected, result);
}

@Test
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;

int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
assertEquals(2, result);

List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
assertEquals(Collections.emptyList(), entities);
}

@Test
public void testDeleteByInstanceIdAndInstanceType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START_EVENT;

int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
assertEquals(1, result);

List<NodeInstanceEntity> entities = nodeInstanceEntityRepository.findByInstanceId(instanceId);
assertEquals(1, entities.size());
}
}