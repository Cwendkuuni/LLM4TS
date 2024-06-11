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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

@Mock
private RuleService ruleService;

@Mock
private EnvironmentService environmentService;

private SqlCheckService sqlCheckService;

@Before
public void setUp() {
MockitoAnnotations.initMocks(this);
sqlCheckService = new SqlCheckService();
sqlCheckService.setRuleService(ruleService);
sqlCheckService.setEnvironmentService(environmentService);
}

@Test
public void testCheck() {
ConnectionSession session = createConnectionSession();
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rules.add(rule);

when(ruleService.list(1L, mock(QueryRuleMetadataParams.class))).thenReturn(rules);

List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
List<CheckResult> checkResults = SqlCheckUtil.buildCheckResults(checkViolations);

// Assert checkResults, checkViolations or specific fields as needed
}

@Test
public void testCheckWithNoRules() {
ConnectionSession session = createConnectionSession();
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

when(ruleService.list(1L, mock(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
List<CheckResult> checkResults = SqlCheckUtil.buildCheckResults(checkViolations);

assertEquals(0, checkResults.size());
}

@Test
public void testCheckForEnvironment() {
Long environmentId = 1L;
String databaseName = "testdb";
List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0));
ConnectionConfig config = new ConnectionConfig();

Environment env = new Environment();
env.setRulesetId(1L);
when(environmentService.detail(environmentId)).thenReturn(env);

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rules.add(rule);

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("org.h2.Driver");
dataSource.setUrl("jdbc:h2:mem:test");
dataSource.setUsername("sa");
dataSource.setPassword("");

JdbcOperations jdbc = new JdbcTemplate(dataSource);

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, config.getDialectType(), jdbc);

DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, sqlCheckRules);
List<CheckViolation> checkViolations = sqlChecker.check(sqls, mock(SqlCheckContext.class));

// Assert checkViolations or specific fields as needed
}

private ConnectionSession createConnectionSession() {
SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("org.h2.Driver");
dataSource.setUrl("jdbc:h2:mem:test");
dataSource.setUsername("sa");
dataSource.setPassword("");

ConnectionSession session = mock(ConnectionSession.class);
ConnectionSessionUtil.setConnection(session, dataSource);

return session;
}
}