
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.MockEntityManager;
import com.oceanbase.odc.config.jpa.MockJpaEntityInformation;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testEnhancedJpaRepositoryConstructor() {
        JpaEntityInformation entityInformation = new MockJpaEntityInformation<>(DummyEntity.class, Integer.class);
        EntityManager entityManager = new MockEntityManager();
        EnhancedJpaRepository repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
        assertNotNull(repository);
    }

    @Test
    public void testGetDataSource() {
        EntityManager entityManager = new MockEntityManager();
        EnhancedJpaRepository repository = new EnhancedJpaRepository(new MockJpaEntityInformation<>(DummyEntity.class, Integer.class), entityManager);
        DataSource dataSource = repository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        EntityManager entityManager = new MockEntityManager();
        EnhancedJpaRepository repository = new EnhancedJpaRepository(new MockJpaEntityInformation<>(DummyEntity.class, Integer.class), entityManager);
        NamedParameterJdbcTemplate template = repository.getNamedParameterJdbcTemplate();
        assertNotNull(template);
    }
}
