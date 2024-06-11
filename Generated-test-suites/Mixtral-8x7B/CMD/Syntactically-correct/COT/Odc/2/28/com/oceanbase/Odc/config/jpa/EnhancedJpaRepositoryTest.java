import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

 private JpaEntityInformation<Object, Serializable> entityInformation;
 private EntityManager entityManager;
 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @Before
 public void setup() {
 entityInformation = mock(JpaEntityInformation.class);
 entityManager = mock(EntityManager.class);
 enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);
 }

 // Test for getEntityManager()
 @Test
 public void testGetEntityManager() {
 assertNotNull(enhancedJpaRepository.getEntityManager());
 }

 // Test for getJdbcTemplate()
 @Test
 public void testGetJdbcTemplate() {
 assertNotNull(enhancedJpaRepository.getJdbcTemplate());
 }

 // Test for getNamedParameterJdbcTemplate()
 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
 }

 // Test for getDataSource() with valid EntityManager
 @Test
 public void testGetDataSourceWithValidEntityManager() {
 ServiceRegistry serviceRegistry = mock(ServiceRegistry.class);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(new SessionFactoryImpl(serviceRegistry));
 DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
 when(serviceRegistry.getService(ConnectionProvider.class))
 .thenReturn(connectionProvider);
 DataSource dataSource = mock(DataSource.class);
 when(connectionProvider.getDataSource()).thenReturn(dataSource);
 assertNotNull(enhancedJpaRepository.getDataSource(entityManager));
 }

 // Test for getDataSource() with null EntityManager
 @Test(expected = NullPointerException.class)
 public void testGetDataSourceWithNullEntityManager() {
 enhancedJpaRepository.getDataSource(null);
 }
}