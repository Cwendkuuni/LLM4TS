import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class EnhancedJpaRepositoryTest {

 @Mock
 private JpaEntityInformation<Object, Serializable> entityInfo;
 @Mock
 private EntityManagerFactory emf;
 @Mock
 private EntityManager em;
 @Mock
 private DataSource ds;
 @InjectMocks
 private EnhancedJpaRepository<Object, Serializable> enhancedJpaRepository;

 @PersistenceUnit
 public void setEntityManagerFactory(EntityManagerFactory emf) {
 this.emf = emf;
 }

 @Test
 public void testGetEntityManager() {
 assertEquals(em, enhancedJpaRepository.getEntityManager());
 }

 @Test
 public void testGetJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
 when(namedParameterJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);
 enhancedJpaRepository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
 assertEquals(jdbcTemplate, enhancedJpaRepository.getJdbcTemplate());
 }

 @Test
 public void testGetNamedParameterJdbcTemplate() {
 NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
 when(enhancedJpaRepository.getNamedParameterJdbcTemplate()).thenReturn(namedParameterJdbcTemplate);
 assertEquals(namedParameterJdbcTemplate, enhancedJpaRepository.getNamedParameterJdbcTemplate());
 }

 @Test
 public void testGetDataSource() {
 SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
 ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
 when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);
 when(cp.getDataSource()).thenReturn(ds);
 when(enhancedJpaRepository.getEntityManager().getEntityManagerFactory()).thenReturn(emf);
 when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
 assertEquals(ds, enhancedJpaRepository.getDataSource(em));
 }
}