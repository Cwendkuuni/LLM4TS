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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
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
        String name = "admin";
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
        String name = "admin";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("admin", "dev");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(2, page.getContent().size());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertEquals(2, roleEntities.size());
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
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(2, roleEntities.size());
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("newAdmin");
        roleEntity.setEnabled(false);
        roleEntity.setDescription("new description");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        RoleEntity updatedRoleEntity = roleRepository.findById(1L).get();
        assertEquals("newAdmin", updatedRoleEntity.getName());
        assertFalse(updatedRoleEntity.isEnabled());
        assertEquals("new description", updatedRoleEntity.getDescription());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        RoleEntity roleEntity = roleRepository.findById(id).get();
        assertNotNull(roleEntity);
        assertEquals(id, roleEntity.getId());
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 100L;
        RoleEntity roleEntity = roleRepository.findById(id).get();
        assertNull(roleEntity);
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("newRole");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
        roleEntity.setDescription("new role description");
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity);
        assertEquals(roleEntity.getName(), savedRoleEntity.getName());
        assertEquals(roleEntity.getOrganizationId(), savedRoleEntity.getOrganizationId());
        assertEquals(roleEntity.isEnabled(), savedRoleEntity.isEnabled());
        assertEquals(roleEntity.getDescription(), savedRoleEntity.getDescription());
    }

    @Test
    public void testDelete() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleRepository.delete(roleEntity);
        RoleEntity deletedRoleEntity = roleRepository.findById(1L).get();
        assertNull(deletedRoleEntity);
    }

    @Test
    public void testDeleteById() {
        roleRepository.deleteById(2L);
        RoleEntity deletedRoleEntity = roleRepository.findById(2L).get();
        assertNull(deletedRoleEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNullRoleEntity() {
        roleRepository.save(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullRoleEntity() {
        roleRepository.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteByIdNegativeId() {
        roleRepository.deleteById(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByIdInEmptyIds() {
        roleRepository.findByIdIn(new ArrayList<Long>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByNameEmptyName() {
        roleRepository.findByName("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdNullPageable() {
        roleRepository.findByOrganizationId(1L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByNameAndOrganizationIdNullName() {
        roleRepository.findByNameAndOrganizationId(null, 1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByNameAndOrganizationIdNullOrganizationId() {
        roleRepository.findByNameAndOrganizationId("admin", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndNameInNullOrganizationId() {
        roleRepository.findByOrganizationIdAndNameIn(null, Arrays.asList("admin", "dev"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsNullOrganizationId() {
        roleRepository.findByOrganizationIdAndRoleIds(null, Arrays.asList(1L, 2L), Pageable.unpaged());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsNullRoleIds() {
        roleRepository.findByOrganizationIdAndRoleIds(1L, null, Pageable.unpaged());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByUserIdNegativeUserId() {
        roleRepository.findByUserId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByUserIdAndEnabledNullUserId() {
        roleRepository.findByUserIdAndEnabled(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByRoleIdsAndEnabledNullRoleIds() {
        roleRepository.findByRoleIdsAndEnabled(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByRoleIdsAndEnabledNullEnabled() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), null);
    }
}