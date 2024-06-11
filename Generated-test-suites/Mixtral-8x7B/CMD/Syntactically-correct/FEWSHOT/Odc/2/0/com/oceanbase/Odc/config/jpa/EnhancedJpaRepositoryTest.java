import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);

 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
 DataSource dataSource = mock(DataSource.class);

 when(entityManager.getEntityManagerFactory()).thenReturn(mock(SessionFactory.class));
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 when(sf.getServiceRegistry()).thenReturn(mock(ServiceRegistry.class));
 when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);

 assertEquals(dataSource, repository.getDataSource(entityManager));
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

 assertEquals(jdbcTemplate, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

 when(new NamedParameterJdbcTemplate(repository.getDataSource(entityManager))).thenReturn(namedParameterJdbcTemplate);

 assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 }
}