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
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;

    @Before
    public void setup() {
        roleEntity1 = new RoleEntity();
        roleEntity1.setName("role1");
        roleEntity1.setDescription("description1");
        roleEntity1.setOrganizationId(1L);
        roleEntity1.setEnabled(true);

        roleEntity2 = new RoleEntity();
        roleEntity2.setName("role2");
        roleEntity2.setDescription("description2");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(true);

        roleEntity3 = new RoleEntity();
        roleEntity3.setName("role3");
        roleEntity3.setDescription("description3");
        roleEntity3.setOrganizationId(2L);
        roleEntity3.setEnabled(false);

        roleRepository.save(roleEntity1);
        roleRepository.save(roleEntity2);
        roleRepository.save(roleEntity3);
    }

    // Test for findByIdIn(Collection<Long> ids)
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roles = roleRepository.findByIdIn(ids);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));

        ids.add(4L);
        roles = roleRepository.findByIdIn(ids);
        assertEquals(2, roles.size());
    }

    // Test for findByName(String name)
    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("role1");
        assertEquals(1, roles.size());
        assertEquals(roleEntity1.getId(), roles.get(0).getId());

        roles = roleRepository.findByName("unknown");
        assertEquals(0, roles.size());
    }

    // Test for findByOrganizationId(Long organizationId, Pageable pageable)
    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(2, page.getTotalElements());
        assertEquals(roleEntity1, page.getContent().get(0));

        pageable = PageRequest.of(1, 10, Sort.by("id").ascending());
        page = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(1, page.getTotalElements());
        assertEquals(roleEntity2, page.getContent().get(0));
    }

    // Test for findByNameAndOrganizationId(String name, Long organizationId)
    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("role1", 1L);
        assertTrue(role.isPresent());
        assertEquals(roleEntity1, role.get());

        role = roleRepository.findByNameAndOrganizationId("unknown", 1L);
        assertFalse(role.isPresent());
    }

    // Test for findByOrganizationIdAndNameIn(Long organizationId, List<String> name)
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));

        names.add("unknown");
        roles = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roles.size());
    }

    // Test for findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds, Pageable pageable)
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Collection<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, page.getTotalElements());

        roleIds.add(4L);
        page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, page.getTotalElements());
    }

    // Test for findByUserId(Long userId)
    @Test
    public void testFindByUserId() {
        List<RoleEntity> roles = roleRepository.findByUserId(roleEntity1.getId());
        assertEquals(1, roles.size());
        assertEquals(roleEntity1, roles.get(0));

        roles = roleRepository.findByUserId(4L);
        assertEquals(0, roles.size());
    }

    // Test for findByUserIdAndEnabled(Long userId, Boolean enabled)
    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(roleEntity1.getId(), true);
        assertEquals(1, roles.size());
        assertEquals(roleEntity1, roles.get(0));

        roles = roleRepository.findByUserIdAndEnabled(roleEntity1.getId(), false);
        assertEquals(0, roles.size());

        roles = roleRepository.findByUserIdAndEnabled(4L, true);
        assertEquals(0, roles.size());
    }

    // Test for findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled)
    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));

        roleIds.add(4L);
        roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roles.size());
    }

    // Test for update(RoleEntity roleEntity)
    @Test
    @Transactional
    public void testUpdate() {
        roleEntity1.setName("newRoleName");
        roleEntity1.setDescription("newDescription");
        roleEntity1.setEnabled(false);

        int updatedRows = roleRepository.update(roleEntity1);
        assertEquals(1, updatedRows);

        RoleEntity updatedRole = roleRepository.findById(roleEntity1.getId()).get();
        assertNotSame(roleEntity1, updatedRole);
        assertEquals(roleEntity1.getId(), updatedRole.getId());
        assertEquals("newRoleName", updatedRole.getName());
        assertEquals("newDescription", updatedRole.getDescription());
        assertFalse(updatedRole.isEnabled());
    }
}