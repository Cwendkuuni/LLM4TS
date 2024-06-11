import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Before
    public void setUp() {
        UserTaskInstanceEntity entity1 = new UserTaskInstanceEntity();
        entity1.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceRepository.save(entity1);

        UserTaskInstanceEntity entity2 = new UserTaskInstanceEntity();
        entity2.setStatus(FlowNodeStatus.APPROVED);
        userTaskInstanceRepository.save(entity2);

        UserTaskInstanceEntity entity3 = new UserTaskInstanceEntity();
        entity3.setStatus(FlowNodeStatus.REJECTED);
        userTaskInstanceRepository.save(entity3);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        Assert.assertEquals(1, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        Assert.assertEquals(1, entities.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("test");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        Assert.assertEquals(1, entities.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("test");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        Assert.assertEquals(0, entities.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        Assert.assertEquals(0, entities.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        Assert.assertEquals(2, entities.size());
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        entity.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceRepository.update(entity);
        UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.findById(1L).get();
        Assert.assertEquals(FlowNodeStatus.CREATED, savedEntity.getStatus());
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        userTaskInstanceRepository.updateUserTaskIdById(1L, "test-user-task-id");
        UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.findById(1L).get();
        Assert.assertEquals("test-user-task-id", savedEntity.getUserTaskId());
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.APPROVED);
        UserTaskInstanceEntity savedEntity = userTaskInstanceRepository.findById(1L).get();
        Assert.assertEquals(FlowNodeStatus.APPROVED, savedEntity.getStatus());
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        List<UserTaskInstanceEntity> savedEntities = userTaskInstanceRepository.findAll();
        Assert.assertEquals(0, savedEntities.size());
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "test-activity-id", 1L);
        Assert.assertFalse(entity.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "test", 1L);
        Assert.assertFalse(entity.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        Assert.assertNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.CREATED);
        Assert.assertEquals(1, entities.size());
    }
}