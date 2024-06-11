import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;
    private List<RoleEntity> roleEntityList;

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
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roles = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roles = roleRepository.findByName("Role1");
        assertEquals(1, roles.size());
        assertTrue(roles.contains(roleEntity1));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().contains(roleEntity1));
        assertTrue(page.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> role = roleRepository.findByNameAndOrganizationId("Role1", 1L);
        assertTrue(role.isPresent());
        assertEquals(roleEntity1, role.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roles = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Role1", "Role2"));
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().contains(roleEntity1));
        assertTrue(page.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        List<Long> userIds = Arrays.asList(1L);
        List<RoleEntity> roles = roleRepository.findByUserId(1L);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<Long> userIds = Arrays.asList(1L);
        List<RoleEntity> roles = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roles = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roles.size());
        assertTrue(roles.contains(roleEntity1));
        assertTrue(roles.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        roleEntity1.setName("UpdatedRole1");
        int result = roleRepository.update(roleEntity1);
        assertEquals(1, result);

        Optional<RoleEntity> updatedRole = roleRepository.findById(1L);
        assertTrue(updatedRole.isPresent());
        assertEquals("UpdatedRole1", updatedRole.get().getName());
    }

    @Test
    public void testSave() {
        RoleEntity savedRole = roleRepository.save(roleEntity3);
        assertNotNull(savedRole.getId());

        Optional<RoleEntity> role = roleRepository.findById(savedRole.getId());
        assertTrue(role.isPresent());
        assertEquals(roleEntity3, role.get());
    }

    @Test
    public void testDelete() {
        roleRepository.delete(roleEntity1);
        assertFalse(roleRepository.existsById(1L));
    }

    @Test
    public void testCount() {
        long count = roleRepository.count();
        assertEquals(3, count);
    }

    @Test
    public void testExistsById() {
        assertTrue(roleRepository.existsById(1L));
        assertFalse(roleRepository.existsById(4L));
    }
}