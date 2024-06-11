import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

 private static class TestEntityInformation<T, ID extends Serializable> implements JpaEntityInformation<T, ID> {
 @Override
 public Class<T> getEntityType() {
 return null;
 }

 @Override
 public ID getId(T entity) {
 return null;
 }

 @Override
 public Class<ID> getIdType() {
 return null;
 }
 }

 @Test
 public void testGetDataSource() {
 EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
 EntityManager entityManager = mock(EntityManager.class);
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(new TestEntityInformation<>(),
 entityManager);
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 DataSource dataSource = mock(DataSource.class);
 when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
 when(cp.getDataSource()).thenReturn(dataSource);
 assertEquals(dataSource, repo.getDataSource(entityManager));
 }

 @Test
 public void testGetEntityManager() {
 EntityManager entityManager = mock(EntityManager.class);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(new TestEntityInformation<>(),
 entityManager);
 assertEquals(entityManager, repo.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(new TestEntityInformation<>(),
 mock(EntityManager.class), namedParameterJdbcTemplate);
 assertEquals(jdbcTemplate, repo.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(new TestEntityInformation<>(),
 mock(EntityManager.class), mock(NamedParameterJdbcTemplate.class));
 assertEquals(repo.getNamedParameterJdbcTemplate(), repo.getNamedParameterJdbcTemplate());
 }
}