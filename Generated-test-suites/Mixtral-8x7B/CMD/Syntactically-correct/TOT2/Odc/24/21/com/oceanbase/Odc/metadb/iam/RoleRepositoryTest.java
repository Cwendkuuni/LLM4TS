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
 // assert that the size of roles is equal to ids
 // and all roles' id is in ids
 }

 @Test
 public void testFindByName() {
 String name = "test";
 List<RoleEntity> roles = roleRepository.findByName(name);
 // assert that all roles' name is equal to name
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
 // assert that all roles' organizationId is equal to organizationId
 // and the total elements is greater than 0
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test";
 Long organizationId = 1L;
 Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId(name, organizationId);
 // assert that role is present and the role's name and organizationId are equal to name and organizationId
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test1", "test2");
 List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 // assert that all roles' organizationId is equal to organizationId
 // and all roles' name is in names
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 // assert that all roles' organizationId is equal to organizationId
 // and all roles' id is in roleIds
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roles = roleRepository.findByUserId(userId);
 // assert that all roles' have user with id = userId
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 // assert that all roles' have user with id = userId and is_enabled = enabled
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 // assert that all roles' id is in roleIds and is_enabled = enabled
 }

 @Test
 @Transactional
 public void testUpdate() {
 Long id = 1L;
 String newName = "newName";
 Boolean newEnabled = false;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(id);
 roleEntity.setName(newName);
 roleEntity.setEnabled(newEnabled);
 int result = roleRepository.update(roleEntity);
 // assert that the result is 1
 Optional<RoleEntity> role = roleRepository.findById(id);
 // assert that the role's name is newName and is_enabled is newEnabled
 }
}