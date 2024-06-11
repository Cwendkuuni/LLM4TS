import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 @Test
 public void testFindByIdIn() {
 Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 assertEquals(3, result.size());
 }

 @Test
 public void testFindByName() {
 List<RoleEntity> result = roleRepository.findByName("test_role");
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Long organizationId = 1L;
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
 assertEquals(10, result.getContent().size());
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
 List<String> names = Arrays.asList("test_role", "another_role");
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 Long organizationId = 1L;
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Pageable pageable = PageRequest.of(0, 10);
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 assertEquals(3, result.getContent().size());
 }

 @Test
 public void testFindByUserId() {
 Long userId = 1L;
 List<RoleEntity> result = roleRepository.findByUserId(userId);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 Long userId = 1L;
 Boolean enabled = true;
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(1, result.size());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
 Boolean enabled = true;
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(1, result.size());
 }

 @Test
 public void testUpdate() {
 Long id = 1L;
 String newName = "updated_role";
 Boolean newEnabled = false;
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(id);
 roleEntity.setName(newName);
 roleEntity.setEnabled(newEnabled);
 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }
}