import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepositoryTestContext.class)
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.RUNNING);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L,
                resourceRoleIdentifiers);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        Collection<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("role1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(0, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(0, result.size());
    }

    @Test
    @Transactional
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(0, result);
    }

    @Test
    @Transactional
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId1");
        assertEquals(0, result);
    }

    @Test
    @Transactional
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.RUNNING);
        assertEquals(0, result);
    }

    @Test
    @Transactional
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(0, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.APPROVAL, "activityId1", 1L);
        assertEquals(false, result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.APPROVAL,
                "name1", 1L);
        assertEquals(false, result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertNull(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        Collection<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.RUNNING);
        assertEquals(0, result.size());
    }
}

@org.springframework.context.annotation.Configuration
class UserTaskInstanceRepositoryTestContext {
}