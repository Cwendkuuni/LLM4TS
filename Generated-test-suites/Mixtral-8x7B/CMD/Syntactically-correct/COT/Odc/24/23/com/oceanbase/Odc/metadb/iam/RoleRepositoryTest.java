/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oceanbase.odc.metadb.iam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepositoryMock;

    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutorMock;

    @InjectMocks
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1, roleEntity2, roleEntity3;

    @Before
    public void setUp() {
        roleEntity1 = new RoleEntity(1L, "Role1", true, "Description1");
        roleEntity2 = new RoleEntity(2L, "Role2", true, "Description2");
        roleEntity3 = new RoleEntity(3L, "Role3", false, "Description3");
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L);
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity1, roleEntity2);

        // Mock the jpaRepositoryMock to return the expected result
        java.util.List<RoleEntity> jpaRepositoryReturnValue = new java.util.ArrayList<>();
        jpaRepositoryReturnValue.addAll(expectedResult);
        java.util.List<RoleEntity> jpaRepositoryFindByIdInMock = java.util.Collections.unmodifiableList(jpaRepositoryReturnValue);

        java.util.List<RoleEntity> findByIdInMock = jpaRepositoryMock.findAll(ids);
        Assert.assertEquals(findByIdInMock, jpaRepositoryFindByIdInMock);
    }

    @Test
    public void testFindByName() {
        String name = "Role1";
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity1);

        // Mock the jpaRepositoryMock to return the expected result
        java.util.List<RoleEntity> jpaRepositoryReturnValue = new java.util.ArrayList<>();
        jpaRepositoryReturnValue.addAll(expectedResult);
        java.util.List<RoleEntity> jpaRepositoryFindByNameMock = java.util.Collections.unmodifiableList(jpaRepositoryReturnValue);

        java.util.List<RoleEntity> findByNameMock = roleRepository.findByName(name);
        Assert.assertEquals(findByNameMock, jpaRepositoryFindByNameMock);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> expectedResult = new PageImpl<>(Arrays.asList(roleEntity1));

        // Mock the jpaSpecificationExecutorMock to return the expected result
        Specification<RoleEntity> specification = (root, query, cb) -> cb.equal(root.get("organizationId"), organizationId);
        Page<RoleEntity> jpaSpecificationExecutorMock = new PageImpl<>(Arrays.asList(roleEntity1),
                pageable, 1);

        Page<RoleEntity> findByOrganizationIdMock = roleRepository.findByOrganizationId(organizationId, pageable);
        Assert.assertEquals(findByOrganizationIdMock, jpaSpecificationExecutorMock);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Role1";
        Long organizationId = 1L;
        Optional<RoleEntity> expectedResult = Optional.of(roleEntity1);

        // Mock the jpaRepositoryMock to return the expected result
        RoleEntity jpaRepositoryMock = roleEntity1;

        Optional<RoleEntity> findByNameAndOrganizationIdOptional = jpaRepositoryMock.findByNameAndOrganizationId(name, organizationId);
        Assert.assertEquals(expectedResult, findByNameAndOrganizationIdOptional);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> name = Arrays.asList("Role1");
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity1);

        // Mock the jpaRepositoryMock to return the expected result
        java.util.List<RoleEntity> jpaRepositoryReturnValue = new java.util.ArrayList<>();
        jpaRepositoryReturnValue.addAll(expectedResult);
        java.util.List<RoleEntity> jpaRepositoryFindByOrganizationIdAndNameInMock = java.util.Collections.unmodifiableList(jpaRepositoryReturnValue);

        java.util.List<RoleEntity> findByOrganizationIdAndNameInMock = roleRepository.findByOrganizationIdAndNameIn(organizationId, name);
        Assert.assertEquals(findByOrganizationIdAndNameInMock, jpaRepositoryFindByOrganizationIdAndNameInMock);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> expectedResult = new PageImpl<>(Arrays.asList(roleEntity1));

        // Mock the jpaSpecificationExecutorMock to return the expected result
        Specification<RoleEntity> specification = (root, query, cb) -> cb.and(
                cb.equal(root.get("organizationId"), organizationId),
                cb.in(root.get("id")).value(roleIds));

        Page<RoleEntity> jpaSpecificationExecutorMock = new PageImpl<>(Arrays.asList(roleEntity1),
                pageable, 1);

        Page<RoleEntity> findByOrganizationIdAndRoleIdsMock = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        Assert.assertEquals(findByOrganizationIdAndRoleIdsMock, jpaSpecificationExecutorMock);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity1);

        // Mock the jpaRepositoryMock to return the expected result
        java.util.List<RoleEntity> jpaRepositoryReturnValue = new java.util.ArrayList<>();
        jpaRepositoryReturnValue.addAll(expectedResult);
        java.util.List<RoleEntity> jpaRepositoryFindByUserIdMock = java.util.Collections.unmodifiableList(jpaRepositoryReturnValue);

        java.util.List<RoleEntity> findByUserIdMock = roleRepository.findByUserId(userId);
        Assert.assertEquals(findByUserIdMock, jpaRepositoryFindByUserIdMock);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity1);

        // Mock the jpaRepositoryMock to return the expected result
        java.util.List<RoleEntity> jpaRepositoryReturnValue = new java.util.ArrayList<>();
        jpaRepositoryReturnValue.addAll(expectedResult);
        java.util.List<RoleEntity> jpaRepositoryFindByUserIdAndEnabledMock = java.util.Collections.unmodifiableList(jpaRepositoryReturnValue);

        java.util.List<RoleEntity> findByUserIdAndEnabledMock = roleRepository.findByUserIdAndEnabled(userId, enabled);
        Assert.assertEquals(findByUserIdAndEnabledMock, jpaRepositoryFindByUserIdAndEnabledMock);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L);
        Boolean enabled = true;
        List<RoleEntity> expectedResult = Arrays.asList(roleEntity1, roleEntity2);

        // Mock the jpaRepositoryMock to return the expected result
        java.util.List<RoleEntity> jpaRepositoryReturnValue = new java.util.ArrayList<>();
        jpaRepositoryReturnValue.addAll(expectedResult);
        java.util.List<RoleEntity> jpaRepositoryFindByRoleIdsAndEnabledMock = java.util.Collections.unmodifiableList(jpaRepositoryReturnValue);

        java.util.List<RoleEntity> findByRoleIdsAndEnabledMock = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        Assert.assertEquals(findByRoleIdsAndEnabledMock, jpaRepositoryFindByRoleIdsAndEnabledMock);
    }

    @Test
    @Transactional
    public void testUpdate() {
        RoleEntity roleEntity = roleEntity1;
        int expectedResult = 1;

        int actualResult = roleRepository.update(roleEntity);

        Assert.assertEquals(expectedResult, actualResult);
    }

    // Additional test methods for exceptional cases and edge cases can be added here
}