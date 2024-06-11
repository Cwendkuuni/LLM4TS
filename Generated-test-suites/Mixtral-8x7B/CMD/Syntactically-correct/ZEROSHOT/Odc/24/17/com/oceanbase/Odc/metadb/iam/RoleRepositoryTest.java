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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity(1L, "Role1", true, "Description1"));
        roleEntities.add(new RoleEntity(2L, "Role2", false, "Description2"));
        roleEntities.add(new RoleEntity(3L, "Role3", true, "Description3"));

        List<RoleEntity> foundRoleEntities = roleRepository.findByIdIn(ids);
        assertEquals(roleEntities, foundRoleEntities);
    }

    @Test
    public void testFindByName() {
        String name = "Role1";
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, name, true, "Description1"));

        List<RoleEntity> foundRoleEntities = roleRepository.findByName(name);
        assertEquals(roleEntities, foundRoleEntities);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = null; // Set the Pageable object based on your requirements
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"));
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        Page<RoleEntity> foundRoleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(page, foundRoleEntities);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Role1";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity(1L, name, true, "Description1"));

        Optional<RoleEntity> foundRoleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertEquals(optionalRoleEntity, foundRoleEntity);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("Role1", "Role2");
        List<RoleEntity> roleEntities = Arrays.asList(
                new RoleEntity(1L, "Role1", true, "Description1"),
                new RoleEntity(2L, "Role2", false, "Description2"));

        List<RoleEntity> foundRoleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(roleEntities, foundRoleEntities);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = null; // Set the Pageable object based on your requirements
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"));
        Page<RoleEntity> page = new PageImpl<>(roleEntities);

        Page<RoleEntity> foundRoleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                pageable);
        assertEquals(page, foundRoleEntities);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"));

        List<RoleEntity> foundRoleEntities = roleRepository.findByUserId(userId);
        assertEquals(roleEntities, foundRoleEntities);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"));

        List<RoleEntity> foundRoleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(roleEntities, foundRoleEntities);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(1L, "Role1", true, "Description1"));

        List<RoleEntity> foundRoleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleEntities, foundRoleEntities);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity(1L, "Role1", true, "Description1");
        int updatedRows = roleRepository.update(roleEntity);
        assertTrue(updatedRows > 0);
    }
}