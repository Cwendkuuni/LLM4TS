import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.PROCESS, FlowableElementType.SQL);
assertEquals(2, nodeInstanceEntities.size());
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindByIds() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(2, nodeInstanceEntities.size());
}

@Test
public void testDeleteByFlowInstanceId() {
int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(5, result);
}

@Test
public void testDeleteByInstanceIdAndInstanceType() {
int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS);
assertEquals(2, result);
}

@Test
public void testSave() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.SQL);
nodeInstanceEntity.setName("test_node");
nodeInstanceEntityRepository.save(nodeInstanceEntity);
assertNotNull(nodeInstanceEntity.getId());
}

@Test
public void testFindAll() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
assertTrue(nodeInstanceEntities.size() > 0);
}

@Test
public void testCount() {
long count = nodeInstanceEntityRepository.count();
assertTrue(count > 0);
}

@Test
public void testExistsById() {
Long id = 1L;
boolean exists = nodeInstanceEntityRepository.existsById(id);
assertTrue(exists);
}

@Test
public void testFindById() {
Long id = 1L;
NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(id).get();
assertNotNull(nodeInstanceEntity);
}

@Test
public void testSaveAll() {
List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
for (long i = 0; i < 10; i++) {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.SQL);
nodeInstanceEntity.setName("test_node_" + i);
nodeInstanceEntities.add(nodeInstanceEntity);
}
nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);
List<NodeInstanceEntity> savedNodeInstanceEntities = nodeInstanceEntityRepository.findAll();
assertEquals(10, savedNodeInstanceEntities.size());
}

@Test
public void testDeleteAll() {
List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
for (long i = 0; i < 10; i++) {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.SQL);
nodeInstanceEntity.setName("test_node_" + i);
nodeInstanceEntities.add(nodeInstanceEntity);
}
nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);
int result = nodeInstanceEntityRepository.deleteAll();
assertEquals(10, result);
}

@Test
public void testFlush() {
List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
for (long i = 0; i < 10; i++) {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.SQL);
nodeInstanceEntity.setName("test_node_" + i);
nodeInstanceEntities.add(nodeInstanceEntity);
}
nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);
nodeInstanceEntityRepository.flush();
List<NodeInstanceEntity> savedNodeInstanceEntities = nodeInstanceEntityRepository.findAll();
assertEquals(10, savedNodeInstanceEntities.size());
}

@Test
public void testDetach() {
List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
for (long i = 0; i < 10; i++) {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.PROCESS);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.SQL);
nodeInstanceEntity.setName("test_node_" + i);
nodeInstanceEntities.add(nodeInstanceEntity);
}
nodeInstanceEntityRepository.saveAll(nodeInstanceEntities);
NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findById(1L).get();
nodeInstanceEntityRepository.detach(nodeInstanceEntity);
}

@Test
public void testGetOne() {
Long id = 1L;
NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.getOne(id);
assertNotNull(nodeInstanceEntity);
}

@Test
public void testFindAllById() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllById(ids);
assertEquals(2, nodeInstanceEntities.size());
}

@Test
public void testDeleteById() {
Long id = 1L;
nodeInstanceEntityRepository.deleteById(id);
}

@Test
public void testExistsAllById() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
boolean exists = nodeInstanceEntityRepository.existsAllById(ids);
assertTrue(exists);
}

