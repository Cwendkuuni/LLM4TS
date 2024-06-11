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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

@Mock
private RuleService ruleService;

@Mock
private EnvironmentService environmentService;

@InjectMocks
private SqlCheckService sqlCheckService;

private ConnectionConfig connectionConfig;

private ConnectionSession connectionSession;

private SingleConnectionDataSource dataSource;

private JdbcTemplate jdbcTemplate;

@Before
public void setup() {
dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("org.mariadb.jdbc.Driver");
dataSource.setUrl("jdbc:mariadb://localhost:3306/testdb");
dataSource.setUsername("test");
dataSource.setPassword("test");

jdbcTemplate = new JdbcTemplate(dataSource);

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.MYSQL);

connectionSession = new ConnectionSession();
connectionSession.setDialectType(DialectType.MYSQL);
ConnectionSessionUtil.setConnectionSession(ConnectionSessionConstants.CONSOLE_DS_KEY, connectionSession);
}

@Test
public void testCheck() {
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.setEnabled(true);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, ";", sqlCheckRules);
ReflectionTestUtils.setField(sqlCheckService, "sqlChecker", sqlChecker);

List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, req);

assertEquals(0, checkViolations.size());
}

@Test
public void testCheckEdgeCase() {
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.setEnabled(false);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, ";", sqlCheckRules);
ReflectionTestUtils.setField(sqlCheckService, "sqlChecker", sqlChecker);

List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, req);

assertEquals(0, checkViolations.size());
}

@Test(expected = RuntimeException.class)
public void testCheckException() {
List<Rule> rules = new ArrayList<>();

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq req = new SqlCheckReq();
req.setScriptContent(null);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, ";", sqlCheckRules);
ReflectionTestUtils.setField(sqlCheckService, "sqlChecker", sqlChecker);

sqlCheckService.check(connectionSession, req);
}

@Test
public void testCheck_WithEnvironment() {
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.setEnabled(true);
rules.add(rule);

when(environmentService.detail(anyLong())).thenReturn(getEnvironment());
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("SELECT * FROM users;", 0));

connectionSession.setSchema("testdb");
ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);

List<CheckViolation> checkViolations = sqlCheckService.check(connectionConfig, "testdb", sqls, connectionConfig);

assertEquals(0, checkViolations.size());
}

@Test
public void testCheck_WithEnvironment_EdgeCase() {
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.setEnabled(true);
rules.add(rule);

when(environmentService.detail(anyLong())).thenReturn(getEnvironment());
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("", 0));

connectionSession.setSchema("testdb");
ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);

List<CheckViolation> checkViolations = sqlCheckService.check(connectionConfig, "testdb", sqls, connectionConfig);

assertEquals(0, checkViolations.size());
}

@Test(expected = RuntimeException.class)
public void testCheck_WithEnvironment_Exception() {
List<Rule> rules = new ArrayList<>();

when(environmentService.detail(anyLong())).thenReturn(getEnvironment());
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<OffsetString> sqls = new ArrayList<>();
sqls.add(null);

connectionSession.setSchema("testdb");
ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);

sqlCheckService.check(connectionConfig, "testdb", sqls, connectionConfig);
}

private Environment getEnvironment() {
Environment environment = new Environment();
environment.setRulesetId(1L);
return environment;
}

}