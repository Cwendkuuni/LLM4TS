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
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

 private EnhancedJpaRepository<Object, Serializable> repository;

 private EntityManagerFactory entityManagerFactory;

 private JpaEntityInformation<Object, Serializable> entityInformation;

 private EntityManager entityManager;

 private DataSource dataSource;

 @Before
 public void setup() {
 entityManagerFactory = mock(EntityManagerFactory.class);
 entityManager = mock(EntityManager.class);
 dataSource = mock(DataSource.class);

 when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
 when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

 entityInformation = mock(JpaEntityInformation.class);

 repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager) {
 @PersistenceUnit
 private void setEntityManagerFactory(EntityManagerFactory emf) {
 ReflectionTestUtils.setField(this, "entityManagerFactory", emf);
 }
 };

 ReflectionTestUtils.setField(repository, "entityManagerFactory", entityManagerFactory);
 ReflectionTestUtils.setField(repository, "entityManager", entityManager);

 SimpleJpaRepository.setEntityInformation(entityInformation);
 }

 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);

 ConnectionProvider cp = mock(ConnectionProvider.class);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

 DatasourceConnectionProviderImpl dcp = new DatasourceConnectionProviderImpl();
 dcp.setDataSource(dataSource);
 ReflectionTestUtils.setField(cp, "delegate", dcp);

 DataSource ds = repository.getDataSource(entityManager);
 assertEquals(dataSource, ds);
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate npt = repository.getNamedParameterJdbcTemplate();
 JdbcTemplate jt = (JdbcTemplate) npt.getJdbcOperations();
 assertEquals(jt, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate npt = repository.getNamedParameterJdbcTemplate();
 assertEquals(npt, repository.getNamedParameterJdbcTemplate());
 }
}