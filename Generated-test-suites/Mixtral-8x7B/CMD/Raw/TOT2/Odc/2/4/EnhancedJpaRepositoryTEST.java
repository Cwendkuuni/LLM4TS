import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(entityInfo, entityManager);
        assertNotNull(repo);
    }

}