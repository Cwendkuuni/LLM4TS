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
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

 @Autowired
 private TestEntityManager entityManager;

 @Autowired
 private NodeInstanceEntityRepository nodeInstanceEntityRepository;

 private List<NodeInstanceEntity> nodeInstanceEntities;

 @Before
 public void setup() {
 nodeInstanceEntities = new ArrayList<>();
 NodeInstanceEntity nodeInstanceEntity1 = NodeInstanceEntity.builder()
 .id(1L)
 .instanceId(1L)
 .instanceType(FlowNodeType.START)
 .flowableElementType(FlowableElementType.SERVICE_TASK)
 .build();
 NodeInstanceEntity nodeInstanceEntity2 = NodeInstanceEntity.builder()
 .id(2L)
 .instanceId(1L)
 .instanceType(FlowNodeType.END)
 .flowableElementType(FlowableElementType.BOUNDARY_EVENT)
 .build();
 nodeInstanceEntities.add(nodeInstanceEntity1);
 nodeInstanceEntities.add(nodeInstanceEntity2);
 entityManager.persist(nodeInstanceEntity1);
 entityManager.persist(nodeInstanceEntity2);
 entityManager.flush();
 }

 @After
 public void cleanup() {
 nodeInstanceEntityRepository.deleteAll();
 entityManager.clear();
 }

 // Test for findByInstanceIdAndInstanceTypeAndFlowableElementType method
 @Test
 public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
 FlowNodeType.START, FlowableElementType.SERVICE_TASK);
 assert (result.size() == 1);
 assert (result.get(0).getId().equals(1L));
 }

 // Test for findByFlowInstanceId method
 @Test
 public void testFindByFlowInstanceId() {
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
 assert (result.size() == 2);
 }

 // Test for findByIds method
 @Test
 public void testFindByIds() {
 List<Long> ids = Arrays.asList(1L, 2L);
 List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
 assert (result.size() == 2);
 }

 // Test for deleteByFlowInstanceId method
 @Test
 public void testDeleteByFlowInstanceId() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
 assert (rowsAffected == 2);
 }

 // Test for deleteByInstanceIdAndInstanceType method
 @Test
 public void testDeleteByInstanceIdAndInstanceType() {
 int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
 assert (rowsAffected == 1);
 }

}