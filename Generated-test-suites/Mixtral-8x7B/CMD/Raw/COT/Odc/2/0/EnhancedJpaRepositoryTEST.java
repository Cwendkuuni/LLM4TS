import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

private JpaEntityInformation<Object, Serializable> jpaEntityInformation;

private EntityManagerFactory entityManagerFactory;

private EntityManager entityManager;

private DataSource dataSource;

@Before
public void setup() {
jpaEntityInformation = mock(JpaEntityInformation.class);
entityManagerFactory = mock(EntityManagerFactory.class);
entityManager = mock(EntityManager.class);
dataSource = mock(DataSource.class);

when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
when(sessionFactory.getServiceRegistry()).thenReturn(mock(org.hibernate.service.ServiceRegistry.class));

ConnectionProvider connectionProvider = mock(ConnectionProvider.class);
when(connectionProvider.getAnyConnection()).thenAnswer(new Answer<Object>() {
@Override
public Object answer(InvocationOnMock invocation) throws Throwable {
return dataSource.getConnection();
}
});

when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);

when(entityManager.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);

enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(jpaEntityInformation, entityManager) {
@Override
public DataSource getDataSource(EntityManager entityManager) {
return EnhancedJpaRepositoryTest.this.dataSource;
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

@Test(expected = IllegalArgumentException.class)
public void testGetDataSource_ThrowsExceptionOnNullEntityManager() {
EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(
jpaEntityInformation, null) {
@Override
public DataSource getDataSource(EntityManager entityManager) {
return null;
}
};
enhancedJpaRepository.getDataSource(null);
}

@Test
public void testGetDataSource() {
when(dataSource.getConnection()).thenReturn(mock(java.sql.Connection.class));

DataSource dataSourceSpy = mock(DataSource.class);
when(dataSourceSpy.getConnection()).thenReturn(mock(java.sql.Connection.class));

enhancedJpaRepository.setDataSource(dataSourceSpy);

assertEquals(dataSourceSpy, enhancedJpaRepository.getDataSource(entityManager));
}
}