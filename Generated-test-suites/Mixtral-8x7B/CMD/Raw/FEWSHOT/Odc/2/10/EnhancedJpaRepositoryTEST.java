import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
 private EnhancedJpaRepository<Object, Serializable> repository;
 private EntityManagerFactory emf;
 private JpaEntityInformation<Object, Serializable> entityInfo;
 private EntityManager em;
 private DataSource ds;
 private NamedParameterJdbcTemplate nt;
 private JdbcTemplate jt;

 @Test
 public void testConstructor() {
 entityInfo = mock(JpaEntityInformation.class);
 em = mock(EntityManager.class);
 when(em.getEntityManagerFactory()).thenReturn(emf);
 repository = new EnhancedJpaRepository<Object, Serializable>(entityInfo, em);
 assertEquals(repository.getEntityManager(), em);
 }

 @Test
 public void testGetDataSource() {
 ds = mock(DataSource.class);
 when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
 when(mock(SessionFactoryImpl.class).getServiceRegistry()
 .getService(ConnectionProvider.class)).thenReturn(mock(DatasourceConnectionProviderImpl.class));
 when(mock(DatasourceConnectionProviderImpl.class).getDataSource()).thenReturn(ds);
 assertEquals(repository.getDataSource(em), ds);
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 nt = mock(NamedParameterJdbcTemplate.class);
 when(nt.getJdbcOperations()).thenReturn(mock(JdbcTemplate.class));
 repository.setNamedParameterJdbcTemplate(nt);
 assertEquals(repository.getNamedParameterJdbcTemplate(), nt);
 }

 @Test
 public void testGetJdbcTemplate() {
 jt = mock(JdbcTemplate.class);
 when(nt.getJdbcOperations()).thenReturn(jt);
 repository.setNamedParameterJdbcTemplate(nt);
 assertEquals(repository.getJdbcTemplate(), jt);
 }
}