import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        JpaEntityInformation mockEntityInfo = mock(JpaEntityInformation.class);
        EntityManager mockEntityManager = mock(EntityManager.class);

        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mockEntityInfo, mockEntityManager);

        assertNotNull(repo);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(new StandardServiceRegistryBuilder().applySettings(new HashMap<>()).build().unwrap(SessionFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(new ServiceRegistryBuilder().applySettings(new HashMap<>()).build());
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, entityManager);
        DataSource result = repo.getDataSource(entityManager);

        assertEquals(dataSource, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        when(entityManager.getEntityManagerFactory()).thenReturn(new StandardServiceRegistryBuilder().applySettings(new HashMap<>()).build().unwrap(SessionFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sessionFactory);
        when(sessionFactory.getServiceRegistry()).thenReturn(new ServiceRegistryBuilder().applySettings(new HashMap<>()).build());
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);

        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, entityManager);
        JdbcTemplate result = repo.getJdbcTemplate();

        assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), result);
    }

}