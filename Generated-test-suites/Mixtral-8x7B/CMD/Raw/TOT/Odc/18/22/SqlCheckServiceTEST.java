import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckService {

@Mock
private RuleService ruleService;

@Mock
private EnvironmentService environmentService;

@InjectMocks
private SqlCheckService sqlCheckService;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);
}

@Test
public void testCheck() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(0, results.size());
}

@Test
public void testCheckWithViolations() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("SELECT * FROM users;");

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(RuleMetadata.builder().type(RuleType.SQL_CHECK).name("test_rule").build());
rule.setLevel("ERROR");
rule.setEnabled(true);
rules.add(rule);

when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
when(sqlCheckRule.check(any(String.class))).thenReturn(Collections.singletonList(new CheckViolation()));
sqlCheckRules.add(sqlCheckRule);
when(SqlCheckRules.createByRule(any(), any(), any())).thenReturn(sqlCheckRule);

CheckViolation violation = new CheckViolation();
violation.setLevel("ERROR");
List<CheckViolation> violations = Collections.singletonList(violation);
when(sqlCheckRule.check(anyList(), any(SqlCheckContext.class))).thenReturn(violations);

List<CheckResult> results = sqlCheckService.check(session, req);
assertEquals(1, results.size());
assertTrue(results.get(0).getViolations().size() > 0);
}

@Test
public void testCheckWithEnvironment() {
ConnectionConfig config = new ConnectionConfig();
List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0, 20));
Long environmentId = 1L;
String databaseName = "test_db";

Environment environment = new Environment();
environment.setRulesetId(1L);

when(environmentService.detail(environmentId)).thenReturn(environment);

JdbcOperations jdbcOperations = mock(JdbcOperations.class);
SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
when(dataSource.getConnection()).thenReturn(jdbcOperations);
when(dataSource.getConnection().createStatement()).thenReturn(mock(java.sql.Statement.class));

OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
when(factory.getDataSource()).thenReturn(dataSource);
when(factory.resetSchema(any())).thenReturn(null);

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(RuleMetadata.builder().type(RuleType.SQL_CHECK).name("test_rule").build());
rule.setLevel("ERROR");
rule.setEnabled(true);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
when(sqlCheckRule.check(any(String.class))).thenReturn(Collections.singletonList(new CheckViolation()));
sqlCheckRules.add(sqlCheckRule);
when(SqlCheckRules.createByRule(any(), any(), any())).thenReturn(sqlCheckRule);

CheckViolation violation = new CheckViolation();
violation.setLevel("ERROR");
List<CheckViolation> violations = Collections.singletonList(violation);
when(sqlCheckRule.check(anyList(), any(SqlCheckContext.class))).thenReturn(violations);

List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
assertEquals(1, checkViolations.size());
assertTrue(checkViolations.get(0).getLevel().equals("ERROR"));
}

@Test
public void testGetRules() {
ConnectionSession session = mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), session);
assertEquals(0, rules.size());

List<Rule> ruleList = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(RuleMetadata.builder().type(RuleType.SQL_CHECK).name("test_rule").build());
rule.setLevel("ERROR");
rule.setEnabled(true);
ruleList.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(ruleList);

List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(DialectType.MYSQL, mock(JdbcOperations.class));

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(ruleList, session);
assertEquals(1, sqlCheckRules.size());
}

}