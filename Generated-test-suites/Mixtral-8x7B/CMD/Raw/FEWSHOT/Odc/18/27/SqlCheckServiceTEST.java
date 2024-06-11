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
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
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

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(req.getScriptContent()).thenReturn("SELECT * FROM users;");

 List<Rule> rules = new ArrayList<>();
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);

 SqlChecker sqlChecker = mock(DefaultSqlChecker.class);
 when(sqlChecker.check(anyString())).thenReturn(Collections.emptyList());

 sqlCheckService.setSqlChecker(sqlChecker);

 List<CheckViolation> checkViolations = sqlCheckService.check(session, req);

 verify(sqlChecker, times(1)).check(req.getScriptContent());
 }

 @Test
 public void testCheck_EmptyRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(req.getScriptContent()).thenReturn("SELECT * FROM users;");

 List<Rule> rules = Collections.emptyList();
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 sqlCheckService.getRules(rules, session);

 List<CheckViolation> checkViolations = sqlCheckService.check(session, req);

 assertTrue(checkViolations.isEmpty());
 }

 @Test
 public void testGetRules() {
 ConnectionSession session = mock(ConnectionSession.class);

 DialectType dialectType = DialectType.MYSQL;
 when(session.getDialectType()).thenReturn(dialectType);

 JdbcOperations jdbcOperations = mock(JdbcOperations.class);
 when(session.getSyncJdbcExecutor(anyString()))
 .thenReturn(jdbcOperations);

 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRuleFactory> candidates = new ArrayList<>();

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations))
 .thenReturn(candidates);

 when(SqlCheckRules.createByRule(candidates, dialectType, any(Rule.class)))
 .thenReturn(new SqlCheckRule(null));

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

 assertEquals(1, sqlCheckRules.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();

 Rule rule1 = new Rule();
 RuleViolation violation1 = new RuleViolation();
 violation1.setLevel("HIGH");
 rule1.setViolation(violation1);

 Rule rule2 = new Rule();
 RuleViolation violation2 = new RuleViolation();
 violation2.setLevel("LOW");
 rule2.setViolation(violation2);

 rules.add(rule1);
 rules.add(rule2);

 CheckViolation violation = new CheckViolation();
 violation.setType(rule1.getMetadata());

 violations.add(violation);

 List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

 assertEquals("HIGH", filledRules.get(0).getLevel());
 assertEquals("LOW", filledRules.get(1).getLevel());
 }

 @Test
 public void testFullFillRiskLevel_EmptyViolations() {
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();

 List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

 assertTrue(filledRules.isEmpty());
 }

 @Test
 public void testCheck_WithEnvironment() {
 ConnectionConfig config = mock(ConnectionConfig.class);
 DialectType dialectType = DialectType.MYSQL;
 when(config.getDialectType()).thenReturn(dialectType);

 SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
 when(dataSource.getConnection()).thenReturn(mock(javax.sql.Connection.class));

 JdbcTemplate jdbcTemplate = new JdbcTemplate();
 jdbcTemplate.setDataSource(dataSource);

 when(config.createDataSource()).thenReturn(dataSource);

 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT * FROM users;", 0));

 Environment env = mock(Environment.class);
 when(env.getRulesetId()).thenReturn(1L);

 when(environmentService.detail(anyLong())).thenReturn(env);

 List<Rule> rules = new ArrayList<>();
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
 when(factory.getDataSource()).thenReturn(dataSource);

 SqlCheckContext checkContext = mock(SqlCheckContext.class);
 when(checkContext.getTotal()).thenReturn(1L);

 DefaultSqlChecker sqlChecker = mock(DefaultSqlChecker.class);
 when(sqlChecker.check(anyList(), any(SqlCheckContext.class)))
 .thenReturn(Collections.emptyList());

 sqlCheckService.setSqlChecker(sqlChecker);

 List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, config);

 verify(sqlChecker, times(1)).check(sqls, checkContext);
 }

 @Test
 public void testGetRules_WithJdbcOperations() {
 ConnectionSession session = mock(ConnectionSession.class);

 DialectType dialectType = DialectType.MYSQL;
 when(session.getDialectType()).thenReturn(dialectType);

 JdbcOperations jdbcOperations = mock(JdbcOperations.class);
 when(session.getSyncJdbcExecutor(anyString()))
 .thenReturn(jdbcOperations);

 List<Rule> rules = new ArrayList<>();
 List<SqlCheckRuleFactory> candidates = new ArrayList<>();

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations))
 .thenReturn(candidates);

 when(SqlCheckRules.createByRule(candidates, dialectType, any(Rule.class)))
 .thenReturn(new SqlCheckRule(null));

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

 assertEquals(1, sqlCheckRules.size());
 }
}