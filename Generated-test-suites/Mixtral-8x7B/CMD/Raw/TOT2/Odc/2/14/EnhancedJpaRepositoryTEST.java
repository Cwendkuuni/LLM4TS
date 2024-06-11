import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repo;

    private JpaEntityInformation entityInformation;

    private EntityManager entityManager;

    @Before
    public void setUp() {
        entityInformation = Mockito.mock(JpaEntityInformation.class);
        entityManager = Mockito.mock(EntityManager.class);
        repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(repo.getEntityManager());
        assertNotNull(repo.getJdbcTemplate());
        assertNotNull(repo.getNamedParameterJdbcTemplate());
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repo.getEntityManager();
        assertNotNull(entityManager);
        assertEquals(entityManager, repo.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = repo.getJdbcTemplate();
        assertNotNull(jdbcTemplate);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = repo.getNamedParameterJdbcTemplate();
        assertNotNull(namedParameterJdbcTemplate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEntityInformation() {
        new EnhancedJpaRepository<>(null, entityManager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullEntityManager() {
        new EnhancedJpaRepository<>(entityInformation, null);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorClosedEntityManager() {
        EntityManager closedEntityManager = Mockito.mock(EntityManager.class);
        Mockito.when(closedEntityManager.isOpen()).thenReturn(false);
        new EnhancedJpaRepository<>(entityInformation, closedEntityManager);
    }
}