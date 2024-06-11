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
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

@Before
public void setup() {
JpaEntityInformation<Object, Serializable> entityInformation = mock(JpaEntityInformation.class);
EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
EntityManager entityManager = mock(EntityManager.class);
DataSource dataSource = mock(DataSource.class);

when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
.thenReturn(mock(SessionFactoryImpl.class, Answers.RETURNS_DEEP_STUBS));

when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
.getServiceRegistry())
.thenReturn(mock(org.hibernate.service.ServiceRegistry.class));

when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(new DatasourceConnectionProviderImpl(dataSource));

when(dataSource.getConnection()).thenReturn(mock(Connection.class));

enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager) {
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

@Test(expected = IllegalArgumentException.class)
public void testGetDataSource_EntityManagerNull() {
new EnhancedJpaRepository<Object, Serializable>(null, mock(EntityManager.class));
}

@Test(expected = IllegalArgumentException.class)
public void testGetDataSource_EntityManagerFactoryNull() {
EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
EntityManager entityManager = mock(EntityManager.class);

when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

when(entityManagerFactory.unwrap(SessionFactoryImpl.class))
.thenReturn(mock(SessionFactoryImpl.class, Answers.RETURNS_DEEP_STUBS));

when(entityManagerFactory.unwrap(SessionFactoryImpl.class)
.getServiceRegistry())
.thenReturn(mock(org.hibernate.service.ServiceRegistry.class));

when(entityManagerFactory.unwrap(SessionFactoryImpl.class)
.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(new DatasourceConnectionProviderImpl(mock(DataSource.class)));

new EnhancedJpaRepository<Object, Serializable>(null, entityManager);
}

@Test
public void testGetDataSource() {
DataSource result = enhancedJpaRepository.getDataSource(mock(EntityManager.class));
assertNotNull(result);
Mockito.verify(result).getConnection();
}
}