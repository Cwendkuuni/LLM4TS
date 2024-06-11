package com.oceanbase.odc.config.jpa;

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
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);

ConnectionProvider cp = mock(ConnectionProvider.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(cp);

DatasourceConnectionProviderImpl dcp = new DatasourceConnectionProviderImpl();
dcp.setDataSource(mock(DataSource.class));
when(cp.getDataSource()).thenReturn(dcp.getDataSource());

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, em);
assertEquals(dcp.getDataSource(), repo.getDataSource());
}

@Test
public void testGetEntityManager() {
EntityManager em = mock(EntityManager.class);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, em);
assertEquals(em, repo.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
DataSource ds = mock(DataSource.class);
NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(ds);
JdbcTemplate jt = (JdbcTemplate) nt.getJdbcOperations();

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, mock(EntityManager.class)) {
{
this.namedParameterJdbcTemplate = nt;
}
};
assertEquals(jt, repo.getJdbcTemplate());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
DataSource ds = mock(DataSource.class);
NamedParameterJdbcTemplate nt = new NamedParameterJdbcTemplate(ds);

EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, mock(EntityManager.class)) {
{
this.namedParameterJdbcTemplate = nt;
}
};
assertEquals(nt, repo.getNamedParameterJdbcTemplate());
}
}