import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<Object, Serializable> repository;
 private JpaEntityInformation<Object, Serializable> entityInformation;
 private EntityManagerFactory entityManagerFactory;
 private EntityManager entityManager;
 private DataSource dataSource;

 @Before
 public void setup() {
 entityInformation = mock(JpaEntityInformation.class);
 entityManagerFactory = mock(EntityManagerFactory.class);
 entityManager = mock(EntityManager.class);
 dataSource = mock(DataSource.class);

 repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager) {
 @PersistenceUnit
 public void setEntityManagerFactory(EntityManagerFactory emf) {
 this.entityManagerFactory = emf;
 }
 };

 ReflectionTestUtils.setField(repository, "entityManagerFactory", entityManagerFactory);
 ReflectionTestUtils.setField(repository, "entityManager", entityManager);

 when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
 .thenReturn(mock(SessionFactoryImpl.class));
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
 .getServiceRegistry())
 .thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
 .getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(new DatasourceConnectionProviderImpl(dataSource));
 when(entityManagerFactory.createEntityManager().getDataSource()).thenReturn(dataSource);
 }

 @Test
 public void testGetDataSource() {
 DataSource result = repository.getDataSource(entityManager);
 assertEquals(dataSource, result);
 }

 @Test
 public void testGetEntityManager() {
 EntityManager result = repository.getEntityManager();
 assertEquals(entityManager, result);
 }

 @Test
 public void testGetJdbcTemplate() {
 JdbcTemplate result = repository.getJdbcTemplate();
 assertEquals(new JdbcTemplate(dataSource), result);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
 assertEquals(new NamedParameterJdbcTemplate(dataSource), result);
 }
}