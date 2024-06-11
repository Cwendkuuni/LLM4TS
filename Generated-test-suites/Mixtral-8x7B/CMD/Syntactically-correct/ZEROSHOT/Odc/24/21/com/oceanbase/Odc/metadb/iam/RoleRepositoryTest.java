import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> result = roleRepository.findByName("Admin");
        assertEquals(1, result.size());
        assertEquals("Admin", result.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = RoleRepository.getDefaultPageRequest();
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(result.isPresent());
        assertEquals("Admin", result.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("Admin", "User");
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = RoleRepository.getDefaultPageRequest();
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L,
                Arrays.asList(1L, 2L), pageable);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Updated Admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("This is an updated admin role.");

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);

        RoleEntity updatedRole = roleRepository.findById(1L).get();
        assertEquals("Updated Admin", updatedRole.getName());
        assertTrue(updatedRole.isEnabled());
        assertEquals("This is an updated admin role.", updatedRole.getDescription());
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("New Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("This is a new role.");

        RoleEntity savedRole = roleRepository.save(roleEntity);
        assertNotNull(savedRole.getId());

        RoleEntity retrievedRole = roleRepository.findByName("New Role").get(0);
        assertEquals(savedRole.getId(), retrievedRole.getId());
    }

    @Test
    public void testDelete() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("Role To Be Deleted");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("This role will be deleted.");

        RoleEntity savedRole = roleRepository.save(roleEntity);
        assertNotNull(savedRole.getId());

        roleRepository.deleteById(savedRole.getId());
        assertNull(roleRepository.findById(savedRole.getId()).orElse(null));
    }

    @Test
    public void testCount() {
        long totalRoles = roleRepository.count();
        assertTrue(totalRoles >= 2);
    }

    private static Pageable getDefaultPageRequest() {
        return org.springframework.data.domain.PageRequest.of(0, 10);
    }

    private RoleEntity createTestRoleEntity(Long id, String name, Long organizationId, Boolean enabled) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(id);
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);
        roleEntity.setEnabled(enabled);
        return roleEntity;
    }

    private List<RoleEntity> createTestRoleEntityList(Long id, String name, Long organizationId,
            Boolean enabled) {
        RoleEntity roleEntity1 = createTestRoleEntity(id, name, organizationId, enabled);
        RoleEntity roleEntity2 = createTestRoleEntity(id + 1, name + "2", organizationId, enabled);
        return Arrays.asList(roleEntity1, roleEntity2);
    }

    private void assertRoleEntity(RoleEntity roleEntity, Long id, String name, Long organizationId,
            Boolean enabled) {
        assertEquals(id, roleEntity.getId());
        assertEquals(name, roleEntity.getName());
        assertEquals(organizationId, roleEntity.getOrganizationId());
        assertEquals(enabled, roleEntity.isEnabled());
    }
}