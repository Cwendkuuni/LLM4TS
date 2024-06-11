import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<DummyClass, Long> repository;

 @PersistenceUnit
 private EntityManagerFactory emf;

 @Test
 public void testGetDataSource() {
 EntityManager entityManager = mock(EntityManager.class);
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
 DataSource dataSource = mock(DataSource.class);

 when(entityManager.getEntityManagerFactory()).thenReturn(emf);
 when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 when(sf.getServiceRegistry()).thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
 when(sf.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);

 EnhancedJpaRepository<DummyClass, Long> enhancedJpaRepository = new EnhancedJpaRepository<DummyClass, Long>(
 mock(JpaEntityInformation.class), entityManager);

 DataSource result = enhancedJpaRepository.getDataSource(entityManager);

 assertEquals(dataSource, result);
 }

 @Test
 public void testGetEntityManager() {
 JpaEntityInformation<DummyClass, Long> entityInformation = mock(JpaEntityInformation.class);
 EntityManager entityManager = mock(EntityManager.class);

 EnhancedJpaRepository<DummyClass, Long> enhancedJpaRepository = new EnhancedJpaRepository<DummyClass, Long>(
 entityInformation, entityManager);

 EntityManager result = enhancedJpaRepository.getEntityManager();

 assertEquals(entityManager, result);
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

 EnhancedJpaRepository<DummyClass, Long> enhancedJpaRepository = new EnhancedJpaRepository<DummyClass, Long>(
 mock(JpaEntityInformation.class), mock(EntityManager.class)) {
 {
 this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
 }
 };

 JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();

 assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), result);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

 EnhancedJpaRepository<DummyClass, Long> enhancedJpaRepository = new EnhancedJpaRepository<DummyClass, Long>(
 mock(JpaEntityInformation.class), mock(EntityManager.class)) {
 {
 this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
 }
 };

 NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();

 assertEquals(namedParameterJdbcTemplate, result);
 }

 static class DummyClass {
 }
}