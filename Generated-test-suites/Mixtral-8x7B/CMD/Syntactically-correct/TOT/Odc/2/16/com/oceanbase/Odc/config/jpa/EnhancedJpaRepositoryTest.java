import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private static class DummyEntity {
    }

    @Test
    public void testConstructor() {
        JpaEntityInformation<DummyEntity, Serializable> entityInformation = Mockito.mock(JpaEntityInformation.class);
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        Assert.assertNotNull(repository);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        DataSource dataSource = repository.getDataSource(entityManager);
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        Assert.assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        Assert.assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        Assert.assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testNamedParameterJdbcTemplate_execute() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        String sql = "SELECT * FROM dual";
        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class)
            .getConnection())
            .thenReturn(Mockito.mock(Connection.class));

        namedParameterJdbcTemplate.execute(sql, (SqlParameterSource) null, (ResultSetExtractor<Object>) null);

        Mockito.verify(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class))
            .getConnection();
    }

    @Test
    public void testNamedParameterJdbcTemplate_query() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class)
            .getConnection())
            .thenReturn(Mockito.mock(Connection.class));

        namedParameterJdbcTemplate.query("SELECT * FROM dual", (SqlParameterSource) null, (ResultSetExtractor<Object>) null);

        Mockito.verify(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class))
            .getConnection();
    }

    @Test
    public void testNamedParameterJdbcTemplate_queryForObject() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class)
            .getConnection())
            .thenReturn(Mockito.mock(Connection.class));

        namedParameterJdbcTemplate.queryForObject("SELECT * FROM dual", (SqlParameterSource) null, Object.class);

        Mockito.verify(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class))
            .getConnection();
    }

    @Test
    public void testNamedParameterJdbcTemplate_queryForList() {
        EntityManager entityManager = Mockito.mock(EntityManager.class);
        EnhancedJpaRepository<DummyEntity, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        Mockito.when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class)
            .getConnection())
            .thenReturn(Mockito.mock(Connection.class));

        namedParameterJdbcTemplate.queryForList("SELECT * FROM dual", (SqlParameterSource) null, Object.class);

        Mockito.verify(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class))
            .getConnection();
    }

}