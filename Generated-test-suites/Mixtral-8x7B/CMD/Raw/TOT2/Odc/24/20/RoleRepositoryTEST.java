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
 assertEquals(3, roles.size());
 }

 @Test
 public void testFindByName() {
 String name = "admin";
 List<RoleEntity> roles = roleRepository.findByName(name);
 assertEquals(1, roles.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(10, page.getTotalElements());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "admin";
 Long organizationId = 1L;
 Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(role.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("admin", "guest");
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(2, roles.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertEquals(2, page.getTotalElements());
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roles = roleRepository.findByUserId(userId);
 assertEquals(1, roles.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(1, roles.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L);
 Boolean enabled = true;
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(2, roles.size());
 }

 @Test
 public void testUpdate() {
 Long id = 1L;
 String newName = "newAdmin";
 Boolean newEnabled = false;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(id);
 roleEntity.setName(newName);
 roleEntity.setEnabled(newEnabled);
 int rowsAffected = roleRepository.update(roleEntity);
 assertEquals(1, rowsAffected);
 }

}

class RoleRepositoryTestConfig {

}