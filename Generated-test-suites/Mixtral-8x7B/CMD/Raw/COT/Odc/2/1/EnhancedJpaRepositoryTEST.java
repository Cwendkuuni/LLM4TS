import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repo;

    @Mock
    private JpaEntityInformation<TestEntity, Long> entityInfo;

    @Mock
    private EntityManager entityManager;

    private DriverManagerDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");

        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
            .thenReturn(new SessionFactoryImpl(null));

        repo = new EnhancedJpaRepository<>(entityInfo, entityManager) {
            @Override
            public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
                return new NamedParameterJdbcTemplate(dataSource);
            }
        };
    }

    // Test for getEntityManager()
    @Test
    public void testGetEntityManager() {
        assertNotNull(repo.getEntityManager());
    }

    // Test for getJdbcTemplate()
    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(repo.getJdbcTemplate());
    }

    // Test for getNamedParameterJdbcTemplate()
    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(repo.getNamedParameterJdbcTemplate());
    }

    // Edge cases and exception handling scenarios

    // Test when entityInformation is null
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEntityInformation() {
        new EnhancedJpaRepository<>(null, entityManager);
    }

    // Test when entityManager is null
    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEntityManager() {
        new EnhancedJpaRepository<>(entityInfo, null);
    }

    // Test when entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class) returns null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullSessionFactory() {
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
            .thenReturn(null);

        new EnhancedJpaRepository<>(entityInfo, entityManager);
    }

    // Test when ConnectionProvider.getDataSource() returns null
    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullDataSource() {
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
            .thenReturn(new SessionFactoryImpl(null));

        when(((DatasourceConnectionProviderImpl)
            entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class))
            .getDataSource()).thenReturn(null);

        new EnhancedJpaRepository<>(entityInfo, entityManager);
    }

    // Test when JdbcTemplate constructor throws an exception
    @Test(expected = RuntimeException.class)
    public void testGetJdbcTemplateWithException() {
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
            .thenReturn(new SessionFactoryImpl(null));

        when(((DatasourceConnectionProviderImpl)
            entityManager.getEntityManagerFactory().getServiceRegistry().getService(ConnectionProvider.class))
            .getDataSource()).thenReturn(dataSource);

        doThrow(new RuntimeException()).when(repo).getNamedParameterJdbcTemplate();

        repo.getJdbcTemplate();
    }

    // Test entity
    private static class TestEntity {
    }
}