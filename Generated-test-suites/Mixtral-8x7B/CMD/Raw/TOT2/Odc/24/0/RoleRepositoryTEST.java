import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;
import com.oceanbase.odc.metadb.iam.specification.RoleSpecification;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 @Mock
 private JpaRepository<RoleEntity, Long> jpaRepository;

 @InjectMocks
 private RoleRepository roleRepository = new RoleRepository() {
 @Override
 public List<RoleEntity> findByIdIn(Collection<Long> ids) {
 return jpaRepository.findByIdIn(ids);
 }

 @Override
 public List<RoleEntity> findByName(String name) {
 return jpaRepository.findByName(name);
 }

 @Override
 public Page<RoleEntity> findByOrganizationId(Long organizationId, Pageable pageable) {
 return jpaRepository.findAll(new RoleSpecification(organizationId), pageable);
 }

 @Override
 public Optional<RoleEntity> findByNameAndOrganizationId(String name, Long organizationId) {
 return jpaRepository.findOne(new RoleSpecification(name, organizationId));
 }

 @Override
 public List<RoleEntity> findByOrganizationIdAndNameIn(Long organizationId, List<String> name) {
 return jpaRepository.findAll(new RoleSpecification(organizationId, name));
 }

 @Override
 public Page<RoleEntity> findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds,
 Pageable pageable) {
 return jpaRepository.findAll(new RoleSpecification(organizationId, roleIds), pageable);
 }

 @Override
 public List<RoleEntity> findByUserId(Long userId) {
 // Mockito cannot mock final or static methods, so we use when() to stub the findByUserId method
 when(jpaRepository.findByUserId(userId)).thenReturn(getRoleEntities());
 return jpaRepository.findByUserId(userId);
 }

 @Override
 public List<RoleEntity> findByUserIdAndEnabled(Long userId, Boolean enabled) {
 // Mockito cannot mock final or static methods, so we use when() to stub the findByUserIdAndEnabled method
 when(jpaRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(getRoleEntities());
 return jpaRepository.findByUserIdAndEnabled(userId, enabled);
 }

 @Override
 public List<RoleEntity> findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled) {
 // Mockito cannot mock final or static methods, so we use when() to stub the findByRoleIdsAndEnabled method
 when(jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(getRoleEntities());
 return jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Override
 @Modifying
 @Transactional
 public int update(RoleEntity roleEntity) {
 return jpaRepository.update(roleEntity);
 }
 };

 @Test
 public void testFindByIdIn() {
 // Test basic functionality
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(Collections.singletonList(1L));
 assertEquals(1, roleEntities.size());

 // Test edge cases
 List<RoleEntity> emptyRoleEntities = roleRepository.findByIdIn(Collections.emptyList());
 assertTrue(emptyRoleEntities.isEmpty());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByIdIn(null);
 });
 assertEquals("The Collection argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByName() {
 // Test basic functionality
 List<RoleEntity> roleEntities = roleRepository.findByName("admin");
 assertEquals(1, roleEntities.size());

 // Test edge cases
 List<RoleEntity> emptyRoleEntities = roleRepository.findByName(null);
 assertTrue(emptyRoleEntities.isEmpty());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByName("");
 });
 assertEquals("The String argument must not be null or empty!", exception.getMessage());
 }

 @Test
 public void testFindByOrganizationId() {
 // Test basic functionality
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
 assertEquals(1, page.getTotalElements());

 // Test edge cases
 Page<RoleEntity> emptyPage = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 0));
 assertEquals(0, emptyPage.getTotalElements());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByOrganizationId(null, pageable);
 });
 assertEquals("The Long argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 // Test basic functionality
 Optional<RoleEntity> optional = roleRepository.findByNameAndOrganizationId("admin", 1L);
 assertTrue(optional.isPresent());
 assertEquals(1L, optional.get().getId().longValue());

 // Test edge cases
 Optional<RoleEntity> emptyOptional = roleRepository.findByNameAndOrganizationId(null, 1L);
 assertFalse(emptyOptional.isPresent());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByNameAndOrganizationId("", null);
 });
 assertEquals("The Long argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 // Test basic functionality
 List<String> names = Collections.singletonList("admin");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertEquals(1, roleEntities.size());

 // Test edge cases
 List<RoleEntity> emptyRoleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, Collections.emptyList());
 assertTrue(emptyRoleEntities.isEmpty());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByOrganizationIdAndNameIn(null, names);
 });
 assertEquals("The Long argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 // Test basic functionality
 Collection<Long> roleIds = Collections.singletonList(1L);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, PageRequest.of(0, 10));
 assertEquals(1, page.getTotalElements());

 // Test edge cases
 Page<RoleEntity> emptyPage = roleRepository.findByOrganizationIdAndRoleIds(1L, Collections.emptyList(),
 PageRequest.of(0, 10));
 assertEquals(0, emptyPage.getTotalElements());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, PageRequest.of(0, 10));
 });
 assertEquals("The Long argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByUserId() {
 // Test basic functionality
 List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
 assertEquals(1, roleEntities.size());

 // Test edge cases
 List<RoleEntity> emptyRoleEntities = roleRepository.findByUserId(2L);
 assertTrue(emptyRoleEntities.isEmpty());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByUserId(null);
 });
 assertEquals("The Long argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 // Test basic functionality
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
 assertEquals(1, roleEntities.size());

 // Test edge cases
 List<RoleEntity> emptyRoleEntities = roleRepository.findByUserIdAndEnabled(1L, false);
 assertTrue(emptyRoleEntities.isEmpty());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByUserIdAndEnabled(null, true);
 });
 assertEquals("The Long argument must not be null!", exception.getMessage());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 // Test basic functionality
 Collection<Long> roleIds = Collections.singletonList(1L);
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
 assertEquals(1, roleEntities.size());

 // Test edge cases
 List<RoleEntity> emptyRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, false);
 assertTrue(emptyRoleEntities.isEmpty());

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.findByRoleIdsAndEnabled(null, true);
 });
 assertEquals("The Collection argument must not be null!", exception.getMessage());
 }

 @Test
 public void testUpdate() {
 // Test basic functionality
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);

 // Test edge cases
 roleEntity.setId(null);
 result = roleRepository.update(roleEntity);
 assertEquals(0, result);

 // Test exception handling scenarios
 Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
 roleRepository.update(null);
 });
 assertEquals("The RoleEntity argument must not be null!", exception.getMessage());
 }

 private List<RoleEntity> getRoleEntities() {
 List<RoleEntity> roleEntities = new ArrayList<>();
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntities.add(roleEntity);
 return roleEntities;
 }
}