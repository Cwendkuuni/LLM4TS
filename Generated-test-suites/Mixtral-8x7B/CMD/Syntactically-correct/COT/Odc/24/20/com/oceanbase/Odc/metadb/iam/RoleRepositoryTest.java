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

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTestConfig.class)
@Transactional
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
        roleEntity1.setIsEnabled(true);
        roleEntity1.setDescription("description1");

        roleEntity2 = new RoleEntity();
        roleEntity2.setName("role2");
        roleEntity2.setIsEnabled(false);
        roleEntity2.setDescription("description2");

        roleEntity3 = new RoleEntity();
        roleEntity3.setName("role3");
        roleEntity3.setIsEnabled(true);
        roleEntity3.setDescription("description3");

        roleRepository.saveAll(Arrays.asList(roleEntity1, roleEntity2, roleEntity3));
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(roleEntity1.getId(), roleEntity3.getId());
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity3));
    }

    @Test
    public void testFindByName() {
        String name = roleEntity1.getName();
        List<RoleEntity> roleEntities = roleRepository.findByName(name);

        assertEquals(1, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(3, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = roleEntity1.getName();
        Long organizationId = roleEntity1.getOrganizationId();

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(optionalRoleEntity.isPresent());
        assertEquals(roleEntity1, optionalRoleEntity.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList(roleEntity1.getName(), roleEntity3.getName());
        Long organizationId = roleEntity1.getOrganizationId();

        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity3));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity3.getId());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(2, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);

        assertEquals(3, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;

        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity3));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity3.getId());
        Boolean enabled = true;

        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity3));
    }

    @Test
    public void testUpdate() {
        roleEntity1.setName("updatedRole1");
        roleEntity1.setIsEnabled(false);

        int rowsAffected = roleRepository.update(roleEntity1);

        assertEquals(1, rowsAffected);

        RoleEntity updatedRoleEntity = roleRepository.findById(roleEntity1.getId()).get();

        assertEquals(updatedRoleEntity.getName(), roleEntity1.getName());
        assertEquals(updatedRoleEntity.getIsEnabled(), roleEntity1.getIsEnabled());
    }

    @Test
    public void testFindByIdIn_EmptyIds() {
        List<Long> ids = new ArrayList<>();

        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);

        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByName_EmptyName() {
        String name = "";

        List<RoleEntity> roleEntities = roleRepository.findByName(name);

        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId_InvalidOrganizationId() {
        Long organizationId = 999L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(0, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId_InvalidName() {
        String name = "invalidName";
        Long organizationId = roleEntity1.getOrganizationId();

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertFalse(optionalRoleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn_InvalidName() {
        List<String> names = Arrays.asList("invalidName1", "invalidName2");
        Long organizationId = roleEntity1.getOrganizationId();

        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_InvalidOrganizationId() {
        Long organizationId = 999L;
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity3.getId());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(0, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByUserId_InvalidUserId() {
        Long userId = 999L;

        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);

        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled_InvalidRoleIds() {
        List<Long> roleIds = Arrays.asList(999L, 999L);
        Boolean enabled = true;

        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testUpdate_InvalidRoleEntity() {
        RoleEntity invalidRoleEntity = new RoleEntity();
        invalidRoleEntity.setId(999L);

        int rowsAffected = roleRepository.update(invalidRoleEntity);

        assertEquals(0, rowsAffected);
    }
}