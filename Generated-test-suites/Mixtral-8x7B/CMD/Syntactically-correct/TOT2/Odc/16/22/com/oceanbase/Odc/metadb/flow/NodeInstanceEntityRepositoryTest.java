import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

private List<NodeInstanceEntity> nodeInstances;

@Before
public void setup() {
nodeInstances = new ArrayList<>();
NodeInstanceEntity nodeInstance1 = new NodeInstanceEntity();
nodeInstance1.setInstanceId(1L);
nodeInstance1.setInstanceType(FlowNodeType.START_EVENT);
nodeInstance1.setFlowableElementType(FlowableElementType.START_EVENT);
nodeInstance1.setFlowInstanceId(1L);

NodeInstanceEntity nodeInstance2 = new NodeInstanceEntity();
nodeInstance2.setInstanceId(2L);
nodeInstance2.setInstanceType(FlowNodeType.INTERMEDIATE_EVENT);
nodeInstance2.setFlowableElementType(FlowableElementType.INTERMEDIATE_EVENT);
nodeInstance2.setFlowInstanceId(1L);

nodeInstances.add(nodeInstance1);
nodeInstances.add(nodeInstance2);

nodeInstanceEntityRepository.saveAll(nodeInstances);
}

@After
public void teardown() {
nodeInstanceEntityRepository.deleteAll(nodeInstances);
}

// Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);

assertEquals(1, result.size());
assertEquals(nodeInstances.get(0), result.get(0));
}

// Test for findByFlowInstanceId method
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

assertEquals(2, result.size());
assertEquals(nodeInstances, result);
}

// Test for findByIds method
@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(nodeInstances.get(0).getId(), nodeInstances.get(1).getId());
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

assertEquals(2, result.size());
assertEquals(nodeInstances, result);
}

// Test for deleteByFlowInstanceId method
@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int initialSize = nodeInstanceEntityRepository.findAll().size();

nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

assertEquals(initialSize - 2, nodeInstanceEntityRepository.findAll().size());
}

// Test for deleteByInstanceIdAndInstanceType method
@Test
@Transactional
public void testDeleteByInstanceIdAndInstanceType() {
int initialSize = nodeInstanceEntityRepository.findAll().size();

nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

assertEquals(initialSize - 1, nodeInstanceEntityRepository.findAll().size());
}

// Edge case: Test for findByInstanceIdAndInstanceTypeAndFlowableElementType with non-existing ids
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNonExistingIds() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository
.findByInstanceIdAndInstanceTypeAndFlowableElementType(3L, FlowNodeType.START_EVENT,
FlowableElementType.START_EVENT);

assertEquals(0, result.size());
}

// Edge case: Test for findByFlowInstanceId with non-existing id
@Test
public void testFindByFlowInstanceIdWithNonExistingId() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(3L);

assertEquals(0, result.size());
}

// Edge case: Test for findByIds with non-existing ids
@Test
public void testFindByIdsWithNonExistingIds() {
List<Long> ids = Arrays.asList(3L, 4L);
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

assertEquals(0, result.size());
}

// Error scenario: Test for deleteByFlowInstanceId with non-existing id
@Test(expected =
org.springframework.dao.EmptyResultDataAccessException.class)
@Transactional
public void testDeleteByFlowInstanceIdWithNonExistingId() {
nodeInstanceEntityRepository.deleteByFlowInstanceId(3L);
}

// Error scenario: Test for deleteByInstanceIdAndInstanceType with non-existing ids
@Test(expected =
org.springframework.dao.EmptyResultDataAccessException.class)
@Transactional
public void testDeleteByInstanceIdAndInstanceTypeWithNonExistingIds() {
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(3L, FlowNodeType.START_EVENT);
}
}