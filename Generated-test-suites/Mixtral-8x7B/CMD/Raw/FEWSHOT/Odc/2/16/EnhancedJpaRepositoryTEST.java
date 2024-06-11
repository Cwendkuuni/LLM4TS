import static org.junit.Assert.assertEquals;
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
import org.mockito.ArgumentMatchers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

private EntityManagerFactory entityManagerFactory;
private DataSource dataSource;
private JpaEntityInformation<Object, Serializable> jpaEntityInformation;
private EnhancedJpaRepository<Object, Serializable> repository;

@Before
public void setup() {
entityManagerFactory = mock(EntityManagerFactory.class);
dataSource = mock(DataSource.class);
jpaEntityInformation = mock(JpaEntityInformation.class);

EntityManager entityManager = mock(EntityManager.class);
when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);

repository = new EnhancedJpaRepository<Object, Serializable>(jpaEntityInformation, entityManager) {
@Override
public EntityManager getEntityManager() {
return entityManager;
}
};
}

@Test
public void testGetDataSource() {
SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
when(cp.getDataSource()).thenReturn(dataSource);

when(repository.getEntityManager().getEntityManagerFactory()).thenReturn(entityManagerFactory);

assertEquals(dataSource, repository.getDataSource());
}

@Test
public void testGetJdbcTemplate() {
NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

when(repository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);

assertEquals(jdbcTemplate, repository.getJdbcTemplate());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

when(repository.getDataSource()).thenReturn(dataSource);
when(repository.getEntityManager()).thenReturn(mock(EntityManager.class));

assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
}
}