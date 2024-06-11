import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<TestEntity, Long> repository;

@PersistenceUnit
private EntityManagerFactory entityManagerFactory;

@Before
public void setUp() {
repository = new EnhancedJpaRepository<>(JpaRepository.class.cast(new TestEntityRepository(entityManagerFactory).getClass().getDeclaredMethod("getEntityInformation")
.invoke(new TestEntityRepository(entityManagerFactory), null)), entityManagerFactory.createEntityManager());
}

@Test
public void testConstructor() {
Assert.assertNotNull(repository);
}

@Test
public void testGetEntityManager() {
Assert.assertNotNull(repository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
Assert.assertNotNull(repository.getJdbcTemplate());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
Assert.assertNotNull(repository.getNamedParameterJdbcTemplate());
}

@Test
public void testGetDataSource() {
DataSource dataSource1 = repository.getDataSource(entityManagerFactory.createEntityManager());
DataSource dataSource2 = ((DatasourceConnectionProviderImpl) entityManagerFactory.createEntityManager().getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
.getServiceRegistry().getService(ConnectionProvider.class)).getDataSource();
Assert.assertEquals(dataSource1, dataSource2);
}

@Test(expected = IllegalArgumentException.class)
public void testConstructorWithNullEntityInformation() {
new EnhancedJpaRepository<>(null, entityManagerFactory.createEntityManager());
}

@Test(expected = IllegalArgumentException.class)
public void testConstructorWithNullEntityManager() {
new EnhancedJpaRepository<>(JpaRepository.class.cast(new TestEntityRepository(entityManagerFactory).getClass().getDeclaredMethod("getEntityInformation")
.invoke(new TestEntityRepository(entityManagerFactory), null)), null);
}

@Test(expected = IllegalArgumentException.class)
public void testGetEntityManagerWithNullEntityManager() {
repository.setEntityManager(null);
repository.getEntityManager();
}

@Test(expected = IllegalArgumentException.class)
public void testGetJdbcTemplateWithNullJdbcTemplate() {
repository.setJdbcTemplate(null);
repository.getJdbcTemplate();
}

@Test(expected = IllegalArgumentException.class)
public void testGetNamedParameterJdbcTemplateWithNullNamedParameterJdbcTemplate() {
repository.setNamedParameterJdbcTemplate(null);
repository.getNamedParameterJdbcTemplate();
}

@Test(expected = IllegalArgumentException.class)
public void testGetDataSourceWithNullEntityManager() {
repository.setEntityManager(null);
repository.getDataSource(entityManagerFactory.createEntityManager());
}

private static class TestEntityRepository implements JpaRepository<TestEntity, Long> {

private final EntityManagerFactory entityManagerFactory;

public TestEntityRepository(EntityManagerFactory entityManagerFactory) {
this.entityManagerFactory = entityManagerFactory;
}

@Override
public EntityManager getEntityManager() {
return entityManagerFactory.createEntityManager();
}

@Override
public EntityInformation<TestEntity, ?> getEntityInformation() {
return null;
}

@Override
public <S extends TestEntity> S save(S entity) {
return null;
}

@Override
public TestEntity findOne(Long id) {
return null;
}

@Override
public boolean exists(Long id) {
return false;
}

@Override
public Iterable<TestEntity> findAll() {
return null;
}

@Override
public Iterable<TestEntity> findAll(Iterable<Long> ids) {
return null;
}

@Override
public long count() {
return 0;
}

@Override
public void delete(Long id) {
}

@Override
public void delete(TestEntity entity) {
}

@Override
public void delete(Iterable<? extends TestEntity> entities) {
}

@Override
public void deleteAll() {
}

@Override
public <S extends TestEntity> S findOne(EntitySpecification<TestEntity> spec) {
return null;
}

@Override
public Iterable<TestEntity> findAll(Specification<TestEntity> spec) {
return null;
}

@Override
public Iterable<TestEntity> findAll(Sort sort) {
return null;
}

@Override
public Page<TestEntity> findAll(Pageable pageable) {
return null;
}

@Override
public <S extends TestEntity> Page<S> findAll(Specification<S> spec, Pageable pageable) {
return null;
}

}

private static class TestEntity {

private Long id;

public Long getId() {
return id;
}

public void setId(Long id) {
this.id = id;
}

}

}