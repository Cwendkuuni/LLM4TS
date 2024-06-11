import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
@ContextConfiguration(classes = RoleRepositoryTestConfig.class, loader = AnnotationConfigContextLoader.class)
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
        List<RoleEntity> roleEntities = roleRepository.findByName("test_role");
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(10, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("test_role", 1L);
        assertTrue(roleEntityOptional.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("test_role_1", "test_role_2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(3, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L, 3L), true);
        assertEquals(3, roleEntities.size());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("updated_test_role");
        roleEntity.setEnabled(false);
        roleEntity.setDescription("updated test role description");
        roleEntity.setId(1L);

        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(1, updatedRows);

        RoleEntity updatedRoleEntity = roleRepository.findById(1L).orElseThrow(IllegalStateException::new);
        assertEquals("updated_test_role", updatedRoleEntity.getName());
        assertEquals(false, updatedRoleEntity.getEnabled());
        assertEquals("updated test role description", updatedRoleEntity.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByNameThrowsIllegalArgumentException() {
        roleRepository.findByName(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindByIdThrowsIllegalStateException() {
        roleRepository.findById(-1L).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindByOrganizationIdThrowsIllegalStateException() {
        roleRepository.findByOrganizationId(-1L, Pageable.unpaged()).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByNameAndOrganizationIdThrowsIllegalArgumentException() {
        roleRepository.findByNameAndOrganizationId(null, 1L);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindByNameAndOrganizationIdThrowsIllegalStateException() {
        roleRepository.findByNameAndOrganizationId("test_role", -1L).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndNameInThrowsIllegalArgumentException() {
        roleRepository.findByOrganizationIdAndNameIn(1L, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindByOrganizationIdAndNameInThrowsIllegalStateException() {
        roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("test_role", null)).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByOrganizationIdAndRoleIdsThrowsIllegalArgumentException() {
        roleRepository.findByOrganizationIdAndRoleIds(1L, null, Pageable.unpaged());
    }

    @Test(expected = IllegalStateException.class)
    public void testFindByUserIdThrowsIllegalStateException() {
        roleRepository.findByUserId(-1L).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindByRoleIdsAndEnabledThrowsIllegalArgumentException() {
        roleRepository.findByRoleIdsAndEnabled(null, true);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindByRoleIdsAndEnabledThrowsIllegalStateException() {
        roleRepository.findByRoleIdsAndEnabled(Arrays.asList(-1L, 2L, 3L), true).orElseThrow(IllegalStateException::new);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateThrowsIllegalArgumentException() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(null);
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test role description");
        roleEntity.setId(1L);

        roleRepository.update(roleEntity);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateThrowsIllegalStateException() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test_role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test role description");
        roleEntity.setId(-1L);

        roleRepository.update(roleEntity);
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = new ArrayList<>(roleRepository.findAll());
        assertEquals(10, roleEntities.size());
    }

    @Test
    public void testFindAllWithEmptyList() {
        List<RoleEntity> roleEntities = new ArrayList<>(roleRepository.findAll());
        assertEquals(10, roleEntities.size());

        List<RoleEntity> emptyRoleEntities = new ArrayList<>(roleRepository.findAll());
        assertEquals(0, emptyRoleEntities.size());
    }

    @Test
    public void testFindByIdWithOptional() {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(1L);
        assertTrue(roleEntityOptional.isPresent());

        roleEntityOptional = roleRepository.findById(101L);
        assertFalse(roleEntityOptional.isPresent());
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test_role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test role description");

        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity.getId());

        RoleEntity retrievedRoleEntity = roleRepository.findById(savedRoleEntity.getId()).orElseThrow(IllegalStateException::new);
        assertEquals(savedRoleEntity.getName(), retrievedRoleEntity.getName());
        assertEquals(savedRoleEntity.getEnabled(), retrievedRoleEntity.getEnabled());
        assertEquals(savedRoleEntity.getDescription(), retrievedRoleEntity.getDescription());
    }

    @Test
    public void testDelete() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test_role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test role description");

        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity.getId());

        roleRepository.delete(savedRoleEntity);
        assertEquals(9, roleRepository.findAll().size());
    }

    @Test
    public void testCount() {
        long count = roleRepository.count();
        assertEquals(10, count);
    }

    @Test
    public void testExistsById() {
        assertTrue(roleRepository.existsById(1L));
        assertFalse(roleRepository.existsById(101L));
    }
}