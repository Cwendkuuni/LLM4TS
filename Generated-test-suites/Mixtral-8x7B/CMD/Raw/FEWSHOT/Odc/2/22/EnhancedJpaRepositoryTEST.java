import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInfo;

 @Mock
 private EntityManagerFactory emf;

 @Mock
 private EntityManager em;

 private EnhancedJpaRepository<Object, Serializable> repository;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 repository = new EnhancedJpaRepository<>(entityInfo, em);
 when(em.getEntityManagerFactory()).thenReturn(emf);
 when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
 }

 @Test
 public void testGetDataSource() {
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 when(cp.getDataSource()).thenReturn(mock(DataSource.class));
 when(emf.unwrap(SessionFactoryImpl.class).getServiceRegistry()
 .getService(ConnectionProvider.class)).thenReturn(cp);
 DataSource dataSource = repository.getDataSource(em);
 assertEquals(mock(DataSource.class), dataSource);
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(em, repository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate npjt = mock(NamedParameterJdbcTemplate.class);
 when(npjt.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));
 repository.setNamedParameterJdbcTemplate(npjt);
 JdbcTemplate jt = repository.getJdbcTemplate();
 assertEquals(mock(JdbcTemplate.class), jt);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate npjt = mock(NamedParameterJdbcTemplate.class);
 repository.setNamedParameterJdbcTemplate(npjt);
 assertEquals(npjt, repository.getNamedParameterJdbcTemplate());
 }

}