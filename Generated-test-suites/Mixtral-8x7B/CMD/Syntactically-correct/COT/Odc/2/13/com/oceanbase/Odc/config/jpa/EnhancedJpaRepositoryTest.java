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

 private EnhancedJpaRepository<Object, Serializable> repository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
 }

 // Test for getEntityManager()
 @Test
 public void testGetEntityManager() {
 EntityManager result = repository.getEntityManager();
 assertNotNull(result);
 }

 // Test for getJdbcTemplate()
 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate result = repository.getJdbcTemplate();
 assertNotNull(result);
 }

 // Test for getNamedParameterJdbcTemplate()
 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
 assertNotNull(result);
 }

 // Edge case: Test getDataSource() with null EntityManager
 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullEntityManager() {
 EnhancedJpaRepository<Object, Serializable> repositoryWithNullEntityManager = new EnhancedJpaRepository<>(
 entityInformation, null);
 repositoryWithNullEntityManager.getDataSource(null);
 }

 // Edge case: Test getDataSource() with unwrapped EntityManagerFactory as null
 @Test(expected = IllegalStateException.class)
 public void testGetDataSourceWithNullUnwrappedEntityManagerFactory() {
 when(entityManager.getEntityManagerFactory()).thenReturn(null);
 repository.getDataSource(entityManager);
 }
}