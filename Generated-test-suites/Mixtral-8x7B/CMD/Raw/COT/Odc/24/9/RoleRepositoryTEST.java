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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@Transactional
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
        String name = "test_role";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(10, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test_role";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test_role", "test_role_2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(3, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertEquals(1, roleEntities.size());
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
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(3, roleEntities.size());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("updated_role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("updated_role_description");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }

    @Test
    public void testFindByIdIn_EmptyList() {
        List<Long> ids = new ArrayList<>();
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByName_EmptyString() {
        String name = "";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId_NullOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(null, pageable);
        assertEquals(0, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId_NullName() {
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(null, organizationId);
        assertFalse(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn_NullOrganizationId() {
        List<String> names = Arrays.asList("test_role", "test_role_2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(null, names);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_NullOrganizationId() {
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(null, roleIds, pageable);
        assertEquals(0, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByUserId_NullUserId() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(null);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled_NullUserId() {
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(null, enabled);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled_NullRoleIds() {
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(null, enabled);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByIdIn_NullIds() {
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(null);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testUpdate_NullRoleEntity() {
        int result = roleRepository.update(null);
        assertEquals(0, result);
    }

    @Test
    public void testUpdate_IdNullRoleEntity() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("updated_role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("updated_role_description");
        int result = roleRepository.update(roleEntity);
        assertEquals(0, result);
    }
}