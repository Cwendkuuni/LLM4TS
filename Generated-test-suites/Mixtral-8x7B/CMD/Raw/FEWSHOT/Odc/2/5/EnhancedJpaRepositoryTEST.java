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
private EntityManager entityManager;
private DataSource dataSource;
private JpaEntityInformation<Object, Serializable> jpaEntityInformation;
private EnhancedJpaRepository<Object, Serializable> repository;

@Before
public void setUp() {
entityManagerFactory = mock(EntityManagerFactory.class);
entityManager = mock(EntityManager.class);
dataSource = mock(DataSource.class);
jpaEntityInformation = mock(JpaEntityInformation.class);

when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
when(entityManagerFactory.unwrap(SessionFactoryImpl.class))
.thenReturn(mock(SessionFactoryImpl.class));
when(entityManagerFactory.unwrap(SessionFactoryImpl.class))
.thenReturn(mock(SessionFactoryImpl.class)).getServiceRegistry()
.thenReturn(mock(org.hibernate.service.ServiceRegistry.class))
.getService(ConnectionProvider.class)
.thenReturn(new DatasourceConnectionProviderImpl(dataSource));

repository = new EnhancedJpaRepository<Object, Serializable>(jpaEntityInformation, entityManager);
}

@Test
public void testGetDataSource() {
assertEquals(dataSource, repository.getDataSource(entityManager));
}

@Test
public void testGetEntityManager() {
assertEquals(entityManager, repository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
}

}