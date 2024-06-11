
package com.oceanbase.odc.metadb.iam;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
@ContextConfiguration("classpath:test-application-context.xml")
@Transactional
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(3, result.size());
    }

    @Test
    public void testFindByIdInEmptyCollection() {
        Collection<Long> ids = new ArrayList<>();
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(0, result.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testFindByIdInNonExistentIds() {
        Collection<Long> ids = Arrays.asList(100L, 200L, 300L);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        String name = "admin";
        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(1, result.size());
    }

    @Test
    public void testFindByNameEmptyString() {
        String name = "";
        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(0, result.size());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testFindByNameNonExistentName() {
        String name = "nonexistent";
        List<RoleEntity> result = roleRepository.findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(10, result.getTotalElements());
    }

    @Test
    public void testFindByOrganizationIdPageZero() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 0);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(0, result.getTotalElements());
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testFindByOrganizationIdNonExistentOrganizationId() {
        Long organizationId = 100L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
    }
}
