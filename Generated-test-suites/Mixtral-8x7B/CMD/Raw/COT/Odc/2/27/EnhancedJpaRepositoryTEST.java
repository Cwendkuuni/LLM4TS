import static org.junit.Assert.assertEquals;
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
import org.mockito.ArgumentMatchers;
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

 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 enhancedJpaRepository = new EnhancedJpaRepository<>(entityInfo, entityManagerFactory);
 }

 // Test for getEntityManager()
 @Test
 public void testGetEntityManager() {
 EntityManager result = enhancedJpaRepository.getEntityManager();
 assertNotNull(result);
 }

 // Test for getJdbcTemplate()
 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
 assertNotNull(result);
 }

 // Test for getNamedParameterJdbcTemplate()
 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
 assertNotNull(result);
 }

 // Edge case: getDataSource() with null EntityManager
 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEntityManager() {
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInfo, null);
 repo.getDataSource(null);
 }

 // Edge case: getDataSource() with unwrapped SessionFactoryImpl as null
 @Test
 public void testGetDataSourceWithNullSessionFactory() {
 EntityManager em = mock(EntityManager.class);
 when(em.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(null);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInfo, em);
 DataSource result = repo.getDataSource(em);
 assertNotNull(result);
 }

 // Test for getDataSource() with valid EntityManager
 @Test
 public void testGetDataSourceWithValidEntityManager() {
 EntityManager em = mock(EntityManager.class);
 when(em.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 DataSource ds = mock(DataSource.class);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
 when(cp.getDataSource()).thenReturn(ds);
 when(em.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInfo, em);
 DataSource result = repo.getDataSource(em);
 assertNotNull(result);
 }
}