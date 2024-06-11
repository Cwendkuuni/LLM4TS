import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

 private EntityManager entityManager;
 private JpaEntityInformation<Object, Serializable> entityInformation;
 private EnhancedJpaRepository<Object, Serializable> repository;

 @Before
 public void setup() {
 entityManager = mock(EntityManager.class);
 entityInformation = mock(JpaEntityInformation.class);
 repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
 assertNotNull(namedParameterJdbcTemplate);
 }

 @Test
 public void testGetEntityManager() {
 EntityManager em = repository.getEntityManager();
 assertNotNull(em);
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
 assertNotNull(jdbcTemplate);
 }

 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 when(entityManager.getEntityManagerFactory()).thenReturn(sf);
 when(sf.getServiceRegistry()).thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
 when(sf.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(new DatasourceConnectionProviderImpl(getDriverManagerDataSource()));
 DriverManagerDataSource dataSource = getDriverManagerDataSource();
 assertNotNull(repository.getDataSource(entityManager));
 }

 private DriverManagerDataSource getDriverManagerDataSource() {
 DriverManagerDataSource dataSource = new DriverManagerDataSource();
 dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
 dataSource.setUrl("jdbc:mysql://localhost:3306/oceanbase");
 dataSource.setUsername("oceanbase");
 dataSource.setPassword("oceanbase");
 return dataSource;
 }
}