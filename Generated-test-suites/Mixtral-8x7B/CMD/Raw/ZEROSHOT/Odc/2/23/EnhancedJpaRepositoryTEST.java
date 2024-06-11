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
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Serializable> repository;
    private JpaEntityInformation<TestEntity, Serializable> entityInformation;
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private DataSource dataSource;

    @Before
    public void setup() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManagerFactory = mock(EntityManagerFactory.class);
        entityManager = mock(EntityManager.class);
        dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class))
                .thenReturn(new SessionFactoryImpl(entityManagerFactory));
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl(entityManagerFactory));

        ConnectionProvider connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)
                .getServiceRegistry()
                .getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
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
        DataSource dataSourceSpy = mock(DataSource.class);
        when(dataSourceSpy.getConnection()).thenReturn(mock(Connection.class));
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)
                .getServiceRegistry()
                .getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(dataSourceSpy));

        assertEquals(dataSource, repository.getDataSource(entityManager));
    }

    static class TestEntity {
    }
}