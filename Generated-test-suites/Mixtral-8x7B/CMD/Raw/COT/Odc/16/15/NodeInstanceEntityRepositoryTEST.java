import com.oceanbase.odc.core.flow.model.FlowableElementType;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntityList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(0, nodeInstanceEntityList.size());

        // Insert test data
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        ReflectionTestUtils.setField(nodeInstanceEntity, "instanceId", instanceId);
        ReflectionTestUtils.setField(nodeInstanceEntity, "instanceType", instanceType.name());
        ReflectionTestUtils.setField(nodeInstanceEntity, "flowableElementType", flowableElementType.name());
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntityList = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(1, nodeInstanceEntityList.size());
        NodeInstanceEntity retrievedNodeInstanceEntity = nodeInstanceEntityList.get(0);
        assertNotNull(retrievedNodeInstanceEntity);
        assertEquals(instanceId, retrievedNodeInstanceEntity.getInstanceId());
        assertEquals(instanceType.name(), retrievedNodeInstanceEntity.getInstanceType());
        assertEquals(flowableElementType.name(), retrievedNodeInstanceEntity.getFlowableElementType());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntityList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(0, nodeInstanceEntityList.size());

        // Insert test data
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        ReflectionTestUtils.setField(nodeInstanceEntity, "flowInstanceId", flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntityList = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, nodeInstanceEntityList.size());
        NodeInstanceEntity retrievedNodeInstanceEntity = nodeInstanceEntityList.get(0);
        assertNotNull(retrievedNodeInstanceEntity);
        assertEquals(flowInstanceId, retrievedNodeInstanceEntity.getFlowInstanceId());
    }

    @Test
    public void testFindByIds() {
        Long id1 = 1L;
        Long id2 = 2L;

        List<Long> ids = new ArrayList<>(Arrays.asList(id1, id2));

        List<NodeInstanceEntity> nodeInstanceEntityList = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(0, nodeInstanceEntityList.size());

        // Insert test data
        NodeInstanceEntity nodeInstanceEntity1 = new NodeInstanceEntity();
        ReflectionTestUtils.setField(nodeInstanceEntity1, "id", id1);
        nodeInstanceEntityRepository.save(nodeInstanceEntity1);

        NodeInstanceEntity nodeInstanceEntity2 = new NodeInstanceEntity();
        ReflectionTestUtils.setField(nodeInstanceEntity2, "id", id2);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        nodeInstanceEntityList = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(2, nodeInstanceEntityList.size());
        assertEquals(id1, nodeInstanceEntityList.get(0).getId());
        assertEquals(id2, nodeInstanceEntityList.get(1).getId());
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        int rowCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "flow_instance_node");
        nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(rowCount - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "flow_instance_node"));
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START_EVENT;

        int rowCount = JdbcTestUtils.countRowsInTable(jdbcTemplate, "flow_instance_node");
        nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(rowCount - 1, JdbcTestUtils.countRowsInTable(jdbcTemplate, "flow_instance_node"));
    }
}