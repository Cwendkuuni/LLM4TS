import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.Serializable;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;
    private JpaEntityInformation jpaEntityInformation;

    @Before
    public void setup() {
        EntityManagerFactory emf = Mockito.mock(EntityManagerFactory.class);
        entityManager = Mockito.mock(EntityManager.class);
        Mockito.when(emf.createEntityManager()).thenReturn(entityManager);

        jpaEntityInformation = Mockito.mock(JpaEntityInformation.class);

        repository = new EnhancedJpaRepository<>(jpaEntityInformation, entityManager);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = repository.getDataSource(entityManager);
        Assert.assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager em = repository.getEntityManager();
        Assert.assertEquals(entityManager, em);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
        Assert.assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
        Assert.assertNotNull(namedParameterJdbcTemplate);
    }

    @Test
    public void testConstructorWithJpaEntityInformationAndEntityManager() {
        EnhancedJpaRepository<Object, Serializable> repositorySpy = Mockito.spy(EnhancedJpaRepository.class);
        Mockito.when(repositorySpy.getEntityInformation()).thenReturn(jpaEntityInformation);
        Mockito.when(repositorySpy.getEntityManager()).thenReturn(entityManager);

        Mockito.doCallRealMethod().when(repositorySpy).EnhancedJpaRepository(jpaEntityInformation, entityManager);

        repositorySpy.EnhancedJpaRepository(jpaEntityInformation, entityManager);
        Mockito.verify(repositorySpy).setJpaEntityInformation(jpaEntityInformation);
        Mockito.verify(repositorySpy).setEntityManager(entityManager);
    }

    @Test
    public void testConstructorWithClassAndEntityManager() {
        EnhancedJpaRepository<Object, Serializable> repositorySpy = Mockito.spy(EnhancedJpaRepository.class);
        Mockito.when(repositorySpy.getEntityInformation(Object.class)).thenReturn(jpaEntityInformation);
        Mockito.when(repositorySpy.getEntityManager()).thenReturn(entityManager);

        Mockito.doCallRealMethod().when(repositorySpy).EnhancedJpaRepository(Object.class, entityManager);

        repositorySpy.EnhancedJpaRepository(Object.class, entityManager);
        Mockito.verify(repositorySpy).setJpaEntityInformation(jpaEntityInformation);
        Mockito.verify(repositorySpy).setEntityManager(entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testGetDataSourceWithNullEntityManager() {
        new EnhancedJpaRepository<>(jpaEntityInformation, null).getDataSource(entityManager);
    }

    @Test(expected = NullPointerException.class)
    public void testGetEntityManagerWithNullEntityManagerFactory() {
        new EnhancedJpaRepository<>(jpaEntityInformation, null).getEntityManager();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testJdbcTemplateUnsupportedOperations() {
        repository.getJdbcTemplate().execute("Invalid Operation");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNamedParameterJdbcTemplateUnsupportedOperations() {
        repository.getNamedParameterJdbcTemplate().execute("Invalid Operation");
    }
}