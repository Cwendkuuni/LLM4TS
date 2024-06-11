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
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class))
                .thenReturn(mock(SessionFactoryImpl.class));

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(entityManagerFactory, entityManager);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(entityManager, enhancedJpaRepository.getEntityManager());
    }

    @Test
    public void testGetDataSource() {
        DataSource result = enhancedJpaRepository.getDataSource(entityManager);

        verify(entityManagerFactory.unwrap(SessionFactoryImpl.class))
                .getServiceRegistry()
                .getService(ConnectionProvider.class);
        verify(entityManagerFactory.unwrap(SessionFactoryImpl.class))
                .getServiceRegistry()
                .getService(DatasourceConnectionProviderImpl.class)
                .getDataSource();

        assertSame(dataSource, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));

        EnhancedJpaRepository<Object, Serializable> repositoryWithTemplate =
                spy(new EnhancedJpaRepository<>(entityInformation, entityManager));

        doReturn(namedParameterJdbcTemplate).when(repositoryWithTemplate).getNamedParameterJdbcTemplate();

        JdbcTemplate result = repositoryWithTemplate.getJdbcTemplate();

        verify(repositoryWithTemplate).getNamedParameterJdbcTemplate();

        assertSame(result, ((JdbcTemplate) namedParameterJdbcTemplate.getJdbcOperations()));
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();

        verify(entityManagerFactory.unwrap(SessionFactoryImpl.class))
                .getServiceRegistry()
                .getService(ConnectionProvider.class);
        verify(entityManagerFactory.unwrap(SessionFactoryImpl.class))
                .getServiceRegistry()
                .getService(DatasourceConnectionProviderImpl.class)
                .getDataSource();

        verify(entityManager).getEntityManagerFactory();
        verify(entityManagerFactory).createNamedParameterJdbcTemplate(dataSource);

        assertSame(result, enhancedJpaRepository.getNamedParameterJdbcTemplate());
    }
}