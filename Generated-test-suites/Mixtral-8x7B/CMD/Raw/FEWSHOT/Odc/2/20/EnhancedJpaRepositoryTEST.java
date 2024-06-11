import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> repository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 ReflectionTestUtils.setField(repository, "entityManager", entityManager);
 }

 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 DataSource ds = mock(DataSource.class);

 when(entityManager.getEntityManagerFactory()).thenReturn(mock(Object.class));
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(sf);
 when(sf.getServiceRegistry()).thenReturn(mock(Object.class));
 when(sf.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(cp);
 when(cp.getDataSource()).thenReturn(ds);

 DataSource result = repository.getDataSource(entityManager);

 verify(entityManager, times(1)).getEntityManagerFactory();
 verify(entityManager.getEntityManagerFactory(), times(1)).unwrap(SessionFactoryImpl.class);
 verify(sf, times(1)).getServiceRegistry();
 verify(sf.getServiceRegistry(), times(1)).getService(ConnectionProvider.class);
 verify(cp, times(1)).getDataSource();
 assertEquals(ds, result);
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, repository.getEntityManager());
 verify(repository, times(1)).entityManager;
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

 when(repository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

 assertEquals(jdbcTemplate, repository.getJdbcTemplate());
 verify(repository, times(1)).getNamedParameterJdbcTemplate();
 verify(namedParameterJdbcTemplate, times(1)).getJdbcOperations();
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

 when(repository.getDataSource(entityManager)).thenReturn(mock(DataSource.class));

 assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 verify(repository, times(1)).getDataSource(entityManager);
 verify(repository, times(1)).namedParameterJdbcTemplate;
 }

}