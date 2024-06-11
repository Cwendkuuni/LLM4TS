import static org.junit.Assert.*;

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
@ContextConfiguration(locations = { "classpath:application-context.xml" })
public class NodeInstanceEntityRepositoryTest {

@Autowired
private NodeInstanceEntityRepository nodeInstanceEntityRepository;

// JpaRepository findOne test
@Test
public void testFindOne() {
NodeInstanceEntity nodeInstanceEntity = nodeInstanceEntityRepository.findOne(1L);
assertNotNull(nodeInstanceEntity);
}

// JpaRepository findAll test
@Test
public void testFindAll() {
Collection<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
assertTrue(nodeInstanceEntities.size() > 0);
}

// JpaRepository save test
@Test
@Transactional
public void testSave() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
nodeInstanceEntity.setFlowInstanceId(1L);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.ACTIVITY);
nodeInstanceEntityRepository.save(nodeInstanceEntity);
NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.findOne(nodeInstanceEntity.getId());
assertNotNull(savedNodeInstanceEntity);
}

// JpaRepository delete test
@Test
@Transactional
public void testDelete() {
NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
nodeInstanceEntity.setInstanceId(1L);
nodeInstanceEntity.setInstanceType(FlowNodeType.START_EVENT);
nodeInstanceEntity.setFlowInstanceId(1L);
nodeInstanceEntity.setFlowableElementType(FlowableElementType.ACTIVITY);
nodeInstanceEntityRepository.save(nodeInstanceEntity);
nodeInstanceEntityRepository.delete(nodeInstanceEntity);
NodeInstanceEntity deletedNodeInstanceEntity = nodeInstanceEntityRepository.findOne(nodeInstanceEntity.getId());
assertNull(deletedNodeInstanceEntity);
}

// JpaSpecificationExecutor findAll test
@Test
public void testFindAllBySpecification() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(null);
assertTrue(nodeInstanceEntities.size() > 0);
}

// findByInstanceIdAndInstanceTypeAndFlowableElementType test
@Test
public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
1L, FlowNodeType.START_EVENT, FlowableElementType.ACTIVITY);
assertTrue(nodeInstanceEntities.size() > 0);
}

// findByFlowInstanceId test
@Test
public void testFindByFlowInstanceId() {
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
assertTrue(nodeInstanceEntities.size() > 0);
}

// findByIds test
@Test
public void testFindByIds() {
List<Long> ids = Arrays.asList(1L, 2L, 3L);
List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
assertTrue(nodeInstanceEntities.size() > 0);
}

// deleteByFlowInstanceId test
@Test
@Transactional
public void testDeleteByFlowInstanceId() {
int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
assertEquals(rowsAffected, 3);
}

// deleteByInstanceIdAndInstanceType test
@Test
@Transactional
public void testDeleteByInstanceIdAndInstanceType() {
int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);
assertEquals(rowsAffected, 2);
}

// Native query pagination test
@Test
public void testFindByInstanceIdNativePagination() {
Pageable pageable = new PageRequest(0, 10);
Page<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdNativePagination(1L,
pageable);
assertTrue(nodeInstanceEntities.getContent().size() > 0);
}

// Native query count test
@Test
public void testCountByInstanceTypeNative() {
long count = nodeInstanceEntityRepository.countByInstanceTypeNative(FlowNodeType.USER_TASK);
assertTrue(count > 0);
}

}