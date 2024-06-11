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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

@Mock
private JpaEntityInformation<Object, Serializable> entityInformation;

@Mock
private EntityManagerFactory emf;

@Mock
private EntityManager em;

private EnhancedJpaRepository<Object, Serializable> repository;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
when(emf.createEntityManager()).thenReturn(em);
when(entityInformation.getEntityClass()).thenReturn(Object.class);
repository = new EnhancedJpaRepository<>(entityInformation, emf);
}

// Test for getEntityManager()
@Test
public void testGetEntityManager() {
assertNotNull(repository.getEntityManager());
}

// Test for getJdbcTemplate()
@Test
public void testGetJdbcTemplate() {
assertNotNull(repository.getJdbcTemplate());
}

// Test for getNamedParameterJdbcTemplate()
@Test
public void testGetNamedParameterJdbcTemplate() {
assertNotNull(repository.getNamedParameterJdbcTemplate());
}

// Edge case: getDataSource() with null EntityManager
@Test(expected = IllegalArgumentException.class)
public void testGetDataSourceWithNullEntityManager() {
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, null);
repo.getDataSource(null);
}

// Edge case: getDataSource() with unwrapped SessionFactoryImpl as null
@Test(expected = IllegalStateException.class)
public void testGetDataSourceWithNullSessionFactory() {
when(em.getEntityManagerFactory()).thenReturn(emf);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(null);
repository = new EnhancedJpaRepository<>(entityInformation, em);
repository.getDataSource(em);
}

// Edge case: getDataSource() with DatasourceConnectionProviderImpl as null
@Test(expected = IllegalStateException.class)
public void testGetDataSourceWithNullConnectionProvider() {
SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
when(em.getEntityManagerFactory()).thenReturn(emf);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
when(sf.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(null);
repository = new EnhancedJpaRepository<>(entityInformation, em);
repository.getDataSource(em);
}
}