import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

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
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<TestEntity, Long> jpaEntityInformation;

    @Mock
    private EntityManager entityManager;

    private DriverManagerDataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");

        enhancedJpaRepository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager) {
            {
                setDataSource(dataSource);
            }
        };
    }

    @After
    public void tearDown() {
        enhancedJpaRepository = null;
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
        ConnectionProvider connectionProvider = ((SessionFactoryImpl) entityManager.getEntityManagerFactory()
                .unwrap(SessionFactoryImpl.class)).getServiceRegistry()
                        .getService(ConnectionProvider.class);
        DataSource dataSourceMock = mock(DataSource.class);
        when(connectionProvider.getDataSource()).thenReturn(dataSourceMock);

        assertEquals(dataSourceMock, enhancedJpaRepository.getDataSource());
    }

    static class TestEntity {
    }
}