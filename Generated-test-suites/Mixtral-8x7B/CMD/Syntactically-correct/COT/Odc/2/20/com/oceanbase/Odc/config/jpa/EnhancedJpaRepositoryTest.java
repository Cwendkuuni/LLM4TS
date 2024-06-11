import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
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
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<TestEntity, Long> entityInformation;

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

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        enhancedJpaRepository = new EnhancedJpaRepository<TestEntity, Long>(entityInformation, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertNotNull(enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate_WithValidParameters_ShouldReturnNamedParameterJdbcTemplate() {
        TestEntity testEntity = new TestEntity();
        String sql = "SELECT * FROM test_entity WHERE name = :name";
        namedParameterJdbcTemplate.getJdbcOperations().queryForObject(sql, testEntity);
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNamedParameterJdbcTemplate_WithNullEntityManager_ShouldThrowIllegalArgumentException() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, null);
        enhancedJpaRepository.getNamedParameterJdbcTemplate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_WithNullEntityManager_ShouldThrowIllegalArgumentException() {
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, null);
        enhancedJpaRepository.getDataSource(entityManager);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetJdbcOperations_WithJdbcTemplate_ShouldThrowUnsupportedOperationException() {
        JdbcTemplate jdbcTemplate = enhancedJpaRepository.getJdbcTemplate();
        jdbcTemplate.queryForObject("SELECT * FROM test_entity WHERE name = ?", new Object[] { "test" }, Long.class);
    }

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.entityManagerFactory = emf;
    }

    public static class TestEntity {
        private Long id;

        private String name;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}