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
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
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
        List<RoleEntity> roleEntities = roleRepository.findByName("Role1");
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(10, page.getContent().size());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId("Role1", 1L);
        assertTrue(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("Role1", "Role2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(3, page.getContent().size());
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
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("UpdatedRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("UpdatedRoleDescription");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("TestRole");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("TestRoleDescription");
        roleEntity = roleRepository.save(roleEntity);
        assertNotNull(roleEntity.getId());
        roleRepository.deleteById(roleEntity.getId());
    }
}