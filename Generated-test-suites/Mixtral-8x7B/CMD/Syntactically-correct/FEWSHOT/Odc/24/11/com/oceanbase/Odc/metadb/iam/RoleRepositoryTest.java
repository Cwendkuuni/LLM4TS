import static org.junit.Assert.assertEquals;
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
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> result = roleRepository.findByName("admin");
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(10, result.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("admin", 1L);
        assertTrue(result.isPresent());
        assertEquals("admin", result.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("admin", "guest");
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, result.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, result.size());
        assertEquals("admin", result.get(0).getName());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("new_admin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("new description");

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);

        Optional<RoleEntity> updatedRole = roleRepository.findById(1L);
        assertTrue(updatedRole.isPresent());
        assertEquals("new_admin", updatedRole.get().getName());
        assertEquals(true, updatedRole.get().getEnabled());
        assertEquals("new description", updatedRole.get().getDescription());
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test description");

        RoleEntity savedRole = roleRepository.save(roleEntity);
        assertNotNull(savedRole.getId());

        Optional<RoleEntity> savedRoleOptional = roleRepository.findById(savedRole.getId());
        assertTrue(savedRoleOptional.isPresent());
        assertEquals("test", savedRoleOptional.get().getName());
        assertEquals(true, savedRoleOptional.get().getEnabled());
        assertEquals("test description", savedRoleOptional.get().getDescription());
    }
}