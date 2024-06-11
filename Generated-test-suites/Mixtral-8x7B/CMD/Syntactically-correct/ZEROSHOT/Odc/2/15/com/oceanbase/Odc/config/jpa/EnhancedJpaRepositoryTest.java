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
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @After
    public void teardown() {
        verifyNoMoreInteractions(entityManagerFactory, entityManager, dataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, repository.getEntityManager());
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
        DataSource dataSource = repository.getDataSource(entityManager);
        assertSame(dataSource, repository.getNamedParameterJdbcTemplate().getJdbcOperations().getDataSource());
    }

    @Test
    public void testGetDataSource_WhenEntityManagerIsNull() {
        repository.setEntityManager(null);
        assertNull(repository.getDataSource(null));
    }
}