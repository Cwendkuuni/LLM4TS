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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTestConfig.class)
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
        List<RoleEntity> roleEntities = roleRepository.findByName("admin");
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(10, page.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("admin", 1L);
        assertTrue(roleEntityOptional.isPresent());
        assertEquals(1L, roleEntityOptional.get().getId().longValue());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("admin", "guest");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), pageable);
        assertEquals(2, page.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);
        assertEquals(2, roleEntities.size());
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = roleRepository.findById(1L).get();
        roleEntity.setName("newName");
        roleEntity.setEnabled(false);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        RoleEntity updatedRoleEntity = roleRepository.findById(1L).get();
        assertEquals("newName", updatedRoleEntity.getName());
        assertFalse(updatedRoleEntity.isEnabled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullRoleEntity() {
        roleRepository.update(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullId() {
        RoleEntity roleEntity = new RoleEntity();
        roleRepository.update(roleEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullName() {
        RoleEntity roleEntity = roleRepository.findById(1L).get();
        roleEntity.setName(null);
        roleRepository.update(roleEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateNullEnabled() {
        RoleEntity roleEntity = roleRepository.findById(1L).get();
        roleEntity.setEnabled(null);
        roleRepository.update(roleEntity);
    }
}

class RoleRepositoryTestConfig {

    // Initialize test data for the repository

}