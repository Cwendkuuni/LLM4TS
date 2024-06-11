import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
@Transactional
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START_EVENT, FlowableElementType.BOUNDARY_EVENT);
assertEquals(0, nodeInstances.size());
}

// Test for findByFlowInstanceId method
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(0, nodeInstances.size());
}

// Test for findByIds method
@Test
public void testFindByIds() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(0, nodeInstances.size());
}

// Test for deleteByFlowInstanceId method
@Test
public void testDeleteByFlowInstanceId() {
int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(0, result);
}

// Test for deleteByInstanceIdAndInstanceType method
@Test
public void testDeleteByInstanceIdAndInstanceType() {
int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
assertEquals(0, result);
}
}