import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository repository;

@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> entities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
FlowNodeType.START_EVENT, FlowableElementType.NODE);
assertEquals(3, entities.size());
}

@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> entities = repository.findByFlowInstanceId(1L);
assertEquals(5, entities.size());
}

@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> entities = repository.findByIds(ids);
assertEquals(3, entities.size());
}

@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int rowsAffected = repository.deleteByFlowInstanceId(1L);
assertEquals(3, rowsAffected);
}

@Test
@Transactional
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = repository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
assertEquals(1, rowsAffected);
}

@Test
public void testSave() {
NodeInstanceEntity entity = new NodeInstanceEntity();
entity.setInstanceId(1L);
entity.setInstanceType(FlowNodeType.START_EVENT);
entity.setFlowableElementType(FlowableElementType.NODE);
repository.save(entity);
Optional<NodeInstanceEntity> savedEntity = repository.findById(entity.getId());
assertTrue(savedEntity.isPresent());
assertEquals(entity, savedEntity.get());
}

@Test
public void testUpdate() {
NodeInstanceEntity entity = new NodeInstanceEntity();
entity.setInstanceId(1L);
entity.setInstanceType(FlowNodeType.START_EVENT);
entity.setFlowableElementType(FlowableElementType.NODE);
entity.setId(1L);
repository.save(entity);
entity.setInstanceType(FlowNodeType.END_EVENT);
repository.save(entity);
Optional<NodeInstanceEntity> updatedEntity = repository.findById(entity.getId());
assertTrue(updatedEntity.isPresent());
assertEquals(FlowNodeType.END_EVENT, updatedEntity.get().getInstanceType());
}

@Test(expected = IllegalArgumentException.class)
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNullId() {
repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(null, FlowNodeType.START_EVENT,
FlowableElementType.NODE);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithNegativeId() {
repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(-1L, FlowNodeType.START_EVENT,
FlowableElementType.NODE);
}

@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementTypeWithEmptyList() {
List<NodeInstanceEntity> entities = repository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L,
null, null);
assertEquals(0, entities.size());
}

@Test
public void testFindByFlowInstanceIdWithEmptyList() {
List<NodeInstanceEntity> entities = repository.findByFlowInstanceId(null);
assertEquals(0, entities.size());
}

@Test
public void testFindByIdsWithEmptyList() {
List<Long> ids = new ArrayList<Long>();
List<NodeInstanceEntity> entities = repository.findByIds(ids);
assertEquals(0, entities.size());
}

@Test
@Transactional
public void testDeleteByFlowInstanceIdWithZero() {
int rowsAffected = repository.deleteByFlowInstanceId(0L);
assertEquals(0, rowsAffected);
}

@Test
@Transactional
public void testDeleteByInstanceIdAndInstanceTypeWithZero() {
int rowsAffected = repository.deleteByInstanceIdAndInstanceType(0L, FlowNodeType.START_EVENT);
assertEquals(0, rowsAffected);
}

@Test(expected = IllegalArgumentException.class)
public void testSaveWithNullEntity() {
repository.save(null);
}

@Test(expected = IllegalArgumentException.class)
public void testUpdateWithNullEntity() {
repository.save(null);
}

@Test
public void testFindByIdWithEmptyOptional() {
Optional<NodeInstanceEntity> entity = repository.findById(1L);
assertFalse(entity.isPresent());
}
}