import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

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

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class).getServiceRegistry())
                .thenReturn(new org.hibernate.service.ServiceRegistryBuilder().buildServiceRegistry());
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl());
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class)
                .getDataSource()).thenReturn(dataSource);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry())
                .thenReturn(new org.hibernate.service.ServiceRegistryBuilder().buildServiceRegistry());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry()
                .getService(ConnectionProvider.class)).thenReturn(new DatasourceConnectionProviderImpl());
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class).getServiceRegistry()
                .getService(ConnectionProvider.class).getDataSource()).thenReturn(dataSource);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(entityInformation, entityManagerFactory, entityManager, dataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, enhancedJpaRepository.getEntityManager());
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

}