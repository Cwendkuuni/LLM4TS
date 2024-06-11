
package com.oceanbase.odc.metadb.iam;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class RoleRepositoryTest {

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

    @InjectMocks
    private RoleRepository roleRepository;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void testFindByIdIn() {
        // Arrange
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        List<RoleEntity> roleEntities = Arrays.asList(role1, role2);
        when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roleEntities);

        // Act
        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        // Assert
        assertEquals(result, roleEntities);
        verify(jpaRepository).findAll(Arrays.asList(1L, 2L));
    }

    @Test
    public void testFindByName() {
        // Arrange
        RoleEntity role = new RoleEntity();
        role.setName("roleName");
        when(roleRepository.findByName("roleName")).thenReturn(Arrays.asList(role));

        // Act
        List<RoleEntity> result = roleRepository.findByName("roleName");

        // Assert
        assertEquals(result.get(0).getName(), "roleName");
        verify(jpaRepository).findAll(Arrays.asList("roleName"));
    }

    @Test
    public void testFindByOrganizationId() {
        // Arrange
        RoleEntity role1 = new RoleEntity();
        role1.setId(1L);
        role1.setOrganizationId(1L);
        RoleEntity role2 = new RoleEntity();
        role2.setId(2L);
        role2.setOrganizationId(1L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(role1, role2), pageable, 2);
        when(roleRepository.findByOrganizationId(1L, pageable)).thenReturn(page);

        // Act
        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, pageable);

        // Assert
        assertEquals(result.getTotalElements(), 2);
        verify(jpaRepository).findAll(
                org.springframework.data.jpa.domain.Specification.where(
                        (root, query, cb) -> cb.equal(root.get("organizationId"), 1L)));
    }
}