@Test
public void testFindAllByInstanceId() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceId(1L);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceType(FlowNodeType.PROCESS);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindAllByFlowableElementType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByFlowableElementType(FlowableElementType.SQL);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdAndInstanceType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findAllByInstanceIdAndInstanceType(1L, FlowNodeType.PROCESS);
assertEquals(2, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdAndFlowableElementType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findAllByInstanceIdAndFlowableElementType(1L,
FlowableElementType.SQL);
assertEquals(2, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceRepository.findAllByInstanceTypeAndFlowableElementType(FlowNodeType.PROCESS,
FlowableElementType.SQL);
assertEquals(2, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdIn() {
List<Long> instanceIds = new ArrayList<>();
instanceIds.add(1L);
instanceIds.add(2L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceIdIn(instanceIds);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceTypeIn() {
List<FlowNodeType> instanceTypes = new ArrayList<>();
instanceTypes.add(FlowNodeType.PROCESS);
instanceTypes.add(FlowNodeType.START_EVENT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceTypeIn(instanceTypes);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindAllByFlowableElementTypeIn() {
List<FlowableElementType> flowableElementTypes = new ArrayList<>();
flowableElementTypes.add(FlowableElementType.SQL);
flowableElementTypes.add(FlowableElementType.SCRIPT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByFlowableElementTypeIn(flowableElementTypes);
assertEquals(5, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdAndInstanceTypeIn() {
List<Long> instanceIds = new ArrayList<>();
instanceIds.add(1L);
instanceIds.add(2L);
List<FlowNodeType> instanceTypes = new ArrayList<>();
instanceTypes.add(FlowNodeType.PROCESS);
instanceTypes.add(FlowNodeType.START_EVENT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceIdAndInstanceTypeIn(instanceIds,
instanceTypes);
assertEquals(4, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdAndFlowableElementTypeIn() {
List<Long> instanceIds = new ArrayList<>();
instanceIds.add(1L);
instanceIds.add(2L);
List<FlowableElementType> flowableElementTypes = new ArrayList<>();
flowableElementTypes.add(FlowableElementType.SQL);
flowableElementTypes.add(FlowableElementType.SCRIPT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceIdAndFlowableElementTypeIn(instanceIds,
flowableElementTypes);
assertEquals(4, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceTypeAndFlowableElementTypeIn() {
List<FlowNodeType> instanceTypes = new ArrayList<>();
instanceTypes.add(FlowNodeType.PROCESS);
instanceTypes.add(FlowNodeType.START_EVENT);
List<FlowableElementType> flowableElementTypes = new ArrayList<>();
flowableElementTypes.add(FlowableElementType.SQL);
flowableElementTypes.add(FlowableElementType.SCRIPT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceTypeAndFlowableElementTypeIn(instanceTypes,
flowableElementTypes);
assertEquals(4, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdInAndInstanceTypeIn() {
List<Long> instanceIds = new ArrayList<>();
instanceIds.add(1L);
instanceIds.add(2L);
List<FlowNodeType> instanceTypes = new ArrayList<>();
instanceTypes.add(FlowNodeType.PROCESS);
instanceTypes.add(FlowNodeType.START_EVENT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceIdInAndInstanceTypeIn(instanceIds,
instanceTypes);
assertEquals(4, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdInAndFlowableElementTypeIn() {
List<Long> instanceIds = new ArrayList<>();
instanceIds.add(1L);
instanceIds.add(2L);
List<FlowableElementType> flowableElementTypes = new ArrayList<>();
flowableElementTypes.add(FlowableElementType.SQL);
flowableElementTypes.add(FlowableElementType.SCRIPT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceIdInAndFlowableElementTypeIn(instanceIds,
flowableElementTypes);
assertEquals(4, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceTypeInAndFlowableElementTypeIn() {
List<FlowNodeType> instanceTypes = new ArrayList<>();
instanceTypes.add(FlowNodeType.PROCESS);
instanceTypes.add(FlowNodeType.START_EVENT);
List<FlowableElementType> flowableElementTypes = new ArrayList<>();
flowableElementTypes.add(FlowableElementType.SQL);
flowableElementTypes.add(FlowableElementType.SCRIPT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceTypeInAndFlowableElementTypeIn(instanceTypes,
flowableElementTypes);
assertEquals(4, nodeInstanceEntities.size());
}

@Test
public void testFindAllByInstanceIdInAndInstanceTypeInAndFlowableElementTypeIn() {
List<Long> instanceIds = new ArrayList<>();
instanceIds.add(1L);
instanceIds.add(2L);
List<FlowNodeType> instanceTypes = new ArrayList<>();
instanceTypes.add(FlowNodeType.PROCESS);
instanceTypes.add(FlowNodeType.START_EVENT);
List<FlowableElementType> flowableElementTypes = new ArrayList<>();
flowableElementTypes.add(FlowableElementType.SQL);
flowableElementTypes.add(FlowableElementType.SCRIPT);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAllByInstanceIdInAndInstanceTypeInAndFlowableElementTypeIn(instanceIds,
instanceTypes, flowableElementTypes);
assertEquals(4, nodeInstanceEntities.size());
}
}