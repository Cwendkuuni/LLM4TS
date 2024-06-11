import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 @Mock
 private JpaRepository<RoleEntity, Long> jpaRepository;

 @Mock
 private JpaSpecificationExecutor<RoleEntity> specificationExecutor;

 @InjectMocks
 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByIdIn(ids));
 }

 @Test
 public void testFindByName() {
 String name = "test";
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByName(name)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByName(name));
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = null;
 Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));

 when(specificationExecutor.findAll(any(), eq(pageable))).thenReturn(page);
 assertEquals(page, roleRepository.findByOrganizationId(organizationId, pageable));
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test";
 Long organizationId = 1L;
 Optional<RoleEntity> optional = Optional.of(new RoleEntity());

 when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);
 assertEquals(optional, roleRepository.findByNameAndOrganizationId(name, organizationId));
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> name = Arrays.asList("test1", "test2");
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, name)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(organizationId, name));
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));

 when(jpaRepository.findAll(any(), eq(pageable))).thenReturn(page);
 assertEquals(page, roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable));
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findAllByNativeQuery(anyString(), anyMap())).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByUserId(userId));
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findAllByNativeQuery(anyString(), anyMap())).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(userId, enabled));
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findAllByNativeQuery(anyString(), anyMap())).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(roleIds, enabled));
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 int rowsAffected = 1;

 when(jpaRepository.modifyingQueryByNative(anyString(), anyMap())).thenReturn(rowsAffected);
 assertEquals(rowsAffected, roleRepository.update(roleEntity));
 }
}