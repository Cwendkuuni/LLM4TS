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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 @Test
 @Transactional
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
 assertEquals(1, nodeInstances.size());
 NodeInstanceEntity nodeInstance = nodeInstances.get(0);
 assertEquals(1L, nodeInstance.getInstanceId().longValue());
 assertEquals(FlowNodeType.START_EVENT, nodeInstance.getInstanceType());
 assertEquals(FlowableElementType.START_EVENT, nodeInstance.getFlowableElementType());
 }

 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assertTrue(nodeInstances.size() > 0);
 NodeInstanceEntity nodeInstance = nodeInstances.get(0);
 assertEquals(1L, nodeInstance.getFlowInstanceId().longValue());
 }

 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findByIds(ids);
 assertTrue(nodeInstances.size() >= 3);
 }

 @Test
 @Transactional
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assertEquals(2, rowsAffected);
 }

 @Test
 @Transactional
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
 assertEquals(1, rowsAffected);
 }

 @Test
 public void testFindAll() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<NodeInstanceEntity> nodeInstances = nodeInstanceEntityRepository.findAll(pageable);
 assertEquals(3, nodeInstances.getTotalElements());
 }

 @Test
 public void testSave() {
 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setInstanceId(1L);
 nodeInstance.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstance.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstance.setFlowInstanceId(1L);
 nodeInstance.setName("Test Node");

 NodeInstanceEntity savedNodeInstance = nodeInstanceEntityRepository.save(nodeInstance);
 assertNotNull(savedNodeInstance.getId());
 }
}

@Configuration
@EnableJpaRepositories(basePackages = "com.oceanbase.odc.metadb.flow")
class NodeInstanceEntityRepositoryTestConfig {

 @Bean
 public NodeInstanceEntityRepository nodeInstanceEntityRepository() {
 return new NodeInstanceEntityRepository() {
 };
 }

 @Bean
 public FlowNodeType startEvent() {
 return FlowNodeType.START_EVENT;
 }

 @Bean
 public FlowNodeType endEvent() {
 return FlowNodeType.END_EVENT;
 }

 @Bean
 public FlowableElementType startEventType() {
 return FlowableElementType.START_EVENT;
 }

 @Bean
 public List<NodeInstanceEntity> nodeInstances() {
 List<NodeInstanceEntity> nodeInstances = new ArrayList<>();
 NodeInstanceEntity nodeInstance = new NodeInstanceEntity();
 nodeInstance.setInstanceId(1L);
 nodeInstance.setInstanceType(FlowNodeType.START_EVENT);
 nodeInstance.setFlowableElementType(FlowableElementType.START_EVENT);
 nodeInstance.setFlowInstanceId(1L);
 nodeInstances.add(nodeInstance);

 nodeInstance = new NodeInstanceEntity();
 nodeInstance.setInstanceId(1L);
 nodeInstance.setInstanceType(FlowNodeType.END_EVENT);
 nodeInstance.setFlowableElementType(FlowableElementType.END_EVENT);
 nodeInstance.setFlowInstanceId(1L);
 nodeInstances.add(nodeInstance);
 return nodeInstances;
 }
}