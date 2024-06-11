import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
 public void testJpaRepository() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setDescription("test description");
 roleEntity.setEnabled(true);

 RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
 assertNotNull(savedRoleEntity.getId());

 RoleEntity findRoleEntity = roleRepository.findById(savedRoleEntity.getId()).orElse(null);
 assertEquals(savedRoleEntity.getName(), findRoleEntity.getName());

 List<RoleEntity> findRoleEntities = roleRepository.findAll();
 assertTrue(findRoleEntities.size() > 0);

 roleRepository.deleteById(savedRoleEntity.getId());
 RoleEntity deletedRoleEntity = roleRepository.findById(savedRoleEntity.getId()).orElse(null);
 assertNull(deletedRoleEntity);
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertTrue(roleEntities.size() >= 3);
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> roleEntities = roleRepository.findByName("testRole");
 assertTrue(roleEntities.size() > 0);
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
 assertTrue(page.getTotalElements() > 0);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> optional = roleRepository.findByNameAndOrganizationId("testRole", 1L);
 assertTrue(optional.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<String> names = Arrays.asList("testRole1", "testRole2");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertTrue(roleEntities.size() >= 2);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertTrue(page.getTotalElements() > 0);
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
 assertTrue(roleEntities.size() > 0);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
 assertTrue(roleEntities.size() > 0);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
 assertTrue(roleEntities.size() >= 3);
 }

 @Test
 @Transactional
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setDescription("test description");
 roleEntity.setEnabled(true);

 RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
 assertNotNull(savedRoleEntity.getId());

 int updatedRows = roleRepository.update(savedRoleEntity);
 assertEquals(1, updatedRows);

 RoleEntity updatedRoleEntity = roleRepository.findById(savedRoleEntity.getId()).orElse(null);
 assertEquals(savedRoleEntity.getName(), updatedRoleEntity.getName());
 assertEquals(savedRoleEntity.getDescription(), updatedRoleEntity.getDescription());
 assertEquals(savedRoleEntity.isEnabled(), updatedRoleEntity.isEnabled());
 }
}