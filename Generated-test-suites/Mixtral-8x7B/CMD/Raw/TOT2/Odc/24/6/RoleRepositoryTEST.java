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
import org.springframework.transaction.annotation.Transactional;

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
 Page<RoleEntity> roles = roleRepository.findByOrganizationId(organizationId, pageable);
 assertFalse(roles.isEmpty());
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
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> roles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertFalse(roles.isEmpty());
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
 @Transactional
 public void testUpdate() {
 Long id = 1L;
 String newName = "new_test_role";
 Boolean newEnabled = false;
 RoleEntity role = new RoleEntity();
 role.setId(id);
 role.setName(newName);
 role.setEnabled(newEnabled);
 int updatedRows = roleRepository.update(role);
 assertEquals(1, updatedRows);
 }
}

class RoleRepositoryTestConfig {

 // Configure your test data source and entity manager here

}