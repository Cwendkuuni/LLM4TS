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
import org.mockito.ArgumentMatchers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

private EntityManagerFactory entityManagerFactory;

private JpaEntityInformation<TestEntity, Long> jpaEntityInformation;

private EntityManager entityManager;

private DataSource dataSource;

@Before
public void setUp() {
entityManagerFactory = mock(EntityManagerFactory.class);
jpaEntityInformation = mock(JpaEntityInformation.class);
entityManager = mock(EntityManager.class);
dataSource = mock(DataSource.class);

when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
when(jpaEntityInformation.getEntityType()).thenReturn(TestEntity.class);
when(jpaEntityInformation.getIdType()).thenReturn(Long.class);
when(getDataSource(entityManager)).thenReturn(dataSource);

enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(jpaEntityInformation,
entityManagerFactory) {
};
}

private ConnectionProvider getDataSource(EntityManager entityManager) {
SessionFactoryImpl sf = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
return ((DatasourceConnectionProviderImpl) sf.getServiceRegistry()
.getService(ConnectionProvider.class))
.getDataSource();
}

@Test
public void testConstructor() {
assertNotNull(enhancedJpaRepository);
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

static class TestEntity {

}

}