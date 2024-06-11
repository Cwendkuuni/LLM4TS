
package com.oceanbase.odc.metadb.iam;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepositoryTestConfiguration.class)
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
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(1, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> roleEntityOpt = roleRepository.findByNameAndOrganizationId("Role1", 1L);
        assertTrue(roleEntityOpt.isPresent());
        RoleEntity roleEntity = roleEntityOpt.get();
        assertEquals("Role1", roleEntity.getName());
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
        List<Long> roleIds = Arrays.asList(1L, 2L);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, roleEntities.getTotalElements());
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
        List<Long> roleIds = Arrays.asList(1L, 2L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(1, roleEntities.size());
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("UpdatedRole");
        roleEntity.setEnabled(true);
        int rowsAffected = roleRepository.update(roleEntity);
        assertEquals(1, rowsAffected);

        Optional<RoleEntity> updatedRoleOpt = roleRepository.findById(1L);
        assertTrue(updatedRoleOpt.isPresent());
        RoleEntity updatedRole = updatedRoleOpt.get();
        assertEquals("UpdatedRole", updatedRole.getName());
        assertEquals(true, updatedRole.getEnabled());
    }

    @Test
    public void testFindById_negative() {
        Optional<RoleEntity> roleEntityOpt = roleRepository.findById(-1L);
        assertFalse(roleEntityOpt.isPresent());
    }

    @Test
    public void testFindByName_notFound() {
        List<RoleEntity> roleEntities = roleRepository.findByName("NonExistentRole");
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationId_negative() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(-1L, pageable);
        assertEquals(0, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByNameAndOrganizationId_notFound() {
        Optional<RoleEntity> roleEntityOpt = roleRepository.findByNameAndOrganizationId("NonExistentRole", 1L);
        assertFalse(roleEntityOpt.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn_notFound() {
        List<String> names = Arrays.asList("NonExistentRole1", "NonExistentRole2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_noneFound() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Long> roleIds = Arrays.asList(-1L, -2L);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(0, roleEntities.getTotalElements());
    }

    @Test
    public void testFindByUserId_negative() {
        List<RoleEntity> roleEntities = roleRepository.findByUserId(-1L);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByUserIdAndEnabled_negative() {
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(-1L, true);
        assertEquals(0, roleEntities.size());
    }

    @Test
    public void testFindByRoleIdsAndEnabled_noneFound() {
        List<Long> roleIds = Arrays.asList(-1L, -2L);
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(0, roleEntities.size());
    }

    @Test
    @Transactional
    public void testUpdate_notFound() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(-1L);
        roleEntity.setName("UpdatedRole");
        roleEntity.setEnabled(true);
        int rowsAffected = roleRepository.update(roleEntity);
        assertEquals(0, rowsAffected);
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = (List<RoleEntity>) roleRepository.findAll();
        assertNotNull(roleEntities);
    }

    @Test
    public void testCount() {
        long count = roleRepository.count();
        assertTrue(count > 0);
    }

    @Test
    public void testDeleteAll() {
        roleRepository.deleteAll();
        long count = roleRepository.count();
        assertEquals(0, count);
    }

    @Test
    public void testDeleteById() {
        roleRepository.deleteById(1L);
        Optional<RoleEntity> roleEntityOpt = roleRepository.findById(1L);
        assertFalse(roleEntityOpt.isPresent());
    }

    @Test
    public void testExistsById() {
        assertTrue(roleRepository.existsById(1L));
        assertFalse(roleRepository.existsById(-1L));
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName("NewRole");
        roleEntity.setEnabled(true);
        RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
        assertNotNull(savedRoleEntity.getId());

        Optional<RoleEntity> savedRoleOpt = roleRepository.findById(savedRoleEntity.getId());
        assertTrue(savedRoleOpt.isPresent());
        RoleEntity savedRole = savedRoleOpt.get();
        assertEquals("NewRole", savedRole.getName());
    }
}
