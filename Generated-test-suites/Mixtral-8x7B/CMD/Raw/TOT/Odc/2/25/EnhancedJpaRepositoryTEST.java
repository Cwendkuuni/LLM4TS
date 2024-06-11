import org.hibernate.service.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EnhancedJpaRepositoryTest {

    private static final class DummyEntity {}

    private static final class SerializablePrimaryKeyGenerator implements Serializable<String> {
        @Override
        public String generate(String s, Object o) {
            return null;
        }
    }

    @Mock
    private EntityManager entityManager;

    @Mock
    private JpaEntityInformation<DummyEntity, Serializable> entityInformation;

    private EnhancedJpaRepository<DummyEntity, Serializable> repo;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(entityInformation.getJavaType()).thenReturn(DummyEntity.class);
        repo = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        assertNotNull(repo);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = repo.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repo.getEntityManager();
        assertNotNull(entityManager);
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

}