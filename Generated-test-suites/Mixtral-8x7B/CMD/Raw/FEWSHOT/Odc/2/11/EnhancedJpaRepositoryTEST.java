package com.oceanbase.odc.config.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
private static class TestEntityInformation<T, ID extends Serializable> implements JpaEntityInformation<T, ID> {
getId(Object entity) {
return (ID) entity;
}
getJavaType() {
return Object.class;
}
isNew(Object entity) {
return false;
}
}

@Test
public void testGetDataSource() {
EntityManagerFactory emf = mock(EntityManagerFactory.class);
EntityManager em = mock(EntityManager.class);
SessionFactoryImpl sf = mock(SessionFactoryImpl.class);
DatasourceConnectionProviderImpl cp = mock(DatasourceConnectionProviderImpl.class);
DataSource ds = mock(DataSource.class);
when(em.getEntityManagerFactory()).thenReturn(emf);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
when(sf.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(cp);
when(cp.getDataSource()).thenReturn(ds);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(
new TestEntityInformation<Object, Serializable>(), em);
DataSource result = repo.getDataSource(em);
assertEquals(ds, result);
}

@Test
public void testGetEntityManager() {
EntityManager em = mock(EntityManager.class);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(
new TestEntityInformation<Object, Serializable>(), em);
EntityManager result = repo.getEntityManager();
assertEquals(em, result);
}

@Test
public void testGetJdbcTemplate() {
EntityManager em = mock(EntityManager.class);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(
new TestEntityInformation<Object, Serializable>(), em);
JdbcTemplate result = repo.getJdbcTemplate();
assertNotNull(result);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
EntityManager em = mock(EntityManager.class);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<Object, Serializable>(
new TestEntityInformation<Object, Serializable>(), em);
NamedParameterJdbcTemplate result = repo.getNamedParameterJdbcTemplate();
assertNotNull(result);
}
}