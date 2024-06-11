import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
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
assertEquals(1, roleEntities.size());
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
List<String> names = Arrays.asList("test_role1", "test_role2");
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
int rowsAffected = roleRepository.update(roleEntity);
assertEquals(1, rowsAffected);
}

}