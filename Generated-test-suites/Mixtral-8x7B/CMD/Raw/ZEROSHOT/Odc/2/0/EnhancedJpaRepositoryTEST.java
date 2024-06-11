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
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> repository;

    @Before
    public void setup() {
        EntityManagerFactory entityManagerFactory = mock(EntityManagerFactory.class);
        EntityManager entityManager = mock(EntityManager.class);
        DataSource dataSource = mock(DataSource.class);
        JpaEntityInformation<TestEntity, Long> entityInformation = mock(JpaEntityInformation.class);
        ConnectionProvider connectionProvider = mock(ConnectionProvider.class, Answers.RETURNS_DEEP_STUBS);
        SessionFactoryImpl sessionFactoryImpl = mock(SessionFactoryImpl.class);

        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactoryImpl);
        when(sessionFactoryImpl.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManager.find(TestEntity.class, 1L)).thenReturn(new TestEntity());

        repository = new EnhancedJpaRepository<TestEntity, Long>(entityInformation, entityManager);
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
    public void testFind() {
        TestEntity entity = repository.find(1L);
        assertNotNull(entity);
    }

    private static class TestEntity {
    }
}