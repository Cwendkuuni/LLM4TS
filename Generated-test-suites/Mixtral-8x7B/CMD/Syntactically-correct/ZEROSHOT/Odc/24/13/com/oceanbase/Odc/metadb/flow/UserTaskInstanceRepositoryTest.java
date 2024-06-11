import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepository.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setStatus(FlowNodeStatus.CREATED);
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(entities.size(), 1);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Long userId = 1L;
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.addCandidateRoleId(roleIds.get(0));
        entity.setCandidateUserId(userId);
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(userId, roleIds);
        assertEquals(entities.size(), 1);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Long userId = 1L;
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRole1");

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.addCandidateResourceRoleIdentifier(resourceRoleIdentifiers.get(0));
        entity.setCandidateUserId(userId);
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(userId, resourceRoleIdentifiers);
        assertEquals(entities.size(), 1);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRole1");

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.addCandidateResourceRoleIdentifier(resourceRoleIdentifiers.get(0));
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(entities.size(), 1);
    }

    @Test
    public void testFindByRoleId() {
        Long roleId = 1L;

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.addCandidateRoleId(roleId);
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findByRoleId(roleId);
        assertEquals(entities.size(), 1);
    }

    @Test
    public void testFindByCandidateUserId() {
        Long userId = 1L;

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setCandidateUserId(userId);
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findByCandidateUserId(userId);
        assertEquals(entities.size(), 1);
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        userTaskInstanceRepository.save(entity);

        int result = userTaskInstanceRepository.update(entity);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        Long id = 1L;
        String userTaskId = "userTaskId1";

        int result = userTaskInstanceRepository.updateUserTaskIdById(id, userTaskId);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        Long id = 1L;
        FlowNodeStatus status = FlowNodeStatus.CREATED;

        int result = userTaskInstanceRepository.updateStatusById(id, status);
        assertEquals(result, 1);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        Long flowInstanceId = 1L;

        int result = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(result, 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(flowInstanceId);
        userTaskInstanceRepository.save(entity);

        result = userTaskInstanceRepository.deleteByFlowInstanceId(flowInstanceId);
        assertEquals(result, 1);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Long flowInstanceId = 1L;
        String activityId = "activityId1";
        FlowNodeType instanceType = FlowNodeType.APPROVAL;

        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertEquals(entityOptional.isPresent(), false);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(flowInstanceId);
        entity.setActivityId(activityId);
        userTaskInstanceRepository.save(entity);

        entityOptional = userTaskInstanceRepository.findByInstanceTypeAndActivityId(instanceType, activityId, flowInstanceId);
        assertEquals(entityOptional.isPresent(), true);
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Long flowInstanceId = 1L;
        String name = "name1";
        FlowNodeType instanceType = FlowNodeType.APPROVAL;

        Optional<UserTaskInstanceEntity> entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertEquals(entityOptional.isPresent(), false);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(flowInstanceId);
        entity.setName(name);
        userTaskInstanceRepository.save(entity);

        entityOptional = userTaskInstanceRepository.findByInstanceTypeAndName(instanceType, name, flowInstanceId);
        assertEquals(entityOptional.isPresent(), true);
    }

    @Test
    public void testFindConfirmById() {
        Long id = 1L;
        Boolean confirm = false;

        Boolean result = userTaskInstanceRepository.findConfirmById(id);
        assertEquals(result, confirm);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setWaitForConfirm(true);
        userTaskInstanceRepository.save(entity);

        result = userTaskInstanceRepository.findConfirmById(entity.getId());
        assertEquals(result, true);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Long flowInstanceId = 1L;
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(flowInstanceId);
        FlowNodeStatus status = FlowNodeStatus.CREATED;

        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertEquals(entities.size(), 0);

        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setFlowInstanceId(flowInstanceId);
        entity.setStatus(status);
        userTaskInstanceRepository.save(entity);

        entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(flowInstanceIds, status);
        assertEquals(entities.size(), 1);
    }
}