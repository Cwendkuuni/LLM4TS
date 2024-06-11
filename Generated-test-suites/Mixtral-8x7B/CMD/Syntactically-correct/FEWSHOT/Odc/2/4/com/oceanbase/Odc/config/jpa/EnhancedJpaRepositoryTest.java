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
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class EnhancedJpaRepositoryTest {

private EntityManagerFactory emf;
private JpaEntityInformation<Object, Serializable> entityInfo;
private EntityManager em;
private DataSource dataSource;
private ConnectionProvider cp;
private SessionFactoryImpl sf;
private EnhancedJpaRepository<Object, Serializable> repo;

@Before
public void setup() {
emf = mock(EntityManagerFactory.class);
entityInfo = mock(JpaEntityInformation.class);
em = mock(EntityManager.class);
dataSource = mock(DataSource.class, Answers.RETURNS_DEEP_STUBS);
cp = mock(ConnectionProvider.class);
sf = mock(SessionFactoryImpl.class);

when(emf.unwrap(SessionFactoryImpl.class)).thenReturn(sf);
when(sf.getServiceRegistry().getService(ConnectionProvider.class))
.thenReturn(cp);
when(cp.getDataSource()).thenReturn(dataSource);

repo = new EnhancedJpaRepository<Object, Serializable>(entityInfo, em) {
};
}

@Test
public void testGetEntityManager() {
assertEquals(em, repo.getEntityManager());
}

@Test
public void testGetJdbcTemplate() {
JdbcTemplate jdbcTemplate = repo.getJdbcTemplate();
assertEquals(dataSource, jdbcTemplate.getDataSource());
}

@Test
public void testGetNamedParameterJdbcTemplate() {
NamedParameterJdbcTemplate namedParameterJdbcTemplate = repo.getNamedParameterJdbcTemplate();
assertEquals(dataSource, namedParameterJdbcTemplate.getJdbcOperations().getDataSource());
}

}