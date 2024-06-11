import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

private NodeInstanceEntity nodeInstanceEntity1;
private NodeInstanceEntity nodeInstanceEntity2;
private NodeInstanceEntity nodeInstanceEntity3;

@Before
public void setup() {
nodeInstanceEntity1 = new NodeInstanceEntity();
nodeInstanceEntity1.setInstanceId(1L);
nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
nodeInstanceEntity1.setFlowableElementType(FlowableElementType.BOUNDARY_EVENT);

nodeInstanceEntity2 = new NodeInstanceEntity();
nodeInstanceEntity2.setInstanceId(1L);
nodeInstanceEntity2.setInstanceType(FlowNodeType.END_EVENT);
nodeInstanceEntity2.setFlowableElementType(FlowableElementType.INTERMEDIATE_THROW_EVENT);

nodeInstanceEntity3 = new NodeInstanceEntity();
nodeInstanceEntity3.setInstanceId(2L);
nodeInstanceEntity3.setInstanceType(FlowNodeType.USER_TASK);
nodeInstanceEntity3.setFlowableElementType(FlowableElementType.BOUNDARY_EVENT);

nodeInstanceEntityRepository.saveAll(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2, nodeInstanceEntity3));
}

@After
public void teardown() {
nodeInstanceEntityRepository.deleteAll();
}

// Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.BOUNDARY_EVENT);
assertEquals(1, result.size());
assertEquals(nodeInstanceEntity1, result.get(0));
}

// Test for edge case when there is no data found
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_noDataFound() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(3L,
FlowNodeType.START_EVENT, FlowableElementType.BOUNDARY_EVENT);
assertEquals(0, result.size());
}

// Test for findByFlowInstanceId method
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(2, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2), result);
}

// Test for edge case when there is no data found
@Test
public void testFindByFlowInstanceId_noDataFound() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(3L);
assertEquals(0, result.size());
}

// Test for findByIds method
@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L);
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(2, result.size());
assertEquals(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2), result);
}

// Test for edge case when there is no data found
@Test
public void testFindByIds_noDataFound() {
List<Long> ids = Arrays.asList(3L, 4L);
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(0, result.size());
}

// Test for deleteByFlowInstanceId method
@Test
public void testDeleteByFlowInstanceId() {
int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(2, result);
List<NodeInstanceEntity> allNodes = nodeInstanceEntityRepository.findAll();
assertEquals(1, allNodes.size());
assertEquals(nodeInstanceEntity3, allNodes.get(0));
}

// Test for deleteByInstanceIdAndInstanceType method
@Test
public void testDeleteByInstanceIdAndInstanceType() {
int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
assertEquals(1, result);
List<NodeInstanceEntity> allNodes = nodeInstanceEntityRepository.findAll();
assertEquals(1, allNodes.size());
assertEquals(nodeInstanceEntity3, allNodes.get(0));
}

// Test for exception handling when passing null values
@Test(expected = NullPointerException.class)
public void testDeleteByInstanceIdAndInstanceType_nullValues() {
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, null);
}

}