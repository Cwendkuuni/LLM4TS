import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Collections;
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

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

    @Mock
    private JpaRepository<RoleEntity, Long> jpaRepository;

    @InjectMocks
    private RoleRepository roleRepository;

    @Test
    public void testFindByIdIn() {
        List<Long> ids = Collections.singletonList(1L);
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity());
        when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity());
        when(jpaRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = null; // replace with a valid Pageable instance for your test
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(new RoleEntity()));
        when(jpaRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> optional = Optional.of(new RoleEntity());
        when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertEquals(optional, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> name = Collections.singletonList("test");
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity());
        when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, name);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Collections.singletonList(1L);
        Pageable pageable = null; // replace with a valid Pageable instance for your test
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(new RoleEntity()));
        when(jpaRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(page, result);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity());
        when(jpaRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity());
        when(jpaRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Collections.singletonList(1L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = Collections.singletonList(new RoleEntity());
        when(jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(roleEntities, result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int expectedResult = 1;
        when(jpaRepository.update(roleEntity)).thenReturn(expectedResult);

        int result = roleRepository.update(roleEntity);

        assertEquals(expectedResult, result);
    }
}