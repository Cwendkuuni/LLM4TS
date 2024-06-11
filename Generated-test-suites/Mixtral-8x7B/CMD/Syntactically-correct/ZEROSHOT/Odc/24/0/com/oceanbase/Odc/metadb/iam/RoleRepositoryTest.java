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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RoleRepository.class}, loader = AnnotationConfigContextLoader.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;

    @Before
    public void setUp() {
        roleEntity1 = RoleEntity.builder()
                .name("role1")
                .organizationId(1L)
                .isEnabled(true)
                .build();

        roleEntity2 = RoleEntity.builder()
                .name("role2")
                .organizationId(1L)
                .isEnabled(true)
                .build();

        roleEntity3 = RoleEntity.builder()
                .name("role3")
                .organizationId(2L)
                .isEnabled(false)
                .build();

        roleRepository.saveAll(Arrays.asList(roleEntity1, roleEntity2, roleEntity3));
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("role1");

        assertEquals(1, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);

        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().contains(roleEntity1));
        assertTrue(page.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("role1", 1L);

        assertTrue(roleEntityOptional.isPresent());
        assertEquals(roleEntity1, roleEntityOptional.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Collection<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, PageRequest.of(0, 10));

        assertEquals(2, page.getTotalElements());
        assertTrue(page.getContent().contains(roleEntity1));
        assertTrue(page.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

        assertEquals(2, roleEntities.size());
        assertTrue(roleEntities.contains(roleEntity1));
        assertTrue(roleEntities.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        roleEntity1.setName("updatedRole1");
        roleEntity1.setDescription("updated description for role1");
        roleEntity1.setEnabled(false);

        int result = roleRepository.update(roleEntity1);

        assertEquals(1, result);
        Optional<RoleEntity> updatedRoleEntityOptional = roleRepository.findById(roleEntity1.getId());
        assertTrue(updatedRoleEntityOptional.isPresent());
        RoleEntity updatedRoleEntity = updatedRoleEntityOptional.get();
        assertEquals("updatedRole1", updatedRoleEntity.getName());
        assertEquals("updated description for role1", updatedRoleEntity.getDescription());
        assertFalse(updatedRoleEntity.isEnabled());
    }
}