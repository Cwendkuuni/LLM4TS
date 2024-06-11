package com.oceanbase.odc.metadb.iam;
import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
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
@ContextConfiguration(classes = RoleRepositoryTest.Config.class, loader = AnnotationConfigContextLoader.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testFindByName() {
        String name = "test_role";
        List<RoleEntity> roleEntities = roleRepository.findByName(name);
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationId(organizationId, pageable);
        assertTrue(roleEntities.getTotalElements() > 0);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test_role";
        Long organizationId = 1L;
        Optional<RoleEntity> roleEntity = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertTrue(roleEntity.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test_role_1", "test_role_2");
        List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertTrue(roleEntities.getTotalElements() > 0);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertTrue(roleEntities.size() > 0);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("test_role_updated");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test_role_updated_description");
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }

    static class Config {
        // empty
    }
}