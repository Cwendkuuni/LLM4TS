import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
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
 private JpaRepository jpaRepository;

 @InjectMocks
 private RoleRepository roleRepository = new RoleRepository() {
 };

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);

 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByName() {
 String name = "test";

 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByName(name)).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByName(name);

 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = null;

 Page<RoleEntity> roleEntitiesPage = new PageImpl<>(Collections.singletonList(new RoleEntity()));

 when(jpaRepository.findByOrganizationId(organizationId, pageable)).thenReturn(roleEntitiesPage);

 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

 assertEquals(roleEntitiesPage, result);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test";
 Long organizationId = 1L;

 Optional<RoleEntity> roleEntityOptional = Optional.of(new RoleEntity());

 when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(roleEntityOptional);

 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

 assertEquals(roleEntityOptional, result);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> name = new ArrayList<>();
 name.add("test1");
 name.add("test2");

 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, name)).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, name);

 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Pageable pageable = null;

 Page<RoleEntity> roleEntitiesPage = new PageImpl<>(Collections.singletonList(new RoleEntity()));

 when(jpaRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
 .thenReturn(roleEntitiesPage);

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

 assertEquals(roleEntitiesPage, result);
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;

 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());

 when(jpaRepository.query(any(String.class), any(Pageable.class))).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByUserId(userId);

 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;

 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());

 when(jpaRepository.query(any(String.class), any(Pageable.class))).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 roleIds.add(2L);
 Boolean enabled = true;

 List<RoleEntity> roleEntities = new ArrayList<>();
 roleEntities.add(new RoleEntity());

 when(jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

 assertEquals(roleEntities, result);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();

 when(jpaRepository.update(roleEntity)).thenReturn(1);

 int result = roleRepository.update(roleEntity);

 assertEquals(1, result);
 }

}