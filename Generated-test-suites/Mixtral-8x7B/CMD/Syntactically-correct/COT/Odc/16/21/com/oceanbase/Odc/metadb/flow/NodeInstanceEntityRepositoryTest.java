import com.oceanbase.odc.metadb.flow.NodeInstanceEntity;
import com.oceanbase.odc.metadb.flow.NodeInstanceEntityRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import com.oceanbase.odc.service.flow.model.FlowableElementType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:application-context.xml")
public class NodeInstanceEntityRepositoryTest {

    @Autowired
    private NodeInstanceEntityRepository nodeInstanceEntityRepository;

    @Test
    public void testFindByInstanceIdAndInstanceTypeAndFlowableElementType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;
        FlowableElementType flowableElementType = FlowableElementType.TASK;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntity.setFlowableElementType(flowableElementType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceIdAndInstanceTypeAndFlowableElementType(instanceId, instanceType, flowableElementType);
        assertEquals(1, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByFlowInstanceId() {
        Long flowInstanceId = 1L;

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(flowInstanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(flowInstanceId);
        assertEquals(1, nodeInstanceEntities.size());
    }

    @Test
    public void testFindByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setId(1L);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findByIds(ids);
        assertEquals(1, nodeInstanceEntities.size());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long instanceId = 1L;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setFlowInstanceId(instanceId);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int rowsAffected = nodeInstanceEntityRepository.deleteByFlowInstanceId(instanceId);
        assertEquals(1, rowsAffected);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByFlowInstanceId(instanceId);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    @Transactional
    public void testDeleteByInstanceIdAndInstanceType() {
        Long instanceId = 1L;
        FlowNodeType instanceType = FlowNodeType.START;

        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntity.setInstanceId(instanceId);
        nodeInstanceEntity.setInstanceType(instanceType);
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        int rowsAffected = nodeInstanceEntityRepository.deleteByInstanceIdAndInstanceType(instanceId, instanceType);
        assertEquals(1, rowsAffected);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findByInstanceId(instanceId);
        assertEquals(0, nodeInstanceEntities.size());
    }

    @Test
    public void testFindAll() {
        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(1, nodeInstanceEntities.size());
    }

    @Test
    public void testFindAllWithPage() {
        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);

        Page<NodeInstanceEntity> nodeInstanceEntityPage = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(0, nodeInstanceEntityPage.getTotalElements());

        // Add a test node instance entity
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        nodeInstanceEntityPage = nodeInstanceEntityRepository.findAll(pageable);
        assertEquals(1, nodeInstanceEntityPage.getTotalElements());
        assertEquals(1, nodeInstanceEntityPage.getContent().size());
    }

    @Test
    public void testSave() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();

        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindOne() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        Long id = nodeInstanceEntity.getId();

        NodeInstanceEntity savedNodeInstanceEntity = nodeInstanceEntityRepository.findOne(id);
        assertEquals(nodeInstanceEntity, savedNodeInstanceEntity);
    }

    @Test
    public void testExists() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        Long id = nodeInstanceEntity.getId();

        boolean exists = nodeInstanceEntityRepository.exists(id);
        assertEquals(true, exists);
    }

    @Test
    public void testCount() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        long count = nodeInstanceEntityRepository.count();
        assertEquals(1, count);
    }

    @Test
    public void testFindAllBySpec() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        Specification<NodeInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("id"), nodeInstanceEntity.getId());

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll(spec);
        assertEquals(1, nodeInstanceEntities.size());
        assertEquals(nodeInstanceEntity, nodeInstanceEntities.get(0));
    }

    @Test
    public void testFindAllBySpecWithPage() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        Specification<NodeInstanceEntity> spec = (root, query, cb) -> cb.equal(root.get("id"), nodeInstanceEntity.getId());

        int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize);

        Page<NodeInstanceEntity> nodeInstanceEntityPage = nodeInstanceEntityRepository.findAll(spec, pageable);
        assertEquals(1, nodeInstanceEntityPage.getTotalElements());
        assertEquals(1, nodeInstanceEntityPage.getContent().size());
    }

    @Test
    public void testDelete() {
        NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
        nodeInstanceEntityRepository.save(nodeInstanceEntity);

        Long id = nodeInstanceEntity.getId();

        nodeInstanceEntityRepository.delete(nodeInstanceEntity);

        List<NodeInstanceEntity> nodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, nodeInstanceEntities.size());

        boolean exists = nodeInstanceEntityRepository.exists(id);
        assertEquals(false, exists);
    }

    @Test
    @Transactional
    public void testDeleteInBatch() {
        List<NodeInstanceEntity> nodeInstanceEntities = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            NodeInstanceEntity nodeInstanceEntity = new NodeInstanceEntity();
            nodeInstanceEntityRepository.save(nodeInstanceEntity);
            nodeInstanceEntities.add(nodeInstanceEntity);
        }

        nodeInstanceEntityRepository.deleteInBatch(nodeInstanceEntities);

        List<NodeInstanceEntity> allNodeInstanceEntities = nodeInstanceEntityRepository.findAll();
        assertEquals(0, allNodeInstanceEntities.size());
    }
}