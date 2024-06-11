import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 @Mock
 private JpaRepository<RoleEntity, Long> jpaRepositoryMock;

 @InjectMocks
 private RoleRepository roleRepository;

 private RoleEntity roleEntity1;
 private RoleEntity roleEntity2;
 private List<RoleEntity> roleEntities;
 private Page<RoleEntity> roleEntityPage;

 @Before
 public void setup() {
 roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("role1");
 roleEntity1.setOrganizationId(100L);
 roleEntity1.setEnabled(true);

 roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("role2");
 roleEntity2.setOrganizationId(100L);
 roleEntity2.setEnabled(false);

 roleEntities = Arrays.asList(roleEntity1, roleEntity2);

 roleEntityPage = new PageImpl<>(roleEntities, PageRequest.of(0, 10), roleEntities.size());
 }

 @Test
 public void testFindByIdIn() {
 when(jpaRepositoryMock.findByIdIn(any())).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

 assertEquals(result.size(), 2);
 assertEquals(result.get(0).getId(), 1L);
 assertEquals(result.get(1).getId(), 2L);
 }

 @Test
 public void testFindByName() {
 when(jpaRepositoryMock.findByName(anyString())).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByName("role1");

 assertEquals(result.size(), 1);
 assertEquals(result.get(0).getId(), 1L);
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = PageRequest.of(0, 10);

 doReturn(roleEntityPage).when(jpaRepositoryMock).findByOrganizationId(anyLong(), any(Pageable.class));

 Page<RoleEntity> result = roleRepository.findByOrganizationId(100L, pageable);

 assertEquals(result.getTotalElements(), 2L);
 assertEquals(result.getContent().size(), 2);
 assertEquals(result.getContent().get(0).getId(), 1L);
 assertEquals(result.getContent().get(1).getId(), 2L);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 when(jpaRepositoryMock.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(Optional.of(roleEntity1));

 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("role1", 100L);

 assertTrue(result.isPresent());
 assertEquals(result.get().getId(), 1L);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 when(jpaRepositoryMock.findByOrganizationIdAndNameIn(anyLong(), any())).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(100L, Arrays.asList("role1", "role2"));

 assertEquals(result.size(), 2);
 assertEquals(result.get(0).getId(), 1L);
 assertEquals(result.get(1).getId(), 2L);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Page<RoleEntity> pageable = new PageImpl<>(roleEntities, PageRequest.of(0, 10), roleEntities.size());

 when(jpaRepositoryMock.findByOrganizationIdAndRoleIds(anyLong(), any())).thenReturn(pageable);

 Page<RoleEntity> result = roleRepository
 .findByOrganizationIdAndRoleIds(100L, Arrays.asList(1L, 2L), PageRequest.of(0, 10));

 assertEquals(result.getTotalElements(), 2L);
 assertEquals(result.getContent().size(), 2);
 assertEquals(result.getContent().get(0).getId(), 1L);
 assertEquals(result.getContent().get(1).getId(), 2L);
 }

 @Test
 public void testFindByUserId() {
 when(jpaRepositoryMock.query(anyString(), any())).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByUserId(1L);

 assertEquals(result.size(), 2);
 assertEquals(result.get(0).getId(), 1L);
 assertEquals(result.get(1).getId(), 2L);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 when(jpaRepositoryMock.query(anyString(), any())).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);

 assertEquals(result.size(), 1);
 assertEquals(result.get(0).getId(), 1L);

 result = roleRepository.findByUserIdAndEnabled(1L, false);

 assertEquals(result.size(), 1);
 assertEquals(result.get(0).getId(), 2L);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 when(jpaRepositoryMock.query(anyString(), any())).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);

 assertEquals(result.size(), 2);
 assertEquals(result.get(0).getId(), 1L);
 assertEquals(result.get(1).getId(), 2L);
 }

 @Test
 public void testUpdate() {
 when(jpaRepositoryMock.update(any(RoleEntity.class))).thenReturn(1);

 int result = roleRepository.update(roleEntity1);

 assertEquals(result, 1);
 }
}