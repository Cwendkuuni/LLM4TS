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
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

@ContextConfiguration
public class EnhancedJpaRepositoryTest {

 @PersistenceUnit
 private EntityManagerFactory emf;

 @Test
 public void testGetDataSource() {
 EntityManager entityManager = mock(EntityManager.class);
 when(entityManager.getEntityManagerFactory()).thenReturn(emf);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, entityManager) {
 };
 DatasourceConnectionProviderImpl datasourceConnectionProviderImpl = mock(DatasourceConnectionProviderImpl.class);
 when(datasourceConnectionProviderImpl.getDataSource()).thenReturn(mock(DataSource.class));
 SessionFactoryImpl sessionFactoryImpl = mock(SessionFactoryImpl.class);
 when(sessionFactoryImpl.getServiceRegistry()).thenReturn(mock(ServiceRegistry.class));
 when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class))
 .thenReturn(datasourceConnectionProviderImpl);
 repo.getNamedParameterJdbcTemplate();
 }

 @Test
 public void testGetEntityManager() {
 EntityManager entityManager = mock(EntityManager.class);
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, entityManager) {
 };
 assertEquals(entityManager, repo.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, mock(EntityManager.class)) {
 };
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
 when(repo.getNamedParameterJdbcTemplate().getJdbcOperations()).thenReturn(jdbcTemplate);
 assertEquals(jdbcTemplate, repo.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, mock(EntityManager.class)) {
 };
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 when(repo.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
 assertEquals(namedParameterJdbcTemplate, repo.getNamedParameterJdbcTemplate());
 }
}