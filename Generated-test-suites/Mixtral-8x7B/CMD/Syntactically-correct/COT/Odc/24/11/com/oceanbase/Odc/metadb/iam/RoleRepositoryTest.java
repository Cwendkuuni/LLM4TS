import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
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
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;

    @Before
    public void setup() {
        roleEntity1 = new RoleEntity(1L, "role1", true, "description1");
        roleEntity2 = new RoleEntity(2L, "role2", true, "description2");
        roleEntity3 = new RoleEntity(3L, "role3", false, "description3");

        roleRepository.saveAll(Arrays.asList(roleEntity1, roleEntity2, roleEntity3));
    }

    // Test for findByIdIn(Collection<Long> ids)
    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertEquals(2, roleEntities.size());
    }

    // Test for findByName(String name)
    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("role1");
        assertEquals(1, roleEntities.size());
    }

    // Test for findByOrganizationId(Long organizationId, Pageable pageable)
    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(1, roleEntities.getTotalElements());
    }

    // Test for findByNameAndOrganizationId(String name, Long organizationId)
    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByNameAndOrganizationId("role1", 1L);
        assertTrue(optionalRoleEntity.isPresent());
        assertEquals(roleEntity1, optionalRoleEntity.get());
    }

    // Test for findByOrganizationIdAndNameIn(Long organizationId, List<String> name)
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, roleEntities.size());
    }

    // Test for findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds, Pageable pageable)
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, roleEntities.getTotalElements());
    }

    // Test for findByUserId(Long userId)
    @Test
    public void testFindByUserId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);

        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
        assertEquals(1, roleEntities.size());
        assertEquals(roleEntity1.getId(), roleEntities.get(0).getId());
    }

    // Test for findByUserIdAndEnabled(Long userId, Boolean enabled)
    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, roleEntities.size());
        assertEquals(roleEntity1.getId(), roleEntities.get(0).getId());
    }

    // Test for findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled)
    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, roleEntities.size());
    }

    // Test for update(RoleEntity roleEntity)
    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity(1L, "updatedRole", true, "updatedDescription");
        int rowsAffected = roleRepository.update(updatedRoleEntity);
        assertEquals(1, rowsAffected);

        RoleEntity updatedEntity = roleRepository.findById(1L).orElse(null);
        assertNotNull(updatedEntity);
        assertEquals("updatedRole", updatedEntity.getName());
        assertEquals("updatedDescription", updatedEntity.getDescription());
    }
}

class RoleRepositoryTestConfig {

}