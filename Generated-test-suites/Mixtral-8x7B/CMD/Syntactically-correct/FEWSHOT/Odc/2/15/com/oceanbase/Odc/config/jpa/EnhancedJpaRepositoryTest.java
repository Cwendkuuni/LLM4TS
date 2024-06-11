import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
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

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    @PersistenceUnit
    private DataSource dataSource;

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sf = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        DataSource expectedDataSource = spy(dataSource);

        when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(
                new DatasourceConnectionProviderImpl(expectedDataSource));

        DataSource resultDataSource = repository.getDataSource(entityManager);

        assertEquals(expectedDataSource, resultDataSource);
    }

    @Test
    public void testGetEntityManager() {
        assertEquals(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate expectedJdbcTemplate = new JdbcTemplate(dataSource);

        JdbcTemplate resultJdbcTemplate = repository.getJdbcTemplate();

        assertEquals(expectedJdbcTemplate, resultJdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate expectedNamedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);

        NamedParameterJdbcTemplate resultNamedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();

        assertEquals(expectedNamedParameterJdbcTemplate, resultNamedParameterJdbcTemplate);
    }

}