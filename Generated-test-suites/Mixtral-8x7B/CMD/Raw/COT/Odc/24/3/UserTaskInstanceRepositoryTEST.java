import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {}, loader = AnnotationConfigContextLoader.class)
@Transactional
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2", "role3");
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, resourceRoleIdentifiers);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifiers = Arrays.asList("role1", "role2", "role3");
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByResourceRoleIdentifierIn(resourceRoleIdentifiers);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByRoleId(1L);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        entity.setId(1L);
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(result, 1);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "userTaskId");
        assertEquals(result, 1);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED);
        assertEquals(result, 1);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(result, 1);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.ACTIVITY, "activityId", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.ACTIVITY, "name", 1L);
        assertTrue(result.isPresent());
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertTrue(result);
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = Arrays.asList(1L, 2L, 3L);
        List<UserTaskInstanceEntity> results = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.CREATED);
        assertEquals(results.size(), 1);
    }
}