import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import com.oceanbase.odc.metadb.flow.FlowableElementType;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
@Transactional
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    private NodeInstanceEntity nodeInstanceEntity1;
    private NodeInstanceEntity nodeInstanceEntity2;
    private List<Long> ids;

    @Before
    public void setup() {
        nodeInstanceEntity1 = new NodeInstanceEntity();
        nodeInstanceEntity1.setInstanceId(1L);
        nodeInstanceEntity1.setInstanceType(FlowNodeType.START_EVENT);
        nodeInstanceEntity1.setFlowInstanceId(1L);
        nodeInstanceEntity1.setFlowableElementType(FlowableElementType.USER_TASK);

        nodeInstanceEntity2 = new NodeInstanceEntity();
        nodeInstanceEntity2.setInstanceId(2L);
        nodeInstanceEntity2.setInstanceType(FlowNodeType.END_EVENT);
        nodeInstanceEntity2.setFlowInstanceId(1L);
        nodeInstanceEntity2.setFlowableElementType(FlowableElementType.USER_TASK);

        nodeInstanceEntityRepository.save(nodeInstanceEntity1);
        nodeInstanceEntityRepository.save(nodeInstanceEntity2);

        ids = new ArrayList<>();
        ids.add(nodeInstanceEntity1.getId());
        ids.add(nodeInstanceEntity2.getId());
    }

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(
                1L, FlowNodeType.START_EVENT, FlowableElementType.USER_TASK);

        assertEquals(1, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
    }

    @Test
    public void testFindByFlowInstanceId() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByFlowInstanceId(1L);

        assertEquals(2, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
        assertEquals(nodeInstanceEntity2, result.get(1));
    }

    @Test
    public void testFindByIds() {
        List<NodeInstanceEntity> result = nodeInstanceEntityRepository.findByIds(ids);

        assertEquals(2, result.size());
        assertEquals(nodeInstanceEntity1, result.get(0));
        assertEquals(nodeInstanceEntity2, result.get(1));
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = nodeInstanceEntityRepository.deleteByFlowInstanceId(1L);

        assertEquals(2, result);
    }

    @Test
    public void testDeleteByInstanceIdAndInstanceType() {
        int result = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(1L, FlowNodeType.START_EVENT);

        assertEquals(1, result);
    }
}