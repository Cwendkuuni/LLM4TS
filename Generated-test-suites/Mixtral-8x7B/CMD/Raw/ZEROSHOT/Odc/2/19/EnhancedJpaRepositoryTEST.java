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

private JpaEntityInformation<TestEntity, Long> jpaEntityInformation;

private EntityManagerFactory entityManagerFactory;

private EntityManager entityManager;

private DataSource dataSource;

private SessionFactoryImpl sessionFactory;

private ConnectionProvider connectionProvider;

@Before
public void setup() {
jpaEntityInformation = mock(JpaEntityInformation.class);
entityManagerFactory = mock(EntityManagerFactory.class);
entityManager = mock(EntityManager.class);
dataSource = mock(DataSource.class);
sessionFactory = mock(SessionFactoryImpl.class);
connectionProvider = mock(DatasourceConnectionProviderImpl.class);

when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(connectionProvider);
when(connectionProvider.getDataSource()).thenReturn(dataSource);

enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(jpaEntityInformation, entityManager) {
@Override
protected EntityManager createEntityManager() {
return entityManager;
}
};
}

@Test
public void testGetEntityManager() {
assertNotNull(enhancedJpaRepository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
assertNotNull(jdbcTemplate);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
assertNotNull(namedParameterJdbcTemplate);
}

private static class TestEntity {

}

}