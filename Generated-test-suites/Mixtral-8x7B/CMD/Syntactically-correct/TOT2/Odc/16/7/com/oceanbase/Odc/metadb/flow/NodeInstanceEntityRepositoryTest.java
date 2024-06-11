import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// Test for JpaRepository.save(S s)
@Test
public void testSave() {
NodeInstanceEntity ni = new NodeInstanceEntity();
ni = nodeInstanceEntityRepository.save(ni);
assertNotNull(ni.getId());
}

// Test for JpaRepository.findById(ID id)
@Test
public void testFindById() {
NodeInstanceEntity ni = new NodeInstanceEntity();
ni = nodeInstanceEntityRepository.save(ni);
Optional<NodeInstanceEntity> optionalNi = nodeInstanceEntityRepository.findById(ni.getId());
assertTrue(optionalNi.isPresent());
assertEquals(ni, optionalNi.get());
}

// Test for JpaRepository.findAll()
@Test
public void testFindAll() {
Iterable<NodeInstanceEntity> niIterable = nodeInstanceEntityRepository.findAll();
List<NodeInstanceEntity> niList = new ArrayList<>();
niIterable.forEach(niList::add);
assertTrue(niList.size() > 0);
}

// Test for JpaRepository.deleteById(ID id)
@Test
public void testDeleteById() {
NodeInstanceEntity ni = new NodeInstanceEntity();
ni = nodeInstanceEntityRepository.save(ni);
nodeInstanceEntityRepository.deleteById(ni.getId());
Optional<NodeInstanceEntity> optionalNi = nodeInstanceEntityRepository.findById(ni.getId());
assertFalse(optionalNi.isPresent());
}

// Test for findByInstanceIdAndInstanceTypeAndFlowableElementType
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> niList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
assertTrue(niList.size() > 0);
}

// Test for findByFlowInstanceId
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> niList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertTrue(niList.size() > 0);
}

// Test for findByIds
@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> niList = nodeInstanceEntityRepository.findByIds(ids);
assertTrue(niList.size() > 0);
}

// Test for deleteByFlowInstanceId
@Test
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;
int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
assertEquals(result, 1);
}

// Test for deleteByInstanceIdAndInstanceType
@Test
public void testDeleteByInstanceIdAndInstanceType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START_EVENT;
int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
assertEquals(result, 1);
}
}