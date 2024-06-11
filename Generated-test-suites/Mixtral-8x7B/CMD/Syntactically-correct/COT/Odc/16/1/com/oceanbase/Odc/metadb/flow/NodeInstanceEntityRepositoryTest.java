import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collections;
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
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NodeInstanceEntityRepositoryTestConfig.class)
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START, FlowableElementType.NODE);
        assertEquals(nodeInstanceEntities.size(), 1);
        assertEquals(nodeInstanceEntities.get(0).getInstanceId(), 1L);
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType_EmptyResult() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                2L, FlowNodeType.START, FlowableElementType.NODE);
        assertEquals(nodeInstanceEntities.size(), 0);
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(1L);
        assertEquals(nodeInstanceEntities.size(), 1);
        assertEquals(nodeInstanceEntities.get(0).getInstanceId(), 1L);
    }

    @Test
    public void testFindByFlowInstanceId_EmptyResult() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(2L);
        assertEquals(nodeInstanceEntities.size(), 0);
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(nodeInstanceEntities.size(), 1);
        assertEquals(nodeInstanceEntities.get(0).getInstanceId(), 1L);
    }

    @Test
    public void testFindByIds_EmptyResult() {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(nodeInstanceEntities.size(), 0);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId_ZeroResult() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(2L);
        assertEquals(result, 0);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType_ZeroResult() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(2L, FlowNodeType.START);
        assertEquals(result, 0);
    }

}