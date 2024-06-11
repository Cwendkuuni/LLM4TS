import static org.junit.Assert.assertNotNull;
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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManagerFactory entityManagerFactory;

 @Mock
 private EntityManager entityManager;

 @Mock
 private SessionFactoryImpl sessionFactory;

 @Mock
 private DatasourceConnectionProviderImpl connectionProvider;

 @Mock
 private DataSource dataSource;

 private EnhancedJpaRepository<Object, Serializable> repository;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
 when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);
 }

 // Test for getEntityManager()
 @Test
 public void testGetEntityManager() {
 assertNotNull(repository.getEntityManager());
 }

 // Test for getJdbcTemplate()
 @Test
 public void testGetJdbcTemplate() {
 assertNotNull(repository.getJdbcTemplate());
 }

 // Test for getNamedParameterJdbcTemplate()
 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertNotNull(repository.getNamedParameterJdbcTemplate());
 }

 // Edge case for getDataSource() - DataSource should not be null
 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEntityManager() {
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(entityInformation, null);
 repo.getDataSource(entityManager);
 }

 // Exception handling for getDataSource() - IllegalArgumentException when EntityManager is not an instance of SessionFactoryImpl
 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithInvalidEntityManager() {
 when(entityManager.getEntityManagerFactory()).thenReturn(null);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);
 repo.getDataSource(entityManager);
 }
}