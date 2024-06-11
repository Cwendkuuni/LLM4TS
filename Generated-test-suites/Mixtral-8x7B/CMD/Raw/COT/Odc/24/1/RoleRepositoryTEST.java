import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Before;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @InjectMocks
    private RoleRepository roleRepository;

    @Mock
    private JpaRepository jpaRepository;

    @Mock
    private JpaSpecificationExecutor jpaSpecificationExecutor;

    private RoleEntity roleEntity;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Test Role");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
    }

    // Test for findByIdIn(Collection<Long> ids)
    @Test
    public void testFindByIdIn() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(jpaRepository.findAllById(any())).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(Arrays.asList(1L, 2L));

        assertEquals(roleEntities, result);
        verify(jpaRepository).findAllById(any());
    }

    // Test for findByName(String name)
    @Test
    public void testFindByName() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(jpaRepository.findByName(anyString())).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName("Test Role");

        assertEquals(roleEntities, result);
        verify(jpaRepository).findByName(anyString());
    }

    // Test for findByOrganizationId(Long organizationId, Pageable pageable)
    @Test
    public void testFindByOrganizationId() {
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(roleEntity));
        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(1L, null);

        assertEquals(page, result);
        verify(jpaSpecificationExecutor).findAll(any(Specification.class), any(Pageable.class));
    }

    // Test for findByNameAndOrganizationId(String name, Long organizationId)
    @Test
    public void testFindByNameAndOrganizationId() {
        Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);
        when(jpaRepository.findByNameAndOrganizationId(anyString(), anyLong()))
                .thenReturn(optionalRoleEntity);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Test Role", 1L);

        assertEquals(optionalRoleEntity, result);
        verify(jpaRepository).findByNameAndOrganizationId(anyString(), anyLong());
    }

    // Test for findByOrganizationIdAndNameIn(Long organizationId, List<String> name)
    @Test
    public void testFindByOrganizationIdAndNameIn() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(jpaRepository.findByOrganizationIdAndNameIn(anyLong(), anyList()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("Test Role"));

        assertEquals(roleEntities, result);
        verify(jpaRepository).findByOrganizationIdAndNameIn(anyLong(), anyList());
    }

    // Test for findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds, Pageable pageable)
    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Page<RoleEntity> page = new PageImpl<>(Arrays.asList(roleEntity));
        when(jpaSpecificationExecutor.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L,
                Arrays.asList(1L, 2L), null);

        assertEquals(page, result);
        verify(jpaSpecificationExecutor).findAll(any(Specification.class), any(Pageable.class));
    }

    // Test for findByUserId(Long userId)
    @Test
    public void testFindByUserId() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(jpaRepository.query(anyString(), anyMap()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(1L);

        assertEquals(roleEntities, result);
        verify(jpaRepository).query(anyString(), anyMap());
    }

    // Test for findByUserIdAndEnabled(Long userId, Boolean enabled)
    @Test
    public void testFindByUserIdAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(jpaRepository.query(anyString(), anyMap()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);

        assertEquals(roleEntities, result);
        verify(jpaRepository).query(anyString(), anyMap());
    }

    // Test for findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled)
    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
        when(jpaRepository.query(anyString(), anyMap()))
                .thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);

        assertEquals(roleEntities, result);
        verify(jpaRepository).query(anyString(), anyMap());
    }

    // Test for update(RoleEntity roleEntity)
    @Test
    public void testUpdate() {
        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
        verify(jpaRepository).query(anyString(), anyMap());
    }
}