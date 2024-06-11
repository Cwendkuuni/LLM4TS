import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

@Autowired
private RuleService ruleService;

@Autowired
private EnvironmentService environmentService;

private SqlCheckService sqlCheckService;

@Before
public void setUp() {
ruleService = mock(RuleService.class);
environmentService = mock(EnvironmentService.class);
sqlCheckService = new SqlCheckService();
ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);
}

@Test
public void testCheck_happyPath() {
ConnectionSession session = createConnectionSession();
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");
List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(0, results.size());
}

@Test
public void testCheck_withViolations() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
List<Rule> rules = createRules();
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users; -- This is a test comment");
List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(1, results.size());

CheckViolation violation = results.get(0).getViolations().get(0);
assertEquals("TestCommentRule", violation.getType().getName());
assertEquals("Comment is not allowed.", violation.getMessage());
}

@Test
public void testCheck_multipleViolations() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
List<Rule> rules = createRules();
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users; -- This is a test comment\n" +
"DELETE FROM users; -- This is another test comment");
List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(2, results.size());

List<CheckViolation> violations = results.stream().flatMap(r -> r.getViolations().stream()).collect(Collectors.toList());
assertEquals(2, violations.size());

CheckViolation violation1 = violations.get(0);
assertEquals("TestCommentRule", violation1.getType().getName());
assertEquals("Comment is not allowed.", violation1.getMessage());

CheckViolation violation2 = violations.get(1);
assertEquals("TestCommentRule", violation2.getType().getName());
assertEquals("Comment is not allowed.", violation2.getMessage());
}

@Test
public void testCheck_connectionException() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
when(jdbcOperations.execute(any(String.class))).thenThrow(new RuntimeException("Connection failed."));

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");
assertThrows(RuntimeException.class, () -> sqlCheckService.check(session, req));
}

@Test
public void testCheck_ruleServiceException() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenThrow(new RuntimeException("Rule service failed."));

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");
assertThrows(RuntimeException.class, () -> sqlCheckService.check(session, req));
}

@Test
public void testCheck_environmentServiceException() {
when(environmentService.detail(anyLong())).thenThrow(new RuntimeException("Environment service failed."));

ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.MYSQL);
List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;"));

assertThrows(RuntimeException.class, () -> sqlCheckService.check(1L, "test_db", sqls, config));
}

@Test
public void testGetRules_happyPath() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
List<Rule> rules = createRules();
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
when(jdbcOperations.queryForList(anyString(), any(Object[].class))).thenReturn(Collections.emptyList());

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testGetRules_ruleServiceException() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenThrow(new RuntimeException("Rule service failed."));

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
when(jdbcOperations.queryForList(anyString(), any(Object[].class))).thenReturn(Collections.emptyList());

assertThrows(RuntimeException.class, () -> sqlCheckService.getRules(Collections.emptyList(), session));
}

@Test
public void testGetRules_jdbcException() {
ConnectionSession session = createConnectionSession();
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
List<Rule> rules = createRules();
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
when(jdbcOperations.queryForList(anyString(), any(Object[].class))).thenThrow(new RuntimeException("JDBC failed."));

assertThrows(RuntimeException.class, () -> sqlCheckService.getRules(rules, session));
}

private ConnectionSession createConnectionSession() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
dataSource.setUrl("jdbc:mysql://localhost:3306/test_db");
dataSource.setUsername("test_user");
dataSource.setPassword("test_password");
ConnectionSession session = new ConnectionSession();
session.setDataSource(dataSource);
return session;
}

private List<Rule> createRules() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata());
rule1.getMetadata().setType(RuleType.SQL_CHECK);
rule1.setEnabled(true);
RuleViolation violation1 = new RuleViolation();
violation1.setMessage("Comment is not allowed.");
rule1.setViolation(violation1);
rules.add(rule1);
return rules;
}
}