import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.MapJpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    @Mock
    private EntityManager entityManager;

    private EnhancedJpaRepository<Object, Serializable> repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        JpaEntityInformation entityInformation = new MapJpaEntityInformation<Object, Serializable>(Object.class, new EmptyEntityMetadata());
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertSame(entityManager, repository.getEntityManager());
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = mock(DataSource.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class))
            .thenReturn(new DatasourceConnectionProviderImpl(dataSource));
        DataSource result = repository.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        DataSource dataSource = mock(DataSource.class);
        when(entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class)
            .getServiceRegistry().getService(ConnectionProvider.class))
            .thenReturn(new DatasourceConnectionProviderImpl(dataSource));
        NamedParameterJdbcTemplate template = repository.getNamedParameterJdbcTemplate();
        assertNotNull(template);
        assertSame(dataSource, template.getJdbcTemplate().getDataSource());
    }
}