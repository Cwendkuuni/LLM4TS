import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

@PersistenceUnit
private EntityManagerFactory entityManagerFactory;

@Before
public void setup() {
EntityManager entityManager = mock(EntityManager.class);
JpaEntityInformation<Object, Serializable> jpaEntityInformation = mock(JpaEntityInformation.class);

when(entityManager.getEntityManagerFactory()).thenReturn(entityManagerFactory);

enhancedJpaRepository = new EnhancedJpaRepository<Object, Serializable>(jpaEntityInformation, entityManager) {
@Override
public Class<?> getDomainClass() {
return Object.class;
}
};

ReflectionTestUtils.setField(enhancedJpaRepository, "entityManager", entityManager);
}

@Test
public void testGetEntityManager() {
assertNotNull(enhancedJpaRepository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
assertNotNull(enhancedJpaRepository.getJdbcTemplate());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
assertNotNull(enhancedJpaRepository.getNamedParameterJdbcTemplate());
}

@Test
public void testGetDataSource() throws Exception {
Method getDataSourceMethod = EnhancedJpaRepository.class.getDeclaredMethod("getDataSource", EntityManager.class);
getDataSourceMethod.setAccessible(true);

DataSource dataSource = (DataSource) getDataSourceMethod.invoke(enhancedJpaRepository, Mockito.any(EntityManager.class));

assertNotNull(dataSource);
}
}