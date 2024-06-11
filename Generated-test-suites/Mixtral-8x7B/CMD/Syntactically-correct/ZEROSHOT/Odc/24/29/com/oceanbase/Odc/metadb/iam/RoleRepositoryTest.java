import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
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
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTest.TestConfig.class, loader = AnnotationConfigContextLoader.class)
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
        roleEntity1.setOrganizationId(1L);
        roleEntity1.setEnabled(true);

        roleEntity2 = new RoleEntity();
        roleEntity2.setName("role2");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(true);

        roleEntity3 = new RoleEntity();
        roleEntity3.setName("role3");
        roleEntity3.setOrganizationId(1L);
        roleEntity3.setEnabled(false);

        roleRepository.saveAll(Arrays.asList(roleEntity1, roleEntity2, roleEntity3));
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("role1");
        assertEquals(1, roleEntities.size());
        assertEquals(roleEntity1, roleEntities.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(3, roleEntities.getTotalElements());
        assertTrue(roleEntities.getContent().contains(roleEntity1));
        assertTrue(roleEntities.getContent().contains(roleEntity2));
        assertTrue(roleEntities.getContent().contains(roleEntity3));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("role1", 1L);
        assertTrue(roleEntity.isPresent());
        assertEquals(roleEntity1, roleEntity.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, roleEntities.getTotalElements());
        assertTrue(roleEntities.getContent().contains(roleEntity1));
        assertTrue(roleEntities.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
        assertEquals(3, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
        assertTrue(roleEntities.contains(roleEntity3));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        String newName = "newRoleName";
        boolean newEnabled = false;
        roleEntity1.setName(newName);
        roleEntity1.setEnabled(newEnabled);
        int rowsAffected = roleRepository.update(roleEntity1);
        assertEquals(1, rowsAffected);

        Optional<RoleEntity> updatedRoleEntity = roleRepository.findById(roleEntity1.getId());
        assertTrue(updatedRoleEntity.isPresent());
        RoleEntity roleEntity = updatedRoleEntity.get();
        assertEquals(newName, roleEntity.getName());
        assertEquals(newEnabled, roleEntity.isEnabled());
    }

    @Test
    public void testSave() {
        RoleEntity newRoleEntity = new RoleEntity();
        newRoleEntity.setName("newRole");
        newRoleEntity.setOrganizationId(1L);
        newRoleEntity.setEnabled(true);
        roleRepository.save(newRoleEntity);

        Optional<RoleEntity> savedRoleEntity = roleRepository.findById(newRoleEntity.getId());
        assertTrue(savedRoleEntity.isPresent());
        RoleEntity roleEntity = savedRoleEntity.get();
        assertEquals("newRole", roleEntity.getName());
        assertEquals(1L, roleEntity.getOrganizationId().longValue());
        assertEquals(true, roleEntity.isEnabled());
    }

    @Test
    public void testDeleteById() {
        roleRepository.deleteById(roleEntity1.getId());

        Optional<RoleEntity> deletedRoleEntity = roleRepository.findById(roleEntity1.getId());
        assertFalse(deletedRoleEntity.isPresent());
    }

    @Test
    public void testExistsById() {
        assertTrue(roleRepository.existsById(roleEntity1.getId()));
        roleRepository.deleteById(roleEntity1.getId());
        assertFalse(roleRepository.existsById(roleEntity1.getId()));
    }

    @Test
    public void testCount() {
        long count = roleRepository.count();
        assertEquals(3, count);
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = (List<RoleEntity>) roleRepository.findAll();
        assertEquals(3, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
        assertTrue(roleEntities.contains(roleEntity3));
    }

    @Test
    public void testFindAllById() {
        List<Long> ids = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roleEntities = (List<RoleEntity>) roleRepository.findAllById(ids);
        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindAllByPageable() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findAll(pageable);
        assertEquals(3, roleEntities.getTotalElements());
        assertEquals(3, roleEntities.getContent().size());
        assertTrue(roleEntities.getContent().contains(roleEntity1));
        assertTrue(roleEntities.getContent().contains(roleEntity2));
        assertTrue(roleEntities.getContent().contains(roleEntity3));
    }

    @Test
    public void testFindAllByPageableWithSort() {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(0, 10, sort);
        Page<RoleEntity> roleEntities = roleRepository.findAll(pageable);
        assertEquals(3, roleEntities.getTotalElements());
        assertEquals(3, roleEntities.getContent().size());
        assertEquals(roleEntity3.getId(), roleEntities.getContent().get(0).getId());
        assertEquals(roleEntity2.getId(), roleEntities.getContent().get(1).getId());
        assertEquals(roleEntity1.getId(), roleEntities.getContent().get(2).getId());
    }

    @Test
    public void testFindAllByPageableWithCustomQuery() {
        String name = "role1";
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByName(name, pageable);
        assertEquals(1, roleEntities.getTotalElements());
        assertEquals(1, roleEntities.getContent().size());
        assertEquals(roleEntity1.getId(), roleEntities.getContent().get(0).getId());
    }

    @Test
    public void testFindAllByPageableWithCustomQueryAndNamedParameters() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("role1", "role2");
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names, pageable);
        assertEquals(2, roleEntities.getTotalElements());
        assertEquals(2, roleEntities.getContent().size());
        assertTrue(roleEntities.getContent().contains(roleEntity1));
        assertTrue(roleEntities.getContent().contains(roleEntity2));
    }

    static class TestConfig {
    }
}