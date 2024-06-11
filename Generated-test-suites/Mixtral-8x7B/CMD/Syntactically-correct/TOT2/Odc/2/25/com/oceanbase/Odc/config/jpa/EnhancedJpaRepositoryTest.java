
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
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
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private ConnectionProvider connectionProvider;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(enhancedJpaRepository);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
        assertEquals(dataSource, jdbcTemplate.getDataSource());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
        assertEquals(dataSource, namedParameterJdbcTemplate.getJdbcOperations().getDataSource());
    }

    @Test
    public void testConstructorWithNullArguments() {
        assertThrows(IllegalArgumentException.class,
                () -> new EnhancedJpaRepository<>(null, entityManager));
        assertThrows(IllegalArgumentException.class,
                () -> new EnhancedJpaRepository<>(entityInformation, null));
    }

    @Test
    public void testGetDataSourceWithNullEntityManager() {
        EnhancedJpaRepository<Object, Serializable> enhancedJpaRepositoryWithNullEntityManager = new EnhancedJpaRepository<>(
                entityInformation, null);
        assertThrows(NullPointerException.class, () -> enhancedJpaRepositoryWithNullEntityManager.getDataSource(entityManager));
    }

    @Test
    public void testGetJdbcTemplateAndGetNamedParameterJdbcTemplateWithClosedEntityManager() {
        when(entityManager.isOpen()).thenReturn(false);
        assertThrows(IllegalStateException.class, () -> enhancedJpaRepository.getJdbcTemplate());
        assertThrows(IllegalStateException.class, () -> enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

}
