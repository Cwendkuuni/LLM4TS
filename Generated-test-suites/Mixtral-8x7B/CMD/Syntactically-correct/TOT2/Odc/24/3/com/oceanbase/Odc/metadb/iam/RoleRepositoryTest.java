import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

@RunWith(MockitoJUnitRunner.class)
public class RoleRepositoryTest {

 @Mock
 private JpaRepository<RoleEntity, Long> jpaRepository;

 @InjectMocks
 private RoleRepository roleRepository = new RoleRepositoryImpl(jpaRepository);

 private RoleEntity roleEntity;

 private Pageable pageable;

 @Before
 public void setup() {
 roleEntity = new RoleEntity();
 roleEntity.setId(1L);
 roleEntity.setName("testRole");
 roleEntity.setOrganizationId(1L);
 roleEntity.setEnabled(true);

 pageable = new PageRequest(0, 10);
 }

 @Test
 public void testFindByIdIn() {
 List<Long> ids = new ArrayList<>();
 ids.add(1L);

 when(jpaRepository.findAll(ids)).thenReturn(new ArrayList<>());

 List<RoleEntity> result = roleRepository.findByIdIn(ids);

 assertEquals(0, result.size());
 }

 @Test
 public void testFindByName() {
 when(jpaRepository.findByName(roleEntity.getName())).thenReturn(new ArrayList<>());

 List<RoleEntity> result = roleRepository.findByName(roleEntity.getName());

 assertEquals(0, result.size());
 }

 @Test
 public void testFindByOrganizationId() {
 Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

 when(jpaRepository.findAll(new Specification<RoleEntity>() {
 @Override
 public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query,
 CriteriaBuilder cb) {
 return cb.equal(root.get("organizationId"), roleEntity.getOrganizationId());
 }
 }));
 thenReturn(page);

 Page<RoleEntity> result = roleRepository.findByOrganizationId(roleEntity.getOrganizationId(), pageable);

 assertEquals(0, result.getTotalElements());
 }

 @Test
 public void testFindByNameAndOrganizationId() {
 Optional<RoleEntity> optional = Optional.of(roleEntity);

 when(jpaRepository.findOne(new Specification<RoleEntity>() {
 @Override
 public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query,
 CriteriaBuilder cb) {
 return cb.and(cb.equal(root.get("name"), roleEntity.getName()),
 cb.equal(root.get("organizationId"), roleEntity.getOrganizationId()));
 }
 })).thenReturn(optional);

 Optional<RoleEntity> result = roleRepository.findByNameAndOrganizationId(roleEntity.getName(),
 roleEntity.getOrganizationId());

 assertTrue(result.isPresent());
 assertEquals(roleEntity, result.get());
 }

 @Test
 public void testFindByOrganizationIdAndNameIn() {
 List<RoleEntity> result = roleRepository.findByOrganizationIdAndNameIn(roleEntity.getOrganizationId(),
 Arrays.asList("testRole1", "testRole2"));

 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindByOrganizationIdAndRoleIds() {
 List<Long> roleIds = new ArrayList<>();
 roleIds.add(1L);

 Page<RoleEntity> page = new PageImpl<>(new ArrayList<>(), pageable, 0);

 when(jpaRepository.findAll(new Specification<RoleEntity>() {
 @Override
 public Predicate toPredicate(Root<RoleEntity> root, CriteriaQuery<?> query,
 CriteriaBuilder cb) {
 return cb.and(cb.equal(root.get("organizationId"), roleEntity.getOrganizationId()),
 root.get("id").in(roleIds));
 }
 }))
 .thenReturn(page);

 Page<RoleEntity> result = roleRepository.findByOrganizationIdAndRoleIds(roleEntity.getOrganizationId(),
 roleIds, pageable);

 assertEquals(0, result.getTotalElements());
 }

 @Test
 public void testFindByUserId() {
 List<RoleEntity> result = roleRepository.findByUserId(1L);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindByUserIdAndEnabled() {
 List<RoleEntity> result = roleRepository.findByUserIdAndEnabled(1L, true);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testFindByRoleIdsAndEnabled() {
 List<RoleEntity> result = roleRepository.findByRoleIdsAndEnabled(Arrays.asList(1L, 2L), true);

 assertTrue(result.isEmpty());
 }

 @Test
 public void testUpdate() {
 when(jpaRepository.save(roleEntity)).thenReturn(roleEntity);

 int result = roleRepository.update(roleEntity);

 assertEquals(1, result);
 }
}

class RoleRepositoryImpl extends RoleRepository {

 @Override
 public List<RoleEntity> findByIdIn(Collection<Long> ids) {
 return super.findByIdIn(ids);
 }

 @Override
 public List<RoleEntity> findByName(String name) {
 return super.findByName(name);
 }

 @Override
 public Page<RoleEntity> findByOrganizationId(Long organizationId, Pageable pageable) {
 return super.findByOrganizationId(organizationId, pageable);
 }

 @Override
 public Optional<RoleEntity> findByNameAndOrganizationId(String name, Long organizationId) {
 return super.findByNameAndOrganizationId(name, organizationId);
 }

 @Override
 public List<RoleEntity> findByOrganizationIdAndNameIn(Long organizationId, List<String> name) {
 return super.findByOrganizationIdAndNameIn(organizationId, name);
 }

 @Override
 public Page<RoleEntity> findByOrganizationIdAndRoleIds(Long organizationId, Collection<Long> roleIds,
 Pageable pageable) {
 return super.findByOrganizationIdAndRoleIds(organizationId, roleIds, pageable);
 }

 @Override
 public List<RoleEntity> findByUserId(Long userId) {
 return super.findByUserId(userId);
 }

 @Override
 public List<RoleEntity> findByUserIdAndEnabled(Long userId, Boolean enabled) {
 return super.findByUserIdAndEnabled(userId, enabled);
 }

 @Override
 public List<RoleEntity> findByRoleIdsAndEnabled(Collection<Long> roleIds, Boolean enabled) {
 return super.findByRoleIdsAndEnabled(roleIds, enabled);
 }

 @Override
 @Modifying
 @Transactional
 public int update(RoleEntity roleEntity) {
 return super.update(roleEntity);
 }
}