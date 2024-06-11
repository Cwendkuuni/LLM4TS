import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.internal.DatasourceConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {
private EnhancedJpaRepository<Object, Serializable> repository;
private JpaEntityInformation<Object, Serializable> entityInformation;
private EntityManagerFactory emf;
private EntityManager em;
private DataSource dataSource;
private ConnectionProvider cp;

@Before
public void setup() {
entityInformation = mock(JpaEntityInformation.class);
emf = mock(EntityManagerFactory.class);
em = mock(EntityManager.class);
dataSource = mock(DataSource.class);
cp = mock(ConnectionProvider.class);

when(em.getEntityManagerFactory()).thenReturn(emf);
when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(mock(SessionFactoryImpl.class));
when(emf.unwrap(SessionFactoryImpl.class).getServiceRegistry())
.thenReturn(mock(SessionFactoryImpl.class).getServiceRegistry());
when(emf.unwrap(SessionFactoryImpl.class).getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(cp);
when(cp.getDataSource()).thenReturn(dataSource);

repository = new EnhancedJpaRepository<Object, Serializable>(entityInformation, em);
}

@Test
public void testGetEntityManager() {
assertEquals(em, repository.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = repository.getJdbcTemplate();
assertEquals(namedParameterJdbcTemplate.getJdbcOperations(), jdbcTemplate);
}

@Test
public void testGetNamedParameterJdbcTemplate() {
assertEquals(namedParameterJdbcTemplate, repository.getNamedParameterJdbcTemplate());
}

class SessionFactoryImpl {}
}