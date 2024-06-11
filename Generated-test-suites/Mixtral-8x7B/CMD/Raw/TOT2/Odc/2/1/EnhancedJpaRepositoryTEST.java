import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;
import com.oceanbase.odc.config.jpa.TestConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EnhancedJpaRepositoryTest {

    @Autowired
    private EnhancedJpaRepository<Object, Long> repository;

    @Test
    @Transactional
    public void testGetEntityManager() {
        EntityManager entityManager = repository.getEntityManager();
        assertNotNull(entityManager);
    }
}