import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
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

    @InjectMocks
    private RoleRepository roleRepository;

    private RoleEntity roleEntity1;
    private RoleEntity roleEntity2;
    private RoleEntity roleEntity3;
    private List<RoleEntity> roleEntities;
    private Pageable pageable;

    @Before
    public void setup() {
        roleEntity1 = new RoleEntity(1L, "Role 1", true, "Description 1");
        roleEntity2 = new RoleEntity(2L, "Role 2", true, "Description 2");
        roleEntity3 = new RoleEntity(3L, "Role 3", false, "Description 3");
        roleEntities = new ArrayList<RoleEntity>() {{
            add(roleEntity1);
            add(roleEntity2);
            add(roleEntity3);
        }};
        pageable = null; // Initialize pageable if needed
    }

    @Test
    public void testFindByIdIn() {
        Collection<Long> ids = new ArrayList<Long>() {{
            add(roleEntity1.getId());
            add(roleEntity2.getId());
        }};
        when(jpaRepository.findByIdIn(ids)).thenReturn(roleEntities.subList(0, 2));

        List<RoleEntity> result = roleRepository.findByIdIn(ids);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByName() {
        String name = "Role 1";
        when(jpaRepository.findByName(name)).thenReturn(roleEntities.subList(0, 1));

        List<RoleEntity> result = roleRepository.findByName(name);

        assertEquals(1, result.size());
        assertEquals(roleEntity1, result.get(0));
    }

    @Test
    public void testFindByOrganizationId() {
        Long organizationId = 1L;
        Page<RoleEntity> page = new PageImpl<RoleEntity>(roleEntities.subList(0, 2), pageable, roleEntities.size());
        when(jpaRepository.findByOrganizationId(organizationId, pageable)).thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(roleEntity1, result.getContent().get(0));
        assertEquals(roleEntity2, result.getContent().get(1));
    }

    @Test
    public void testFindByNameAndOrganizationId() {
        String name = "Role 1";
        Long organizationId = 1L;
        when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(Optional.of(roleEntity1));

        Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);

        assertTrue(result.isPresent());
        assertEquals(roleEntity1, result.get());
    }

    @Test
    public void testFindByOrganizationIdAndNameIn() {
        String name1 = "Role 1";
        String name2 = "Role 2";
        Long organizationId = 1L;
        List<String> names = new ArrayList<String>() {{
            add(name1);
            add(name2);
        }};
        when(jpaRepository.findByOrganizationIdAndNameIn(organizationId, names)).thenReturn(roleEntities.subList(0, 2));

        List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByOrganizationIdAndRoleIds() {
        Long organizationId = 1L;
        Collection<Long> roleIds = new ArrayList<Long>() {{
            add(roleEntity1.getId());
            add(roleEntity2.getId());
        }};
        Page<RoleEntity> page = new PageImpl<RoleEntity>(roleEntities.subList(0, 2), pageable, roleEntities.size());
        when(jpaRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable))
                .thenReturn(page);

        Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

        assertEquals(2, result.getContent().size());
        assertEquals(roleEntity1, result.getContent().get(0));
        assertEquals(roleEntity2, result.getContent().get(1));
    }

    @Test
    public void testFindByUserId() {
        Long userId = 1L;
        when(jpaRepository.findByUserId(userId)).thenReturn(roleEntities.subList(0, 3));

        List<RoleEntity> result = roleRepository.findByUserId(userId);

        assertEquals(3, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
        assertTrue(result.contains(roleEntity3));
    }

    @Test
    public void testFindByUserIdAndEnabled() {
        Long userId = 1L;
        Boolean enabled = true;
        when(jpaRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities.subList(0, 2));

        List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testFindByRoleIdsAndEnabled() {
        Collection<Long> roleIds = new ArrayList<Long>() {{
            add(roleEntity1.getId());
            add(roleEntity2.getId());
        }};
        Boolean enabled = true;
        when(jpaRepository.findByRoleIdsAndEnabled(roleIds, enabled)).thenReturn(roleEntities.subList(0, 2));

        List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

        assertEquals(2, result.size());
        assertTrue(result.contains(roleEntity1));
        assertTrue(result.contains(roleEntity2));
    }

    @Test
    public void testUpdate() {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1L);
        roleEntity.setName("Updated Role");
        roleEntity.setEnabled(true);
        roleEntity.setDescription("Updated Description");

        when(jpaRepository.update(roleEntity)).thenReturn(1);

        int result = roleRepository.update(roleEntity);

        assertEquals(1, result);
    }
}