import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
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
 private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

 @InjectMocks
 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntities.add(roleEntity1);
 roleEntities.add(roleEntity2);
 when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByIdIn(ids));
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setName("Role1");
 roleEntities.add(roleEntity1);
 when(jpaRepository.findByName("Role1")).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByName("Role1"));
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = null;
 Long organizationId = 1L;
 Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
 when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);
 assertEquals(page, roleRepository.findByOrganizationId(organizationId, pageable));
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Long organizationId = 1L;
 String name = "Role1";
 Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity());
 when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optionalRoleEntity);
 assertEquals(optionalRoleEntity, roleRepository.findByNameAndOrganizationId(name, organizationId));
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = new ArrayList<>();
 names.add("Role1");
 names.add("Role2");
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setName("Role1");
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setName("Role2");
 roleEntities.add(roleEntity1);
 roleEntities.add(roleEntity2);
 when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(organizationId, names));
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Pageable pageable = null;
 Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
 when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(page);
 assertEquals(page,
 roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable));
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntities.add(roleEntity1);
 roleEntities.add(roleEntity2);
 when(jpaRepository.findByUserId(userId)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByUserId(userId));
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntities.add(roleEntity1);
 roleEntities.add(roleEntity2);
 when(jpaRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(userId, enabled));
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntities.add(roleEntity1);
 roleEntities.add(roleEntity2);
 when(jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(roleIds, enabled));
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 @Test
 public void testFindByIdIn_EmptyIds() {
 List<RoleEntity> roleEntities = new ArrayList<>();
 when(jpaRepository.findByIdIn(Collections.<Long>emptyList())).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByIdIn(Collections.<Long>emptyList()));
 }

 @Test
 public void testFindByName_EmptyName() {
 List<RoleEntity> roleEntities = new ArrayList<>();
 when(jpaRepository.findByName("")).thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByName(""));
 }

 @Test
 public void testFindByOrganizationId_NullOrganizationId() {
 Pageable pageable = null;
 Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
 when(roleRepository.findByOrganizationId(null, pageable)).thenReturn(page);
 assertEquals(page, roleRepository.findByOrganizationId(null, pageable));
 }

 @Test
 public void testFindByNameAndOrganizationId_NullName() {
 Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity());
 when(roleRepository.findByNameAndOrganizationId(null, 1L)).thenReturn(optionalRoleEntity);
 assertEquals(optionalRoleEntity, roleRepository.findByNameAndOrganizationId(null, 1L));
 }

 @Test
 public void testFindByOrganizationIdAndNameIn_EmptyNames() {
 List<RoleEntity> roleEntities = new ArrayList<>();
 when(roleRepository.findByOrganizationIdAndNameIn(1L, Collections.<String>emptyList()))
 .thenReturn(roleEntities);
 assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Collections.<String>emptyList()));
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds_NullOrganizationId() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Pageable pageable = null;
 Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);
 when(roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, pageable))
 .thenReturn(page);
 assertEquals(page,
 roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, pageable));
 }

 @Test(expected = NullPointerException.class)
 public void testFindByUserId_NullUserId() {
 roleRepository.findByUserId(null);
 }

 @Test(expected = NullPointerException.class)
 public void testFindByUserIdAndEnabled_NullUserId() {
 roleRepository.findByUserIdAndEnabled(null, true);
 }

 @Test(expected = NullPointerException.class)
 public void testFindByRoleIdsAndEnabled_NullRoleIds() {
 roleRepository.findByRoleIdsAndEnabled(null, true);
 }

 @Test
 public void testFindByUserId_EmptyResult() {
 Long userId = 1L;
 when(jpaRepository.findByUserId(userId)).thenReturn(Collections.<RoleEntity>emptyList());
 assertEquals(Collections.<RoleEntity>emptyList(), roleRepository.findByUserId(userId));
 }

 @Test
 public void testFindByUserIdAndEnabled_EmptyResult() {
 Long userId = 1L;
 when(jpaRepository.findByUserIdAndEnabled(userId, true)).thenReturn(Collections.<RoleEntity>emptyList());
 assertEquals(Collections.<RoleEntity>emptyList(), roleRepository.findByUserIdAndEnabled(userId, true));
 }

 @Test
 public void testFindByRoleIdsAndEnabled_EmptyResult() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 when(jpaRepository.findByRoleIdsAndEnabled(roleIds, true)).thenReturn(Collections.<RoleEntity>emptyList());
 assertEquals(Collections.<RoleEntity>emptyList(),
 roleRepository.findByRoleIdsAndEnabled(roleIds, true));
 }

 @Test
 public void testUpdate_ZeroResult() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(2L);
 int result = roleRepository.update(roleEntity);
 assertEquals(0, result);
 }
}