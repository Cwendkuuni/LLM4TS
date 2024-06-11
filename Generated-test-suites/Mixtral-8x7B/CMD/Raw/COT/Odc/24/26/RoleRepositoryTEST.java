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

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    // Test for findByIdIn(Collection<Long> ids)
    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(3, roleEntities.size());
    }

    // Test for findByName(String name)
    @Test
    public void testFindByName() {
        String name = "Test Role";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertEquals(1, roleEntities.size());
    }

    // Test for findByOrganizationId(Long organizationId, Pageable pageable)
    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(10, page.getSize());
    }

    // Test for findByNameAndOrganizationId(String name, Long organizationId)
    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Test Role";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(roleEntity.isPresent());
    }

    // Test for findByOrganizationIdAndNameIn(Long organizationId, List<String> name)
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> name = Arrays.asList("Test Role", "Another Test Role");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, name);
        assertEquals(2, roleEntities.size());
    }

    // Test for findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds, Pageable pageable)
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(3, page.getTotalElements());
    }

    // Test for findByUserId(Long userId)
    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertEquals(2, roleEntities.size());
    }

    // Test for findByUserIdAndEnabled(Long userId, Boolean enabled)
    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(1, roleEntities.size());
    }

    // Test for findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled)
    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(3, roleEntities.size());
    }

    // Test for update(RoleEntity roleEntity)
    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Updated Test Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Test Role Description");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }

    // Test for edge case when findByIdIn() with empty list
    @Test
    public void testFindByIdInEdgeCase() {
        List<Long> ids = new ArrayList<>();
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(0, roleEntities.size());
    }

    // Test for edge case when findByName(String name) with empty string
    @Test
    public void testFindByNameEdgeCase() {
        String name = "";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertEquals(0, roleEntities.size());
    }

    // Test for edge case when findByOrganizationId(Long organizationId, Pageable pageable) with null organizationId
    @Test(expected = NullPointerException.class)
    public void testFindByOrganizationIdEdgeCase() {
        Pageable pageable = PageRequest.of(0, 10);
        roleRepository.findByOrganizationId(null, pageable);
    }

    // Test for edge case when findByNameAndOrganizationId(String name, Long organizationId) with null name
    @Test(expected = NullPointerException.class)
    public void testFindByNameAndOrganizationIdEdgeCase() {
        Long organizationId = 1L;
        roleRepository.findByNameAndOrganizationId(null, organizationId);
    }

    // Test for edge case when findByOrganizationIdAndNameIn(Long organizationId, List<String> name) with null organizationId
    @Test(expected = NullPointerException.class)
    public void testFindByOrganizationIdAndNameInEdgeCase() {
        Long organizationId = null;
        List<String> name = Arrays.asList("Test Role", "Another Test Role");
        roleRepository.findByOrganizationIdAndNameIn(organizationId, name);
    }

    // Test for edge case when findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds, Pageable pageable)
    // with null organizationId
    @Test(expected = NullPointerException.class)
    public void testFindByOrganizationIdAndRoleIdsEdgeCase() {
        Long organizationId = null;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
    }

    // Test for edge case when findByUserId(Long userId) with null userId
    @Test(expected = NullPointerException.class)
    public void testFindByUserIdEdgeCase() {
        roleRepository.findByUserId(null);
    }

    // Test for edge case when findByUserIdAndEnabled(Long userId, Boolean enabled) with null userId
    @Test(expected = NullPointerException.class)
    public void testFindByUserIdAndEnabledEdgeCase() {
        roleRepository.findByUserIdAndEnabled(null, true);
    }

    // Test for edge case when findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled) with null roleIds
    @Test(expected = NullPointerException.class)
    public void testFindByRoleIdsAndEnabledEdgeCase() {
        roleRepository.findByRoleIdsAndEnabled(null, true);
    }

    // Test for exception handling when update(RoleEntity roleEntity) with null roleEntity
    @Test(expected = NullPointerException.class)
    public void testUpdateException() {
        roleRepository.update(null);
    }

}