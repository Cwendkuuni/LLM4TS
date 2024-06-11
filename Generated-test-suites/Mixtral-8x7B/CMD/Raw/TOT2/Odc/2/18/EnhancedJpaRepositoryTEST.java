import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.provider.JpaProvider;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation entityInformation;

    @Mock
    private EntityManager entityManager;

    private EnhancedJpaRepository<Object, Serializable> repository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setPackagesToScan("com.oceanbase");
        emfb.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        emfb.afterPropertiesSet();

        ReflectionTestUtils.setField(repository, "entityManagerFactory", emfb.getObject());

        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        // Test with valid parameters

        // Test with null entityInformation
        assertThrows(NullPointerException.class,
                () -> new EnhancedJpaRepository<>(null, entityManager));

        // Test with null entityManager
        assertThrows(NullPointerException.class,
                () -> new EnhancedJpaRepository<>(entityInformation, null));
    }

    @Test
    public void testGetDataSource() {
        // Test with valid EntityManager

        // Test with null SessionFactory
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        ReflectionTestUtils.setField(repository, "entityManagerFactory", sessionFactory);
        assertThrows(IllegalStateException.class, () -> repository.getDataSource(entityManager));

        // Test with null ConnectionProvider
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenThrow(new RuntimeException("Get DataSource error"));
        assertThrows(RuntimeException.class, () -> repository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        // Test with valid EntityManager
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        // Test with valid DataSource

        // Test with null DataSource
        DataSource dataSource = mock(DataSource.class);
        ReflectionTestUtils.setField(repository, "dataSource", dataSource);
        assertThrows(IllegalStateException.class, () -> repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        // Test with valid DataSource

        // Test with null DataSource
        DataSource dataSource = mock(DataSource.class);
        ReflectionTestUtils.setField(repository, "dataSource", dataSource);
        assertThrows(IllegalStateException.class, () -> repository.getNamedParameterJdbcTemplate());
    }
}