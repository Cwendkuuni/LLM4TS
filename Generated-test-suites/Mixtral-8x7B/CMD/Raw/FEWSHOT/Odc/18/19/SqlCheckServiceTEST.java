package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

@Spy
private DialectType dialectType = DialectType.MYSQL;

@Mock
private RuleService ruleService;

@Mock
private EnvironmentService environmentService;

@InjectMocks
private SqlCheckService sqlCheckService;

@Test
public void testCheck_nullConnectionSession() {
List<CheckResult> result = sqlCheckService.check(null, new SqlCheckReq());
assertNull(result);
}

@Test
public void testCheck_nullSqlCheckReq() {
List<CheckResult> result = sqlCheckService.check(mock(ConnectionSession.class), null);
assertNull(result);
}

@Test
public void testCheck_emptyListOfRules() {
ConnectionSession session = mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(dialectType);
List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());
assertNotNull(result);
assertEquals(0, result.size());
}

@Test
public void testCheck_emptyListOfSqlCheckRules() {
ConnectionSession session = mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(dialectType);
when(ruleService.list(ArgumentMatchers.anyLong(), any()))
.thenReturn(Collections.emptyList());
List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());
assertNotNull(result);
assertEquals(0, result.size());
}

@Test
public void testCheck_nonEmptyListOfCheckViolations() {
ConnectionSession session = mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(dialectType);
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
.thenReturn(jdbcOperations);
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata("testRule", RuleType.SQL_CHECK));
rules.add(rule);
when(ruleService.list(ArgumentMatchers.anyLong(), any()))
.thenReturn(rules);
List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
when(sqlCheckRule.check(any())).thenReturn(Collections.singletonList(new CheckViolation()));
sqlCheckRules.add(sqlCheckRule);
when(SqlCheckRules.createByRule(any(), eq(dialectType), any()))
.thenReturn(sqlCheckRules);
List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());
assertNotNull(result);
assertEquals(1, result.size());
}

@Test
public void testCheck_nullEnvironmentId() {
List<CheckViolation> result = sqlCheckService.check(null, "databaseName",
Arrays.asList(new OffsetString("sql")), new ConnectionConfig());
assertNull(result);
}

@Test
public void testCheck_emptyListOfSqls() {
List<CheckViolation> result = sqlCheckService.check(1L, "databaseName",
Collections.emptyList(), new ConnectionConfig());
assertNull(result);
}

@Test
public void testCheck_nonEmptyListOfCheckViolations() {
Environment env = new Environment();
env.setRulesetId(1L);
when(environmentService.detail(1L)).thenReturn(env);
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata("testRule", RuleType.SQL_CHECK));
rules.add(rule);
List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
when(sqlCheckRule.check(any())).thenReturn(Collections.singletonList(new CheckViolation()));
sqlCheckRules.add(sqlCheckRule);
when(SqlCheckRules.createByRule(any(), eq(DialectType.MYSQL), any()))
.thenReturn(sqlCheckRules);
SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
when(jdbcTemplate.query(any(), any()))
.thenReturn(Collections.emptyList());
when(new OBConsoleDataSourceFactory(any(), any(), any())
.getDataSource())
.thenReturn(dataSource);
List<CheckViolation> result = sqlCheckService.check(1L, "databaseName",
Arrays.asList(new OffsetString("sql")), new ConnectionConfig());
assertNotNull(result);
assertEquals(1, result.size());
}

@Test
public void testGetRules_nullRules() {
ConnectionSession session = mock(ConnectionSession.class);
List<Rule> result = sqlCheckService.getRules(null, session);
assertNull(result);
}

@Test
public void testGetRules_nullSession() {
List<Rule> result = sqlCheckService.getRules(Collections.emptyList(), null);
assertNull(result);
}

@Test
public void testGetRules_nonEmptyListOfRules() {
ConnectionSession session = mock(ConnectionSession.class);
when(session.getDialectType()).thenReturn(dialectType);
JdbcOperations jdbcOperations = mock(JdbcOperations.class);
when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
.thenReturn(jdbcOperations);
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata("testRule", RuleType.SQL_CHECK));
rules.add(rule);
List<SqlCheckRuleFactory> sqlCheckRuleFactories = new ArrayList<>();
SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
when(sqlCheckRuleFactory.support(dialectType)).thenReturn(true);
sqlCheckRuleFactories.add(sqlCheckRuleFactory);
List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
when(sqlCheckRule.check(any())).thenReturn(Collections.emptyList());
sqlCheckRules.add(sqlCheckRule);
when(SqlCheckRules.createByRule(eq(sqlCheckRuleFactories), eq(dialectType), any()))
.thenReturn(sqlCheckRules);
when(ruleService.list(ArgumentMatchers.anyLong(), any()))
.thenReturn(rules);
List<Rule> result = sqlCheckService.getRules(rules, session);
assertNotNull(result);
assertEquals(1, result.size());
}

@Test
public void testFullFillRiskLevel_nullRules() {
List<Rule> result = sqlCheckService.fullFillRiskLevel(null, Collections.emptyList());
assertNull(result);
}

@Test
public void testFullFillRiskLevel_nullViolations() {
List<Rule> result = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), null);
assertNull(result);
}

@Test
public void testFullFillRiskLevel_nonEmptyListOfRulesAndViolations() {
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata("testRule", RuleType.SQL_CHECK));
rule.setLevel("LOW");
rules.add(rule);
List<CheckViolation> violations = new ArrayList<>();
CheckViolation violation = new CheckViolation();
violation.setType(rule.getMetadata());
violation.setLevel("MEDIUM");
violations.add(violation);
List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
assertNotNull(result);
assertEquals(1, result.size());
assertEquals("MEDIUM", result.get(0).getLevel());
}
}