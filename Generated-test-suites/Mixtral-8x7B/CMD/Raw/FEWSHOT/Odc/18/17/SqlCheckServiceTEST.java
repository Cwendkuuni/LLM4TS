package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

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
 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
 JdbcOperations jdbc = mock(JdbcOperations.class);
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbc);
 CheckViolations checkViolations = new CheckViolations();
 SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "go", sqlCheckRules);
 when(sqlChecker.check(anyString())).thenReturn(checkViolations);
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_withViolations() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 Rule rule = mock(Rule.class);
 RuleViolation violation = mock(RuleViolation.class);
 when(rule.getViolation()).thenReturn(violation);
 when(rule.getLevel()).thenReturn(1);
 CheckViolation checkViolation = new CheckViolation();
 checkViolation.setType(rule);
 checkViolation.setLevel(1);
 List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
 when(sqlCheckService.fullFillRiskLevel(rules, checkViolations)).thenReturn(Collections.singletonList(rule));
 JdbcOperations jdbc = mock(JdbcOperations.class);
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbc);
 SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "go", sqlCheckRules);
 when(sqlChecker.check(anyString())).thenReturn(checkViolations);
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getViolations().size());
 }

 @Test
 public void testCheck_withEmptyRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_withNullRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(null);
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheckEnvironment() {
 Long environmentId = 1L;
 String databaseName = "testdb";
 List<OffsetString> sqls = new ArrayList<>();
 ConnectionConfig config = mock(ConnectionConfig.class);
 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(environmentService.detail(environmentId)).thenReturn(mock(Environment.class));
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(sqlCheckService.getRules(rules, config.getDialectType(), any(JdbcOperations.class))).thenReturn(sqlCheckRules);
 SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
 JdbcTemplate jdbc = mock(JdbcTemplate.class);
 when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
 when(new OBConsoleDataSourceFactory(config, true, false).getDataSource()).thenReturn(dataSource);
 CheckViolations checkViolations = new CheckViolations();
 SqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, sqlCheckRules);
 when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheckEnvironment_withViolations() {
 Long environmentId = 1L;
 String databaseName = "testdb";
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("select * from table", 0, 10));
 ConnectionConfig config = mock(ConnectionConfig.class);
 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 Rule rule = mock(Rule.class);
 RuleViolation violation = mock(RuleViolation.class);
 when(rule.getViolation()).thenReturn(violation);
 when(rule.getLevel()).thenReturn(1);
 CheckViolation checkViolation = new CheckViolation();
 checkViolation.setType(rule);
 checkViolation.setLevel(1);
 List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);
 when(environmentService.detail(environmentId)).thenReturn(mock(Environment.class));
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(sqlCheckService.getRules(rules, config.getDialectType(), any(JdbcOperations.class))).thenReturn(sqlCheckRules);
 when(sqlCheckService.fullFillRiskLevel(rules, checkViolations)).thenReturn(Collections.singletonList(rule));
 SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
 JdbcTemplate jdbc = mock(JdbcTemplate.class);
 when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
 when(new OBConsoleDataSourceFactory(config, true, false).getDataSource()).thenReturn(dataSource);
 SqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, sqlCheckRules);
 when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getViolations().size());
 }

 @Test
 public void testGetRules() {
 List<Rule> rules = new ArrayList<>();
 ConnectionSession session = mock(ConnectionSession.class);
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbc = mock(JdbcOperations.class);
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbc);
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
 assertEquals(0, result.size());
 }

 @Test
 public void testGetRules_withRules() {
 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());
 ConnectionSession session = mock(ConnectionSession.class);
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbc = mock(JdbcOperations.class);
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbc);
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 sqlCheckRules.add(mock(SqlCheckRule.class));
 when(SqlCheckRules.createByRule(anyList(), any(DialectType.class), any(Rule.class))).thenReturn(sqlCheckRules.get(0));
 when(SqlCheckRules.getAllFactories(dialectType, jdbc)).thenReturn(Collections.singletonList(mock(SqlCheckRules.SqlCheckRuleFactory.class)));
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
 assertEquals(1, result.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();
 CheckViolation violation = new CheckViolation();
 violation.setLevel(1);
 violation.setType(new Rule());
 violations.add(violation);
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getLevel());
 }
}

class CheckViolations extends ArrayList<CheckViolation> {
}