import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

private SqlCheckService sqlCheckService;
private RuleService ruleService;
private EnvironmentService environmentService;
private ApplicationContext applicationContext;

@Before
public void setup() {
ruleService = mock(RuleService.class);
environmentService = mock(EnvironmentService.class);
applicationContext = mock(ApplicationContext.class);

sqlCheckService = new SqlCheckService();
ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);
ReflectionTestUtils.setField(sqlCheckService, "applicationContext", applicationContext);
}

@Test
public void testCheckWithEmptySession() {
ConnectionSession session = Mockito.mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(session.getRuleSetId()).thenReturn(1L);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(Collections.emptyList(), results);
}

@Test
public void testCheckWithEmptyRules() {
ConnectionSession session = Mockito.mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(session.getRuleSetId()).thenReturn(1L);

List<Rule> rules = new ArrayList<>();
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(Collections.emptyList(), results);
}

@Test
public void testCheckWithNonEmptyResults() {
ConnectionSession session = Mockito.mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(session.getRuleSetId()).thenReturn(1L);

List<Rule> rules = new ArrayList<>();
rules.add(new Rule());
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
sqlCheckRules.add(new SqlCheckRule());
ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

List<CheckViolation> checkViolations = new ArrayList<>();
checkViolations.add(new CheckViolation());
CheckResult checkResult = new CheckResult();
checkResult.setViolations(checkViolations);

List<CheckResult> results = new ArrayList<>();
results.add(checkResult);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
when(sqlCheckService.getRules(any(), any(), any(JdbcOperations.class))).thenReturn(sqlCheckRules);
when(sqlCheckService.check(any(), any())).thenReturn(checkViolations);

List<CheckResult> actualResults = sqlCheckService.check(session, req);
assertEquals(results, actualResults);
}

@Test
public void testCheckWithEmptySqls() {
Long environmentId = 1L;
String databaseName = "testdb";
List<OffsetString> sqls = new ArrayList<>();
ConnectionConfig config = new ConnectionConfig();

List<CheckViolation> checkViolations = new ArrayList<>();

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
DataAccessAdapter dataAccessAdapter = mock(DataAccessAdapter.class);
when(dataAccessAdapter.createJdbcOperations(any())).thenReturn(jdbcTemplate);

ReflectionTestUtils.setField(sqlCheckService, "dataAccessAdapter", dataAccessAdapter);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

when(environmentService.detail(environmentId)).thenReturn(new Environment());
when(sqlCheckService.getRules(any(), any(), any(JdbcOperations.class))).thenReturn(sqlCheckRules);
when(sqlCheckService.check(any(), any())).thenReturn(checkViolations);

List<CheckViolation> actualCheckViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
assertEquals(checkViolations, actualCheckViolations);
}

@Test
public void testCheckWithNonEmptySqls() {
Long environmentId = 1L;
String databaseName = "testdb";
List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("SELECT * FROM users;", 0, 24));
ConnectionConfig config = new ConnectionConfig();

List<CheckViolation> checkViolations = new ArrayList<>();
checkViolations.add(new CheckViolation());

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
DataAccessAdapter dataAccessAdapter = mock(DataAccessAdapter.class);
when(dataAccessAdapter.createJdbcOperations(any())).thenReturn(jdbcTemplate);

ReflectionTestUtils.setField(sqlCheckService, "dataAccessAdapter", dataAccessAdapter);

List<Rule> rules = new ArrayList<>();
rules.add(new Rule());

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
sqlCheckRules.add(new SqlCheckRule());

when(environmentService.detail(environmentId)).thenReturn(new Environment());
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
when(sqlCheckService.getRules(any(), any(), any(JdbcOperations.class))).thenReturn(sqlCheckRules);
when(sqlCheckService.check(any(), any())).thenReturn(checkViolations);

List<CheckViolation> actualCheckViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
assertEquals(checkViolations, actualCheckViolations);
}

@Test
public void testGetRulesWithEmptyRules() {
ConnectionSession session = Mockito.mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(Mockito.mock(JdbcOperations.class));

List<Rule> rules = new ArrayList<>();

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
assertEquals(Collections.emptyList(), sqlCheckRules);
}

@Test
public void testGetRulesWithNonEmptyRules() {
ConnectionSession session = Mockito.mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(Mockito.mock(JdbcOperations.class));

List<Rule> rules = new ArrayList<>();
rules.add(new Rule());
rules.add(new Rule());

List<SqlCheckRuleFactory> sqlCheckRuleFactories = new ArrayList<>();
sqlCheckRuleFactories.add(Mockito.mock(SqlCheckRuleFactory.class));
sqlCheckRuleFactories.add(Mockito.mock(SqlCheckRuleFactory.class));

when(sqlCheckService.getSqlCheckRules(any(), anyString())).thenReturn(sqlCheckRuleFactories);

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
assertEquals(2, sqlCheckRules.size());
}

@Test
public void testFullFillRiskLevelWithEmptyViolations() {
List<Rule> rules = new ArrayList<>();
rules.add(new Rule());
List<CheckViolation> violations = new ArrayList<>();

List<Rule> actualRules = sqlCheckService.fullFillRiskLevel(rules, violations);
assertEquals(0, actualRules.size());
}

@Test
public void testFullFillRiskLevelWithNonEmptyViolations() {
List<Rule> rules = new ArrayList<>();
rules.add(new Rule());
rules.add(new Rule());
List<CheckViolation> violations = new ArrayList<>();
violations.add(new CheckViolation());
violations.add(new CheckViolation());

List<Rule> actualRules = sqlCheckService.fullFillRiskLevel(rules, violations);
assertEquals(2, actualRules.size());
}
}