import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.data.domain.PageRequest;

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
List<Long> ids = new ArrayList<>();
ids.add(1L);
ids.add(2L);

when(jpaRepository.findByIdIn(ids)).thenReturn(getRoleEntities());

List<RoleEntity> roleEntities = roleRepository.findByIdIn(ids);

assertEquals(2, roleEntities.size());
verify(jpaRepository, times(1)).findByIdIn(ids);
}

@Test
public void testFindByName() {
String name = "test";

when(jpaRepository.findByName(name)).thenReturn(getRoleEntities());

List<RoleEntity> roleEntities = roleRepository.findByName(name);

assertEquals(2, roleEntities.size());
verify(jpaRepository, times(1)).findByName(name);
}

@Test
public void testFindByOrganizationId() {
Long organizationId = 1L;
PageRequest pageable = PageRequest.of(0, 10);

Page<RoleEntity> page = new PageImpl<>(getRoleEntities());
when(jpaRepository.findByOrganizationId(anyLong(), any(Pageable.class))).thenReturn(page);

Page<RoleEntity> resultPage = roleRepository.findByOrganizationId(organizationId, pageable);

assertEquals(2, resultPage.getContent().size());
verify(jpaRepository, times(1)).findByOrganizationId(anyLong(), any(Pageable.class));
}

@Test
public void testFindByNameAndOrganizationId() {
String name = "test";
Long organizationId = 1L;

RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);

Optional<RoleEntity> optional = Optional.of(roleEntity);
when(jpaRepository.findByNameAndOrganizationId(name, organizationId)).thenReturn(optional);

Optional<RoleEntity> resultOptional = roleRepository.findByNameAndOrganizationId(name, organizationId);

assertTrue(resultOptional.isPresent());
assertEquals(1L, resultOptional.get().getId().longValue());
verify(jpaRepository, times(1)).findByNameAndOrganizationId(name, organizationId);
}

@Test
public void testFindByOrganizationIdAndNameIn() {
Long organizationId = 1L;
List<String> names = new ArrayList<>();
names.add("test1");
names.add("test2");

when(jpaRepository.findByOrganizationIdAndNameIn(anyLong(), anyList())).thenReturn(getRoleEntities());

List<RoleEntity> roleEntities = roleRepository.findByOrganizationIdAndNameIn(organizationId, names);

assertEquals(2, roleEntities.size());
verify(jpaRepository, times(1)).findByOrganizationIdAndNameIn(anyLong(), anyList());
}

@Test
public void testFindByOrganizationIdAndRoleIds() {
Long organizationId = 1L;
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);

PageRequest pageable = PageRequest.of(0, 10);

Page<RoleEntity> page = new PageImpl<>(getRoleEntities());
when(jpaRepository.findByOrganizationIdAndRoleIds(anyLong(), anyCollection(), any(Pageable.class)))
.thenReturn(page);

Page<RoleEntity> resultPage = roleRepository.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);

assertEquals(2, resultPage.getContent().size());
verify(jpaRepository, times(1)).findByOrganizationIdAndRoleIds(anyLong(), anyCollection(), any(Pageable.class));
}

@Test
public void testFindByUserId() {
Long userId = 1L;

when(jpaRepository.findByUserId(anyLong())).thenReturn(getRoleEntities());

List<RoleEntity> roleEntities = roleRepository.findByUserId(userId);

assertEquals(2, roleEntities.size());
verify(jpaRepository, times(1)).findByUserId(anyLong());
}

@Test
public void testFindByUserIdAndEnabled() {
Long userId = 1L;
Boolean enabled = true;

when(jpaRepository.findByUserIdAndEnabled(anyLong(), anyBoolean())).thenReturn(getRoleEntities());

List<RoleEntity> roleEntities = roleRepository.findByUserIdAndEnabled(userId, enabled);

assertEquals(2, roleEntities.size());
verify(jpaRepository, times(1)).findByUserIdAndEnabled(anyLong(), anyBoolean());
}

@Test
public void testFindByRoleIdsAndEnabled() {
List<Long> roleIds = new ArrayList<>();
roleIds.add(1L);
roleIds.add(2L);
Boolean enabled = true;

when(jpaRepository.findByRoleIdsAndEnabled(anyCollection(), anyBoolean())).thenReturn(getRoleEntities());

List<RoleEntity> roleEntities = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);

assertEquals(2, roleEntities.size());
verify(jpaRepository, times(1)).findByRoleIdsAndEnabled(anyCollection(), anyBoolean());
}

@Test
public void testUpdate() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);

when(jpaRepository.existsById(anyLong())).thenReturn(true);

doNothing().when(jpaRepository).save(any(RoleEntity.class));

int result = roleRepository.update(roleEntity);

assertEquals(1, result);
verify(jpaRepository, times(1)).existsById(anyLong());
verify(jpaRepository, times(1)).save(any(RoleEntity.class));
}

private List<RoleEntity> getRoleEntities() {
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setId(1L);
roleEntity1.setName("test1");
roleEntity1.setOrganizationId(1L);

RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setId(2L);
roleEntity2.setName("test2");
roleEntity2.setOrganizationId(1L);

roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);

return roleEntities;
}

}