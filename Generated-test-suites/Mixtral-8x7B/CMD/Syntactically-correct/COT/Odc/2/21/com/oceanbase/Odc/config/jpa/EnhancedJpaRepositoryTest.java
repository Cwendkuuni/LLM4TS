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
 private JpaEntityInformation<Object, Serializable> entityInfo;

 @Mock
 private EntityManagerFactory entityManagerFactory;

 @Mock
 private EntityManager entityManager;

 private EnhancedJpaRepository<Object, Serializable> repo;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 repo = new EnhancedJpaRepository<Object, Serializable>(entityInfo, entityManager);
 }

 // Test for getEntityManager()
 @Test
 public void testGetEntityManager() {
 assertNotNull(repo.getEntityManager());
 }

 // Test for getJdbcTemplate()
 @Test
 public void testGetJdbcTemplate() {
 assertNotNull(repo.getJdbcTemplate());
 }

 // Test for getNamedParameterJdbcTemplate()
 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertNotNull(repo.getNamedParameterJdbcTemplate());
 }

 // Edge case: getDataSource() with null EntityManager
 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEntityManager() {
 EnhancedJpaRepository<Object, Serializable> repoWithNullEntityManager = new EnhancedJpaRepository<Object, Serializable>(
 entityInfo, null);
 repoWithNullEntityManager.getDataSource(null);
 }

 // Edge case: getDataSource() with unwrapped null SessionFactory
 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithUnwrappedNullSessionFactory() {
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(null);
 repo.getDataSource(entityManager);
 }

 // Edge case: getDataSource() with null ConnectionProvider
 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullConnectionProvider() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(sf);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(null);
 repo.getDataSource(entityManager);
 }
}