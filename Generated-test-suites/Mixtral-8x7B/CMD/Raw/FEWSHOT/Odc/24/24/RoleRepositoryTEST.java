import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
public class RoleRepositoryTest {

 @Autowired
 RoleRepository roleRepository;

 @Test
 public void testJpaRepository() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);
 roleEntity.setDescription("Test Role Description");

 RoleEntity savedRole = roleRepository.save(roleEntity);
 assertNotNull(savedRole.getId());

 RoleEntity fetchedRole = roleRepository.findById(savedRole.getId()).get();
 assertEquals(savedRole, fetchedRole);

 List<RoleEntity> allRoles = (List<RoleEntity>) roleRepository.findAll();
 assertTrue(allRoles.size() > 0);

 long count = roleRepository.count();
 assertTrue(count > 0);

 roleRepository.deleteById(savedRole.getId());
 count = roleRepository.count();
 assertTrue(count == 0);
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roles = roleRepository.findByIdIn(ids);
 assertTrue(roles.size() <= ids.size());
 }

 @Test
 public void testFindByName() {
 String name = "Test Role";
 List<RoleEntity> roles = roleRepository.findByName(name);
 assertTrue(roles.size() > 0);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
 assertTrue(page.getTotalElements() > 0);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "Test Role";
 Long organizationId = 1L;
 Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(role.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("Test Role 1", "Test Role 2");
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertTrue(roles.size() <= names.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
 assertTrue(page.getTotalElements() > 0);
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> roles = roleRepository.findByUserId(1L);
 assertTrue(roles.size() > 0);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
 assertTrue(roles.size() > 0);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
 assertTrue(roles.size() <= roleIds.size());
 }

 @Test
 public void testUpdate() {
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(roleIds.get(0));
 roleEntity.setName("Updated Test Role");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);
 roleEntity.setDescription("Updated Test Role Description");

 int count = roleRepository.update(roleEntity);
 assertTrue(count == 1);

 RoleEntity updatedRole = roleRepository.findById(roleEntity.getId()).get();
 assertEquals(roleEntity.getName(), updatedRole.getName());
 assertEquals(roleEntity.getDescription(), updatedRole.getDescription());
 assertEquals(roleEntity.isEnabled(), updatedRole.isEnabled());
 }
}