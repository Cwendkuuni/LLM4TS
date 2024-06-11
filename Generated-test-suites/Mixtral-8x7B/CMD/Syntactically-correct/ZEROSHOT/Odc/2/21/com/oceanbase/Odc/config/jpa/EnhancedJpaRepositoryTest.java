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
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<Object, Serializable> repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(entityInformation, entityManagerFactory, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sf = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        ConnectionProvider cp = sf.getServiceRegistry().getService(ConnectionProvider.class);
        DatasourceConnectionProviderImpl dcp = (DatasourceConnectionProviderImpl) cp;
        DataSource ds = dcp.getDataSource();

        assertSame(ds, repository.getDataSource(entityManager));
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        JdbcTemplate jdbcTemplate = (JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations();

        assertSame(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}