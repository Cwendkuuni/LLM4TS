import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

@Mock
private RuleService ruleService;

@Mock
private EnvironmentService environmentService;

@InjectMocks
@Autowired
private SqlCheckService sqlCheckService = new SqlCheckService();

private ConnectionSession connectionSession;

private ConnectionConfig connectionConfig;

private JdbcOperations jdbcOperations;

@Before
public void setup() {
MockitoAnnotations.initMocks(this);

connectionSession = new ConnectionSession();
connectionSession.setDialectType(DialectType.MYSQL);
connectionSession.put(ConnectionSessionConstants.CONSOLE_DS_KEY, new SingleConnectionDataSource());

connectionConfig = new ConnectionConfig();
connectionConfig.setDialectType(DialectType.MYSQL);

jdbcOperations = new JdbcTemplate();
ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
}

@Test
public void testCheck() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata("test_rule1", null, RuleType.SQL_CHECK));
rules.add(rule1);

Rule rule2 = new Rule();
rule2.setMetadata(new RuleMetadata("test_rule2", null, RuleType.SQL_CHECK));
rules.add(rule2);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
sqlCheckRules.add(new SqlCheckRule() {
@Override
public CheckViolation check(String sql) {
return new CheckViolation("test_rule1", "test message1");
}
});

sqlCheckRules.add(new SqlCheckRule() {
@Override
public CheckViolation check(String sql) {
return new CheckViolation("test_rule2", "test message2");
}
});

ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

CheckResult result1 = new CheckResult();
result1.setRuleName("test_rule1");
result1.setMessage("test message1");
result1.setLevel(1);

CheckResult result2 = new CheckResult();
result2.setRuleName("test_rule2");
result2.setMessage("test message2");
result2.setLevel(1);

List<CheckResult> expectedResults = new ArrayList<>();
expectedResults.add(result1);
expectedResults.add(result2);

List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("select 1", null));

assertEquals(expectedResults, results);
}

@Test
public void testCheck_noRules() {
List<Rule> rules = new ArrayList<>();
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("select 1", null));

assertEquals(Collections.emptyList(), results);
}

@Test
public void testCheck_environment() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata("test_rule1", null, RuleType.SQL_CHECK));
rules.add(rule1);

when(environmentService.detail(anyLong())).thenReturn(new Environment());
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
sqlCheckRules.add(new SqlCheckRule() {
@Override
public CheckViolation check(String sql) {
return new CheckViolation("test_rule1", "test message1");
}
});

ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

CheckResult result1 = new CheckResult();
result1.setRuleName("test_rule1");
result1.setMessage("test message1");
result1.setLevel(1);

List<CheckResult> expectedResults = new ArrayList<>();
expectedResults.add(result1);

List<CheckResult> results = sqlCheckService.check(1L, "test_database",
Collections.singletonList(new OffsetString("select 1")), connectionConfig);

assertEquals(expectedResults, results);
}

@Test
public void testGetRules() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata("test_rule1", null, RuleType.SQL_CHECK));
rules.add(rule1);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

assertEquals(1, sqlCheckRules.size());
}

@Test
public void testFullFillRiskLevel() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata("test_rule1", null, RuleType.SQL_CHECK));
rule1.setLevel(1);
rules.add(rule1);

List<CheckViolation> violations = new ArrayList<>();
violations.add(new CheckViolation("test_rule1", "test message1"));

List<Rule> expectedRules = new ArrayList<>();
Rule newRule1 = new Rule();
newRule1.setLevel(1);
newRule1.setViolation(new RuleViolation(1, "test message1"));
expectedRules.add(newRule1);

List<Rule> rules2 = sqlCheckService.fullFillRiskLevel(rules, violations);

assertEquals(expectedRules, rules2);
}
}