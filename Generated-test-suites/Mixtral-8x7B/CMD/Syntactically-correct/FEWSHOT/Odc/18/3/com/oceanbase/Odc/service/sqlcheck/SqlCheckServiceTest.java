package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.OBConsoleDataSourceFactory;
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

@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckService {

private RuleService ruleService = mock(RuleService.class);
private EnvironmentService environmentService = mock(EnvironmentService.class);

@Test
public void testCheck() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("select * from table");
Long ruleSetId = 1L;
when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.getMetadata().setType(RuleType.SQL_CHECK);
rule.setEnabled(true);
rules.add(rule);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
sqlCheckRules.add(sqlCheckRule);
when(getRules(rules, session)).thenReturn(sqlCheckRules);
DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\n", sqlCheckRules);
List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());
assertEquals(0, checkViolations.size());
}

@Test
public void testCheckWithViolations() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("select * from table");
Long ruleSetId = 1L;
when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(new RuleMetadata());
rule.getMetadata().setType(RuleType.SQL_CHECK);
rule.setEnabled(true);
rules.add(rule);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
when(sqlCheckRule.check(anyString())).thenReturn(Collections.singletonList(new CheckViolation()));
sqlCheckRules.add(sqlCheckRule);
when(getRules(rules, session)).thenReturn(sqlCheckRules);
DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\n", sqlCheckRules);
List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());
assertEquals(1, checkViolations.size());
}

@Test
public void testCheckWithEmptyRules() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("select * from table");
when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
List<SqlCheckRule> sqlCheckRules = getRules(Collections.emptyList(), session);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testCheckWithNullRules() {
ConnectionSession session = mock(ConnectionSession.class);
SqlCheckReq req = new SqlCheckReq();
req.setScriptContent("select * from table");
when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(null);
List<SqlCheckRule> sqlCheckRules = getRules(null, session);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testGetRules() {
ConnectionSession session = mock(ConnectionSession.class);
List<Rule> rules = new ArrayList<>();
List<SqlCheckRuleFactory> candidates = new ArrayList<>();
DialectType dialectType = DialectType.MYSQL;
JdbcOperations jdbc = mock(JdbcOperations.class);
when(getRules(rules, dialectType, jdbc)).thenReturn(candidates);
List<SqlCheckRule> sqlCheckRules = getRules(rules, session);
assertEquals(0, sqlCheckRules.size());
}

@Test
public void testGetRulesWithViolations() {
ConnectionSession session = mock(ConnectionSession.class);
List<Rule> rules = new ArrayList<>();
List<SqlCheckRuleFactory> candidates = new ArrayList<>();
candidates.add(mock(SqlCheckRuleFactory.class));
DialectType dialectType = DialectType.MYSQL;
JdbcOperations jdbc = mock(JdbcOperations.class);
when(getRules(rules, dialectType, jdbc)).thenReturn(candidates);
List<SqlCheckRule> sqlCheckRules = getRules(rules, session);
assertNotNull(sqlCheckRules);
assertEquals(1, sqlCheckRules.size());
}

@Test
public void testCheckWithEnvironment() {
List<OffsetString> sqls = new ArrayList<>();
sqls.add(new OffsetString("select * from table"));
Long environmentId = 1L;
String databaseName = "testdb";
ConnectionConfig config = new ConnectionConfig();
config.setDialectType(DialectType.MYSQL);
when(environmentService.detail(environmentId)).thenReturn(new Environment());
List<Rule> rules = new ArrayList<>();
rules.add(new Rule());
OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
when(factory.getDataSource()).thenReturn(dataSource);
JdbcTemplate jdbc = mock(JdbcTemplate.class);
when(jdbc.queryForObject(anyString(), ArgumentMatchers.<Object>any(), ArgumentMatchers.<TypeMapper>any()))
.thenReturn("testdb");
when(new JdbcTemplate(dataSource)).thenReturn(jdbc);
List<SqlCheckRule> checkRules = getRules(rules, config.getDialectType(), jdbc);
assertNotNull(checkRules);
}

@Test
public void testFullFillRiskLevel() {
List<Rule> rules = new ArrayList<>();
rules.add(new Rule());
List<CheckViolation> violations = new ArrayList<>();
violations.add(new CheckViolation());
List<Rule> filledRules = fullFillRiskLevel(rules, violations);
assertEquals(1, filledRules.size());
}

@Test
public void testFullFillRiskLevelWithNullRules() {
List<Rule> filledRules = fullFillRiskLevel(null, new ArrayList<>());
assertEquals(0, filledRules.size());
}

@Test
public void testFullFillRiskLevelWithNullViolations() {
List<Rule> filledRules = fullFillRiskLevel(new ArrayList<>(), null);
assertEquals(0, filledRules.size());
}

private List<SqlCheckRule> getRules(List<Rule> rules, ConnectionSession session) {
// Unmodified code
}

private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
// Unmodified code
}
}