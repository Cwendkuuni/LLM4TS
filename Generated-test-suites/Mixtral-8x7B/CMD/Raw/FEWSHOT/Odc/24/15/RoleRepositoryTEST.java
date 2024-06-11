import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testJpaRepository() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("test_role");
        roleEntity.setDescription("test_description");
        roleEntity.setIsEnabled(true);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity.getId());

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(savedRoleEntity.getId());
        assertTrue(optionalRoleEntity.isPresent());
        RoleEntity foundRoleEntity = optionalRoleEntity.get();
        assertEquals(savedRoleEntity, foundRoleEntity);

        roleRepository.deleteById(savedRoleEntity.getId());
        optionalRoleEntity = roleRepository.findById(savedRoleEntity.getId());
        assertTrue(optionalRoleEntity.isEmpty());
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(2, roleEntities.size());
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = roleRepository.findByName("test_role");
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageRequest);
        assertEquals(10, page.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = roleRepository.findByNameAndOrganizationId("test_role", 1L);
        assertTrue(optionalRoleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = new ArrayList<>();
        names.add("test_role");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageRequest);
        assertEquals(1, page.getTotalElements());
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(1L);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(1, roleEntities.size());
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("updated_test_role");
        roleEntity.setDescription("updated_test_description");
        roleEntity.setIsEnabled(false);

        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(1, updatedRows);

        Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(roleEntity.getId());
        assertTrue(optionalRoleEntity.isPresent());
        RoleEntity foundRoleEntity = optionalRoleEntity.get();
        assertEquals(roleEntity.getName(), foundRoleEntity.getName());
        assertEquals(roleEntity.getDescription(), foundRoleEntity.getDescription());
        assertEquals(roleEntity.getIsEnabled(), foundRoleEntity.getIsEnabled());
    }

    @Test
    public void testUpdateWithNullValues() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName(null);
        roleEntity.setDescription(null);
        roleEntity.setIsEnabled(null);

        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testUpdateWithNoChanges() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("test_role");
        roleEntity.setDescription("test_description");
        roleEntity.setIsEnabled(true);

        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(0, updatedRows);
    }

    @Test
    public void testFindByOrganizationIdWithEmptyResult() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<RoleEntity> page = roleRepository.findByOrganizationId(2L, pageRequest);
        assertEquals(0, page.getTotalElements());
    }

    @Test
    public void testFindByRoleIdsAndEnabledWithEmptyResult() {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByNameWithEmptyResult() {
        List<RoleEntity> roleEntities = roleRepository.findByName("nonexistent_role");
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByIdInWithEmptyResult() {
        List<Long> ids = new ArrayList<>();
        ids.add(2L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndNameInWithEmptyResult() {
        List<String> names = new ArrayList<>();
        names.add("nonexistent_role");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByUserIdWithEmptyResult() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(2L);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabledWithEmptyResult() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(2L, true);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testUpdateWithNonExistentId() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(2L);
        roleEntity.setName("updated_nonexistent_role");
        roleEntity.setDescription("updated_nonexistent_description");
        roleEntity.setIsEnabled(false);

        int updatedRows = roleRepository.update(roleEntity);
        assertEquals(0, updatedRows);
    }
}