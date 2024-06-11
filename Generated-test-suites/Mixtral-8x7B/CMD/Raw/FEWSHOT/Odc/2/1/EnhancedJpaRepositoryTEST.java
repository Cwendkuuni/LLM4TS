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
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

@Test
public void testGetDataSource() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(em.getEntityManagerFactory()).thenReturn(emf);

SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
when(sf.getServiceRegistry()).thenReturn(mock(org.hibernate.service.ServiceRegistry.class));
ConnectionProvider cp = mock(ConnectionProvider.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

DatasourceConnectionProviderImpl dcp = new DatasourceConnectionProviderImpl();
dcp.setDataSource(mock(DataSource.class));
when(cp.getDataSource()).thenReturn(dcp.getDataSource());

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), em);
DataSource result = repo.getDataSource(em);
assertEquals(dcp.getDataSource(), result);
}

@Test
public void testGetEntityManager() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(emf.createEntityManager()).thenReturn(em);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), em);
repo.setEntityManager(em);
assertEquals(em, repo.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(mock(DataSource.class), jdbcTemplate);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class));
repo.setNamedParameterJdbcTemplate(npjt);
assertEquals(jdbcTemplate, repo.getJdbcTemplate());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate npjt = mock(NamedParameterJdbcTemplate.class);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(mock(JpaEntityInformation.class), mock(EntityManager.class));
repo.setNamedParameterJdbcTemplate(npjt);
assertEquals(npjt, repo.getNamedParameterJdbcTemplate());
}

}