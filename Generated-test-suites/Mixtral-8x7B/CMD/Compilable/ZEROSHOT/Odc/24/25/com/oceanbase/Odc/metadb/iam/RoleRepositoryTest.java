package com.oceanbase.odc.metadb.iam;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RoleRepository.class)
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;

    @Before
    public void setUp() {
        roleEntity1 = new RoleEntity();
        roleEntity1.setName("role1");
        roleEntity1.setOrganizationId(1L);
        roleEntity1.setEnabled(true);

        roleEntity2 = new RoleEntity();
        roleEntity2.setName("role2");
        roleEntity2.setOrganizationId(1L);
        roleEntity2.setEnabled(true);

        roleEntity3 = new RoleEntity();
        roleEntity3.setName("role3");
        roleEntity3.setOrganizationId(2L);
        roleEntity3.setEnabled(false);

        roleRepository.save(roleEntity1);
        roleRepository.save(roleEntity2);
        roleRepository.save(roleEntity3);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        List<RoleEntity> result = roleRepository.findByName("role1");
        assertEquals(1, result.size());
        assertEquals(roleEntity1, result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("role1", 1L);
        assertTrue(result.isPresent());
        assertEquals(roleEntity1, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<String> names = Arrays.asList("role1", "role2");
        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, names);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        List<RoleEntity> result = roleRepository.findByUserId(1L);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(roleEntity1.getId(), roleEntity2.getId());
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, true);
        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        roleEntity1.setName("updatedRole1");
        roleEntity1.setEnabled(false);
        int result = roleRepository.update(roleEntity1);
        assertEquals(1, result);

        Optional<RoleEntity> updatedRole = roleRepository.findById(roleEntity1.getId());
        assertTrue(updatedRole.isPresent());
        assertEquals("updatedRole1", updatedRole.get().getName());
        assertFalse(updatedRole.get().getEnabled());
    }
}