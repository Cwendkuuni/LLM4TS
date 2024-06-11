import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.EntityType;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private JpaEntityInformation<Object, Serializable> entityInformation;
    private EntityManager entityManager;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private EnhancedJpaRepository<Object, Serializable> repo;

    @Before
    public void setup() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

        repo = new EnhancedJpaRepository<>(entityInformation, entityManager, namedParameterJdbcTemplate);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), entityManager);
        assertEquals(entityManager, repo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(mock(JdbcOperations.class));
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class), namedParameterJdbcTemplate);
        assertNotNull(repo.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class), namedParameterJdbcTemplate);
        assertEquals(namedParameterJdbcTemplate, repo.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testConstructor() {
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class)).thenReturn(mock(SessionFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getServiceRegistry()).thenReturn(mock(ServiceRegistry.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(mock(ConnectionProvider.class));
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager, namedParameterJdbcTemplate);
        verify(entityManager).getEntityManagerFactory();
        verify(entityManager.getEntityManagerFactory()).unwrap(SessionFactory.class);
        verify(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class)).getServiceRegistry();
        verify(entityManager.getEntityManagerFactory().unwrap(SessionFactory.class).getServiceRegistry()).getService(ConnectionProvider.class);
    }

    @Test
    public void testSave() {
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager, namedParameterJdbcTemplate);
        Object entity = mock(Object.class);
        repo.save(entity);
        verify(entityManager).persist(entity);
    }
}