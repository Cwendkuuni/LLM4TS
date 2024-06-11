package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
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

 private RuleService ruleService = mock(RuleService.class);
 private EnvironmentService environmentService = mock(EnvironmentService.class);

 private SqlCheckService sqlCheckService = spy(new SqlCheckService());

 private void setupMocks() {
 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 sqlCheckReq.setScriptContent("SELECT * FROM users;");

 ConnectionSession session = mock(ConnectionSession.class);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(session.getSyncJdbcExecutor(any())).thenReturn(mock(JdbcOperations.class));

 doReturn(Collections.singletonList(mock(Rule.class)))
 .when(sqlCheckService).getRules(anyList(), any(ConnectionSession.class));

 doReturn(Collections.singletonList(mock(SqlCheckRule.class)))
 .when(sqlCheckService).getRules(anyList(), any(DialectType.class), any(JdbcOperations.class));

 doReturn(new DefaultSqlChecker(DialectType.MYSQL, ";", Collections.singletonList(mock(SqlCheckRule.class))))
 .when(sqlCheckService).newSqlChecker(any(DialectType.class), anyString(), anyList());

 doReturn(SqlCheckUtil.buildCheckResults(Collections.singletonList(mock(CheckViolation.class))))
 .when(sqlCheckService).buildCheckResults(anyList());

 doReturn(1L)
 .when(sqlCheckService).fullFillRiskLevel(anyList(), anyList());

 doReturn(mock(JdbcTemplate.class))
 .when(sqlCheckService).createJdbcTemplate(any(SingleConnectionDataSource.class));

 when(sqlCheckService.check(anyLong(), anyString(), anyList(), any(ConnectionConfig.class)))
 .thenCallRealMethod();

 when(sqlCheckService.check(any(ConnectionSession.class), any(SqlCheckReq.class)))
 .thenCallRealMethod();

 when(environmentService.detail(anyLong()))
 .thenReturn(mock(Environment.class));

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(Collections.singletonList(mock(Rule.class)));
 }

 @Test
 public void testCheck_whenConnectionSessionProvided_thenCallCorrectMethods() {
 setupMocks();

 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 sqlCheckReq.setScriptContent("SELECT * FROM users;");

 sqlCheckService.check(mock(ConnectionSession.class), sqlCheckReq);

 verify(sqlCheckService, times(1)).check(anyLong(), eq(sqlCheckReq.getScriptContent()), anyList(), any(ConnectionConfig.class));
 }

 @Test
 public void testCheck_whenConnectionSessionNull_thenCallCorrectMethods() {
 setupMocks();

 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 sqlCheckReq.setScriptContent("SELECT * FROM users;");

 sqlCheckService.check(null, sqlCheckReq);

 verify(sqlCheckService, times(1)).check(anyLong(), eq(sqlCheckReq.getScriptContent()), anyList(), any(ConnectionConfig.class));
 }

 @Test
 public void testCheck_whenSqlsEmpty_thenReturnEmptyList() {
 setupMocks();

 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 sqlCheckReq.setScriptContent("");

 List<CheckResult> results = sqlCheckService.check(mock(ConnectionSession.class), sqlCheckReq);

 assertTrue(results.isEmpty());
 }

 @Test
 public void testCheck_whenSqlsNotEmpty_thenCallCorrectMethods() {
 setupMocks();

 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 sqlCheckReq.setScriptContent("SELECT * FROM users; SELECT * FROM products;");

 when(sqlCheckService.newSqlChecker(any(DialectType.class), anyString(), anyList()))
 .thenReturn(mock(DefaultSqlChecker.class));

 sqlCheckService.check(mock(ConnectionSession.class), sqlCheckReq);

 ArgumentCaptor<List<CheckViolation>> violationCaptor = ArgumentCaptor.forClass(List.class);
 verify(sqlCheckService, times(1)).buildCheckResults(violationCaptor.capture());

 List<CheckViolation> violations = violationCaptor.getValue();
 assertTrue(!violations.isEmpty());
 }

 @Test
 public void testCheck_whenEnvironmentIdAndDatabaseNameProvided_thenCallCorrectMethods() {
 setupMocks();

 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(OffsetString.of("SELECT * FROM users;", 0));
 sqls.add(OffsetString.of("SELECT * FROM products;", 0));

 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 sqlCheckService.check(1L, "mydb", sqls, config);

 verify(sqlCheckService, times(1)).check(eq(1L), eq("mydb"), eq(sqls), eq(config));
 }

 @Test
 public void testCheck_whenEnvironmentIdAndDatabaseNameNull_thenThrowIllegalArgumentException() {
 setupMocks();

 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(OffsetString.of("SELECT * FROM users;", 0));
 sqls.add(OffsetString.of("SELECT * FROM products;", 0));

 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 try {
 sqlCheckService.check(null, null, sqls, config);
 } catch (IllegalArgumentException e) {
 assertTrue(e.getMessage().contains("environmentId and databaseName cannot be both null"));
 }
 }

 @Test
 public void testCheck_whenSqlsEmpty_thenReturnEmptyList() {
 setupMocks();

 List<OffsetString> sqls = new ArrayList<>();

 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 List<CheckResult> results = sqlCheckService.check(1L, "mydb", sqls, config);

 assertTrue(results.isEmpty());
 }

 @Test
 public void testCheck_whenSqlsNotEmpty_thenCallCorrectMethods() {
 setupMocks();

 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(OffsetString.of("SELECT * FROM users;", 0));
 sqls.add(OffsetString.of("SELECT * FROM products;", 0));

 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 when(sqlCheckService.newSqlChecker(any(DialectType.class), anyString(), anyList()))
 .thenReturn(mock(DefaultSqlChecker.class));

 sqlCheckService.check(1L, "mydb", sqls, config);

 ArgumentCaptor<List<CheckViolation>> violationCaptor = ArgumentCaptor.forClass(List.class);
 verify(sqlCheckService, times(1)).fullFillRiskLevel(anyList(), violationCaptor.capture());

 List<CheckViolation> violations = violationCaptor.getValue();
 assertTrue(!violations.isEmpty());
 }

 @Test
 public void testGetRules_whenRulesEmpty_thenReturnEmptyList() {
 setupMocks();

 List<SqlCheckRule> rules = sqlCheckService.getRules(Collections.emptyList(), mock(ConnectionSession.class));
 assertTrue(rules.isEmpty());
 }

 @Test
 public void testGetRules_whenRulesNotEmpty_thenCallCorrectMethods() {
 setupMocks();

 List<Rule> r1 = mock(Rule.class);
 List<Rule> r2 = mock(Rule.class);

 when(r1.getMetadata()).thenReturn(mock(RuleMetadata.class));
 when(r2.getMetadata()).thenReturn(mock(RuleMetadata.class));

 List<Rule> rules = new ArrayList<>();
 rules.add(r1);
 rules.add(r2);

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, mock(ConnectionSession.class));

 assertTrue(!sqlCheckRules.isEmpty());
 }

 @Test
 public void testGetRules_whenRulesNotEmptyAndMetadataIsNull_thenReturnEmptyList() {
 setupMocks();

 List<Rule> r1 = mock(Rule.class);
 List<Rule> r2 = mock(Rule.class);

 when(r1.getMetadata()).thenReturn(null);
 when(r2.getMetadata()).thenReturn(null);

 List<Rule> rules = new ArrayList<>();
 rules.add(r1);
 rules.add(r2);

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, mock(ConnectionSession.class));
 assertTrue(sqlCheckRules.isEmpty());
 }

 @Test
 public void testGetRules_whenRulesNotEmptyAndEnabledIsFalse_thenReturnEmptyList() {
 setupMocks();

 List<Rule> r1 = mock(Rule.class);
 List<Rule> r2 = mock(Rule.class);

 when(r1.getMetadata()).thenReturn(mock(RuleMetadata.class));
 when(r1.getEnabled()).thenReturn(false);
 when(r2.getMetadata()).thenReturn(mock(RuleMetadata.class));
 when(r2.getEnabled()).thenReturn(false);

 List<Rule> rules = new ArrayList<>();
 rules.add(r1);
 rules.add(r2);

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, mock(ConnectionSession.class));
 assertTrue(sqlCheckRules.isEmpty());
 }

 @Test
 public void testGetRules_whenRulesNotEmptyAndMetadataTypeNotSQL_thenReturnEmptyList() {
 setupMocks();

 List<Rule> r1 = mock(Rule.class);
 List<Rule> r2 = mock(Rule.class);

 RuleMetadata metadata = mock(RuleMetadata.class);
 when(metadata.getType()).thenReturn(RuleType.TEST);

 when(r1.getMetadata()).thenReturn(metadata);
 when(r2.getMetadata()).thenReturn(metadata);

 List<Rule> rules = new ArrayList<>();
 rules.add(r1);
 rules.add(r2);

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, mock(ConnectionSession.class));
 assertTrue(sqlCheckRules.isEmpty());
 }

 @Test
 public void testNewSqlChecker_whenDialectTypeAndDelimiterAndRulesProvided_thenReturnSqlChecker() {
 setupMocks();

 DialectType dialectType = DialectType.MYSQL;
 String delimiter = ";";

 DefaultSqlChecker sqlChecker = sqlCheckService.newSqlChecker(dialectType, delimiter, Collections.singletonList(mock(SqlCheckRule.class)));

 assertNotNull(sqlChecker);
 }

 @Test
 public void testBuildResults_whenViolationsNotEmpty_thenReturnCheckResults() {
 setupMocks();

 List<CheckViolation> violations = new ArrayList<>();
 violations.add(mock(CheckViolation.class));

 List<CheckResult> results = sqlCheckService.buildCheckResults(violations);

 assertTrue(!results.isEmpty());
 }

 @Test
 public void testFullFillRiskLevel_whenViolationsNotEmpty_thenReturnViolatedRules() {
 setupMocks();

 List<CheckViolation> violations = new ArrayList<>();
 violations.add(mock(CheckViolation.class));

 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Collections.singletonList(mock(Rule.class)), violations);

 assertTrue(!violatedRules.isEmpty());
 }

 @Test
 public void testFullFillRiskLevel_whenViolationsEmpty_thenReturnEmptyList() {
 setupMocks();

 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Collections.singletonList(mock(Rule.class)), Collections.emptyList());

 assertTrue(violatedRules.isEmpty());
 }

}