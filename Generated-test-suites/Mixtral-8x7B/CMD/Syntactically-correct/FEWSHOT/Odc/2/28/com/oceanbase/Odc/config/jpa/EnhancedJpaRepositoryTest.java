import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<Object, Long> enhancedJpaRepository;

private JpaEntityInformation<Object, Long> jpaEntityInformation;

private EntityManagerFactory entityManagerFactory;

private EntityManager entityManager;

private DataSource dataSource;

private ConnectionProvider connectionProvider;

private SessionFactoryImpl sessionFactoryImpl;

@Before
public void setup() {
jpaEntityInformation = mock(JpaEntityInformation.class);
entityManagerFactory = mock(EntityManagerFactory.class);
entityManager = mock(EntityManager.class);
dataSource = mock(DataSource.class);
connectionProvider = mock(ConnectionProvider.class);
sessionFactoryImpl = mock(SessionFactoryImpl.class);

when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryImpl);
when(sessionFactoryImpl.getServiceRegistry()
.getService(ConnectionProvider.class))
.thenReturn(connectionProvider);
when(connectionProvider.getDataSource()).thenReturn(dataSource);

enhancedJpaRepository = new EnhancedJpaRepository<Object, Long>(jpaEntityInformation, entityManager) {
};
}

@Test
public void testGetDataSource() {
assertEquals(dataSource, enhancedJpaRepository.getDataSource(entityManager));
}

@Test
public void testGetEntityManager() {
assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
}
}