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
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    @PersistenceUnit
    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);
        when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
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

    @Test(expected = IllegalArgumentException.class)
    public void testGetDataSource_entityManagerNull() {
        repository.getDataSource(null);
    }

    @Test
    public void testGetDataSource_SessionFactoryNull() {
        when(entityManager.getEntityManagerFactory()).thenReturn(null);
        DataSource result = repository.getDataSource(entityManager);
        assertNotNull(result);
    }

    @Test
    public void testGetDataSource_ConnectionProviderNull() {
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class))
                .thenReturn(mock(SessionFactoryImpl.class));
        when(entityManager.getEntityManagerFactory().getServiceRegistry())
                .thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
        when(entityManager.getEntityManagerFactory().getServiceRegistry()
                .getService(ConnectionProvider.class))
                .thenReturn(null);
        DataSource result = repository.getDataSource(entityManager);
        assertNotNull(result);
    }
}