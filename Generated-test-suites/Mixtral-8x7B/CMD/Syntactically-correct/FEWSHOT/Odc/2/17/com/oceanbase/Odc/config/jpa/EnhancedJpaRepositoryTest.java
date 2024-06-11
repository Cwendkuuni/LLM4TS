import static org.junit.Assert.assertNotNull;
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
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class EnhancedJpaRepositoryTest {

@PersistenceUnit
private EntityManagerFactory entityManagerFactory;

private DataSource dataSource = mock(DataSource.class);

private JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);

@Test
public void testEnhancedJpaRepositoryConstructor() {
EntityManager entityManager = mock(EntityManager.class);
when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInfo, entityManager);

assertNotNull(repo);
}

@Test
public void testGetDataSource() {
SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
when(cp.getDataSource()).thenReturn(dataSource);

EntityManager entityManager = mock(EntityManager.class);
when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

DataSource result = new EnhancedJpaRepository<>(entityInfo, entityManager).getDataSource(entityManager);

assertNotNull(result);
assertEquals(dataSource, result);
}

@Test
public void testGetEntityManager() {
EntityManager result = new EnhancedJpaRepository<>(entityInfo, mock(EntityManager.class)).getEntityManager();

assertNotNull(result);
}

@Test
public void testGetJdbcTemplate() {
NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
JdbcTemplate result = new EnhancedJpaRepository<>(entityInfo, mock(EntityManager.class))
.getJdbcTemplate();

Mockito.verify(namedParameterJdbcTemplate).getJdbcOperations();
assertEquals(result.getClass(), namedParameterJdbcTemplate.getJdbcOperations().getClass());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate result = new EnhancedJpaRepository<>(entityInfo, mock(EntityManager.class))
.getNamedParameterJdbcTemplate();

assertNotNull(result);
}
}