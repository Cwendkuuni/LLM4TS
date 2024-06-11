package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

public class SqlCheckServiceTest {

 private SqlCheckService sqlCheckService;
 private RuleService ruleService;
 private EnvironmentService environmentService;
 private ConnectionSession session;
 private SqlCheckReq req;
 private JdbcOperations jdbc;

 @Before
 public void setUp() {
 ruleService = mock(RuleService.class);
 environmentService = mock(EnvironmentService.class);
 session = mock(ConnectionSession.class);
 req = mock(SqlCheckReq.class);
 jdbc = mock(JdbcTemplate.class);

 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 dataSource.setTargetDataSource(new JdbcTemplate());
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbc);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);

 sqlCheckService = new SqlCheckService();
 sqlCheckService.ruleService = ruleService;
 sqlCheckService.environmentService = environmentService;
 }

 @Test
 public void testCheckEmptySqlCheckReq() {
 List<CheckResult> checkResults = sqlCheckService.check(session, req);
 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheckEmptyRules() {
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

 List<CheckResult> checkResults = sqlCheckService.check(session, req);
 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheckNoViolations() {
 List<Rule> rules = createRules(1);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 List<CheckResult> checkResults = sqlCheckService.check(session, req);
 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheckWithViolations() {
 List<Rule> rules = createRules(2);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 when(jdbc.queryForObject(anyString(), any(Object[].class), any(Class.class))).thenReturn("select");

 List<CheckViolation> checkViolations = new ArrayList<>();
 checkViolations.add(new CheckViolation("1", "test violation 1", 1));
 checkViolations.add(new CheckViolation("2", "test violation 2", 2));

 when(sqlCheckService.check(any(ConnectionSession.class), any(SqlCheckReq.class)))
 .thenReturn(checkViolations);

 List<CheckResult> checkResults = sqlCheckService.check(session, req);
 assertEquals(2, checkResults.size());
 }

 @Test
 public void testCheckEnvNull() {
 List<CheckResult> checkResults = sqlCheckService.check(null, null, null, mock(ConnectionConfig.class));
 assertNull(checkResults);
 }

 @Test
 public void testCheckEnvEmpty() {
 List<CheckResult> checkResults = sqlCheckService.check(session, null, Collections.emptyList(),
 mock(ConnectionConfig.class));
 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheckEmptySqls() {
 List<CheckResult> checkResults = sqlCheckService.check(session, null, Collections.emptyList(),
 mock(ConnectionConfig.class));
 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheckNoViolationsForEnv() {
 List<CheckResult> checkResults = sqlCheckService.check(session, null, createOffsetStrings(1),
 mock(ConnectionConfig.class));
 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheckWithViolationsForEnv() {
 List<CheckResult> checkResults = sqlCheckService.check(session, null, createOffsetStrings(2),
 mock(ConnectionConfig.class));
 assertNotNull(checkResults);
 assertEquals(2, checkResults.size());
 }

 private List<Rule> createRules(int count) {
 List<Rule> rules = new ArrayList<>();
 for (int i = 0; i < count; i++) {
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rules.add(rule);
 }
 return rules;
 }

 private List<OffsetString> createOffsetStrings(int count) {
 List<OffsetString> offsetStrings = new ArrayList<>();
 for (int i = 0; i < count; i++) {
 offsetStrings.add(new OffsetString("select", 0));
 }
 return offsetStrings;
 }
}