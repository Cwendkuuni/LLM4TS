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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    @PersistenceUnit
    private void setUp(EntityManagerFactory emf) {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = repository.getEntityManager();
        assertNotNull(result);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate result = repository.getJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testGetDataSource() {
        DataSource result = repository.getDataSource(entityManager);
        assertNotNull(result);
    }

    @Test
    public void testConstructor() {
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertEquals(entityManager, repo.getEntityManager());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEntityInformation() {
        new EnhancedJpaRepository<>(null, entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullEntityManager() {
        new EnhancedJpaRepository<>(entityInformation, null);
    }

    @Test
    public void testGetDataSourceWithMockedEntityManager() {
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        DataSource result = repo.getDataSource(entityManager);
        assertNotNull(result);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithUnwrappedNullSessionFactory() {
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(sf.getServiceRegistry()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        repo.getDataSource(entityManager);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetDataSourceWithNullConnectionProvider() {
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(sf.getServiceRegistry()).thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(((DatasourceConnectionProviderImpl) sf.getServiceRegistry()
                .getService(ConnectionProvider.class)).getDataSource()).thenReturn(null);

        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
        repo.getDataSource(entityManager);
    }
}