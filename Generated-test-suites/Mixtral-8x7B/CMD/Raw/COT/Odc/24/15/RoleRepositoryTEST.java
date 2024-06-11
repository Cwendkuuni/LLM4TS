import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
        Collection<Long> ids = Collections.singletonList(1L);
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByName() {
        String name = "test";
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName(name);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = any(Pageable.class);
        RoleEntity roleEntity = new RoleEntity();
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, 1);
        when(jpaSpecificationExecutor.findAll(any(), any())).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        Assert.assertEquals(page, result);
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;
        Optional<RoleEntity> optional = Optional.of(new RoleEntity());
        when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        Assert.assertEquals(optional, result);
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = Collections.singletonList("test");
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Collections.singletonList(1L);
        Page<RoleEntity> page = new PageImpl<>(Collections.singletonList(new RoleEntity()), any(), 1);
        when(jpaSpecificationExecutor.findAll(any(), any())).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, any());

        Assert.assertEquals(page, result);
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.query(any(), any())).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.query(any(), any())).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = Collections.singletonList(1L);
        Boolean enabled = true;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findAllById(roleIds)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        int rowsAffected = 1;
        when(jpaRepository.update(roleEntity)).thenReturn(rowsAffected);

        int result = roleRepository.update(roleEntity);

        Assert.assertEquals(rowsAffected, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_EdgeCase() {
        Long organizationId = null;
        Collection<Long> roleIds = Collections.singletonList(1L);
        Pageable pageable = any(Pageable.class);
        RoleEntity roleEntity = new RoleEntity();
        List<RoleEntity> roleEntities = Collections.singletonList(roleEntity);
        Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, 1);
        when(jpaSpecificationExecutor.findAll(any(), any())).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        Assert.assertEquals(page, result);
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds_Exception() {
        Long organizationId = 1L;
        Collection<Long> roleIds = Collections.singletonList(null);

        Assert.assertThrows(NullPointerException.class, () -> roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, any()));
    }

    @Test
    public void testFindByRoleIdsAndEnabled_EdgeCase() {
        Collection<Long> roleIds = Collections.singletonList(1L);
        Boolean enabled = null;
        List<RoleEntity> roleEntities = new ArrayList<>();
        roleEntities.add(new RoleEntity());
        when(jpaRepository.findAllById(roleIds)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        Assert.assertEquals(roleEntities, result);
    }

    @Test
    public void testFindByRoleIdsAndEnabled_Exception() {
        Collection<Long> roleIds = Collections.singletonList(null);
        Boolean enabled = true;

        Assert.assertThrows(NullPointerException.class, () -> roleRepository.findByRoleIdsAndEnabled(roleIds, enabled));
    }

    @Test
    public void testUpdate_EdgeCase() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(null);
        int rowsAffected = 0;
        when(jpaRepository.update(roleEntity)).thenReturn(rowsAffected);

        int result = roleRepository.update(roleEntity);

        Assert.assertEquals(rowsAffected, result);
    }

    @Test
    public void testUpdate_Exception() {
        RoleEntity roleEntity = null;

        Assert.assertThrows(NullPointerException.class, () -> roleRepository.update(roleEntity));
    }
}