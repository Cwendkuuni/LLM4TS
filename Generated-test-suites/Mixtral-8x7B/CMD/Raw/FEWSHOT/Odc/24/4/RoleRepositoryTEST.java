import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
 roleRepository.save(roleEntity);

 RoleEntity savedRoleEntity = roleRepository.findById(roleEntity.getId()).orElse(null);
 assertNotNull(savedRoleEntity);
 assertEquals(savedRoleEntity.getName(), "testRole");

 List<RoleEntity> roleEntities = roleRepository.findAll();
 assertTrue(roleEntities.size() > 0);

 roleRepository.deleteById(roleEntity.getId());
 RoleEntity deletedRoleEntity = roleRepository.findById(roleEntity.getId()).orElse(null);
 assertEquals(deletedRoleEntity, null);
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertTrue(roleEntities.size() <= ids.size());
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> roleEntities = roleRepository.findByName("testRole");
 assertTrue(roleEntities.size() > 0);
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
 assertTrue(roleEntities.getContent().size() <= pageable.getPageSize());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("testRole", 1L);
 assertTrue(roleEntityOptional.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<String> names = Arrays.asList("testRole1", "testRole2", "testRole3");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertTrue(roleEntities.size() <= names.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertTrue(roleEntities.getContent().size() <= pageable.getPageSize());
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
 assertTrue(roleEntities.size() <= roleIds.size());
 }

 @Test
 @Transactional
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleRepository.save(roleEntity);

 RoleEntity updatedRoleEntity = new RoleEntity();
 updatedRoleEntity.setId(roleEntity.getId());
 updatedRoleEntity.setName("updatedTestRole");
 updatedRoleEntity.setOrganizationId(1L);
 updatedRoleEntity.setEnabled(false);
 int updatedRows = roleRepository.update(updatedRoleEntity);

 assertEquals(updatedRows, 1);

 RoleEntity updatedInDbRoleEntity = roleRepository.findById(roleEntity.getId()).orElse(null);
 assertNotNull(updatedInDbRoleEntity);
 assertEquals(updatedInDbRoleEntity.getName(), "updatedTestRole");
 assertEquals(updatedInDbRoleEntity.isEnabled(), false);
 }
}