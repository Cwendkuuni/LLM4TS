import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTestConfig.class)
@Transactional
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 private RoleEntity roleEntity1;
 private RoleEntity roleEntity2;
 private RoleEntity roleEntity3;
 private List<RoleEntity> roleEntityList;
 private List<Long> roleIds;
 private Pageable pageable;

 @Before
 public void setup() {
 roleEntity1 = new RoleEntity();
 roleEntity1.setName("Role1");
 roleEntity1.setOrganizationId(1L);
 roleEntity1.setEnabled(true);

 roleEntity2 = new RoleEntity();
 roleEntity2.setName("Role2");
 roleEntity2.setOrganizationId(1L);
 roleEntity2.setEnabled(true);

 roleEntity3 = new RoleEntity();
 roleEntity3.setName("Role3");
 roleEntity3.setOrganizationId(2L);
 roleEntity3.setEnabled(false);

 roleEntityList = Arrays.asList(roleEntity1, roleEntity2, roleEntity3);

 for (RoleEntity roleEntity : roleEntityList) {
 roleRepository.save(roleEntity);
 }

 roleIds = new ArrayList<>();
 roleIds.add(roleEntity1.getId());
 roleIds.add(roleEntity2.getId());

 pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
 }

 @Test
 public void testFindByIdIn() {
 List<RoleEntity> result = roleRepository.findByIdIn(roleIds);
 assertEquals(result.size(), 2);
 assertEquals(result.get(0).getName(), "Role1");
 assertEquals(result.get(1).getName(), "Role2");
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> result = roleRepository.findByName("Role1");
 assertEquals(result.size(), 1);
 assertEquals(result.get(0).getName(), "Role1");
 }

 @Test
 public void testFindByOrganizationId() {
 Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
 assertEquals(result.getTotalElements(), 2L);
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Role1", 1L);
 assertTrue(result.isPresent());
 assertEquals(result.get().getName(), "Role1");
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Role1", "Role3"));
 assertEquals(result.size(), 1);
 assertEquals(result.get(0).getName(), "Role1");
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertEquals(result.getTotalElements(), 2L);
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> result = roleRepository.findByUserId(1L);
 assertEquals(result.size(), 2);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
 assertEquals(result.size(), 2);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
 assertEquals(result.size(), 2);
 }

 @Test
 public void testUpdate() {
 roleEntity1.setName("UpdatedRole1");
 roleEntity1.setEnabled(false);
 roleEntity1.setDescription("Updated Role Description");

 int result = roleRepository.update(roleEntity1);
 assertEquals(result, 1);

 RoleEntity updatedRole = roleRepository.findById(roleEntity1.getId()).get();
 assertNotSame(updatedRole.getName(), roleEntity1.getName());
 assertNotSame(updatedRole.isEnabled(), roleEntity1.isEnabled());
 assertNotSame(updatedRole.getDescription(), roleEntity1.getDescription());
 }

 @Test
 public void testSave() {
 RoleEntity newRoleEntity = new RoleEntity();
 newRoleEntity.setName("NewRole");
 newRoleEntity.setOrganizationId(1L);
 newRoleEntity.setEnabled(true);

 RoleEntity savedRoleEntity = roleRepository.save(newRoleEntity);
 assertNotNull(savedRoleEntity.getId());

 RoleEntity result = roleRepository.findById(savedRoleEntity.getId()).get();
 assertEquals(result.getName(), "NewRole");
 assertEquals(result.isEnabled(), true);
 }

 @Test
 public void testDeleteById() {
 roleRepository.deleteById(roleEntity1.getId());
 assertFalse(roleRepository.existsById(roleEntity1.getId()));
 }

 @Test
 public void testFindAll() {
 List<RoleEntity> result = (List<RoleEntity>) roleRepository.findAll();
 assertEquals(result.size(), 3);
 }

 @Test
 public void testFindAllOrderByIdDesc() {
 List<RoleEntity> result = (List<RoleEntity>) roleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
 assertEquals(result.get(0).getId(), roleEntity3.getId());
 assertEquals(result.get(1).getId(), roleEntity2.getId());
 assertEquals(result.get(2).getId(), roleEntity1.getId());
 }

 @Test
 public void testCount() {
 Long result = roleRepository.count();
 assertEquals(result, 3L);
 }

 @Test
 public void testExistsById() {
 assertTrue(roleRepository.existsById(roleEntity1.getId()));
 assertFalse(roleRepository.existsById(-1L));
 }

 @Test
 public void testFindAllNullRoleId() {
 List<RoleEntity> result = (List<RoleEntity>) roleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
 result.add(null);
 assertEquals(result.size(), 4);
 }

 @Test(expected = NullPointerException.class)
 public void testFindByIdInNullCollection() {
 roleRepository.findByIdIn(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByIdInEmptyCollection() {
 roleRepository.findByIdIn(new ArrayList<>());
 }

 @Test(expected = NullPointerException.class)
 public void testFindByNameNullName() {
 roleRepository.findByName(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdNullOrganizationId() {
 roleRepository.findByOrganizationId(null, pageable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByNameAndOrganizationIdNullName() {
 roleRepository.findByNameAndOrganizationId(null, 1L);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndNameInNullOrganizationId() {
 roleRepository.findByOrganizationIdAndNameIn(null, Arrays.asList("Role1", "Role3"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByOrganizationIdAndRoleIdsNullOrganizationId() {
 roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, pageable);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByUserIdNullUserId() {
 roleRepository.findByUserId(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindByRoleIdsAndEnabledNullRoleIds() {
 roleRepository.findByRoleIdsAndEnabled(null, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdateNullRoleEntity() {
 roleRepository.update(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSaveNullRoleEntity() {
 roleRepository.save(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDeleteByIdNullId() {
 roleRepository.deleteById(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllNullPageable() {
 roleRepository.findAll(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFindAllOrderByIdDescNullSort() {
 roleRepository.findAll(Sort.by(null, "id"));
 }

}