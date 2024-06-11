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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

	@Mock
	private JpaEntityInformation<Object, ?> entityInformation;

	@Mock
	private EntityManagerFactory entityManagerFactory;

	@Mock
	private EntityManager entityManager;

	@Mock
	private SessionFactoryImpl sessionFactory;

	@Mock
	private ConnectionProvider connectionProvider;

	@Mock
	private DataSource dataSource;

	@InjectMocks
	private EnhancedJpaRepository<Object, Serializable> repository;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
		when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
		when(connectionProvider.getDataSource()).thenReturn(dataSource);

		when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
	}

	@Test
	public void testEnhancedJpaRepositoryConstruction() {
		assertNotNull(repository);
	}

	@Test
	public void testGetEntityManager() {
		assertNotNull(repository.getEntityManager());
	}

	@Test
	public void testGetJdbcTemplate() {
		assertNotNull(repository.getJdbcTemplate());
	}

	@Test
	public void testGetNamedParameterJdbcTemplate() {
		assertNotNull(repository.getNamedParameterJdbcTemplate());
	}

	@Test
	public void testGetDataSource() {
		assertNotNull(repository.getDataSource(entityManager));
	}

	@Test
	public void testEdgeCaseGetDataSource() {
		when(entityManager.getEntityManagerFactory()).thenReturn(null);
		assertNotNull(repository.getDataSource(entityManager));
	}

	@Test(expected = NullPointerException.class)
	public void testExceptionGetDataSource() {
		when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(null);
		repository.getDataSource(entityManager);
	}
}