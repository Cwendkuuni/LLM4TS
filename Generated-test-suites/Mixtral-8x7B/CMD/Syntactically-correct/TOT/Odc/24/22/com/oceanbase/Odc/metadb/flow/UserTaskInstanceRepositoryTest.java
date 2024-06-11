
package com.oceanbase.odc.metadb.flow;

import com.oceanbase.odc.metadb.flow.entity.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeStatus;
import com.oceanbase.odc.metadb.flow.enums.FlowNodeType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserTaskInstanceRepositoryTest {

    @Autowired
    private UserTaskInstanceRepository userTaskInstanceRepository;

    @Test
    @DisplayName("Test findByStatus method")
    public void testFindByStatus() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByStatus(FlowNodeStatus.CREATED);
        assertFalse(entities.isEmpty());
        entities.forEach(entity -> assertEquals(FlowNodeStatus.CREATED, entity.getStatus()));
    }

    @Test
    @DisplayName("Test findByCandidateUserIdOrRoleIds method")
    public void testFindByCandidateUserIdOrRoleIds() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, Arrays.asList(2L, 3L));
        assertFalse(entities.isEmpty());
        entities.forEach(entity -> {
            assertTrue(entity.getCandidateUserIds().contains(1L) || entity.getCandidateRoleIds().contains(2L) || entity.getCandidateRoleIds().contains(3L));
        });
    }

    @Test
    @DisplayName("Test findByCandidateUserIdOrResourceRoleIdentifier method")
    public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
        List<UserTaskInstanceEntity> entities = userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, Arrays.asList("role1", "role2"));
        assertFalse(entities.isEmpty());
        entities.forEach(entity -> {
            assertTrue(entity.getCandidateUserIds().contains(1L) || entity.getCandidateRoleIdentifiers().contains("role1") || entity.getCandidateRoleIdentifiers().contains("role2"));
        });
    }

    // Add the remaining test cases for other methods here

}
