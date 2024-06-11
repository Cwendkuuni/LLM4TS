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
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

@ContextConfiguration
public class EnhancedJpaRepositoryTest {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serializable>(entityInfo,
                entityManager);
        assertNotNull(repository);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(sf.getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
        DataSource dataSource = mock(DataSource.class);
        when(cp.getDataSource()).thenReturn(dataSource);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serializable>(null,
                entityManager);
        DataSource result = repository.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serializable>(null,
                entityManager);
        EntityManager result = repository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(sf.getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
        DataSource dataSource = mock(DataSource.class);
        when(cp.getDataSource()).thenReturn(dataSource);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serializable>(null,
                entityManager) {
            @Override
            public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
                return namedParameterJdbcTemplate;
            }
        };
        JdbcTemplate result = repository.getJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(emf);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        when(sf.getServiceRegistry()).thenReturn(Mockito.mock(org.hibernate.service.ServiceRegistry.class));
        ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
        DataSource dataSource = mock(DataSource.class);
        when(cp.getDataSource()).thenReturn(dataSource);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<Object, Serializable>(null,
                entityManager) {
            @Override
            public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
                return namedParameterJdbcTemplate;
            }
        };
        NamedParameterJdbcTemplate result = repository.getNamedParameterJdbcTemplate();
        assertEquals(namedParameterJdbcTemplate, result);
    }

}