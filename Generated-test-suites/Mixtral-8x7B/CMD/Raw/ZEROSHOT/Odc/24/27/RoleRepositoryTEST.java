import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    private RoleEntity roleEntity;

    private Pageable pageable;

    @Before
    public void setUp() {
        roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("test_role");
        roleEntity.setOrganizationId(1L);
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test_description");

        pageable = mock(Pageable.class);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Collections.singletonList(1L);
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        doReturn(roleEntities).when(jpaRepository).findByIdIn(ids);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(roleEntities, result);
        verify(jpaRepository, times(1)).findByIdIn(ids);
    }

    @Test
    public void testFindByName() {
        String name = "test_role";
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        doReturn(roleEntities).when(jpaRepository).findByName(name);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(roleEntities, result);
        verify(jpaRepository, times(1)).findByName(name);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity), pageable, 1);
        doReturn(page).when(jpaSpecificationExecutor).findAll(any(com.oceanbase.odc.metadb.iam.specification.RoleSpecification.class), any(Pageable.class));

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(page, result);
        verify(jpaSpecificationExecutor, times(1)).findAll(any(com.oceanbase.odc.metadb.iam.specification.RoleSpecification.class), any(Pageable.class));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test_role";
        Long organizationId = 1L;
        Optional<RoleEntity> optional = Optional.of(roleEntity);
        doReturn(optional).when(jpaRepository).findByNameAndOrganizationId(name, organizationId);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertEquals(optional, result);
        verify(jpaRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> nameList = Collections.singletonList("test_role");
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        doReturn(roleEntities).when(jpaRepository).findByOrganizationIdAndNameIn(organizationId, nameList);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, nameList);

        assertEquals(roleEntities, result);
        verify(jpaRepository, times(1)).findByOrganizationIdAndNameIn(organizationId, nameList);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        List<Long> roleIds = Collections.singletonList(1L);
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(roleEntity), pageable, 1);
        doReturn(page).when(jpaSpecificationExecutor).findAll(any(com.oceanbase.odc.metadb.iam.specification.RoleSpecification.class), any(Pageable.class));

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds,
                pageable);

        assertEquals(page, result);
        verify(jpaSpecificationExecutor, times(1)).findAll(any(com.oceanbase.odc.metadb.iam.specification.RoleSpecification.class),
                any(Pageable.class));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        doReturn(roleEntities).when(jpaRepository).findByUserId(userId);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(roleEntities, result);
        verify(jpaRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        doReturn(roleEntities).when(jpaRepository).findByUserIdAndEnabled(userId, enabled);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(roleEntities, result);
        verify(jpaRepository, times(1)).findByUserIdAndEnabled(userId, enabled);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        List<Long> roleIds = Collections.singletonList(1L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        doReturn(roleEntities).when(jpaRepository).findByRoleIdsAndEnabled(roleIds, enabled);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(roleEntities, result);
        verify(jpaRepository, times(1)).findByRoleIdsAndEnabled(roleIds, enabled);
    }

    @Test
    public void testUpdate() {
        doNothing().when(jpaRepository).update(any(RoleEntity.class));

        roleRepository.update(roleEntity);

        verify(jpaRepository, times(1)).update(any(RoleEntity.class));
    }
}