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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.NODE,
FlowableElementType.ENTITY);
assertEquals(0, result.size());

result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(2L, FlowNodeType.NODE,
FlowableElementType.ENTITY);
assertEquals(1, result.size());
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertEquals(0, result.size());

result = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
assertTrue(result.size() > 0);
}

@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);
assertEquals(0, result.size());

ids = Arrays.asList(2L, 5L, 7L);
result = nodeInstanceEntityRepository.findByIds(ids);
assertTrue(result.size() > 0);
}

@Test
public void testDeleteByFlowInstanceId() {
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(0, rowsAffected);

rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(2L);
assertTrue(rowsAffected > 0);
}

@Test
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.NODE);
assertEquals(0, rowsAffected);

rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.NODE);
assertTrue(rowsAffected > 0);
}

@Test
public void testSave() {
NodeInstanceEntity entity = new NodeInstanceEntity();
entity.setInstanceId(1L);
entity.setInstanceType(FlowNodeType.NODE);
entity.setFlowableElementType(FlowableElementType.ENTITY);
entity.setFlowInstanceId(2L);
entity.setName("Test Node");

NodeInstanceEntity savedEntity = nodeInstanceEntityRepository.save(entity);
assertNotNull(savedEntity);
assertEquals(entity.getInstanceId(), savedEntity.getInstanceId());
assertEquals(entity.getInstanceType(), savedEntity.getInstanceType());
assertEquals(entity.getFlowableElementType(), savedEntity.getFlowableElementType());
assertEquals(entity.getFlowInstanceId(), savedEntity.getFlowInstanceId());
assertEquals(entity.getName(), savedEntity.getName());
}

@Test
public void testFindAll() {
Sort sort = Sort.by(Sort.Direction.ASC, "id");
PageRequest pageRequest = PageRequest.of(0, 10, sort);
Page<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAll(pageRequest);
assertEquals(0, result.getTotalElements());

nodeInstanceEntityRepository.save(new NodeInstanceEntity(1L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node"));
nodeInstanceEntityRepository.save(new NodeInstanceEntity(2L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node 2"));
result = nodeInstanceEntityRepository.findAll(pageRequest);
assertEquals(2, result.getTotalElements());
}

@Test
public void testCount() {
long count = nodeInstanceEntityRepository.count();
assertEquals(0, count);

nodeInstanceEntityRepository.save(new NodeInstanceEntity(1L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node"));
nodeInstanceEntityRepository.save(new NodeInstanceEntity(2L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node 2"));
count = nodeInstanceEntityRepository.count();
assertEquals(2, count);
}

@Test
public void testExistsById() {
assertFalse(nodeInstanceEntityRepository.existsById(1L));

nodeInstanceEntityRepository.save(new NodeInstanceEntity(1L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node"));
assertTrue(nodeInstanceEntityRepository.existsById(1L));
}

@Test
public void testFindAllById() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
ids.add(3L);

List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findAllById(ids);
assertEquals(0, result.size());

nodeInstanceEntityRepository.save(new NodeInstanceEntity(1L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node"));
nodeInstanceEntityRepository.save(new NodeInstanceEntity(2L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node 2"));
result = nodeInstanceEntityRepository.findAllById(ids);
assertEquals(2, result.size());
}

@Test
public void testDeleteById() {
assertFalse(nodeInstanceEntityRepository.existsById(1L));

NodeInstanceEntity entity = new NodeInstanceEntity(1L, FlowNodeType.NODE, FlowableElementType.ENTITY, 2L, "Test Node");
nodeInstanceEntityRepository.save(entity);
assertTrue(nodeInstanceEntityRepository.existsById(1L));

nodeInstanceEntityRepository.deleteById(1L);
assertFalse(nodeInstanceEntityRepository.existsById(1L));
}
}