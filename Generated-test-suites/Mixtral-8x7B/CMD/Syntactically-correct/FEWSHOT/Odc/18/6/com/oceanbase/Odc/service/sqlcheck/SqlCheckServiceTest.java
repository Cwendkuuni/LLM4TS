package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private JdbcOperations jdbcOperations;
 @Mock
 private JdbcTemplate jdbcTemplate;
 @Mock
 private SingleConnectionDataSource dataSource;
 @Mock
 private OBConsoleDataSourceFactory obConsoleDataSourceFactory;
 @Mock
 private ConnectionSession session;
 @Mock
 private ConnectionConfig connectionConfig;
 @Mock
 private Environment environment;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @Before
 public void setup() {
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(session.getSyncJdbcExecutor(any())).thenReturn(jdbcOperations);
 when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
 when(obConsoleDataSourceFactory.getDataSource()).thenReturn(dataSource);
 when(dataSource.getConnection()).thenReturn(jdbcTemplate.getDataSource().getConnection());
 when(environmentService.detail(anyLong())).thenReturn(environment);
 }

 @Test
 public void testCheck() {
 SqlCheckReq req = new SqlCheckReq();
 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 SqlChecker sqlChecker = mock(DefaultSqlChecker.class);
 List<CheckViolation> checkViolations = new ArrayList<>();
 List<CheckResult> checkResults = sqlCheckService.check(session, req);
 assertEquals(Collections.emptyList(), checkResults);

 req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM table;");
 rules = Collections.singletonList(rule());
 sqlCheckRules = Collections.singletonList(sqlCheckRule());
 sqlChecker = mock(DefaultSqlChecker.class);
 when(sqlChecker.check(any())).thenReturn(checkViolations);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
 checkResults = sqlCheckService.check(session, req);
 assertEquals(Collections.emptyList(), checkResults);

 req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM table;");
 rules = Collections.singletonList(rule());
 sqlCheckRules = Collections.singletonList(sqlCheckRule());
 sqlChecker = mock(DefaultSqlChecker.class);
 checkViolations = Collections.singletonList(checkViolation());
 when(sqlChecker.check(any())).thenReturn(checkViolations);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
 when(sqlCheckRules.get(0).check(any())).thenReturn(checkViolations);
 checkResults = sqlCheckService.check(session, req);
 assertEquals(1, checkResults.size());
 }

 @Test
 public void testCheck_with_environment() {
 SqlCheckReq req = new SqlCheckReq();
 List<Rule> rules = new ArrayList<>();
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(offsetString());
 List<CheckViolation> checkViolations = new ArrayList<>();
 when(environmentService.detail(anyLong())).thenReturn(environment);
 when(environment.getRulesetId()).thenReturn(1L);

 rules = Collections.singletonList(rule());
 List<SqlCheckRule> sqlCheckRules = Collections.singletonList(sqlCheckRule());
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(obConsoleDataSourceFactory.getDataSource(any(), anyBoolean(), anyBoolean())).thenReturn(obConsoleDataSourceFactory);
 when(obConsoleDataSourceFactory.resetSchema(any())).thenReturn(obConsoleDataSourceFactory);
 when(sqlCheckService.getRules(rules, connectionConfig.getDialectType(), jdbcTemplate)).thenReturn(sqlCheckRules);
 when(sqlCheckRules.get(0).check(any())).thenReturn(checkViolations);
 List<CheckResult> checkResults = sqlCheckService.check(1L, null, sqls, connectionConfig);
 assertEquals(1, checkResults.size());
 }

 @Test
 public void testGetRules() {
 List<Rule> rules = new ArrayList<>();
 rules.add(rule());
 List<SqlCheckRule> sqlCheckRules = Collections.singletonList(sqlCheckRule());
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 when(SqlCheckRules.createByRule(any(), any(), any())).thenReturn(sqlCheckRules.get(0));
 assertEquals(sqlCheckRules, sqlCheckService.getRules(rules, session));
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 rules.add(rule());
 List<CheckViolation> checkViolations = new ArrayList<>();
 checkViolations.add(checkViolation());
 List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
 assertEquals("${" + CheckViolation.Type.SQL_INJECTION.getName() + "}", filledRules.get(0).getMetadata().getName());
 }

 private Rule rule() {
 Rule rule = new Rule();
 RuleMetadata metadata = new RuleMetadata();
 metadata.setType(RuleType.SQL_CHECK);
 metadata.setName("${" + CheckViolation.Type.SQL_INJECTION.getName() + "}");
 rule.setMetadata(metadata);
 rule.setLevel("ERROR");
 rule.setEnabled(true);
 return rule;
 }

 private SqlCheckRule sqlCheckRule() {
 SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
 when(sqlCheckRule.check(any())).thenCallRealMethod();
 return sqlCheckRule;
 }

 private CheckViolation checkViolation() {
 CheckViolation checkViolation = new CheckViolation();
 checkViolation.setType(CheckViolation.Type.SQL_INJECTION);
 return checkViolation;
 }

 private OffsetString offsetString() {
 OffsetString offsetString = mock(OffsetString.class);
 when(offsetString.getStartOffset()).thenReturn(0L);
 when(offsetString.getEndOffset()).thenReturn(10L);
 when(offsetString.getText()).thenReturn("SELECT * FROM table;");
 return offsetString;
 }
}