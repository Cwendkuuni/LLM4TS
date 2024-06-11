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
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 assertNotNull(result);
 assertTrue(result.size() >= 1);
 }

 @Test
 public void testFindByName() {
 String name = "test_role";
 List<RoleEntity> result = roleRepository.findByName(name);
 assertNotNull(result);
 assertTrue(result.size() >= 1);
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 assertNotNull(result);
 assertTrue(result.getTotalElements() >= 0);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test_role";
 Long organizationId = 1L;
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(result.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test_role1", "test_role2");
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertNotNull(result);
 assertTrue(result.size() >= 1);
 }

 @Test
 @Transactional
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertNotNull(result);
 assertTrue(result.getTotalElements() >= 0);
 }

 @Test
 @Transactional
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> result = roleRepository.findByUserId(userId);
 assertNotNull(result);
 assertTrue(result.size() >= 1);
 }

 @Test
 @Transactional
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertNotNull(result);
 assertTrue(result.size() >= 1);
 }

 @Test
 @Transactional
 public void testFindByRoleIdsAndEnabled() {
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertNotNull(result);
 assertTrue(result.size() >= 1);
 }

 @Test
 @Transactional
 public void testUpdate() {
 Long id = 1L;
 String newName = "updated_test_role";
 Boolean newEnabled = false;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(id);
 roleEntity.setName(newName);
 roleEntity.setEnabled(newEnabled);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }
}