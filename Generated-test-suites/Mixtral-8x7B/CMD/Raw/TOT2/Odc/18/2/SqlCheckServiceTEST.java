import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
 sqlCheckService.ruleService = ruleService;
 sqlCheckService.environmentService = environmentService;
 }

 @Test
 public void testCheck() {
 // Create mock objects
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();

 // Set up expected return values for mock objects
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(ruleService.list(1L, any())).thenReturn(new ArrayList<>());

 // Call the method to be tested
 List<CheckResult> result = sqlCheckService.check(session, req);

 // Verify that appropriate methods were called on mock objects
 verify(ConnectionSessionUtil).getRuleSetId(session);
 verify(ruleService).list(1L, any());

 // Assert that the result is as expected
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testCheckWithViolations() {
 // Create mock objects
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 List<Rule> rules = new ArrayList<>();
 CheckViolation violation1 = new CheckViolation();
 CheckViolation violation2 = new CheckViolation();
 List<CheckViolation> violations = new ArrayList<>();
 violations.add(violation1);
 violations.add(violation2);

 // Set up expected return values for mock objects
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(ruleService.list(1L, any())).thenReturn(rules);

 // Configure mock behavior for fullFillRiskLevel
 List<Rule> violatedRules = new ArrayList<>();
 Rule rule1 = new Rule();
 Rule rule2 = new Rule();
 violatedRules.add(rule1);
 violatedRules.add(rule2);
 when(sqlCheckService.fullFillRiskLevel(rules, violations)).thenReturn(violatedRules);

 // Call the method to be tested
 List<CheckResult> result = sqlCheckService.check(session, req);

 // Verify that appropriate methods were called on mock objects
 verify(ConnectionSessionUtil).getRuleSetId(session);
 verify(ruleService).list(1L, any());
 verify(sqlCheckService).fullFillRiskLevel(rules, violations);

 // Assert that the result is as expected
 assertEquals(2, result.size());
 }

 @Test
 public void testCheckForMultipleSqls() {
 // Create mock objects
 Long environmentId = 1L;
 String databaseName = "testDB";
 List<OffsetString> sqls = new ArrayList<>();
 OffsetString sql1 = new OffsetString("select * from table1;");
 OffsetString sql2 = new OffsetString("insert into table2 values (1, 'test');");
 sqls.add(sql1);
 sqls.add(sql2);
 ConnectionConfig config = new ConnectionConfig();

 // Set up expected return values for mock objects
 Environment env = mock(Environment.class);
 when(env.getRulesetId()).thenReturn(1L);
 when(environmentService.detail(environmentId)).thenReturn(env);

 List<Rule> rules = new ArrayList<>();
 when(ruleService.list(1L, any())).thenReturn(rules);

 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
 JdbcOperations jdbcOperations = jdbcTemplate;

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(sqlCheckService.getRules(rules, config.getDialectType(), jdbcOperations)).thenReturn(sqlCheckRules);

 // Call the method to be tested
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

 // Verify that appropriate methods were called on mock objects
 verify(environmentService).detail(environmentId);
 verify(ruleService).list(1L, any());
 verify(sqlCheckService).getRules(rules, config.getDialectType(), jdbcOperations);

 // Assert that the result is as expected
 assertEquals(0, result.size());
 }

 @Test
 public void testCheckForMultipleSqlsWithViolations() {
 // Create mock objects
 Long environmentId = 1L;
 String databaseName = "testDB";
 List<OffsetString> sqls = new ArrayList<>();
 OffsetString sql1 = new OffsetString("select * from table1;");
 OffsetString sql2 = new OffsetString("insert into table2 values (1, 'test');");
 sqls.add(sql1);
 sqls.add(sql2);
 ConnectionConfig config = new ConnectionConfig();

 // Set up expected return values for mock objects
 Environment env = mock(Environment.class);
 when(env.getRulesetId()).thenReturn(1L);
 when(environmentService.detail(environmentId)).thenReturn(env);

 List<Rule> rules = new ArrayList<>();
 when(ruleService.list(1L, any())).thenReturn(rules);

 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
 JdbcOperations jdbcOperations = jdbcTemplate;

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(sqlCheckService.getRules(rules, config.getDialectType(), jdbcOperations)).thenReturn(sqlCheckRules);

 CheckViolation violation1 = new CheckViolation();
 CheckViolation violation2 = new CheckViolation();
 List<CheckViolation> violations = new ArrayList<>();
 violations.add(violation1);
 violations.add(violation2);

 // Configure mock behavior for fullFillRiskLevel
 List<Rule> violatedRules = new ArrayList<>();
 Rule rule1 = new Rule();
 Rule rule2 = new Rule();
 violatedRules.add(rule1);
 violatedRules.add(rule2);
 when(sqlCheckService.fullFillRiskLevel(rules, violations)).thenReturn(violatedRules);

 // Call the method to be tested
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

 // Verify that appropriate methods were called on mock objects
 verify(environmentService).detail(environmentId);
 verify(ruleService).list(1L, any());
 verify(sqlCheckService).getRules(rules, config.getDialectType(), jdbcOperations);
 verify(sqlCheckService).fullFillRiskLevel(rules, violations);

 // Assert that the result is as expected
 assertEquals(2, result.size());
 }

 @Test
 public void testGetRules() {
 // Create mock objects
 List<Rule> rules = new ArrayList<>();
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcOperations.class);

 // Call the method to be tested
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

 // Assert that the result is as expected
 assertEquals(0, result.size());
 }

 @Test
 public void testGetRulesWithSqlCheckRules() {
 // Create mock objects
 List<Rule> rules = new ArrayList<>();
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcOperations.class);

 SqlCheckRuleFactory factory1 = mock(SqlCheckRuleFactory.class);
 SqlCheckRuleFactory factory2 = mock(SqlCheckRuleFactory.class);
 List<SqlCheckRuleFactory> candidates = new ArrayList<>();
 candidates.add(factory1);
 candidates.add(factory2);

 Rule rule1 = new Rule();
 Rule rule2 = new Rule();
 rules.add(rule1);
 rules.add(rule2);

 when(factory1.createByRule(candidates, dialectType, rule1)).thenReturn(new SqlCheckRule());
 when(factory2.createByRule(candidates, dialectType, rule2)).thenReturn(new SqlCheckRule());

 // Call the method to be tested
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

 // Assert that the result is as expected
 assertEquals(2, result.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 // Create mock objects
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();
 CheckViolation violation1 = new CheckViolation();
 CheckViolation violation2 = new CheckViolation();
 violations.add(violation1);
 violations.add(violation2);

 // Set up expected return values for mock objects
 Rule rule1 = new Rule();
 Rule rule2 = new Rule();
 rule1.setLevel("INFO");
 rule2.setLevel("ERROR");

 Map<String, Rule> name2RuleMap = new HashMap<>();
 name2RuleMap.put("${SQL_CHECK_RULE1}", rule1);
 name2RuleMap.put("${SQL_CHECK_RULE2}", rule2);

 // Call the method to be tested
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

 // Assert that the result is as expected
 assertEquals(2, result.size());
 assertEquals("INFO", result.get(0).getLevel());
 assertEquals("ERROR", result.get(1).getLevel());
 }
}