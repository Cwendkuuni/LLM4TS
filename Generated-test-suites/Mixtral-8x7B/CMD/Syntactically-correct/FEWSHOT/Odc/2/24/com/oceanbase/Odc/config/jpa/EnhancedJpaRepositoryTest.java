import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.oceanbase.odc.config.jpa.EnhancedJpaRepository;

public class EnhancedJpaRepositoryTest {

@Test
public void testEnhancedJpaRepositoryConstructor() {
JpaEntityInformation<Object, Serializable> entityInfo = mock(JpaEntityInformation.class);
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(em.getEntityManagerFactory()).thenReturn(emf);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(entityInfo, em);

assertNotNull(repo);
}

@Test
public void testGetDataSource() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(em.getEntityManagerFactory()).thenReturn(emf);

SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

DataSource ds = mock(DataSource.class);
when(cp.getDataSource()).thenReturn(ds);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(null, em) {
@Override
protected DataSource getDataSource(EntityManager entityManager) {
return super.getDataSource(entityManager);
}
};

DataSource result = repo.getDataSource(em);

assertNotNull(result);
Mockito.verify(cp).getDataSource();
}

@Test
public void testGetEntityManager() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(em.getEntityManagerFactory()).thenReturn(emf);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(null, em) {
@Override
protected DataSource getDataSource(EntityManager entityManager) {
return null;
}

@Override
public EntityManager getEntityManager() {
return em;
}
};

EntityManager result = repo.getEntityManager();

assertNotNull(result);
Mockito.verify(em).getEntityManagerFactory();
}

@Test
public void testGetJdbcTemplate() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(em.getEntityManagerFactory()).thenReturn(emf);

SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

DataSource ds = mock(DataSource.class);
when(cp.getDataSource()).thenReturn(ds);

JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
NamedParameterJdbcTemplate npJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
when(npJdbcTemplate.getJdbcOperations()).thenReturn(jdbcTemplate);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(null, em) {
@Override
protected DataSource getDataSource(EntityManager entityManager) {
return super.getDataSource(entityManager);
}

@Override
public JdbcTemplate getJdbcTemplate() {
return (JdbcTemplate) super.getNamedParameterJdbcTemplate().getJdbcOperations();
}
};

JdbcTemplate result = repo.getJdbcTemplate();

assertNotNull(result);
Mockito.verify(npJdbcTemplate).getJdbcOperations();
}

@Test
public void testGetNamedParameterJdbcTemplate() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
when(em.getEntityManagerFactory()).thenReturn(emf);

SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

DataSource ds = mock(DataSource.class);
when(cp.getDataSource()).thenReturn(ds);

NamedParameterJdbcTemplate npJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(null, em) {
@Override
protected DataSource getDataSource(EntityManager entityManager) {
return super.getDataSource(entityManager);
}

@Override
public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
return npJdbcTemplate;
}
};

NamedParameterJdbcTemplate result = repo.getNamedParameterJdbcTemplate();

assertNotNull(result);
Mockito.verify(em).getEntityManagerFactory();
}
}