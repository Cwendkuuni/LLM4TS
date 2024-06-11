import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.registry.internal.StandardServiceRegistryImpl;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.MapJpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.EntityManagerDetacher;
import com.oceanbase.odc.model.DummyEntity;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        JpaEntityInformation entityInformation = new MapJpaEntityInformation(DummyEntity.class, new EntityManagerDetacher(entityManager));
        new EnhancedJpaRepository<>(entityInformation, entityManager);
        // TODO: Add assertions to check if the object is created correctly
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = mock(EntityManager.class);
        SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl datasourceConnectionProviderImpl = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);

        when(entityManager.getEntityManagerFactory()).thenReturn(new StandardServiceRegistryBuilder().build().getServiceRegistry().getService(SessionFactory.class));
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)).thenReturn(sf);
        when(sf.getServiceRegistry()).thenReturn(new ServiceRegistryBuilder().build().getServiceRegistry());
        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(datasourceConnectionProviderImpl);
        when(datasourceConnectionProviderImpl.getDataSource()).thenReturn(dataSource);

        assertEquals(dataSource, new EnhancedJpaRepository<>(null, entityManager).getDataSource(entityManager));
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = mock(EntityManager.class);
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);

        when(getDataSource(entityManager)).thenReturn(mock(DataSource.class));

        EnhancedJpaRepository<DummyEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }

    // Helper method to mock the getDataSource method
    private DataSource getDataSource(EntityManager entityManager) {
        EnhancedJpaRepository<DummyEntity, Long> repository = new EnhancedJpaRepository<>(null, entityManager);
        return repository.getDataSource(entityManager);
    }

}