import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 private List<NodeInstanceEntity> nodeInstanceEntities;

 @Before
 public void setup() {
 nodeInstanceEntities = new ArrayList<>();
 nodeInstanceEntities.add(NodeInstanceEntity.builder()
 .id(1L)
 .flowInstanceId(1L)
 .instanceId(1L)
 .instanceType(FlowNodeType.START_EVENT)
 .flowableElementType(FlowableElementType.START_EVENT)
 .build());
 nodeInstanceEntities.add(NodeInstanceEntity.builder()
 .id(2L)
 .flowInstanceId(1L)
 .instanceId(2L)
 .instanceType(FlowNodeType.USER_TASK)
 .flowableElementType(FlowableElementType.USER_TASK)
 .build());
 nodeInstanceEntities.add(NodeInstanceEntity.builder()
 .id(3L)
 .flowInstanceId(2L)
 .instanceId(3L)
 .instanceType(FlowNodeType.END_EVENT)
 .flowableElementType(FlowableElementType.END_EVENT)
 .build());
 }

 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
 1L, FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, result.size());
 assertEquals(nodeInstanceEntities.get(0), result.get(0));
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertEquals(2, result.size());
 assertEquals(nodeInstanceEntities.subList(0, 2), result);
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 assertEquals(3, result.size());
 assertEquals(nodeInstanceEntities, result);
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int beforeSize = nodeInstanceEntityRepository.findAll().size();
 nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(beforeSize - 2, nodeInstanceEntityRepository.findAll().size());
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int beforeSize = nodeInstanceEntityRepository.findAll().size();
 nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(beforeSize - 1, nodeInstanceEntityRepository.findAll().size());
 }
}