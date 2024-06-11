import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class RoleRepositoryTest {

@Autowired
private RoleRepository roleRepository;

@Test
public void testFindByIdIn() {
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);
List<RoleEntity> result = roleRepository.findByIdIn(ids);
assertFalse(result.isEmpty());
}

@Test
public void testFindByName() {
String name = "testRole";
List<RoleEntity> result = roleRepository.findByName(name);
assertEquals(1, result.size());
}

@Test
public void testFindByOrganizationId() {
Long organizationId = 1L;
Pageable pageable = null; // configure your Pageable instance
Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
assertFalse(result.getContent().isEmpty());
}

@Test
public void testFindByNameAndOrganizationId() {
String name = "testRole";
Long organizationId = 1L;
Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
assertTrue(result.isPresent());
}

@Test
public void testFindByOrganizationIdAndNameIn() {
Long organizationId = 1L;
List<String> names = new ArrayList<>();
names.add("testRole1");
names.add("testRole2");
List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
assertFalse(result.isEmpty());
}

@Test
public void testFindByOrganizationIdAndRoleIds() {
Long organizationId = 1L;
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, null);
assertFalse(result.getContent().isEmpty());
}

@Test
public void testFindByUserId() {
Long userId = 1L;
List<RoleEntity> result = roleRepository.findByUserId(userId);
assertFalse(result.isEmpty());
}

@Test
public void testFindByUserIdAndEnabled() {
Long userId = 1L;
Boolean enabled = true;
List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
assertFalse(result.isEmpty());
}

@Test
public void testFindByRoleIdsAndEnabled() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
Boolean enabled = true;
List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
assertFalse(result.isEmpty());
}

@Test
public void testUpdate() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);
roleEntity.setName("updatedRoleName");
roleEntity.setEnabled(false);
roleEntity.setDescription("updatedRoleDescription");
int result = roleRepository.update(roleEntity);
assertEquals(1, result);
}

@Test
public void testSave() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setName("testRole");
roleEntity.setEnabled(true);
roleEntity.setDescription("testRoleDescription");
RoleEntity savedRole = roleRepository.save(roleEntity);
assertNotNull(savedRole.getId());
}

@Test
public void testDeleteById() {
Long id = 1L;
roleRepository.deleteById(id);
}

@Test(expected = IllegalArgumentException.class)
public void testDeleteNonExisting() {
Long id = 100L;
roleRepository.deleteById(id);
}

@Test
public void testFindAll() {
List<RoleEntity> result = new ArrayList<>(roleRepository.findAll());
assertFalse(result.isEmpty());
}

@Test
public void testFindAllOrderByDescriptionAsc() {
List<RoleEntity> result = roleRepository.findAll(Collections.singletonMap("direction", "ASC")
, Collections.singletonMap("property", "description"));
assertFalse(result.isEmpty());
}

@Test
public void testFindAllOrderByDescriptionDesc() {
List<RoleEntity> result = roleRepository.findAll(Collections.singletonMap("direction", "DESC")
, Collections.singletonMap("property", "description"));
assertFalse(result.isEmpty());
}

@Test
public void testCount() {
long count = roleRepository.count();
assertTrue(count > 0);
}

@Test
public void testExistsById() {
Long id = 1L;
boolean result = roleRepository.existsById(id);
assertTrue(result);
}

@Test
public void testFindRoleEntityById() {
Long id = 1L;
Optional<RoleEntity> result = roleRepository.findById(id);
assertTrue(result.isPresent());
}

@Test
public void testFindRoleEntityByIdNotPresent() {
Long id = 100L;
Optional<RoleEntity> result = roleRepository.findById(id);
assertFalse(result.isPresent());
}
}