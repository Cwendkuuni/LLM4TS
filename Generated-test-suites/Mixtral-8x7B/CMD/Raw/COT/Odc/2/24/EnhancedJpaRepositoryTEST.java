import static org.junit.Assert.assertEquals;
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
private EntityManagerFactory entityManagerFactory;

@Mock
private EntityManager entityManager;

@Mock
private DataSource dataSource;

private EnhancedJpaRepository<Object, Serializable> repository;

@PersistenceUnit
public void setEntityManagerFactory(EntityManagerFactory emf) {
this.entityManagerFactory = emf;
}

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
when(entityManager.getDataSource()).thenReturn(dataSource);
repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager);
}

@Test
public void testConstructor() {
assertNotNull(repository);
}

@Test
public void testGetEntityManager() {
assertNotNull(repository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
assertNotNull(jdbcTemplate);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
assertNotNull(namedParameterJdbcTemplate);
}

@Test
public void testGetDataSource() {
DataSource ds = repository.getDataSource(entityManager);
assertEquals(dataSource, ds);
}

@Test(expected = IllegalArgumentException.class)
public void testGetDataSourceWithNullEntityManager() {
repository.getDataSource(null);
}
}