import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

@Autowired
private SqlCheckService sqlCheckService;

@Before
public void setUp() {
SqlCheckService sqlCheckService = new SqlCheckService();
ConnectionSession session = mock(ConnectionSession.class);
ReflectionTestUtils.setField(sqlCheckService, "ruleService", mock(RuleService.class));
ReflectionTestUtils.setField(sqlCheckService, "environmentService", mock(EnvironmentService.class));
ReflectionTestUtils.setField(sqlCheckService, "session", session);
}

@Test
public void testCheck() {
// Prepare test data
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

// Mock dependencies
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(any())).thenReturn(jdbcOperations);

List<Rule> rules = new ArrayList<>();
ReflectionTestUtils.setField(sqlCheckService, "rules", rules);

// Call the method to test
List<CheckResult> result = sqlCheckService.check(session, req);

// Assert the result
assertEquals(1, result.size());
assertEquals(0, result.get(0).getViolations().size());
}

@Test
public void testCheckWithViolations() {
// Prepare test data
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM non_existing_table;");

// Mock dependencies
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(jdbcOperations.queryForList(any())).thenReturn(Collections.emptyList());
when(session.getSyncJdbcExecutor(any())).thenReturn(jdbcOperations);

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setLevel("ERROR");
rule.setMetadata(new RuleMetadata("NonExistingTableRule", RuleType.SQL_CHECK, null));
rules.add(rule);
ReflectionTestUtils.setField(sqlCheckService, "rules", rules);

// Call the method to test
List<CheckResult> result = sqlCheckService.check(session, req);

// Assert the result
assertEquals(1, result.size());
assertEquals(1, result.get(0).getViolations().size());
CheckViolation violation = result.get(0).getViolations().get(0);
assertEquals("NonExistingTableRule", violation.getType().getName());
assertEquals("ERROR", violation.getLevel());
}

@Test
public void testCheckMultipleSqls() {
// Prepare test data
List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("SELECT * FROM users;", 0, 18));
sqls.add(new OffsetString("SELECT * FROM non_existing_table;", 19, 47));

// Mock dependencies
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(jdbcOperations.queryForList(any())).thenReturn(Collections.emptyList());
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
dataSource.setUrl("jdbc:mariadb://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");
JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
when(jdbcOperations.getJdbcTemplate()).thenReturn(jdbcTemplate);

ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.MYSQL);
config.setDataSource(dataSource);

// Call the method to test
List<CheckViolation> result = sqlCheckService.check(1L, "testdb", sqls, config);

// Assert the result
assertEquals(2, result.size());

CheckViolation firstViolation = result.get(0);
assertEquals("No violation found for the first SQL", 0, firstViolation.getLevel().length());

CheckViolation secondViolation = result.get(1);
assertEquals("NonExistingTableRule", secondViolation.getType().getName());
assertEquals("ERROR", secondViolation.getLevel());
}
}