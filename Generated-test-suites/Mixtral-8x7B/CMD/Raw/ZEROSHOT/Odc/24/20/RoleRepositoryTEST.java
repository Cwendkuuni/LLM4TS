import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;

public class RoleRepositoryTest {

    private RoleRepository roleRepository;

    @Before
    public void setUp() {
        roleRepository = mock(RoleRepository.class);
    }

    @Test
    public void testFindByIdIn() {
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(2L);

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        String name = "test";

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setName(name);
        roleEntities.add(roleEntity1);

        when(roleRepository.findByName(name)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(1, result.size());
        assertTrue(result.contains(roleEntity1));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Pageable pageable = mock(Pageable.class);

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setOrganizationId(organizationId);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setOrganizationId(organizationId);
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());

        when(roleRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "test";
        Long organizationId = 1L;

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        roleEntity.setOrganizationId(organizationId);

        when(roleRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        assertEquals(roleEntity, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        Long organizationId = 1L;
        List<String> names = new ArrayList<>();
        names.add("test1");
        names.add("test2");

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setOrganizationId(organizationId);
        roleEntity1.setName("test1");
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setOrganizationId(organizationId);
        roleEntity2.setName("test2");
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setOrganizationId(organizationId);
        roleEntity1.setId(1L);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setOrganizationId(organizationId);
        roleEntity2.setId(2L);
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        Pageable pageable = mock(Pageable.class);

        Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());

        when(roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(roleEntity1));
        assertTrue(result.getContent().contains(roleEntity2));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setEnabled(enabled);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setEnabled(enabled);
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<>();
        roleIds.add(1L);
        roleIds.add(2L);

        Boolean enabled = true;

        List<RoleEntity> roleEntities = new ArrayList<>();
        RoleEntity roleEntity1 = new RoleEntity();
        roleEntity1.setId(1L);
        roleEntity1.setEnabled(enabled);
        RoleEntity roleEntity2 = new RoleEntity();
        roleEntity2.setId(2L);
        roleEntity2.setEnabled(enabled);
        roleEntities.add(roleEntity1);
        roleEntities.add(roleEntity2);

        when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities);

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("test");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("test description");

        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
    }
}