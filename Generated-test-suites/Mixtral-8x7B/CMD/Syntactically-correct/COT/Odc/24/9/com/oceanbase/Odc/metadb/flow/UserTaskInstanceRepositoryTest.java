import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = UserTaskInstanceRepositoryTest.Config.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    private List<UserTaskInstanceEntity> userTaskInstances;

    @Before
    public void setUp() {
        userTaskInstances = new ArrayList<>();
        UserTaskInstanceEntity userTaskInstance1 = new UserTaskInstanceEntity();
        userTaskInstance1.setStatus(FlowNodeStatus.CREATED);
        userTaskInstances.add(userTaskInstance1);

        UserTaskInstanceEntity userTaskInstance2 = new UserTaskInstanceEntity();
        userTaskInstance2.setStatus(FlowNodeStatus.EXECUTING);
        userTaskInstances.add(userTaskInstance2);

        UserTaskInstanceEntity userTaskInstance3 = new UserTaskInstanceEntity();
        userTaskInstance3.setStatus(FlowNodeStatus.FAILED);
        userTaskInstances.add(userTaskInstance3);

        userTaskInstanceRepository.saveAll(userTaskInstances);
    }

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(1, result.size());
        assertEquals(userTaskInstances.get(0).getId(), result.get(0).getId());
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("resourceRoleIdentifier1");
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity userTaskInstance = new UserTaskInstanceEntity();
        userTaskInstance.setId(1L);
        userTaskInstance.setStatus(FlowNodeStatus.EXECUTING);
        int result = userTaskInstanceRepository.update(userTaskInstance);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "newUserTaskId");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.EXECUTING);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(2, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findByInstanceTypeAndActivityId(FlowNodeType.ACTIVITY, "activityId", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstances.get(0).getId(), result.get().getId());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository
                .findByInstanceTypeAndName(FlowNodeType.ACTIVITY, "name", 1L);
        assertTrue(result.isPresent());
        assertEquals(userTaskInstances.get(0).getId(), result.get().getId());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertFalse(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        List<UserTaskInstanceEntity> result = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.CREATED);
        assertEquals(1, result.size());
    }

    @Configuration
    static class Config {
    }
}