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
import org.springframework.data.domain.Sort;
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
        assertEquals(ids.size(), roleEntities.size());
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(10, roleEntities.getContent().size());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test1", "test2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                pageable);
        assertEquals(10, roleEntities.getContent().size());
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
        roleEntity.setName("test");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test desc");

        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
        assertNotNull(roleEntity);
    }

    @Test
    public void testFindByIdNotFound() {
        Long id = 999L;
        RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
        assertNull(roleEntity);
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test desc");

        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity.getId());
    }

    @Test
    public void testDelete() {
        Long id = 1L;
        roleRepository.deleteById(id);
        RoleEntity roleEntity = roleRepository.findById(id).orElse(null);
        assertNull(roleEntity);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteNullId() {
        roleRepository.deleteById(null);
    }

    @Test
    public void testExistsById() {
        Long id = 1L;
        boolean exists = roleRepository.existsById(id);
        assertTrue(exists);
    }

    @Test
    public void testExistsByIdNotFound() {
        Long id = 999L;
        boolean exists = roleRepository.existsById(id);
        assertFalse(exists);
    }

    @Test
    public void testCount() {
        Long count = roleRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = (List<RoleEntity>) roleRepository.findAll();
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testFindAllSort() {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        List<RoleEntity> roleEntities = (List<RoleEntity>) roleRepository.findAll(sort);
        assertEquals(roleEntities.get(0).getId(), 1L);
    }
}