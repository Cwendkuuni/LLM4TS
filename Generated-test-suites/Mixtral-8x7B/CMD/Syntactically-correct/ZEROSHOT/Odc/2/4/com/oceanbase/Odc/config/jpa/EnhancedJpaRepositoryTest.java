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
    private JpaEntityInformation<Object, Serializable> entityInfo;

    @Mock
    private EntityManagerFactory emf;

    @Mock
    private EntityManager em;

    private EnhancedJpaRepository<Object, Serializable> repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        repository = new EnhancedJpaRepository<Object, Serializable>(entityInfo, em);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(entityInfo, emf, em);
    }

    @Test
    public void testGetEntityManager() {
        assertSame(em, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        DataSource ds = mock(DataSource.class);
        when(em.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(new SessionFactoryImpl().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(ds));
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertSame(jdbcTemplate.getDataSource(), ds);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        DataSource ds = mock(DataSource.class);
        when(em.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(new SessionFactoryImpl().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(ds));
        NamedParameterJdbcTemplate nt = repository.getNamedParameterJdbcTemplate();
        assertSame(nt.getJdbcOperations().getDataSource(), ds);
    }

    @Test
    public void testGetDataSource() {
        DataSource ds = mock(DataSource.class);
        when(em.getEntityManagerFactory()).thenReturn(emf);
        when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(new SessionFactoryImpl());
        when(new SessionFactoryImpl().getServiceRegistry().getService(ConnectionProvider.class))
                .thenReturn(new DatasourceConnectionProviderImpl(ds));
        assertSame(repository.getDataSource(em), ds);
    }

}