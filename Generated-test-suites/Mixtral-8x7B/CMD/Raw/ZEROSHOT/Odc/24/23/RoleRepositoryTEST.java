import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;

    @Mock
    private JpaSpecificationExecutor<RoleEntity> jpaSpecificationExecutor;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByIdIn(ids);
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findByName(name)).thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByName(name);
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = null; // replace with a real Pageable instance for a more comprehensive test
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));
        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> optionalRoleEntity = Optional.of(new RoleEntity());
        when(jpaRepository.findByNameAndOrganizationId(name, organizationId))
                .thenReturn(optionalRoleEntity);
        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
        assertEquals(optionalRoleEntity, result);
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
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Pageable pageable = null; // replace with a real Pageable instance for a more comprehensive test
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));
        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);
        when(jpaRepository.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(Arrays.asList(new RoleEntity(), new RoleEntity()));
        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
        assertEquals(page, result);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByUserId(userId);
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Arrays.asList(1L, 2L, 3L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Arrays.asList(new RoleEntity(), new RoleEntity());
        when(jpaRepository.query(anyString(), any(RowMapper.class), any(Object[].class)))
                .thenReturn(roleEntities);
        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
        assertEquals(roleEntities, result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        when(jpaRepository.update(roleEntity)).thenReturn(1);
        int result = roleRepository.update(roleEntity);
        assertEquals(1, result);
    }
}