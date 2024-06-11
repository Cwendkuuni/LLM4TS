import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
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
 List<Long> ids = new ArrayList<>();
 ids.add(1L);
 ids.add(2L);
 List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
 assertEquals(2, roleEntities.size());
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> roleEntities = roleRepository.findByName("TestRole");
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
 assertEquals(1, roleEntities.getTotalElements());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("TestRole", 1L);
 assertTrue(roleEntity.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<String> names = new ArrayList<>();
 names.add("TestRole");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Pageable pageable = PageRequest.of(0, 10);
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);
 Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertEquals(1, roleEntities.getTotalElements());
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(Collections.singletonList(1L), true);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("UpdatedTestRole");
 roleEntity.setEnabled(true);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUpdate_NullRoleEntity() {
 roleRepository.update(null);
 }

 @Test
 public void testUpdate_EmptyRoleEntity() {
 RoleEntity roleEntity = new RoleEntity();
 int result = roleRepository.update(roleEntity);
 assertEquals(0, result);
 }
}