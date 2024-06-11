import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
 private EnhancedJpaRepository<Object, Serializable> repository;
 private EntityManagerFactory entityManagerFactory;
 private EntityManager entityManager;
 private DataSource dataSource;
 private JpaEntityInformation<Object, Serializable> entityInformation;
 private SessionFactoryImpl sessionFactory;
 private ConnectionProvider connectionProvider;
 private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

 @Before
 public void setUp() {
 entityManagerFactory = mock(EntityManagerFactory.class);
 entityManager = mock(EntityManager.class);
 dataSource = mock(DataSource.class);
 entityInformation = mock(JpaEntityInformation.class);
 sessionFactory = mock(SessionFactoryImpl.class, Answers.RETURNS_DEEP_STUBS);
 connectionProvider = mock(DatasourceConnectionProviderImpl.class);
 namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

 when(entityManagerFactory.unwrap(EntityManager.class)).thenReturn(entityManager);
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(sessionFactory);
 when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);
 when(repository.getEntityManager().getEntityManagerFactory()).thenReturn(entityManagerFactory);

 repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager) {
 @Override
 public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
 return namedParameterJdbcTemplate;
 }
 };
 }

 @Test
 public void testGetDataSource() {
 DataSource result = repository.getDataSource(entityManager);
 assertEquals(dataSource, result);
 }

 @Test
 public void testGetEntityManager() {
 EntityManager result = repository.getEntityManager();
 assertEquals(entityManager, result);
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate result = repository.getJdbcTemplate();
 assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), result);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
 assertEquals(namedParameterJdbcTemplate, result);
 }
}