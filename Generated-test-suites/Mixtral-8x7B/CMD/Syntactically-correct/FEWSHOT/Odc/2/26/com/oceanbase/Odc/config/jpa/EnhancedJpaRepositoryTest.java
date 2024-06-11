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
ConnectionProvider cp = mock(DatasourceConnectionProviderImpl.class);
when(sf.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(cp);
DataSource ds = mock(DataSource.class);
when(cp.getDataSource()).thenReturn(ds);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, em);
assertEquals(ds, repo.getDataSource(em));
}

@Test
public void testGetEntityManager() {
EntityManager em = mock(EntityManager.class);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, em);
assertEquals(em, repo.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
NamedParameterJdbcTemplate npjt = mock(NamedParameterJdbcTemplate.class);
JdbcTemplate jt = mock(JdbcTemplate.class);
when(npjt.getJdbcOperations()).thenReturn(jt);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, mock(EntityManager.class)) {
@Override
public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
return npjt;
}
};
assertEquals(jt, repo.getJdbcTemplate());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate npjt = mock(NamedParameterJdbcTemplate.class);
EnhancedJpaRepository<Object, Serializable> repo = new EnhancedJpaRepository<>(null, mock(EntityManager.class)) {
@Override
public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
return npjt;
}
};
assertEquals(npjt, repo.getNamedParameterJdbcTemplate());
}
}