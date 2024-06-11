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
 assertEquals(3, roleEntities.size());
 }

 @Test
 public void testFindByName() {
 String name = "test_role";
 List<RoleEntity> roleEntities = roleRepository.findByName(name);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(10, page.getContent().size());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 String name = "test_role";
 Long organizationId = 1L;
 Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);
 assertTrue(roleEntityOptional.isPresent());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 Long organizationId = 1L;
 List<String> names = Arrays.asList("test_role_1", "test_role_2");
 List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(2, roleEntities.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertEquals(3, page.getContent().size());
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(1, roleEntities.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(3, roleEntities.size());
 }

 @Test
 public void testUpdate() {
 Long id = 1L;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(id);
 roleEntity.setName("updated_role");
 int rowsAffected = roleRepository.update(roleEntity);
 assertEquals(1, rowsAffected);
 }

}

class RoleRepositoryTestConfig {

 // Configure your test data source and entity manager here

}