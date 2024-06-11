import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
public class RoleRepositoryTest {

 @Autowired
 private RoleRepository roleRepository;

 @Test
 public void testJpaRepositoryMethods() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);
 roleRepository.save(roleEntity);

 // Test findById
 RoleEntity foundRole = roleRepository.findById(roleEntity.getId()).orElse(null);
 assertNotNull(foundRole);

 // Test findAll
 List<RoleEntity> allRoles = (List<RoleEntity>) roleRepository.findAll();
 assertTrue(allRoles.size() > 0);

 // Test findByIdIn
 List<Long> ids = Collections.singletonList(roleEntity.getId());
 List<RoleEntity> foundRoles = roleRepository.findByIdIn(ids);
 assertTrue(foundRoles.size() == 1);

 // Test deleteById
 roleRepository.deleteById(roleEntity.getId());
 }

 @Test
 public void testJpaSpecificationExecutorMethods() {
 // Test findByName
 List<RoleEntity> foundRoles = roleRepository.findByName("Test Role");
 assertTrue(foundRoles.size() == 1);

 // Test findByOrganizationId
 foundRoles = roleRepository.findByOrganizationId(1L, PageRequest.of(0, 10));
 assertTrue(foundRoles.size() > 0);

 // Test findByNameAndOrganizationId
 RoleEntity foundRole = roleRepository.findByNameAndOrganizationId("Test Role", 1L);
 assertNotNull(foundRole);

 // Test findByOrganizationIdAndNameIn
 List<String> names = Collections.singletonList("Test Role");
 foundRoles = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertTrue(foundRoles.size() == 1);

 // Test findByOrganizationIdAndRoleIds
 List<Long> roleIds = Collections.singletonList(roleEntity.getId());
 Page<RoleEntity> pageableRoles = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, PageRequest.of(0, 10));
 assertTrue(pageableRoles.getContent().size() == 1);

 // Test findByUserId
 Long userId = 1L;
 foundRoles = roleRepository.findByUserId(userId);
 assertTrue(foundRoles.size() > 0);

 // Test findByUserIdAndEnabled
 Boolean enabled = true;
 foundRoles = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertTrue(foundRoles.size() > 0);

 // Test findByRoleIdsAndEnabled
 roleIds = Collections.singletonList(roleEntity.getId());
 foundRoles = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertTrue(foundRoles.size() == 1);
 }

 @Test
 public void testCustomQueryMethods() {
 // Test findByOrganizationIdAndRoleIds
 Long organizationId = 1L;
 List<Long> roleIds = Collections.singletonList(roleEntity.getId());
 Page<RoleEntity> pageableRoles = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, PageRequest.of(0, 10));
 assertTrue(pageableRoles.getContent().size() == 1);

 // Test update
 roleEntity.setName("Updated Test Role");
 int rowsAffected = roleRepository.update(roleEntity);
 assertEquals(rowsAffected, 1);
 }
}