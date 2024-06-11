import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository repository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = repository.findByStatus(FlowNodeStatus.READY);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = Collections.singletonList(2L);
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = Collections.singletonList("role1");
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifier = Collections.singletonList("role1");
        List<UserTaskInstanceEntity> entities = repository.findByResourceRoleIdentifierIn(resourceRoleIdentifier);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;
        List<UserTaskInstanceEntity> entities = repository.findByRoleId(roleId);
        assertFalse(entities.isEmpty());
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;
        List<UserTaskInstanceEntity> entities = repository.findByCandidateUserId(userId);
        assertFalse(entities.isEmpty());
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        int result = repository.update(entity);
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "task1";
        int result = repository.updateUserTaskIdById(id, userTaskId);
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.READY;
        int result = repository.updateStatusById(id, status);
        assertEquals(1, result);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;
        int result = repository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(1, result);
    }

    @Test
    public Optional<UserTaskInstanceEntity> testFindByInstanceTypeAndActivityId() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String activityId = "activity1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertTrue(entity.isPresent());
    }

    @Test
    public Optional<UserTaskInstanceEntity> testFindByInstanceTypeAndName() {
        FlowNodeType instanceType = FlowNodeType.APPROVAL;
        String name = "name1";
        Long flowInstanceId = 1L;
        Optional<UserTaskInstanceEntity> entity = repository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertTrue(entity.isPresent());
    }

    @Test
    public Boolean testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = repository.findConfirmById(id);
        assertTrue(confirm);
    }

    @Test
    public List<UserTaskInstanceEntity> testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = Collections.singletonList(1L);
        FlowNodeStatus status = FlowNodeStatus.READY;
        List<UserTaskInstanceEntity> entities = repository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertFalse(entities.isEmpty());
    }

}