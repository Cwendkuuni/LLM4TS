import org.hibernate.SessionFactoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.test.jpa.AbstractJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class EnhancedJpaRepositoryTest extends AbstractJpaTest {

    private EnhancedJpaRepository<TestEntity, Serializable> repository;

    @Mock
    private EntityManager entityManager;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        JpaEntityInformation<TestEntity, Serializable> entityInformation = new MapJpaEntityInformation<TestEntity, Serializable>(TestEntity.class, entityManager);

        EntityManagerFactory emf = createEntityManagerFactory();
        repository = new EnhancedJpaRepository<>(entityInformation, emf.createEntityManager());
    }

    @After
    public void tearDown() {
        closeEntityManagerFactory();
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
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
    public void testGetDataSource() {
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    private EntityManagerFactory createEntityManagerFactory() {
        DefaultPersistenceUnitManager persistenceUnitManager = new DefaultPersistenceUnitManager();

        MutablePersistenceUnitInfo pui = new MutablePersistenceUnitInfo();
        pui.setPersistenceUnitName("test");
        pui.setTransactionType(PersistenceUnitTransactionType.RESOURCE_LOCAL);
        pui.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        pui.setJpaResourceLoader(new ClassPathResourceLoader());

        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        props.setProperty("hibernate.show_sql", "true");

        pui.setProperties(props);
        pui.setPackagesToScan("com.oceanbase.odc.config.jpa.test");

        persistenceUnitManager.setPackagesToScan("com.oceanbase.odc.config.jpa.test");
        persistenceUnitManager.setDefaultDataSource(createDataSource());
        persistenceUnitManager.afterPropertiesSet();

        LocalEntityManagerFactoryBean emf = new LocalEntityManagerFactoryBean();
        emf.setPersistenceUnitManager(persistenceUnitManager);
        emf.afterPropertiesSet();

        return emf.getObject();
    }

    private DataSource createDataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();
    }

}