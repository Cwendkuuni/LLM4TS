import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

private EntityManagerFactory entityManagerFactory;

private JpaEntityInformation<TestEntity, Long> entityInformation;

private EntityManager entityManager;

private DataSource dataSource;

private SessionFactoryImpl sessionFactory;

private ConnectionProvider connectionProvider;

@Before
public void setup() {
entityManagerFactory = mock(EntityManagerFactory.class);
entityInformation = mock(JpaEntityInformation.class);
entityManager = mock(EntityManager.class);
dataSource = mock(DataSource.class);
sessionFactory = mock(SessionFactoryImpl.class);
connectionProvider = mock(DatasourceConnectionProviderImpl.class);

when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(connectionProvider);
when(connectionProvider.getDataSource()).thenReturn(dataSource);

enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(entityInformation, entityManager) {
@Override
protected EntityManager entityManager() {
return entityManager;
}
};
}

@Test
public void testGetEntityManager() {
EntityManager result = enhancedJpaRepository.getEntityManager();
assertNotNull(result);
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
assertNotNull(result);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
assertNotNull(result);
}

@Test
public void testGetDataSource() {
DataSource result = enhancedJpaRepository.getDataSource(entityManager);
assertNotNull(result);
}

static class TestEntity {

}

}