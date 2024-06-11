import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<TestEntity, Long> enhancedJpaRepository;

    @Mock
    private JpaEntityInformation<TestEntity, Long> entityInfo;

    @Mock
    private EntityManager entityManager;

    private LocalContainerEntityManagerFactoryBean factoryBean;

    @Before
    public void setUp() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(true);

        factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setPackagesToScan("com.oceanbase.odc.config.jpa");
        factoryBean.afterPropertiesSet();

        enhancedJpaRepository = new EnhancedJpaRepository<>(entityInfo, entityManager);
        ReflectionTestUtils.setField(enhancedJpaRepository, "entityManager", entityManager);
    }

    @Test
    public void testGetDataSource() {
        DataSource dataSource = enhancedJpaRepository.getDataSource(entityManager);
        assertNotNull(dataSource);
    }

    @Test
    public void testGetEntityManager() {
        EntityManager result = enhancedJpaRepository.getEntityManager();
        assertEquals(entityManager, result);
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate result = enhancedJpaRepository.getJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate result = enhancedJpaRepository.getNamedParameterJdbcTemplate();
        assertNotNull(result);
    }

    @Test
    public void testSave() {
        TestEntity testEntity = new TestEntity();
        enhancedJpaRepository.save(testEntity);
        verify(entityManager, times(1)).persist(testEntity);
    }

    @Test
    public void testFindOne() {
        Long id = 1L;
        when(entityInfo.getEntityIdentifier(any())).thenReturn(id);
        enhancedJpaRepository.findOne(id);
        verify(entityManager, times(1)).find(TestEntity.class, id);
    }

    @Test
    public void testFindAll() {
        enhancedJpaRepository.findAll();
        verify(entityManager, times(1)).createQuery("SELECT e FROM TestEntity e", TestEntity.class);
    }

    @Test
    public void testCount() {
        enhancedJpaRepository.count();
        verify(entityManager, times(1)).createQuery("SELECT COUNT(e) FROM TestEntity e", Long.class);
    }

    @Test
    public void testDelete() {
        TestEntity testEntity = new TestEntity();
        enhancedJpaRepository.delete(testEntity);
        verify(entityManager, times(1)).remove(testEntity);
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;
        enhancedJpaRepository.deleteById(id);
        verify(entityManager, times(1)).remove(new TestEntity());
    }

    @Test
    public void testExistsById() {
        Long id = 1L;
        when(entityManager.contains(any())).thenReturn(true);
        boolean result = enhancedJpaRepository.existsById(id);
        assertTrue(result);
    }

    @Test
    public void testFlush() {
        enhancedJpaRepository.flush();
        verify(entityManager, times(1)).flush();
    }

    @Test
    public void testGetId() {
        TestEntity testEntity = new TestEntity();
        Serializable id = enhancedJpaRepository.getId(testEntity);
        assertNotNull(id);
    }

}

class TestEntity {}