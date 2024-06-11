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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, ?> entityInformation;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(mock(SessionFactoryImpl.class));

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
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
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
                .getServiceRegistry().getService(ConnectionProvider.class))
                        .thenReturn(new DatasourceConnectionProviderImpl(dataSource));

        DataSource result = enhancedJpaRepository.getDataSource(entityManager);

        assertEquals(dataSource, result);
    }
}