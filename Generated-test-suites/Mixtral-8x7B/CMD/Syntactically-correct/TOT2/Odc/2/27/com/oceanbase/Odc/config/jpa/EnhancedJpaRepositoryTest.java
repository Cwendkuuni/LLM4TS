import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = LocalContainerEntityManagerFactoryBean.class)
public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<Object, Long> repository;

@Test
public void testConstructor() {
JpaEntityInformation<Object, Long> entityInformation = null;
EntityManager entityManager = new SharedEntityManagerCreator(
new LocalContainerEntityManagerFactoryBean()).createEntityManager();
repository = new EnhancedJpaRepository<Object, Long>(entityInformation, entityManager);
assertNotNull(repository.getEntityManager());
}

@Test
public void testGetEntityManager() {
EntityManager expectedEntityManager = new SharedEntityManagerCreator(
new LocalContainerEntityManagerFactoryBean()).createEntityManager();
repository = new EnhancedJpaRepository<Object, Long>(null, expectedEntityManager);
EntityManager actualEntityManager = repository.getEntityManager();
assertEquals(expectedEntityManager, actualEntityManager);
}

@Test
public void testGetJdbcTemplate() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
assertTrue(jdbcTemplate instanceof JdbcTemplate);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
assertTrue(namedParameterJdbcTemplate instanceof NamedParameterJdbcTemplate);
}

@Test(expected = IllegalArgumentException.class)
public void testConstructorNullEntityManager() {
JpaEntityInformation<Object, Long> entityInformation = null;
repository = new EnhancedJpaRepository<Object, Long>(entityInformation, null);
}

@Test(expected = NoSuchElementException.class)
public void testGetEntityManagerBeforeConstructor() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
repository.getEntityManager();
}

@Test(expected = NullPointerException.class)
public void testGetJdbcTemplateBeforeConstructor() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
repository.getJdbcTemplate();
}

@Test(expected = NullPointerException.class)
public void testGetNamedParameterJdbcTemplateBeforeConstructor() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
repository.getNamedParameterJdbcTemplate();
}

@Test
public void testConstructorDifferentEntityInformation() {
JpaEntityInformation<Object, Long> entityInformation = null;
JpaEntityInformation<Object, Integer> differentEntityInformation = null;
EntityManager entityManager = new SharedEntityManagerCreator(
new LocalContainerEntityManagerFactoryBean()).createEntityManager();
repository = new EnhancedJpaRepository<Object, Long>(entityInformation, entityManager);
assertNotNull(repository.getEntityManager());
repository = new EnhancedJpaRepository<Object, Integer>(differentEntityInformation, entityManager);
assertNotNull(repository.getEntityManager());
}

@Test
public void testGetEntityManagerMultiThreaded() {
EntityManager firstEntityManager = new SharedEntityManagerCreator(
new LocalContainerEntityManagerFactoryBean()).createEntityManager();
EntityManager secondEntityManager = new SharedEntityManagerCreator(
new LocalContainerEntityManagerFactoryBean()).createEntityManager();
repository = new EnhancedJpaRepository<Object, Long>(null, firstEntityManager);
Thread firstThread = new Thread() {
@Override
public void run() {
TransactionSynchronizationManager.initSynchronization();
EntityManagerManagerHolder.setEntityManager(firstEntityManager);
}
};
Thread secondThread = new Thread() {
@Override
public void run() {
TransactionSynchronizationManager.initSynchronization();
EntityManagerManagerHolder.setEntityManager(secondEntityManager);
}
};
firstThread.start();
secondThread.start();
try {
firstThread.join();
secondThread.join();
} catch (InterruptedException e) {
e.printStackTrace();
}
EntityManager actualFirstEntityManager = repository.getEntityManager();
EntityManager actualSecondEntityManager = repository.getEntityManager();
assertEquals(firstEntityManager, actualFirstEntityManager);
assertEquals(secondEntityManager, actualSecondEntityManager);
}

@Test
public void testGetJdbcTemplateDifferentSql() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
String sql1 = "SELECT * FROM table1";
String sql2 = "INSERT INTO table2 (column) VALUES (value)";
JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
jdbcTemplate.query(sql1, new Object[]{}, new Object[]{}, rs -> {});
jdbcTemplate.update(sql2, new Object[]{});
}

@Test
public void testGetNamedParameterJdbcTemplateDifferentNamedParameters() {
repository = new EnhancedJpaRepository<Object, Long>(null, null);
String sql1 = "SELECT * FROM table1 WHERE column = :value";
String sql2 = "INSERT INTO table2 (column) VALUES (:value)";
MapSqlParameterSource namedParameters1 = new MapSqlParameterSource().addValue("value", 1);
MapSqlParameterSource namedParameters2 = new MapSqlParameterSource().addValue("value", "string");
NamedParameterJdbcTemplate namedParameterJdbcTemplate = repository.getNamedParameterJdbcTemplate();
namedParameterJdbcTemplate.query(sql1, namedParameters1, rs -> {});
namedParameterJdbcTemplate.update(sql2, namedParameters2);
}
}

class EntityManagerManagerHolder {
private static final ThreadLocal<EntityManager> entityManagerThreadLocal = new ThreadLocal<EntityManager>() {
@Override
protected EntityManager initialValue() {
return null;
}
};

public static void setEntityManager(EntityManager entityManager) {
entityManagerThreadLocal.set(entityManager);
}

public static EntityManager getEntityManager() {
return entityManagerThreadLocal.get();
}
}