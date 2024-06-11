import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.flow.UserTaskInstanceEntity;
import com.oceanbase.odc.metadb.flow.UserTaskInstanceRepository;
import com.oceanbase.odc.service.flow.model.FlowNodeStatus;
import com.oceanbase.odc.service.flow.model.FlowNodeType;

@RunWith(MockitoJUnitRunner.class)
public class UserTaskInstanceRepositoryTest {

@Mock
private JpaRepository jpaRepository;

@Mock
private JpaSpecificationExecutor jpaSpecificationExecutor;

@InjectMocks
private UserTaskInstanceRepository userTaskInstanceRepository;

@Test
public void testFindByStatus() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.findByStatus(any(FlowNodeStatus.class))).thenReturn(entities);
assertEquals(entities, userTaskInstanceRepository.findByStatus(any(FlowNodeStatus.class)));
}

@Test
public void testFindByCandidateUserIdOrRoleIds() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.query(anyString(), any(Collection.class), any(Collection.class)))
.thenReturn(entities);
assertEquals(entities, userTaskInstanceRepository.findByCandidateUserIdOrRoleIds(1L, asList(1L, 2L)));
}

@Test
public void testFindByCandidateUserIdOrResourceRoleIdentifier() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.query(anyString(), any(Collection.class), any(Collection.class)))
.thenReturn(entities);
assertEquals(entities, userTaskInstanceRepository.findByCandidateUserIdOrResourceRoleIdentifier(1L, asList("r1", "r2")));
}

@Test
public void testFindByResourceRoleIdentifierIn() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.query(anyString(), any(Collection.class), any(Collection.class)))
.thenReturn(entities);
assertEquals(entities, userTaskInstanceRepository.findByResourceRoleIdentifierIn(asList("r1", "r2")));
}

@Test
public void testFindByRoleId() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.query(anyString(), any(Collection.class), any(Collection.class)))
.thenReturn(entities);
assertEquals(entities, userTaskInstanceRepository.findByRoleId(1L));
}

@Test
public void testFindByCandidateUserId() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.query(anyString(), any(Collection.class), any(Collection.class)))
.thenReturn(entities);
assertEquals(entities, userTaskInstanceRepository.findByCandidateUserId(1L));
}

@Test
@Transactional
public void testUpdate() {
UserTaskInstanceEntity entity = new UserTaskInstanceEntity();
// populate entity with test data
when(jpaRepository.update(anyLong(), any(UserTaskInstanceEntity.class))).thenReturn(1);
assertEquals(1, userTaskInstanceRepository.update(1L, entity));
}

@Test
@Transactional
public void testUpdateUserTaskIdById() {
when(jpaRepository.updateUserTaskIdById(1L, "utid")).thenReturn(1);
assertEquals(1, userTaskInstanceRepository.updateUserTaskIdById(1L, "utid"));
}

@Test
@Transactional
public void testUpdateStatusById() {
when(jpaRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED)).thenReturn(1);
assertEquals(1, userTaskInstanceRepository.updateStatusById(1L, FlowNodeStatus.COMPLETED));
}

@Test
@Transactional
public void testDeleteByFlowInstanceId() {
when(jpaRepository.deleteByFlowInstanceId(1L)).thenReturn(1);
assertEquals(1, userTaskInstanceRepository.deleteByFlowInstanceId(1L));
}

@Test
public void testFindByInstanceTypeAndActivityId() {
Optional<UserTaskInstanceEntity> entity = Optional.of(new UserTaskInstanceEntity());
// populate entity with test data
when(jpaRepository.findByInstanceTypeAndActivityId(any(FlowNodeType.class), anyString(), anyLong()))
.thenReturn(entity);
assertEquals(entity, userTaskInstanceRepository.findByInstanceTypeAndActivityId(FlowNodeType.START, "act1", 1L));
}

@Test
public void testFindByInstanceTypeAndName() {
Optional<UserTaskInstanceEntity> entity = Optional.of(new UserTaskInstanceEntity());
// populate entity with test data
when(jpaRepository.findByInstanceTypeAndName(any(FlowNodeType.class), anyString(), anyLong()))
.thenReturn(entity);
assertEquals(entity, userTaskInstanceRepository.findByInstanceTypeAndName(FlowNodeType.START, "name1", 1L));
}

@Test
public void testFindConfirmById() {
when(jpaRepository.findConfirmById(1L)).thenReturn(true);
assertTrue(userTaskInstanceRepository.findConfirmById(1L));
}

@Test
public void testFindApprovalInstanceIdByFlowInstanceIdAndStatus() {
List<UserTaskInstanceEntity> entities = new ArrayList<>();
// populate entities with test data
when(jpaRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(new PageImpl<>(entities));
assertEquals(entities, userTaskInstanceRepository.findApprovalInstanceIdByFlowInstanceIdAndStatus(asList(1L, 2L), FlowNodeStatus.COMPLETED));
}

}