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
assertFalse(roleEntities.isEmpty());
}

@Test
public void testFindByName() {
String name = "test_role";
List<RoleEntity> roleEntities = roleRepository.findByName(name);
assertFalse(roleEntities.isEmpty());
}

@Test
public void testFindByOrganizationId() {
Long organizationId = 1L;
Pageable pageable = PageRequest.of(0, 10);
Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
assertFalse(roleEntities.getContent().isEmpty());
}

@Test
public void testFindByNameAndOrganizationId() {
String name = "test_role";
Long organizationId = 1L;
Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
assertTrue(roleEntity.isPresent());
}

@Test
public void testFindByOrganizationIdAndNameIn() {
Long organizationId = 1L;
List<String> names = Arrays.asList("test_role_1", "test_role_2");
List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
assertFalse(roleEntities.isEmpty());
}

@Test
public void testFindByOrganizationIdAndRoleIds() {
Long organizationId = 1L;
List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
Pageable pageable = PageRequest.of(0, 10);
Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
assertFalse(roleEntities.getContent().isEmpty());
}

@Test
public void testFindByUserId() {
Long userId = 1L;
List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
assertFalse(roleEntities.isEmpty());
}

@Test
public void testFindByUserIdAndEnabled() {
Long userId = 1L;
Boolean enabled = true;
List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
assertFalse(roleEntities.isEmpty());
}

@Test
public void testFindByRoleIdsAndEnabled() {
List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
Boolean enabled = true;
List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
assertFalse(roleEntities.isEmpty());
}

@Test
public void testUpdate() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);
roleEntity.setName("updated_test_role");
roleEntity.setEnabled(true);
roleEntity.setDescription("updated_test_role_description");
int count = roleRepository.update(roleEntity);
assertEquals(1, count);
}

@Test(expected = IllegalArgumentException.class)
public void testUpdateWithNullId() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setName("updated_test_role");
roleEntity.setEnabled(true);
roleEntity.setDescription("updated_test_role_description");
roleRepository.update(roleEntity);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByNameWithNullName() {
roleRepository.findByName(null);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByOrganizationIdWithNullOrganizationId() {
Pageable pageable = PageRequest.of(0, 10);
roleRepository.findByOrganizationId(null, pageable);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByNameAndOrganizationIdWithNullName() {
Long organizationId = 1L;
roleRepository.findByNameAndOrganizationId(null, organizationId);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByOrganizationIdAndNameInWithNullOrganizationId() {
List<String> names = Arrays.asList("test_role_1", "test_role_2");
roleRepository.findByOrganizationIdAndNameIn(null, names);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByOrganizationIdAndRoleIdsWithNullOrganizationId() {
List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
Pageable pageable = PageRequest.of(0, 10);
roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, pageable);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByUserIdWithNullUserId() {
roleRepository.findByUserId(null);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByUserIdAndEnabledWithNullUserId() {
Boolean enabled = true;
roleRepository.findByUserIdAndEnabled(null, enabled);
}

@Test(expected = IllegalArgumentException.class)
public void testFindByRoleIdsAndEnabledWithNullRoleIds() {
Boolean enabled = true;
roleRepository.findByRoleIdsAndEnabled(null, enabled);
}

@Test(expected = IllegalArgumentException.class)
public void testUpdateWithNullRoleEntity() {
roleRepository.update(null);
}
}