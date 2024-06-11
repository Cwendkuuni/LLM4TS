import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInformation;

 @Mock
 private EntityManagerFactory entityManagerFactory;

 @Mock
 private EntityManager entityManager;

 @Mock
 private DataSource dataSource;

 @Spy
 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> repository;

 @PersistenceUnit
 public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
 ReflectionTestUtils.setField(repository, "entityManagerFactory", entityManagerFactory);
 }

 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 when(entityManagerFactory.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
 when(cp.getDataSource()).thenReturn(dataSource);
 assertEquals(dataSource, repository.getDataSource(entityManager));
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(entityManager, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
 repository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
 assertEquals(jdbcTemplate, repository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 repository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
 assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
 }
}