import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(1, roleEntities.getContent().size());
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
        List<String> names = Arrays.asList("admin", "test");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id")));
        assertEquals(2, roleEntities.getContent().size());
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
        roleEntity.setName("updated-admin");
        roleEntity.setEnabled(false);
        roleEntity.setDescription("updated description");
        int rowsAffected = roleRepository.update(roleEntity);
        assertEquals(1, rowsAffected);

        Optional<RoleEntity> updatedRoleEntity = roleRepository.findById(1L);
        assertTrue(updatedRoleEntity.isPresent());
        RoleEntity entity = updatedRoleEntity.get();
        assertEquals("updated-admin", entity.getName());
        assertEquals(false, entity.getEnabled());
        assertEquals("updated description", entity.getDescription());
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test-role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test role description");
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity.getId());

        Optional<RoleEntity> savedOptional = roleRepository.findById(savedRoleEntity.getId());
        assertTrue(savedOptional.isPresent());
        RoleEntity saved = savedOptional.get();
        assertEquals(savedRoleEntity.getName(), saved.getName());
        assertEquals(savedRoleEntity.getEnabled(), saved.getEnabled());
        assertEquals(savedRoleEntity.getDescription(), saved.getDescription());
    }

    @Test
    public void testExistsById() {
        assertTrue(roleRepository.existsById(1L));
        assertFalse(roleRepository.existsById(1000L));
    }

    @Test
    public void testDelete() {
        roleRepository.deleteById(1L);
        assertFalse(roleRepository.existsById(1L));
    }

    @Test
    @Transactional
    public void testDeleteAll() {
        roleRepository.deleteAll();
        assertTrue(roleRepository.findAll().isEmpty());
    }

    @Test
    public void testCount() {
        Long count = roleRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = new ArrayList<>(roleRepository.findAll());
        assertTrue(!roleEntities.isEmpty());
    }
}