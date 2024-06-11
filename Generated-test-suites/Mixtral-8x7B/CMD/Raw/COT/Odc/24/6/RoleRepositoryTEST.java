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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        roleEntity1 = RoleEntity.builder()
                .id(1L)
                .name("Role1")
                .organizationId(1L)
                .enabled(true)
                .build();

        roleEntity2 = RoleEntity.builder()
                .id(2L)
                .name("Role2")
                .organizationId(1L)
                .enabled(true)
                .build();

        roleEntity3 = RoleEntity.builder()
                .id(3L)
                .name("Role3")
                .organizationId(2L)
                .enabled(false)
                .build();

        roleRepository.save(roleEntity1);
        roleRepository.save(roleEntity2);
        roleRepository.save(roleEntity3);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertThat(result.size(), equalTo(2));
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByIdIn_EmptyList() {
        List<RoleEntity> result = roleRepository.findByIdIn(Collections.emptyList());

        assertEquals(result.size(), 0);
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> result = roleRepository.findByName("Role1");

        assertEquals(result.size(), 1);
        assertEquals(result.get(0), roleEntity1);
    }

    @Test
    public void testFindByName_NotFound() {
        List<RoleEntity> result = roleRepository.findByName("NonExistentRole");

        assertEquals(result.size(), 0);
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);

        assertThat(result.getTotalElements(), equalTo(2L));
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationId_NoRolesFound() {
        Page<RoleEntity> result = roleRepository.findByOrganizationId(3L, Pageable.unpaged());

        assertEquals(result.getTotalElements(), 0L);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Role1", 1L);

        assertTrue(result.isPresent());
        assertEquals(result.get(), roleEntity1);
    }

    @Test
    public void testFindByNameAndOrganizationId_NotFound() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Role1", 3L);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> nameList = Arrays.asList("Role1", "Role2");
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, nameList);

        assertEquals(result.size(), 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndNameIn_NoRolesFound() {
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(3L, Arrays.asList("Role1", "Role2"));

        assertEquals(result.size(), 0);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);

        assertThat(result.getTotalElements(), equalTo(2L));
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_NoRolesFound() {
        List<Long> roleIds = Arrays.asList(1L, 3L);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(3L, roleIds, Pageable.unpaged());

        assertEquals(result.getTotalElements(), 0L);
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> result = roleRepository.findByUserId(1L);

        assertEquals(result.size(), 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserId_NoRolesFound() {
        List<RoleEntity> result = roleRepository.findByUserId(3L);

        assertEquals(result.size(), 0);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);

        assertEquals(result.size(), 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled_NoRolesFound() {
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(3L, true);

        assertEquals(result.size(), 0);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

        assertEquals(result.size(), 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled_NoRolesFound() {
        List<Long> roleIds = Arrays.asList(1L, 3L);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);

        assertEquals(result.size(), 1);
        assertTrue(result.contains(roleEntity1));
    }

    @Test
    public void testUpdate() {
        String newName = "Updated Role";
        boolean newEnabled = false;
        RoleEntity roleToUpdate = RoleEntity.builder()
                .id(1L)
                .name(newName)
                .organizationId(1L)
                .enabled(newEnabled)
                .build();

        int rowsAffected = roleRepository.update(roleToUpdate);

        assertEquals(rowsAffected, 1);

        Optional<RoleEntity> updatedRole = roleRepository.findById(1L);

        assertTrue(updatedRole.isPresent());
        assertEquals(updatedRole.get().getName(), newName);
        assertEquals(updatedRole.get().isEnabled(), newEnabled);
    }

    @Test
    public void testUpdate_NoRowsAffected() {
        RoleEntity roleToUpdate = RoleEntity.builder()
                .id(4L)
                .name("NonExistent Role")
                .organizationId(1L)
                .enabled(true)
                .build();

        int rowsAffected = roleRepository.update(roleToUpdate);

        assertEquals(rowsAffected, 0);
    }
}

@Configuration
@EnableJpaRepositories(basePackages = "com.oceanbase.odc.metadb.iam")
class RoleRepositoryTestConfig {

    @Bean
    public RoleRepository roleRepository() {
        return new RoleRepository() {
            // Add mock implementation for the interface methods if needed
        };
    }
}