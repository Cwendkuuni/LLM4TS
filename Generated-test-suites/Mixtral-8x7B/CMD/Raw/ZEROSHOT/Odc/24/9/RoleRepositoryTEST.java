import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private JpaRepository jpaRepository;

    @Mock
    private JpaSpecificationExecutor jpaSpecificationExecutor;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(2, result.size());
        verify(jpaRepository).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(2, result.size());
        verify(jpaRepository).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(0, result.getTotalElements());
        verify(jpaSpecificationExecutor).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> optional = Optional.of(new RoleEntity());

        when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        verify(jpaRepository).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test1", "test2");
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, result.size());
        verify(jpaRepository).findByOrganizationIdAndNameIn(organizationId, names);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = PageRequest.of(0, 10);
        Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(jpaSpecificationExecutor
                .findAll(any(Specification.class), any(Pageable.class), anyString(), anyMap()))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                pageable);

        assertEquals(0, result.getTotalElements());
        verify(jpaSpecificationExecutor)
                .findAll(any(Specification.class), any(Pageable.class), anyString(), anyMap());
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(2, result.size());
        verify(jpaRepository).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, result.size());
        verify(jpaRepository).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, result.size());
        verify(jpaRepository).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
        verify(jpaRepository).update(roleEntity);
    }
}