import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@EnableJpaRepositories
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<Object, Long> repository;

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
    }
}