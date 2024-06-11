import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.After;
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
	private JpaEntityInformation<Object, Serializable> entityInfo;
	@Mock
	private EntityManagerFactory entityManagerFactory;
	@Mock
	private EntityManager entityManager;
	@Mock
	private DataSource dataSource;

	private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
		when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
		when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
		.thenReturn(new SessionFactoryImpl());
		when(new SessionFactoryImpl().getServiceRegistry().getService(ConnectionProvider.class))
		.thenReturn(new DatasourceConnectionProviderImpl(dataSource));
		enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(entityInfo, entityManager);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetEntityManager() {
		assertEquals(entityManager, enhancedJpaRepository.getEntityManager());
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

	@Test
	public void testGetDataSource() {
		DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
		assertSame(this.dataSource, dataSource);
	}

}