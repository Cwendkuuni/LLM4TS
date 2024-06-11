
package com.oceanbase.odc.config.jpa;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.metamodel.Metamodel;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

    private EnhancedJpaRepository<Object, Serializable> repository;
    private EntityManager entityManager;
    private JpaEntityInformation<Object, Serializable> entityInformation;

    @Before
    public void setUp() {
        entityInformation = mock(JpaEntityInformation.class);
        entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        repository = new EnhancedJpaRepository<>(entityInformation, entityManager);
    }

    @Test
    public void testConstructor() {
        verify(entityManager).unwrap(SessionFactory.class);
    }

    @Test
    public void testGetDataSource() {
        SessionFactoryImpl sessionFactory = mock(SessionFactoryImpl.class);
        DatasourceConnectionProviderImpl connectionProvider = mock(DatasourceConnectionProviderImpl.class);
        DataSource dataSource = mock(DataSource.class);
        when(sessionFactory.getServiceRegistry()).thenReturn(mock(ServiceRegistry.class));
        when(sessionFactory.getServiceRegistry().getService(ConnectionProvider.class)).thenReturn(connectionProvider);
        when(connectionProvider.getDataSource()).thenReturn(dataSource);
        when(entityManager.getEntityManagerFactory()).thenReturn(sessionFactory);
        assertSame(dataSource, repository.getDataSource(entityManager));
    }

    @Test
    public void testGetEntityManager() {
        EntityManager entityManager = mock(EntityManager.class);
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        assertSame(entityManager, repository.getEntityManager());
    }

    @Test
    public void testGetJdbcTemplate() {
        JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
        assertSame(jdbcTemplate, repository.getJdbcTemplate());
    }

    @Test
    public void testGetNamedParameterJdbcTemplate() {
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = mock(NamedParameterJdbcTemplate.class);
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManager.getEntityManagerFactory()).thenReturn(mock(EntityManagerFactory.class));
        EnhancedJpaRepository<Object, Serializable> repository = new EnhancedJpaRepository<>(null, entityManager);
        repository.setNamedParameterJdbcTemplate(namedParameterJdbcTemplate);
        assertSame(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
    }
}
