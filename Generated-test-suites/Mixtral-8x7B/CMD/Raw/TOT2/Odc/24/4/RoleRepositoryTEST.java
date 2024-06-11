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
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertTrue(roleEntities.size() >= 1);
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> roleEntities = roleRepository.findByName("testRole");
 assertTrue(roleEntities.size() >= 1);
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
 assertTrue(roleEntities.getTotalElements() >= 1);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("testRole", 1L);
 assertTrue(roleEntityOptional.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<String> names = Arrays.asList("testRole1", "testRole2");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertTrue(roleEntities.size() >= 1);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Pageable pageable = PageRequest.of(0, 10);
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertTrue(roleEntities.getTotalElements() >= 1);
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
 assertTrue(roleEntities.size() >= 1);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
 assertTrue(roleEntities.size() >= 1);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true);
 assertTrue(roleEntities.size() >= 1);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRoleUpdate");
 roleEntity.setEnabled(false);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 @Test
 public void testFindByIdIn_EmptyList() {
 List<Long> ids = new ArrayList<>();
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertTrue(roleEntities.isEmpty());
 }

 @Test
 public void testFindByName_EmptyString() {
 List<RoleEntity> roleEntities = roleRepository.findByName("");
 assertTrue(roleEntities.isEmpty());
 }

 @Test
 public void testFindByOrganizationId_NegativeId() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(-1L, pageable);
 assertTrue(roleEntities.getTotalElements() == 0);
 }

 @Test
 public void testFindByNameAndOrganizationId_NonMatching() {
 Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("nonMatching", 1L);
 assertFalse(roleEntityOptional.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn_NoMatch() {
 List<String> names = Arrays.asList("nonMatching1", "nonMatching2");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertTrue(roleEntities.isEmpty());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds_NoMatch() {
 Pageable pageable = PageRequest.of(0, 10);
 List<Long> roleIds = Arrays.asList(99L, 98L, 97L);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertTrue(roleEntities.getTotalElements() == 0);
 }

 @Test
 public void testFindByUserId_NonMatching() {
 List<RoleEntity> roleEntities = roleRepository.findByUserId(99L);
 assertTrue(roleEntities.isEmpty());
 }

 @Test
 public void testFindByUserIdAndEnabled_NonMatching() {
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(99L, true);
 assertTrue(roleEntities.isEmpty());
 }

 @Test
 public void testFindByRoleIdsAndEnabled_NoMatch() {
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(99L, 98L, 97L), true);
 assertTrue(roleEntities.isEmpty());
 }

 @Test
 public void testUpdate_NonMatchingId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(99L);
 roleEntity.setName("testRoleUpdate");
 roleEntity.setEnabled(false);
 int result = roleRepository.update(roleEntity);
 assertEquals(0, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdate_NullRoleEntity() {
 int result = roleRepository.update(null);
 fail("IllegalArgumentException expected");
 }

}