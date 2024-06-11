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
        List<String> names = Arrays.asList("role1", "role2", "role3");
        List<RoleEntity> roleEntities = roleRepository.findByName(names.get(0));
        assertEquals(1, roleEntities.size());
        assertEquals("role1", roleEntities.get(0).getName());
    }

    @Test
    public void testFindByOrganizationId() {
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(10, roleEntities.getNumberOfElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("role1", 1L);
        assertTrue(roleEntity.isPresent());
        assertEquals("role1", roleEntity.get().getName());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("role1", "role2", "role3");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, roleEntities.getNumberOfElements());
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
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roleEntities.size());
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("role1_updated");
        roleEntity.setEnabled(false);
        roleEntity.setDescription("description_updated");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
        RoleEntity updatedRoleEntity = roleRepository.findById(1L).get();
        assertEquals("role1_updated", updatedRoleEntity.getName());
        assertFalse(updatedRoleEntity.isEnabled());
        assertEquals("description_updated", updatedRoleEntity.getDescription());
    }

}