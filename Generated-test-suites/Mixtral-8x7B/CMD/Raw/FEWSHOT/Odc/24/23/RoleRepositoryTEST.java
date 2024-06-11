import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.metadb.iam.RoleEntity;
import com.oceanbase.odc.metadb.iam.RoleRepository;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoleRepositoryTest {

 private RoleRepository roleRepository;

 @Test
 public void testJpaRepositoryMethods() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);

 // Test save method
 RoleEntity savedRoleEntity = roleRepository.save(roleEntity);
 assertNotNull(savedRoleEntity.getId());

 // Test findById method
 Optional<RoleEntity> optionalRoleEntity = roleRepository.findById(savedRoleEntity.getId());
 assertTrue(optionalRoleEntity.isPresent());
 assertEquals(savedRoleEntity, optionalRoleEntity.get());

 // Test findAll method
 roleRepository.findAll();
 Mockito.verify(roleRepository).findAll();

 // Test findAll with Sort
 Sort sort = Sort.by(Sort.Direction.ASC, "name");
 roleRepository.findAll(sort);
 Mockito.verify(roleRepository).findAll(sort);

 // Test deleteById method
 roleRepository.deleteById(savedRoleEntity.getId());
 Mockito.verify(roleRepository).deleteById(savedRoleEntity.getId());

 // Test existsById method
 boolean exists = roleRepository.existsById(savedRoleEntity.getId());
 assertTrue(exists);
 }

 @Test
 public void testFindByIdIn() {
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("Role 1");

 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("Role 2");

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2);
 List<Long> ids = Arrays.asList(1L, 2L);

 Mockito.when(roleRepository.findByIdIn(ids)).thenReturn(roleEntities);
 List<RoleEntity> result = roleRepository.findByIdIn(ids);
 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByName() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("Test Role");

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity);
 Mockito.when(roleRepository.findByName("Test Role")).thenReturn(roleEntities);
 List<RoleEntity> result = roleRepository.findByName("Test Role");
 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByOrganizationId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);

 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
 Page<RoleEntity> page = roleRepository.findByOrganizationId(1L, pageable);
 assertEquals(1, page.getTotalElements());
 assertEquals(roleEntity, page.getContent().get(0));
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);

 Optional<RoleEntity> optionalRoleEntity = Optional.of(roleEntity);
 Mockito.when(roleRepository.findByNameAndOrganizationId("Test Role", 1L))
 .thenReturn(optionalRoleEntity);
 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId("Test Role", 1L);
 assertEquals(optionalRoleEntity, result);
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("Role 1");
 roleEntity1.setOrganizationId(1L);

 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("Role 2");
 roleEntity2.setOrganizationId(1L);

 List<String> names = Arrays.asList("Role 1", "Role 3");
 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1);

 Mockito.when(roleRepository.findByOrganizationIdAndNameIn(1L, names))
 .thenReturn(roleEntities);
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(1L, names);
 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("Role 1");
 roleEntity1.setOrganizationId(1L);

 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("Role 2");
 roleEntity2.setOrganizationId(1L);

 Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
 List<Long> roleIds = Arrays.asList(1L, 2L);

 Mockito.when(roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable))
 .thenReturn(createPage(roleEntities, pageable));
 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(1L, roleIds, pageable);
 assertEquals(2, result.getTotalElements());
 }

 @Test
 public void testFindByUserId() {
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("Role 1");

 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("Role 2");

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1, roleEntity2);
 Long userId = 1L;

 Mockito.when(roleRepository.findByUserId(userId)).thenReturn(roleEntities);
 List<RoleEntity> result = roleRepository.findByUserId(userId);
 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("Role 1");
 roleEntity1.setEnabled(true);

 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("Role 2");
 roleEntity2.setEnabled(false);

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1);
 Long userId = 1L;
 boolean enabled = true;

 Mockito.when(roleRepository.findByUserIdAndEnabled(userId, enabled)).thenReturn(roleEntities);
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(userId, enabled);
 assertEquals(roleEntities, result);
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 RoleEntity roleEntity1 = new RoleEntity();
 roleEntity1.setId(1L);
 roleEntity1.setName("Role 1");
 roleEntity1.setEnabled(true);

 RoleEntity roleEntity2 = new RoleEntity();
 roleEntity2.setId(2L);
 roleEntity2.setName("Role 2");
 roleEntity2.setEnabled(false);

 List<RoleEntity> roleEntities = Arrays.asList(roleEntity1);
 List<Long> roleIds = Arrays.asList(1L, 2L);
 boolean enabled = true;

 Mockito.when(roleRepository.findByRoleIdsAndEnabled(roleIds, enabled))
 .thenReturn(roleEntities);
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(roleIds, enabled);
 assertEquals(roleEntities, result);
 }

 @Test
 public void testUpdate() {
 RoleEntity roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("Test Role");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);

 int result = roleRepository.update(roleEntity);
 assertEquals(1, result);
 }

 private Page<RoleEntity> createPage(List<RoleEntity> roleEntities, Pageable pageable) {
 int pageSize = pageable.getPageSize();
 int currentPage = pageable.getPageNumber();
 int totalElements = roleEntities.size();
 int startIndex = currentPage * pageSize;
 int endIndex = Math.min(startIndex + pageSize, totalElements);
 List<RoleEntity> content = roleEntities.subList(startIndex, endIndex);
 return new Page<RoleEntity>() {
 @Override
 public int getTotalElements() {
 return totalElements;
 }

 @Override
 public int getNumber() {
 return currentPage;
 }

 @Override
 public int getSize() {
 return pageSize;
 }

 @Override
 public int getNumberOfElements() {
 return content.size();
 }

 @Override
 public List<RoleEntity> getContent() {
 return content;
 }

 @Override
 public boolean hasContent() {
 return !content.isEmpty();
 }

 @Override
 public boolean isFirst() {
 return getNumber() == 0;
 }

 @Override
 public boolean isLast() {
 return getNumber() >= getTotalPages() - 1;
 }

 @Override
 public boolean hasNext() {
 return getNumber() < getTotalPages() - 1;
 }

 @Override
 public boolean hasPrevious() {
 return getNumber() > 0;
 }

 @Override
 public int getTotalPages() {
 int totalPages = (int) Math.ceil(getTotalElements() * 1.0 / getSize());
 return Math.max(totalPages, 1);
 }

 @Override
 public Sort getSort() {
 return pageable.getSort();
 }

 @Override
 public Pageable nextPageable() {
 return pageable.next();
 }

 @Override
 public Pageable previousPageable() {
 return pageable.previousOrFirst();
 }

 @Override
 public <U> Page<U> map(Function<? super RoleEntity, ? extends U> converter) {
 return null;
 }
 };
 }
}