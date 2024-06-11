import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// Test for JpaRepository.save(S entity)
@Test
public void testSaveNodeInstanceEntity() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
assertThat(nodeInstanceEntity).isNotNull();
}

// Test for JpaRepository.findById(ID id)
@Test
public void testFindNodeInstanceEntityById() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
assertThat(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId())).isPresent();
}

// Test for JpaRepository.findAll()
@Test
public void testFindAllNodeInstanceEntities() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
assertThat(nodeInstanceEntities).isNotNull().isNotEmpty();
}

// Test for JpaRepository.deleteById(ID id)
@Test
public void testDeleteNodeInstanceEntityById() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity = nodeInstanceEntityRepository.save(nodeInstanceEntity);
nodeInstanceEntityRepository.deleteById(nodeInstanceEntity.getId());
assertThat(nodeInstanceEntityRepository.findById(nodeInstanceEntity.getId())).isEmpty();
}

// Test for custom query "findByInstanceIdAndInstanceTypeAndFlowableElementType"
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, FlowableElementType.NODE);
assertThat(nodeInstanceEntities).isNotNull().isEmpty();
}

// Test for NodeInstanceEntityRepository.findByFlowInstanceId(Long flowInstanceId)
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertThat(nodeInstanceEntities).isNotNull().isEmpty();
}

// Test for custom query "findByIds"
@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
assertThat(nodeInstanceEntities).isNotNull().isEmpty();
}

// Test for @Transactional, @Query and @Modifying "deleteByFlowInstanceId"
@Test
public void testDeleteByFlowInstanceId() {
Long instanceId = 1L;
nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
int deletedCount = nodeInstanceEntityRepository.getDeletedRowsByFlowInstanceId(instanceId);
assertEquals(deletedCount, 1);
}

// Test for @Transactional, @Query and @Modifying "deleteByInstanceIdAndInstanceType"
@Test
public void testDeleteByInstanceIdAndInstanceType() {
Long instanceId = 1L;
FlowNodeType instanceType = FlowNodeType.START;
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
int deletedCount = nodeInstanceEntityRepository.getDeletedRowsByInstanceIdAndInstanceType(instanceId, instanceType);
assertEquals(deletedCount, 1);
}

// Negative test case for "findByInstanceIdAndInstanceTypeAndFlowableElementType"
@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_Negative() {
nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(1L, FlowNodeType.START, null);
}

// Negative test case for "findByFlowInstanceId"
@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
public void testFindByFlowInstanceId_Negative() {
nodeInstanceEntityRepository.findByFlowInstanceId(2L);
}

// Negative test case for "findByIds"
@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
public void testFindByIds_Negative() {
List<Long> ids = Arrays.asList(4L, 5L, 6L);
nodeInstanceEntityRepository.findByIds(ids);
}

// Negative test case for "deleteByFlowInstanceId"
@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
public void testDeleteByFlowInstanceId_Negative() {
Long instanceId = 2L;
nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
}

// Negative test case for "deleteByInstanceIdAndInstanceType"
@Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
public void testDeleteByInstanceIdAndInstanceType_Negative() {
Long instanceId = 2L;
FlowNodeType instanceType = FlowNodeType.START;
nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
}
}