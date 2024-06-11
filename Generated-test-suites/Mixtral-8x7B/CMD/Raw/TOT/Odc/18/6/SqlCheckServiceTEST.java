import com.oceanbase.odc.core.datasource.DSFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

private ApplicationContext applicationContext;
private SqlCheckService sqlCheckService;
private RuleService ruleService;
private EnvironmentService environmentService;
private ConnectionConfig connectionConfig;
private ConnectionSession connectionSession;
private JdbcOperations jdbcOperations;

@Before
public void setUp() {
applicationContext = mock(ApplicationContext.class);
ruleService = mock(RuleService.class);
environmentService = mock(EnvironmentService.class);
jdbcOperations = mock(JdbcOperations.class);
DSFactory dsFactory = mock(DSFactory.class);
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
when(dsFactory.getDataSource()).thenReturn(dataSource);
OBConsoleDataSourceFactory consoleDataSourceFactory = new OBConsoleDataSourceFactory(dsFactory);
connectionConfig = new ConnectionConfigBuilder().setDialectType(DialectType.MYSQL).setUsername("test")
.setPassword("test").setHost("localhost").setPort(3306).setDatabase("testdb").build();
connectionSession = ConnectionSessionUtil.createConnectionSession(consoleDataSourceFactory, connectionConfig,
null);
when(applicationContext.getBean(ConnectionSession.class)).thenReturn(connectionSession);
sqlCheckService = new SqlCheckService();
sqlCheckService.setApplicationContext(applicationContext);
sqlCheckService.setEnvironmentService(environmentService);
sqlCheckService.setRuleService(ruleService);
}

@After
public void tearDown() {
// Reset any static state
DSFactory.resetDSFactory();
}

@Test
public void testCheck() {
String scriptContent = "SELECT * FROM users WHERE id = 1;";
SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent(scriptContent);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(createRules());
List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
assertEquals(1, checkResults.size());
CheckResult checkResult = checkResults.get(0);
assertEquals("1", checkResult.getViolationId());
assertEquals("SQL01000", checkResult.getRuleType());
assertEquals("PreparedStatement should be used.", checkResult.getMessage());
}

@Test
public void testCheckWithEnvironment() {
String scriptContent = "SELECT * FROM users WHERE id = 1;";
List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("SELECT * FROM users WHERE id = 1;", 0, 63));
ConnectionConfig config = connectionConfig;
Long environmentId = 1L;
Environment environment = new Environment();
environment.setRulesetId(1L);
when(environmentService.detail(environmentId)).thenReturn(environment);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(createRules());
List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, "testdb", sqls, config);
assertEquals(1, checkViolations.size());
CheckViolation checkViolation = checkViolations.get(0);
assertEquals("1", checkViolation.getViolationId());
assertEquals("SQL01000", checkViolation.getType().getName());
assertEquals("PreparedStatement should be used.", checkViolation.getMessage());
}

@Test
public void testGetRules() {
List<Rule> rules = createRules();
DialectType dialectType = DialectType.MYSQL;
ConnectionConfig config = connectionConfig;
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config);
assertEquals(1, sqlCheckRules.size());
SqlCheckRule sqlCheckRule = sqlCheckRules.get(0);
assertEquals("SQL01000", sqlCheckRule.getType().getName());
}

private List<Rule> createRules() {
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
RuleMetadata metadata = new RuleMetadata();
metadata.setName("SQL01000");
metadata.setType(RuleType.SQL_CHECK);
rule.setMetadata(metadata);
rule.setLevel("ERROR");
rule.setEnabled(true);
rules.add(rule);
return rules;
}

}