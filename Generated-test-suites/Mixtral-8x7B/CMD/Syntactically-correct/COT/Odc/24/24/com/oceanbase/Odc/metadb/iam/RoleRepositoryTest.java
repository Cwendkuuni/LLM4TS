import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTestConfiguration.class, loader = AnnotationConfigContextLoader.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;

    @Before
    public void setup() {
        roleEntity1 = new RoleEntity(1L, "role1", true, "description1");
        roleEntity2 = new RoleEntity(2L, "role2", false, "description2");
        roleEntity3 = new RoleEntity(3L, "role3", true, "description3");

        roleRepository.save(roleEntity1);
        roleRepository.save(roleEntity2);
        roleRepository.save(roleEntity3);
    }

    @Test
    public void testFindByIdIn() {
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));
        assertTrue(result.size() == 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> result = roleRepository.findByName("role1");
        assertTrue(result.size() == 1);
        assertTrue(result.contains(roleEntity1));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertTrue(result.getTotalElements() == 2);
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("role1", 1L);
        assertTrue(result.isPresent());
        assertEquals(result.get(), roleEntity1);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("role1", "role3"));
        assertTrue(result.size() == 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity3));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(1L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertTrue(result.getTotalElements() == 2);
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity3));
    }

    @Test
    public void testFindByUserId() {
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertTrue(result.size() == 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertTrue(result.size() == 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertTrue(result.size() == 1);
        assertTrue(result.contains(roleEntity1));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 3L), true);
        assertTrue(result.size() == 2);
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity3));
    }

    @Test
    public void testUpdate() {
        RoleEntity updatedRoleEntity = new RoleEntity(1L, "updatedRole1", false, "updatedDescription1");
        int result = roleRepository.update(updatedRoleEntity);
        assertEquals(result, 1);

        Optional<RoleEntity> updatedRole = roleRepository.findById(1L);
        assertTrue(updatedRole.isPresent());
        assertEquals(updatedRole.get().getName(), "updatedRole1");
        assertEquals(updatedRole.get().getIsEnabled(), false);
        assertEquals(updatedRole.get().getDescription(), "updatedDescription1");
    }

    @Test
    public void testUpdate_withNonExistentId() {
        RoleEntity updatedRoleEntity = new RoleEntity(4L, "updatedRole4", false, "updatedDescription4");
        int result = roleRepository.update(updatedRoleEntity);
        assertEquals(result, 0);
    }
}

@Configuration
@EnableJpaRepositories(basePackages = "com.oceanbase.odc.metadb.iam")
class RoleRepositoryTestConfiguration {

    @Bean
    public RoleRepository roleRepository() {
        return new RoleRepository() {
            // Add any necessary mock implementations here
        };
    }
}