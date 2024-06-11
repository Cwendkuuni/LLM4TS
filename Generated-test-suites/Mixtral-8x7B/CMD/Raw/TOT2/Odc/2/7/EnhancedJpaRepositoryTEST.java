import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.nullArgumentException;

import java.io.Serializable;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

@EnableTransactionManagement
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private ConnectionProvider connectionProvider;

    @Mock
    private SessionFactoryImpl sessionFactory;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    private EmbeddedDatabase database;

    private LocalEntityManagerFactoryBean emf;

    private PlatformTransactionManager transactionManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        database = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build();
        emf = new LocalEntityManagerFactoryBean();
        emf.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emf.setPackagesToScan("com.oceanbase.odc.config.jpa");
        emf.setDataSource(new DriverManagerDataSource(database.getJdbcUrl(), database.getUsername(), database.getPassword()));
        emf.afterPropertiesSet();
        entityManager = emf.getObject().createEntityManager();
        transactionManager = new JpaTransactionManager(emf.getObject());
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager) {
            @Override
            public DataSource getDataSource(EntityManager entityManager) {
                SessionFactoryImpl sf = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
                return ((DatasourceConnectionProviderImpl) sf.getServiceRegistry().getService(ConnectionProvider.class))
                        .getDataSource();
            }
        };
        transactionManager.setAllowCustomIsolationLevels(true);
    }

    @After
    public void teardown() {
        entityManager.close();
        emf.destroy();
        database.shutdown();
    }

    @Test
    public void testEnhancedJpaRepositoryConstructorWithNullJpaEntityInformation() {
        assertNotNull(new EnhancedJpaRepository<>(null, entityManager));
    }

    @Test(expected = NullPointerException.class)
    public void testEnhancedJpaRepositoryConstructorWithNullEntityManager() {
        new EnhancedJpaRepository<>(entityInformation, null);
    }

    @Test
    public void testGetEntityManagerWithNullEntityManager() {
        repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, null) {
            @Override
            public EntityManager getEntityManager() {
                return entityManager;
            }
        };
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplateWithNullNamedParameterJdbcTemplate() {
        repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager) {
            @Override
            public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
                return null;
            }

            @Override
            public JdbcTemplate getJdbcTemplate() {
                return mock(JdbcTemplate.class);
            }
        };
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplateWithNullNamedParameterJdbcTemplate() {
        repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, entityManager) {
            @Override
            public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
                return null;
            }
        };
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

}