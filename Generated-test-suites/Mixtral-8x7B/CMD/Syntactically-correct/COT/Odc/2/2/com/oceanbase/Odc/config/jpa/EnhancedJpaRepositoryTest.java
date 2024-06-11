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

 private EntityManagerFactory emf;

 private EnhancedJpaRepository<Object, Serializable> repo;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 emf = mock(EntityManagerFactory.class);
 EntityManager em = mock(EntityManager.class);
 when(em.getEntityManagerFactory()).thenReturn(emf);
 repo = new EnhancedJpaRepository<Object, Serializable>(entityInfo, em);
 }

 // Test for getEntityManager()
 @Test
 public void testGetEntityManager() {
 EntityManager result = repo.getEntityManager();
 assertNotNull(result);
 }

 // Test for getJdbcTemplate()
 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate result = repo.getJdbcTemplate();
 assertNotNull(result);
 }

 // Test for getNamedParameterJdbcTemplate()
 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate result = repo.getNamedParameterJdbcTemplate();
 assertNotNull(result);
 }

 // Test for getDataSource() with EntityManager
 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 DataSource ds = mock(DataSource.class);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(cp);
 when(cp.getDataSource()).thenReturn(ds);
 when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 DataSource result = repo.getDataSource(em);
 assertEquals(ds, result);
 }

 // Test for constructor with null EntityManager
 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNullEntityManager() {
 new EnhancedJpaRepository<Object, Serializable>(entityInfo, null);
 }

 // Test for getDataSource() with null EntityManager
 @Test(expected = IllegalArgumentException.class)
 public void testGetDataSourceWithNullEntityManager() {
 repo.getDataSource(null);
 }

 // Test for getJdbcTemplate() with null NamedParameterJdbcTemplate
 @Test(expected = NullPointerException.class)
 public void testGetJdbcTemplateWithNullNamedParameterJdbcTemplate() {
 repo.namedParameterJdbcTemplate = null;
 repo.getJdbcTemplate();
 }

 // Test for getNamedParameterJdbcTemplate() with null NamedParameterJdbcTemplate
 @Test(expected = NullPointerException.class)
 public void testGetNamedParameterJdbcTemplateWithNullNamedParameterJdbcTemplate() {
 repo.namedParameterJdbcTemplate = null;
 repo.getNamedParameterJdbcTemplate();
 }

}