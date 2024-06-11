
package com.oceanbase.odc.metadb.iam;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RoleRepositoryTestConfig.class})
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertEquals(3, roleEntities.size());
        // add more assertions to check if the returned RoleEntity objects match the expected ones
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntityOptional = roleRepository.findByNameAndOrganizationId("Admin", 1L);
        assertTrue(roleEntityOptional.isPresent());
        RoleEntity roleEntity = roleEntityOptional.get();
        assertEquals("Admin", roleEntity.getName());
        // add more assertions to check if the returned RoleEntity object matches the expected one
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("UpdatedAdmin");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Updated Admin Role");

        int rowsAffected = roleRepository.update(roleEntity);
        assertEquals(1, rowsAffected);

        Optional<RoleEntity> roleEntityOptional = roleRepository.findById(1L);
        assertTrue(roleEntityOptional.isPresent());
        RoleEntity updatedRoleEntity = roleEntityOptional.get();
        assertEquals("UpdatedAdmin", updatedRoleEntity.getName());
        assertEquals(true, updatedRoleEntity.getEnabled());
        assertEquals("Updated Admin Role", updatedRoleEntity.getDescription());
    }

    @Test(expected = DataIntegrityViolationException.class)
    @Transactional
    public void testUpdate_with_empty_name() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Updated Admin Role");

        roleRepository.update(roleEntity);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(10, roleEntities.getTotalElements());
        // add more assertions to check if the returned RoleEntity objects match the expected ones
    }
}
