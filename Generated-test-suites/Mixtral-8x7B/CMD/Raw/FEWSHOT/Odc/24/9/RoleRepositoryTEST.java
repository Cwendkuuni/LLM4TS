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

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 @Test
 public void testJpaRepositoryMethods() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);
 roleEntity.setDescription("Test Role Description");

 roleRepository.save(roleEntity);

 Long roleId = roleEntity.getId();
 assertNotNull(roleId);

 RoleEntity savedRoleEntity = roleRepository.findById(roleId).orElse(null);
 assertEquals("Test Role", savedRoleEntity.getName());

 roleRepository.deleteById(roleId);
 Optional<RoleEntity> deletedRoleEntityOptional = roleRepository.findById(roleId);
 assertTrue(deletedRoleEntityOptional.isEmpty());
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertEquals(3, roleEntities.size());
 }

 @Test
 public void testFindByName() {
 String name = "Test Role";
 List<RoleEntity> roleEntities = roleRepository.findByName(name);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(0, page.getTotalElements());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "Test Role";
 Long organizationId = 1L;
 Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(roleEntityOptional.isEmpty());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("Test Role", "Another Test Role");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(0, roleEntities.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertEquals(0, page.getTotalElements());
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
 assertEquals(0, roleEntities.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(0, roleEntities.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(0, roleEntities.size());
 }

 @Test
 public void testUpdate() {
 Long id = 1L;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(id);
 roleEntity.setName("Updated Test Role");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);
 roleEntity.setDescription("Updated Test Role Description");

 int rowsAffected = roleRepository.update(roleEntity);
 assertEquals(1, rowsAffected);

 RoleEntity updatedRoleEntity = roleRepository.findById(id).orElse(null);
 assertEquals("Updated Test Role", updatedRoleEntity.getName());
 }
}