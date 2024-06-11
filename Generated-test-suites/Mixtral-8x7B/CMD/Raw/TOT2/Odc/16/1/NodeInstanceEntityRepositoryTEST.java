import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

private NodeInstanceEntity nodeInstanceEntity1;
private NodeInstanceEntity nodeInstanceEntity2;
private List<NodeInstanceEntity> nodeInstanceEntityList;

@Before
public void setup() {
nodeInstanceEntity1 = new NodeInstanceEntity();
nodeInstanceEntity1.setInstanceId(1L);
nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
nodeInstanceEntity1.setFlowableElementType(FlowableElementType.START_EVENT);

nodeInstanceEntity2 = new NodeInstanceEntity();
nodeInstanceEntity2.setInstanceId(2L);
nodeInstanceEntity2.setInstanceType(FlowNodeType.END_EVENT);
nodeInstanceEntity2.setFlowableElementType(FlowableElementType.END_EVENT);

nodeInstanceEntityList = new ArrayList<>(Arrays.asList(nodeInstanceEntity1, nodeInstanceEntity2));
}

@After
public void teardown() {
nodeInstanceEntityRepository.deleteAll();
}

@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);

assertEquals(nodeInstanceEntityList, resultList);
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

assertEquals(nodeInstanceEntityList, resultList);
}

@Test
public void testFindByIds() {
List<Long> ids = new ArrayList<>(Arrays.asList(1L, 2L));
List<NodeInstanceEntity> resultList = nodeInstanceEntityRepository.findByIds(ids);

assertEquals(nodeInstanceEntityList, resultList);
}

@Test
public void testDeleteByFlowInstanceId() {
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

assertEquals(2, rowsAffected);
}

@Test
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

assertEquals(1, rowsAffected);
}

@Test
public void testSave() {
NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity1);

assertNotNull(savedNodeInstanceEntity.getId());
assertEquals(nodeInstanceEntity1, savedNodeInstanceEntity);
}

@Test
public void testFindById() {
Long id = 1L;
NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity1);
Optional<NodeInstanceEntity> resultOptional = nodeInstanceEntityRepository.findById(id);

assertTrue(resultOptional.isPresent());
assertEquals(savedNodeInstanceEntity, resultOptional.get());
}

@Test
public void testFindAll() {
nodeInstanceEntityRepository.saveAll(nodeInstanceEntityList);
List<NodeInstanceEntity> resultList = new ArrayList<>(nodeInstanceEntityRepository.findAll());

assertEquals(nodeInstanceEntityList, resultList);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InvalidInstanceType() {
nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, null,
FlowableElementType.START_EVENT);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InvalidFlowableElementType() {
nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, null);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_InvalidInstanceId() {
nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null,
FlowNodeType.START_EVENT, FlowableElementType.START_EVENT);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByFlowInstanceId_InvalidFlowInstanceId() {
nodeInstanceEntityRepository.findByFlowInstanceId(null);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByIds_InvalidIds() {
List<Long> ids = null;
nodeInstanceEntityRepository.findByIds(ids);
}

@Test(expected = IllegalArgumentException.class)
public void testDeleteByFlowInstanceId_InvalidFlowInstanceId() {
nodeInstanceEntityRepository.deleteByFlowInstanceId(null);
}

@Test(expected = IllegalArgumentException.class)
public void testDeleteByInstanceIdAndInstanceType_InvalidInstanceId() {
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(null, FlowNodeType.START_EVENT);
}

@Test(expected = IllegalArgumentException.class)
public void testDeleteByInstanceIdAndInstanceType_InvalidInstanceType() {
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, null);
}

@Test(expected = IllegalArgumentException.class)
public void testSave_NullNodeInstanceEntity() {
nodeInstanceEntityRepository.save(null);
}

@Test(expected = IllegalArgumentException.class)
public void testFindById_InvalidId() {
nodeInstanceEntityRepository.findById(-1L);
}

@Test(expected = IllegalArgumentException.class)
public void testFindAll_EmptyList() {
nodeInstanceEntityRepository.findAll();
}
}