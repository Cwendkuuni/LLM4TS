import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.FlowNodeType;
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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {})
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindByCandidateUserIdOrRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, roleIds);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<String> resourceRoleIdentifiers = new ArrayList<>();
        resourceRoleIdentifiers.add("test");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(
                1L, resourceRoleIdentifiers);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindByResourceRoleIdentifierIn() {
        List<String> resourceRoleIdentifier = new ArrayList<>();
        resourceRoleIdentifier.add("test");
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByResourceRoleIdentifierIn(
                resourceRoleIdentifier);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindByRoleId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByRoleId(1L);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindByCandidateUserId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserId(1L);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testUpdate() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        int result = userTaskInstanceRepository.update(entity);
        assertEquals(1, result);
    }

    @Test
    public void testUpdateUserTaskIdById() {
        int result = userTaskInstanceRepository.updateUserTaskIdById(1L, "test");
        assertEquals(1, result);
    }

    @Test
    public void testUpdateStatusById() {
        int result = userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.CREATED);
        assertEquals(1, result);
    }

    @Test
    public void testDeleteByFlowInstanceId() {
        int result = userTaskInstanceRepository.deleteByFlowInstanceId(1L);
        assertEquals(1, result);
    }

    @Test
    public void testFindByInstanceTypeAndActivityId() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndActivityId(
                FlowNodeType.APPROVAL, "test", 1L);
        assertThat(entity).isNotNull();
    }

    @Test
    public void testFindByInstanceTypeAndName() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findByInstanceTypeAndName(
                FlowNodeType.APPROVAL, "test", 1L);
        assertThat(entity).isNotNull();
    }

    @Test
    public void testFindConfirmById() {
        Boolean result = userTaskInstanceRepository.findConfirmById(1L);
        assertThat(result).isNotNull();
    }

    @Test
    public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
        List<Long> flowInstanceIds = new ArrayList<>();
        flowInstanceIds.add(1L);
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(
                flowInstanceIds, FlowNodeStatus.CREATED);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindAll() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAll();
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindAllWithPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<UserTaskInstanceEntity> page = userTaskInstanceRepository.findAll(pageable);
        assertThat(page).isNotNull();
    }

    @Test
    public void testFindById() {
        Optional<UserTaskInstanceEntity> entity = userTaskInstanceRepository.findById(1L);
        assertThat(entity).isNotNull();
    }

    @Test
    public void testExistsById() {
        boolean result = userTaskInstanceRepository.existsById(1L);
        assertThat(result).isTrue();
    }

    @Test
    public void testCount() {
        long result = userTaskInstanceRepository.count();
        assertThat(result).isNotNull();
    }

    @Test
    public void testDeleteById() {
        userTaskInstanceRepository.deleteById(1L);
        boolean result = userTaskInstanceRepository.existsById(1L);
        assertThat(result).isFalse();
    }

    @Test
    public void testSave() {
        UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
        userTaskInstanceRepository.save(entity);
        Optional<UserTaskInstanceEntity> result = userTaskInstanceRepository.findById(entity.getId());
        assertThat(result).isPresent();
    }

    @Test
    public void testFindAllByFlowInstanceId() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllByFlowInstanceId(1L);
        assertThat(entities).isNotNull();
    }

    @Test
    public void testFindAllByFlowInstanceIdAndStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findAllByFlowInstanceIdAndStatus(1L,
                FlowNodeStatus.CREATED);
        assertThat(entities).isNotNull();
    }

}