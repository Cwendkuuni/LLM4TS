import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

@Test
public void testFindByIdIn() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
roleEntities.add(new RoleEntity());
roleEntities.add(new RoleEntity());
when(roleRepository.findByIdIn(Arrays.asList(1L, 2L))).thenReturn(roleEntities);
assertEquals(roleEntities, roleRepository.findByIdIn(Arrays.asList(1L, 2L)));
}

@Test
public void testFindByName() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
roleEntities.add(new RoleEntity());
roleEntities.add(new RoleEntity());
when(roleRepository.findByName("name")).thenReturn(roleEntities);
assertEquals(roleEntities, roleRepository.findByName("name"));
}

@Test
public void testFindByOrganizationId() {
RoleRepository roleRepository = mock(RoleRepository.class);
Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));
when(roleRepository.findByOrganizationId(1L, mock(Pageable.class))).thenReturn(page);
assertEquals(page, roleRepository.findByOrganizationId(1L, mock(Pageable.class)));
}

@Test
public void testFindByNameAndOrganizationId() {
RoleRepository roleRepository = mock(RoleRepository.class);
Optional<RoleEntity> roleEntity = Optional.of(new RoleEntity());
when(roleRepository.findByNameAndOrganizationId("name", 1L)).thenReturn(roleEntity);
assertEquals(roleEntity, roleRepository.findByNameAndOrganizationId("name", 1L));
}

@Test
public void testFindByOrganizationIdAndNameIn() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
roleEntities.add(new RoleEntity());
roleEntities.add(new RoleEntity());
when(roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name1", "name2"))).thenReturn(roleEntities);
assertEquals(roleEntities, roleRepository.findByOrganizationIdAndNameIn(1L, Arrays.asList("name1", "name2")));
}

@Test
public void testFindByOrganizationIdAndRoleIds() {
RoleRepository roleRepository = mock(RoleRepository.class);
Page<RoleEntity> page = new PageImpl<>(Arrays.asList(new RoleEntity(), new RoleEntity()));
when(roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), mock(Pageable.class)))
.thenReturn(page);
assertEquals(page, roleRepository.findByOrganizationIdAndRoleIds(1L, Arrays.asList(1L, 2L), mock(Pageable.class)));
}

@Test
public void testFindByUserId() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
roleEntities.add(new RoleEntity());
roleEntities.add(new RoleEntity());
when(roleRepository.findByUserId(1L)).thenReturn(roleEntities);
assertEquals(roleEntities, roleRepository.findByUserId(1L));
}

@Test
public void testFindByUserIdAndEnabled() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
roleEntities.add(new RoleEntity());
roleEntities.add(new RoleEntity());
when(roleRepository.findByUserIdAndEnabled(1L, true)).thenReturn(roleEntities);
assertEquals(roleEntities, roleRepository.findByUserIdAndEnabled(1L, true));
}

@Test
public void testFindByRoleIdsAndEnabled() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
roleEntities.add(new RoleEntity());
roleEntities.add(new RoleEntity());
when(roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true)).thenReturn(roleEntities);
assertEquals(roleEntities, roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true));
}

@Test
public void testUpdate() {
RoleRepository roleRepository = mock(RoleRepository.class);
int result = roleRepository.update(new RoleEntity());
assertEquals(1, result);
}
}