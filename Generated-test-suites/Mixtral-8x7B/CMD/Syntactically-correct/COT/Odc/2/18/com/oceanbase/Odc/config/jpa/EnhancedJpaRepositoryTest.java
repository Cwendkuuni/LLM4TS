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
    public void setEntityManagerFactory(EntityManagerFactory emf) {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
    }

    @Test
    public void testEnhancedJpaRepositoryConstruction() {
        assertNotNull(repository);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager em = repository.getEntityManager();
        assertNotNull(em);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate jdbcTemplate = repository.getNamedParameterJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNamedParameterJdbcTemplateException() {
        when(entityManagerFactory.createEntityManager()).thenThrow(new IllegalArgumentException());
        EnhancedJpaRepository<Object, Serializable> exceptionRepository = new EnhancedJpaRepository<>(entityInformation,
                entityManagerFactory);
        exceptionRepository.getNamedParameterJdbcTemplate();
    }

    @Test
    public void testGetJdbcTemplateDataSource() {
        DataSource dataSource = repository.getJdbcTemplate().getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetNamedParameterJdbcTemplateDataSource() {
        DataSource dataSource = repository.getNamedParameterJdbcTemplate().getJdbcOperations().getDataSource();
        assertNotNull(dataSource);
    }

    @Test
    public void testGetConnectionProvider() {
        ConnectionProvider provider = ((DatasourceConnectionProviderImpl) repository.getNamedParameterJdbcTemplate()
                .getJdbcOperations().getDataSource().getConnection().getConnectionProvider())
                .getConnectionProvider();
        assertNotNull(provider);
    }

    @Test
    public void testGetSessionFactory() {
        SessionFactoryImpl sf = (SessionFactoryImpl) repository.getEntityManager().getEntityManagerFactory();
        assertNotNull(sf);
    }

    @Test
    public void testGetNamedParameterJdbcTemplateNamedParameterJdbcOperations() {
        assertNotNull(repository.getNamedParameterJdbcTemplate().getJdbcOperations());
    }

    @Test
    public void testGetNamedParameterJdbcTemplateSql() {
        String sql = "SELECT 1";
        assertEquals(sql, repository.getNamedParameterJdbcTemplate().getQueryString(sql));
    }

}