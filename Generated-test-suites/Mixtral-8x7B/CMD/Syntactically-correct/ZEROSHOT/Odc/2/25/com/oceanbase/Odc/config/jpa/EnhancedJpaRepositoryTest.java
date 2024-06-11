import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManager entityManager;

 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @PersistenceContext
 private EntityManager entityManagerMock;

 private DataSource dataSourceMock;

 private ConnectionProvider connectionProviderMock;

 private SessionFactoryImpl sessionFactoryMock;

 private NamedParameterJdbcTemplate namedParameterJdbcTemplateMock;

 private JdbcTemplate jdbcTemplateMock;

 @Before
 public void setup() {
 dataSourceMock = mock(DataSource.class);
 connectionProviderMock = mock(ConnectionProvider.class);
 sessionFactoryMock = mock(SessionFactoryImpl.class);
 namedParameterJdbcTemplateMock = mock(NamedParameterJdbcTemplate.class);
 jdbcTemplateMock = mock(JdbcTemplate.class);
 when(entityManagerMock.getEntityManagerFactory()).thenReturn(new org.hibernate.SessionFactory());
 when(entityManagerMock.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(sessionFactoryMock);
 when(sessionFactoryMock.getServiceRegistry()).thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
 when(sessionFactoryMock.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(connectionProviderMock);
 when(connectionProviderMock.getDataSource()).thenReturn(dataSourceMock);
 when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactoryMock.getSessionFactory());
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(sessionFactoryMock);
 when(sessionFactoryMock.getServiceRegistry()).thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
 when(sessionFactoryMock.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(connectionProviderMock);
 when(connectionProviderMock.getDataSource()).thenReturn(dataSourceMock);
 when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplateMock);
 when(namedParameterJdbcTemplateMock.getJdbcOperations()).thenReturn(jdbcTemplateMock);
 }

 @After
 public void tearDown() {
 entityManagerMock = null;
 dataSourceMock = null;
 connectionProviderMock = null;
 sessionFactoryMock = null;
 namedParameterJdbcTemplateMock = null;
 jdbcTemplateMock = null;
 }

 @Test
 public void testEnhancedJpaRepository() {
 assertNotNull(enhancedJpaRepository);
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 assertEquals(jdbcTemplateMock, enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 assertEquals(namedParameterJdbcTemplateMock, enhancedJpaRepository.getNamedParameterJdbcTemplate());
 }

}