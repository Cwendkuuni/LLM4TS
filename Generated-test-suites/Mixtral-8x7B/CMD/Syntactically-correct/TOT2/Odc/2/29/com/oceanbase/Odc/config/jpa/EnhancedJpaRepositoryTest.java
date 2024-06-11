import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

    @Test
    public void testConstructor() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInfo,
                entityManager);
        assertNotNull(repository);
    }

    @Test
    public void testConstructorNullEntityInformation() {
        EntityManager entityManager = mock(EntityManager.class);
        assertThrows(NullPointerException.class, () -> new EnhancedJpaRepository<>(null, entityManager));
        verify(entityManager, never()).getEntityManagerFactory();
    }

    @Test
    public void testConstructorNullEntityManager() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        assertThrows(NullPointerException.class, () -> new EnhancedJpaRepository<>(entityInfo, null));
    }

    @Test
    public void testConstructorInvalidEntityManager() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        Object invalidEntityManager = new Object();
        assertThrows(IllegalArgumentException.class, () -> new EnhancedJpaRepository<>(entityInfo,
                invalidEntityManager));
    }

    @Test
    public void testGetEntityManager() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInfo,
                entityManager);
        assertNotNull(repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInfo,
                entityManager);
        assertNotNull(repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(entityInfo,
                entityManager);
        assertNotNull(repository.getNamedParameterJdbcTemplate());
    }

}