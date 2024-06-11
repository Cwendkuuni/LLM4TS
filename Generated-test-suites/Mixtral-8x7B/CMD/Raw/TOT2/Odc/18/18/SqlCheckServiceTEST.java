import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
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
import java.util.stream.Collectors;

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

private JdbcOperations jdbcOperations;

@Before
public void setUp() {
connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.MYSQL);

jdbcOperations = new JdbcTemplate();
ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);

connectionSession = new ConnectionSession();
connectionSession.setDialectType(DialectType.MYSQL);

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("com.mysql.jdbc.Driver");
dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");

connectionSession.setSyncJdbcExecutor(session -> jdbcOperations);
}

@Test
public void testCheck() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata());
rule1.getMetadata().setName("rule1");
rule1.setLevel(1);
rule1.setEnabled(true);
rules.add(rule1);

Rule rule2 = new Rule();
rule2.setMetadata(new RuleMetadata());
rule2.getMetadata().setName("rule2");
rule2.setLevel(2);
rule2.setEnabled(true);
rules.add(rule2);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent("SELECT * FROM users;");

List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
assertEquals(2, checkResults.size());

CheckViolation checkViolation1 = new CheckViolation();
checkViolation1.setType(rule1.getMetadata());
checkViolation1.setLevel(1);

CheckViolation checkViolation2 = new CheckViolation();
checkViolation2.setType(rule2.getMetadata());
checkViolation2.setLevel(2);

assertEquals(checkViolation1, checkResults.get(0).getViolations().get(0));
assertEquals(checkViolation2, checkResults.get(1).getViolations().get(0));
}

@Test
public void testCheckWithNoRules() {
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent("SELECT * FROM users;");

List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Test
public void testCheckWithEmptyScriptContent() {
SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent("");

List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Test
public void testCheckWithNullScriptContent() {
SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent(null);

List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Test
public void testCheckForMultipleSqls() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata());
rule1.getMetadata().setName("rule1");
rule1.setLevel(1);
rule1.setEnabled(true);
rules.add(rule1);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

OffsetString offsetString1 = new OffsetString();
offsetString1.setSql("SELECT * FROM users;");

OffsetString offsetString2 = new OffsetString();
offsetString2.setSql("SELECT * FROM products;");

List<OffsetString> sqls = new ArrayList<>();
sqls.add(offsetString1);
sqls.add(offsetString2);

when(environmentService.detail(anyLong())).thenReturn(new Environment());

ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.MYSQL);

OBConsoleDataSourceFactory factory = new OBConsoleDataSourceFactory(config, true, false);
factory.resetSchema(origin -> "testdb");

SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
dataSource.setDriverClassName("com.mysql.jdbc.Driver");
dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
dataSource.setUsername("testuser");
dataSource.setPassword("testpassword");

factory.setDataSource(dataSource);

JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);

ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);

List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, config);
assertEquals(2, checkViolations.size());
}

@Test
public void testCheckForEmptySqls() {
List<OffsetString> sqls = new ArrayList<>();

List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);
assertEquals(0, checkViolations.size());
}

@Test
public void testCheckForNullSqls() {
List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", null, connectionConfig);
assertEquals(0, checkViolations.size());
}

@Test
public void testGetRules() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata());
rule1.getMetadata().setName("rule1");
rule1.setLevel(1);
rule1.setEnabled(true);
rules.add(rule1);

Rule rule2 = new Rule();
rule2.setMetadata(new RuleMetadata());
rule2.getMetadata().setName("rule2");
rule2.setLevel(2);
rule2.setEnabled(true);
rules.add(rule2);

List<SqlCheckRuleFactory> candidates = new ArrayList<>();

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
assertEquals(2, sqlCheckRules.size());
}

@Test
public void testGetRulesWithEmptyRules() {
List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testFullFillRiskLevel() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata());
rule1.getMetadata().setName("rule1");
rule1.setLevel(1);
rule1.setEnabled(true);
rules.add(rule1);

List<CheckViolation> violations = new ArrayList<>();
CheckViolation violation1 = new CheckViolation();
violation1.setType(rule1.getMetadata());
violation1.setLevel(1);
violations.add(violation1);

List<Rule> fullFilledRules = sqlCheckService.fullFillRiskLevel(rules, violations);
assertEquals(1, fullFilledRules.size());

Rule fullFilledRule = fullFilledRules.get(0);
assertEquals(1, fullFilledRule.getLevel().intValue());
CheckViolation fullFilledViolation = fullFilledRule.getViolation();
assertEquals(violation1.getType(), fullFilledViolation.getType());
assertEquals(violation1.getLevel(), fullFilledViolation.getLevel());
}

@Test
public void testFullFillRiskLevelWithEmptyViolations() {
List<Rule> fullFilledRules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), new ArrayList<>());
assertEquals(0, fullFilledRules.size());
}

@Test
public void testFullFillRiskLevelWithEmptyRules() {
List<Rule> fullFilledRules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), new ArrayList<>());
assertEquals(0, fullFilledRules.size());
}

@Test
public void testFullFillRiskLevelWithNullViolations() {
List<Rule> fullFilledRules = sqlCheckService.fullFillRiskLevel(rules, null);
assertEquals(0, fullFilledRules.size());
}
}