package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 public SqlCheckServiceTest() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

 sqlCheckService.check(session, req);

 verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
 verifyNoMoreInteractions(ruleService);
 }

 @Test
 public void testGetRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

 List<SqlCheckRule> result = sqlCheckService.getRules(session);

 verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
 verifyNoMoreInteractions(ruleService);
 assertEquals(0, result.size());
 }

 @Test
 public void testGetRulesWithJdbcOperations() {
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcTemplate.class);
 when(jdbcOperations.queryForObject(anyString(), any(Object[].class), any(Class.class)))
 .thenReturn("test");

 List<Rule> rules = new ArrayList<>();
 rules.add(mock(Rule.class));

 List<SqlCheckRuleFactory> candidates = new ArrayList<>();
 candidates.add(mock(SqlCheckRules.SqlCheckRuleFactory.class));

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

 verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
 verifyNoMoreInteractions(ruleService);
 verify(jdbcOperations, times(1)).queryForObject(anyString(), any(Object[].class), any(Class.class));
 assertEquals(1, result.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();

 sqlCheckService.fullFillRiskLevel(rules, violations);

 verifyNoMoreInteractions(rules);
 verifyNoMoreInteractions(violations);
 }

 @Test
 public void testFullFillRiskLevelWithData() {
 List<Rule> rules = new ArrayList<>();
 rules.add(mock(Rule.class));

 List<CheckViolation> violations = new ArrayList<>();
 violations.add(mock(CheckViolation.class));

 sqlCheckService.fullFillRiskLevel(rules, violations);

 verifyNoMoreInteractions(rules);
 verifyNoMoreInteractions(violations);
 }

 @Test
 public void testCheckWithSqlsAndConfig() {
 Long environmentId = 1L;
 String databaseName = "test";
 List<OffsetString> sqls = new ArrayList<>();
 ConnectionConfig config = mock(ConnectionConfig.class);

 sqlCheckService.check(environmentId, databaseName, sqls, config);

 verifyNoMoreInteractions(ruleService);
 verifyNoMoreInteractions(environmentService);
 }
}