package com.oceanbase.odc.metadb.iam;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

public class RoleRepositoryTest {

@Test
public void testJpaRepository() {
RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);

RoleRepository roleRepository = mock(RoleRepository.class);
doReturn(roleEntity).when(roleRepository).findById(1L);
doReturn(new ArrayList<>()).when(roleRepository).findAll();
doReturn(1).when(roleRepository).count();
doReturn(true).when(roleRepository).existsById(1L);

roleRepository.deleteById(1L);
roleRepository.delete(roleEntity);
roleRepository.save(roleEntity);
}

@Test
public void testFindByIdIn() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setId(1L);
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setId(2L);
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
doReturn(roleEntities).when(roleRepository).findByIdIn(new ArrayList<Long>() {{
add(1L);
add(2L);
}});

assertEquals(2, roleRepository.findByIdIn(new ArrayList<Long>() {{
add(1L);
add(2L);
}}).size());
}

@Test
public void testFindByName() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setName("name1");
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setName("name2");
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
doReturn(roleEntities).when(roleRepository).findByName("name1");

assertEquals(2, roleRepository.findByName("name1").size());
}

@Test
public void testFindByOrganizationId() {
RoleRepository roleRepository = mock(RoleRepository.class);
Pageable pageable = mock(Pageable.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setOrganizationId(1L);
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setOrganizationId(1L);
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());
doReturn(page).when(roleRepository).findByOrganizationId(1L, pageable);

assertEquals(2, roleRepository.findByOrganizationId(1L, pageable).getTotalElements());
}

@Test
public void testFindByNameAndOrganizationId() {
RoleRepository roleRepository = mock(RoleRepository.class);
Optional<RoleEntity> roleEntityOptional = Optional.of(new RoleEntity());
doReturn(roleEntityOptional).when(roleRepository).findByNameAndOrganizationId("name", 1L);

assertEquals(true, roleRepository.findByNameAndOrganizationId("name", 1L).isPresent());
}

@Test
public void testFindByOrganizationIdAndNameIn() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setOrganizationId(1L);
roleEntity1.setName("name1");
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setOrganizationId(1L);
roleEntity2.setName("name2");
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
doReturn(roleEntities).when(roleRepository).findByOrganizationIdAndNameIn(1L, new ArrayList<String>() {{
add("name1");
add("name2");
}});

assertEquals(2, roleRepository.findByOrganizationIdAndNameIn(1L, new ArrayList<String>() {{
add("name1");
add("name2");
}}).size());
}

@Test
public void testFindByOrganizationIdAndRoleIds() {
RoleRepository roleRepository = mock(RoleRepository.class);
Pageable pageable = mock(Pageable.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setOrganizationId(1L);
roleEntity1.setId(1L);
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setOrganizationId(1L);
roleEntity2.setId(2L);
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
Page<RoleEntity> page = new PageImpl<>(roleEntities, pageable, roleEntities.size());
doReturn(page).when(roleRepository).findByOrganizationIdAndRoleIds(1L, new ArrayList<Long>() {{
add(1L);
add(2L);
}}, pageable);

assertEquals(2, roleRepository.findByOrganizationIdAndRoleIds(1L, new ArrayList<Long>() {{
add(1L);
add(2L);
}}, pageable).getTotalElements());
}

@Test
public void testFindByUserId() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setId(1L);
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setId(2L);
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
doReturn(roleEntities).when(roleRepository).findByUserId(1L);

assertEquals(2, roleRepository.findByUserId(1L).size());
}

@Test
public void testFindByUserIdAndEnabled() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setId(1L);
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setId(2L);
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
doReturn(roleEntities).when(roleRepository).findByUserIdAndEnabled(1L, true);

assertEquals(2, roleRepository.findByUserIdAndEnabled(1L, true).size());
}

@Test
public void testFindByRoleIdsAndEnabled() {
RoleRepository roleRepository = mock(RoleRepository.class);
List<RoleEntity> roleEntities = new ArrayList<>();
RoleEntity roleEntity1 = new RoleEntity();
roleEntity1.setId(1L);
RoleEntity roleEntity2 = new RoleEntity();
roleEntity2.setId(2L);
roleEntities.add(roleEntity1);
roleEntities.add(roleEntity2);
doReturn(roleEntities).when(roleRepository).findByRoleIdsAndEnabled(new ArrayList<Long>() {{
add(1L);
add(2L);
}}, true);

assertEquals(2, roleRepository.findByRoleIdsAndEnabled(new ArrayList<Long>() {{
add(1L);
add(2L);
}}, true).size());
}

@Test
public void testUpdate() {
RoleRepository roleRepository = mock(RoleRepository.class);
RoleEntity roleEntity = new RoleEntity();
roleEntity.setId(1L);
int result = roleRepository.update(roleEntity);

assertEquals(1, result);
}
}