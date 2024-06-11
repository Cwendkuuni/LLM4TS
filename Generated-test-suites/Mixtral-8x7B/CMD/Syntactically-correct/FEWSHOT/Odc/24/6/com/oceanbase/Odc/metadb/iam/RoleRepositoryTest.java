import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(2, result.size());
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
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = null;
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));

        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> optional = Optional.of(new RoleEntity());

        when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Arrays.asList("test1", "test2");
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, names))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, result.size());
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = null;
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));

        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                pageable);

        assertEquals(2, result.getContent().size());
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
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();

        int result = roleRepository.update(roleEntity);

        verify(jpaRepository).updateByNameAndIsEnabledAndDescriptionAndUserUpdateTime(
                roleEntity.getName(), roleEntity.getEnabled(), roleEntity.getDescription(),
                roleEntity.getUserUpdateTime(), roleEntity.getId());
    }

    @Test
    public void testUpdateWithNullValues() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(null);
        roleEntity.setEnabled(null);
        roleEntity.setDescription(null);
        roleEntity.setUserUpdateTime(null);

        assertThrows(NullPointerException.class, () -> roleRepository.update(roleEntity));
    }

    @Test
    public void testFindAll() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findAll()).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findAll();

        assertEquals(2, result.size());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Optional<RoleEntity> optional = Optional.of(new RoleEntity());

        when(jpaRepository.findById(id)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    public void testSave() {
        RoleEntity roleEntity = new RoleEntity();

        RoleEntity result = roleRepository.save(roleEntity);

        assertNotNull(result.getId());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        roleRepository.deleteById(id);

        verify(jpaRepository).deleteById(id);
    }

    @Test
    public void testDeleteAll() {
        roleRepository.deleteAll();

        verify(jpaRepository).deleteAll();
    }

    @Test
    public void testCount() {
        Long count = 10L;

        when(jpaRepository.count()).thenReturn(count);

        Long result = roleRepository.count();

        assertEquals(count, result);
    }

    @Test
    public void testExistsById() {
        Long id = 1L;
        boolean exists = true;

        when(jpaRepository.existsById(id)).thenReturn(exists);

        boolean result = roleRepository.existsById(id);

        assertEquals(exists, result);
    }

    @Test
    public void testFindAllByIsEnabledTrue() {
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());

        when(jpaRepository.findAllByIsEnabledTrue()).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findAllByIsEnabledTrue();

        assertEquals(2, result.size());
    }
}