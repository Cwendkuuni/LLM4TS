import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.oceanbase.odc.metadb.iam.entity.RoleEntity;
import com.oceanbase.odc.metadb.iam.repository.RoleRepository;

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

@Mock
private JpaRepository<RoleEntity, Long> jpaRepositoryMock;

@InjectMocks
private RoleRepository roleRepository;

private RoleEntity roleEntity1;
private RoleEntity roleEntity2;

private List<RoleEntity> roleEntities;

private Pageable pageable;

@Before
public void setup() {
roleEntity1 = new RoleEntity();
roleEntity1.setId(1L);
roleEntity1.setName("Role1");
roleEntity1.setOrganizationId(1L);
roleEntity1.setEnabled(true);

roleEntity2 = new RoleEntity();
roleEntity2.setId(2L);
roleEntity2.setName("Role2");
roleEntity2.setOrganizationId(2L);
roleEntity2.setEnabled(false);

roleEntities = new ArrayList<>();
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);

pageable = PageRequest.of(0, 10);
}

@After
public void tearDown() {
roleEntity1 = null;
roleEntity2 = null;
roleEntities = null;
pageable = null;
}

@Test
public void testFindByIdIn() {
List<Long> ids = Collections.singletonList(1L);
when(jpaRepositoryMock.findByIdIn(any())).thenReturn(roleEntities);

List<RoleEntity> result = roleRepository.findByIdIn(ids);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.size(), CoreMatchers.equalTo(2));
MatcherAssert.assertThat(result.get(0).getId(), CoreMatchers.equalTo(1L));
MatcherAssert.assertThat(result.get(1).getId(), CoreMatchers.equalTo(2L));
}

@Test
public void testFindByName() {
String name = "Role1";
when(jpaRepositoryMock.findByName(any())).thenReturn(roleEntities);

List<RoleEntity> result = roleRepository.findByName(name);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.size(), CoreMatchers.equalTo(2));
MatcherAssert.assertThat(result.get(0).getId(), CoreMatchers.equalTo(1L));
MatcherAssert.assertThat(result.get(1).getId(), CoreMatchers.equalTo(2L));
}

@Test
public void testFindByOrganizationId() {
Long organizationId = 1L;
Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());
when(jpaRepositoryMock.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(page);

Page<RoleEntity> result = roleRepository.findByOrganizationId(organizationId, pageable);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.getTotalElements(), CoreMatchers.equalTo(2L));
MatcherAssert.assertThat(result.getContent().size(), CoreMatchers.equalTo(2));
}

@Test
public void testFindByNameAndOrganizationId() {
String name = "Role1";
Long organizationId = 1L;
Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity1);
when(jpaRepositoryMock.findByNameAndOrganizationId(anyString(), anyLong())).thenReturn(optionalRoleEntity);

Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(name, organizationId);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertTrue(result.isPresent());
MatcherAssert.assertThat(result.get().getId(), CoreMatchers.equalTo(1L));
}

@Test
public void testFindByOrganizationIdAndNameIn() {
Long organizationId = 1L;
List<String> names = Collections.singletonList("Role1");
roleEntities.get(0).setName(names.get(0));
when(jpaRepositoryMock.findByOrganizationIdAndNameIn(anyLong(), any())).thenReturn(roleEntities);

List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.size(), CoreMatchers.equalTo(1));
MatcherAssert.assertThat(result.get(0).getId(), CoreMatchers.equalTo(1L));
}

@Test
public void testFindByOrganizationIdAndRoleIds() {
Long organizationId = 1L;
List<Long> roleIds = Collections.singletonList(1L);
Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());
when(jpaRepositoryMock.findByOrganizationIdAndRoleIds(anyLong(), any(), any(Pageable.class)))
.thenReturn(page);

Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.getTotalElements(), CoreMatchers.equalTo(2L));
MatcherAssert.assertThat(result.getContent().size(), CoreMatchers.equalTo(2));
}

@Test
public void testFindByUserId() {
Long userId = 1L;
List<RoleEntity> roleEntitiesByUserId = new ArrayList<>();
roleEntitiesByUserId.add(roleEntity1);
when(jpaRepositoryMock.query(anyString(), any())).thenReturn(roleEntitiesByUserId);

List<RoleEntity> result = roleRepository.findByUserId(userId);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.size(), CoreMatchers.equalTo(1));
MatcherAssert.assertThat(result.get(0).getId(), CoreMatchers.equalTo(1L));
}

@Test
public void testFindByUserIdAndEnabled() {
Long userId = 1L;
Boolean enabled = true;
List<RoleEntity> roleEntitiesByUserIdAndEnabled = new ArrayList<>();
roleEntitiesByUserIdAndEnabled.add(roleEntity1);
when(jpaRepositoryMock.query(anyString(), any())).thenReturn(roleEntitiesByUserIdAndEnabled);

List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.size(), CoreMatchers.equalTo(1));
MatcherAssert.assertThat(result.get(0).getId(), CoreMatchers.equalTo(1L));
}

@Test
public void testFindByRoleIdsAndEnabled() {
List<Long> roleIds = Collections.singletonList(1L);
Boolean enabled = true;
List<RoleEntity> roleEntitiesByRoleIdsAndEnabled = new ArrayList<>();
roleEntitiesByRoleIdsAndEnabled.add(roleEntity1);
when(jpaRepositoryMock.query(anyString(), any())).thenReturn(roleEntitiesByRoleIdsAndEnabled);

List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
MatcherAssert.assertThat(result, CoreMatchers.notNullValue());
MatcherAssert.assertThat(result.size(), CoreMatchers.equalTo(1));
MatcherAssert.assertThat(result.get(0).getId(), CoreMatchers.equalTo(1L));
}

@Test
public void testUpdate() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);
roleEntity.setName("New Role1");
roleEntity.setOrganizationId(1L);
roleEntity.setEnabled(true);

int rowsAffected = 1;
when(jpaRepositoryMock.update(any())).thenReturn(rowsAffected);

int result = roleRepository.update(roleEntity);
MatcherAssert.assertThat(result, CoreMatchers.equalTo(rowsAffected));
}
}