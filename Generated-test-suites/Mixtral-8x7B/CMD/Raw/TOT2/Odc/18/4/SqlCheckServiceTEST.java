import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

private SqlCheckService sqlCheckService;

@Before
public void setup() {
RuleService ruleService = mock(RuleService.class);
EnvironmentService environmentService = mock(EnvironmentService.class);
ConnectionSession session = mock(ConnectionSession.class);
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("org.h2.Driver");
dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
dataSource.setUsername("sa");
dataSource.setPassword("");
ReflectionTestUtils.setField(session, "dataSource", dataSource);
ReflectionTestUtils.setField(session, "dialectType", DialectType.H2);
ConnectionSessionUtil.setConnectionSession(session);

sqlCheckService = new SqlCheckService();
sqlCheckService.ruleService = ruleService;
sqlCheckService.environmentService = environmentService;
}

@Test
public void testCheck() {
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(sqlCheckService.getRules(Mockito.anyList(), Mockito.any(ConnectionSession.class)))
.thenReturn(Collections.emptyList());

ConnectionSessionUtil.setSyncJdbcExecutor(session -> jdbcOperations);

List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(0, results.size());
}

@Test
public void testCheckWithViolations() {
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.getMetadata().setType(RuleType.SQL_CHECK);
rule.setLevel("ERROR");
rule.setEnabled(true);
rules.add(rule);

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(sqlCheckService.getRules(rules, session)).thenReturn(Collections.singletonList(rule));

CheckViolation violation = new CheckViolation();
violation.setType(rule.getMetadata());
violation.setMessage("Violation message");
violation.setStart(10);
violation.setLength(5);

when(jdbcOperations.queryForList(Mockito.anyString(), Mockito.any(Object[].class)))
.thenReturn(Collections.singletonList(new ArrayList<>()));

when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Class.class)))
.thenReturn(1L);

ConnectionSessionUtil.setSyncJdbcExecutor(session -> jdbcOperations);

List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(1, results.size());
assertEquals("ERROR", results.get(0).getLevel());
assertEquals("Violation message", results.get(0).getViolations().get(0).getMessage());
}

@Test
public void testCheckWithEnvironments() {
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.getMetadata().setType(RuleType.SQL_CHECK);
rule.setLevel("ERROR");
rule.setEnabled(true);
rules.add(rule);

Environment environment = new Environment();
environment.setRulesetId(1L);

List<OffsetString> sqls = new ArrayList<>();
OffsetString sql = new OffsetString();
sql.setSql("SELECT * FROM users;");
sqls.add(sql);

ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.H2);

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(sqlCheckService.getRules(rules, config, jdbcOperations)).thenReturn(Collections.singletonList(rule));

CheckViolation violation = new CheckViolation();
violation.setType(rule.getMetadata());
violation.setMessage("Violation message");
violation.setStart(10);
violation.setLength(5);

when(jdbcOperations.queryForList(Mockito.anyString(), Mockito.any(Object[].class)))
.thenReturn(Collections.singletonList(new ArrayList<>()));

when(jdbcOperations.queryForObject(Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Class.class)))
.thenReturn(1L);

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("org.h2.Driver");
dataSource.setUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
dataSource.setUsername("sa");
dataSource.setPassword("");

ReflectionTestUtils.setField(config, "dataSource", dataSource);

List<CheckViolation> violations = sqlCheckService.check(environment.getRulesetId(), "testdb", sqls, config);
assertEquals(1, violations.size());
assertEquals("ERROR", violations.get(0).getLevel());
assertEquals("Violation message", violations.get(0).getMessage());
}
}