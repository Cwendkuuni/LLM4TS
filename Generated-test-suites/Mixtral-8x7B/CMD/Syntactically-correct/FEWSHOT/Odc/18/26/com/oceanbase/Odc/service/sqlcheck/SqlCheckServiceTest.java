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
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
class SqlCheckServiceTest {

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
 SqlCheckReq req = mock(SqlCheckReq.class);
 Long ruleSetId = 1L;
 List<Rule> rules = new ArrayList<>();
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
 when(ruleService.list(ruleSetId, any())).thenReturn(rules);
 sqlCheckService.check(session, req);
 verify(ruleService, times(1)).list(ruleSetId, any());
 }

 @Test
 public void testCheckWithEmptyRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
 List<Rule> rules = Collections.emptyList();
 when(ruleService.list(anyLong(), any())).thenReturn(rules);
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(0, result.size());
 }

 @Test
 public void testGetRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 DialectType dialectType = DialectType.MYSQL;
 List<Rule> rules = new ArrayList<>();
 JdbcOperations jdbcOperations = mock(JdbcTemplate.class);
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
 when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(new ArrayList<>());
 sqlCheckService.getRules(rules, session);
 verify(jdbcOperations, times(1)).query(anyString(), any(), any());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();
 Rule rule = new Rule();
 rule.setLevel("HIGH");
 rules.add(rule);
 CheckViolation violation = new CheckViolation();
 violation.setType(RuleType.SQL_INJECTION);
 violation.setRuleName("SQL Injection");
 violations.add(violation);
 Map<String, Rule> name2RuleMap = Collections.singletonMap("${" + RuleType.SQL_INJECTION.getName() + "}", rule);
 sqlCheckService.fullFillRiskLevel(rules, violations);
 assertEquals("HIGH", violations.get(0).getLevel());
 }

 @Test
 public void testFullFillRiskLevelWithEmptyRules() {
 List<Rule> rules = Collections.emptyList();
 List<CheckViolation> violations = new ArrayList<>();
 sqlCheckService.fullFillRiskLevel(rules, violations);
 assertEquals(0, violations.size());
 }

 @Test
 public void testCheckWithEmptySqls() {
 Long environmentId = 1L;
 String databaseName = "test";
 List<OffsetString> sqls = Collections.emptyList();
 ConnectionConfig config = mock(ConnectionConfig.class);
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheckWithNonEmptySqls() {
 Long environmentId = 1L;
 String databaseName = "test";
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(OffsetString.of("SELECT * FROM users"));
 ConnectionConfig config = mock(ConnectionConfig.class);
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);
 assertEquals(0, result.size());
 }

 @Test
 public void testGetRulesWithEmptyRules() {
 List<Rule> rules = Collections.emptyList();
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcTemplate.class);
 List<SqlCheckRuleFactory> candidates = new ArrayList<>();
 when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(candidates);
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbcOperations);
 assertEquals(0, sqlCheckRules.size());
 }

 @Test
 public void testGetRulesWithNonEmptyRules() {
 List<Rule> rules = new ArrayList<>();
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcTemplate.class);
 List<SqlCheckRuleFactory> candidates = new ArrayList<>();
 when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(candidates);
 SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
 candidates.add(factory);
 when(factory.create(any(), any(), any(), any())).thenReturn(mock(SqlCheckRule.class));
 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbcOperations);
 assertEquals(1, sqlCheckRules.size());
 }
}