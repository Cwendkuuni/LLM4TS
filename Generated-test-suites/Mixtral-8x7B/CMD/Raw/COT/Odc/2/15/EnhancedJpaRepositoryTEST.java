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
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

    @Mock
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Mock
    private EntityManagerFactory entityManagerFactory;

    @Mock
    private EntityManager entityManager;

    @Mock
    private DataSource dataSource;

    @Spy
    @InjectMocks
    private EnhancedJpaRepository<Object, Serializable> repository;

    @PersistenceUnit
    private void setEntityManagerFactory(EntityManagerFactory factory) {
        ReflectionTestUtils.setField(repository, "entityManagerFactory", entityManagerFactory);
    }

    @Before
    public void setUp() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getDataSource()).thenReturn(dataSource);
    }

    @Test
    public void testConstructor() {
        assertNotNull(repository);
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sf = entityManager.getEntityManagerFactory().unwrap(SessionFactoryImpl.class);
        ReflectionTestUtils.setField(repository, "sessionFactory", sf);
        DataSource result = repository.getDataSource(entityManager);
        assertEquals(dataSource, result);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = repository.getEntityManager();
        assertEquals(entityManager, result);
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

}