import static org.junit.Assert.*;

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
@ContextConfiguration(classes = RoleRepositoryTestConfig.class)
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roles = roleRepository.findByIdIn(ids);
 assertFalse(roles.isEmpty());
 }

 @Test
 public void testFindByName() {
 String name = "test_role";
 List<RoleEntity> roles = roleRepository.findByName(name);
 assertFalse(roles.isEmpty());
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
 assertFalse(page.getContent().isEmpty());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test_role";
 Long organizationId = 1L;
 Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(role.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test_role1", "test_role2");
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertFalse(roles.isEmpty());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
 assertFalse(page.getContent().isEmpty());
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roles = roleRepository.findByUserId(userId);
 assertFalse(roles.isEmpty());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertFalse(roles.isEmpty());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertFalse(roles.isEmpty());
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("updated_role");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("updated_description");
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 @Test
 public void testUpdateNonExistentRole() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(-1L);
 roleEntity.setName("updated_role");
 roleEntity.setEnabled(true);
 roleEntity.setDescription("updated_description");
 int result = roleRepository.update(roleEntity);
 assertEquals(0, result);
 }
}

class RoleRepositoryTestConfig {

 // Configure any necessary beans or data sources here

}