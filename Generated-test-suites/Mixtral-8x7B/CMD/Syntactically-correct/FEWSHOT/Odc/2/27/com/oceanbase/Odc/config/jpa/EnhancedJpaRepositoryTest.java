import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
 private EntityManagerFactory entityManagerFactory;

 @Mock
 private EntityManager entityManager;

 @Mock
 private DatasourceConnectionProviderImpl datasourceConnectionProvider;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> repositoryUnderTest;

 @Test
 public void testConstructor() {
 when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(mock(SessionFactoryImpl.class));
 when(entityManager.getEntityManagerFactory().getServiceRegistry())
 .thenReturn(mock(SessionFactoryImpl.class).getServiceRegistry());
 when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(datasourceConnectionProvider);
 when(datasourceConnectionProvider.getDataSource()).thenReturn(mock(DataSource.class));

 repositoryUnderTest = new EnhancedJpaRepository<>(entityInformation, entityManager);

 assertNotNull(repositoryUnderTest.getEntityManager());
 assertNotNull(repositoryUnderTest.getJdbcTemplate());
 assertNotNull(repositoryUnderTest.getNamedParameterJdbcTemplate());
 }

 @Test
 public void testGetDataSource() {
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(mock(SessionFactoryImpl.class));
 when(entityManager.getEntityManagerFactory().getServiceRegistry())
 .thenReturn(mock(SessionFactoryImpl.class).getServiceRegistry());
 when(entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(datasourceConnectionProvider);
 when(datasourceConnectionProvider.getDataSource()).thenReturn(mock(DataSource.class));

 DataSource result = repositoryUnderTest.getDataSource(entityManager);

 assertNotNull(result);
 }

 @Test
 public void testGetEntityManager() {
 assertNotNull(repositoryUnderTest.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 assertNotNull(repositoryUnderTest.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertNotNull(repositoryUnderTest.getNamedParameterJdbcTemplate());
 }
}