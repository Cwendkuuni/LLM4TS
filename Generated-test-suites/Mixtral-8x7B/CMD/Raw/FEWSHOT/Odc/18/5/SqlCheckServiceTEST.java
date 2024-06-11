package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
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
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
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

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @Test
 public void testCheck_connectionSession_returnsEmptyListWhenRuleSetIdIsNull() {
 // Arrange
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

 // Act
 List<CheckResult> result = sqlCheckService.check(session, req);

 // Assert
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testCheck_connectionSession_returnsCheckResults() {
 // Arrange
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();

 Long ruleSetId = 1L;
 List<Rule> rules = new ArrayList<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
 when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 // Act
 List<CheckResult> result = sqlCheckService.check(session, req);

 // Assert
 assertNotNull(result);
 }

 @Test
 public void testCheck_connectionConfig_returnsEmptyListWhenSqlsIsEmpty() {
 // Arrange
 ConnectionConfig config = new ConnectionConfig();
 Long environmentId = 1L;
 String databaseName = "test";
 List<OffsetString> sqls = new ArrayList<>();

 when(environmentService.detail(environmentId)).thenReturn(mock(Environment.class));

 // Act
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

 // Assert
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testCheck_connectionConfig_returnsCheckViolations() {
 // Arrange
 ConnectionConfig config = new ConnectionConfig();
 Long environmentId = 1L;
 String databaseName = "test";
 List<OffsetString> sqls = Collections.singletonList(new OffsetString("test sql", 0));

 Environment env = mock(Environment.class);
 when(env.getRulesetId()).thenReturn(1L);
 when(environmentService.detail(environmentId)).thenReturn(env);

 List<Rule> rules = new ArrayList<>();

 SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
 JdbcTemplate jdbc = mock(JdbcTemplate.class);
 when(dataSource.getJdbcTemplate()).thenReturn(jdbc);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 // Act
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

 // Assert
 assertNotNull(result);
 }

 @Test
 public void testGetRules_connectionSession_returnsEmptyListWhenRulesIsEmpty() {
 // Arrange
 ConnectionSession session = mock(ConnectionSession.class);
 List<Rule> rules = new ArrayList<>();

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 // Act
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

 // Assert
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetRules_connectionSession_returnsListOfSqlCheckRule() {
 // Arrange
 ConnectionSession session = mock(ConnectionSession.class);
 List<Rule> rules = createRules();

 List<SqlCheckRuleFactory> candidates = createSqlCheckRuleFactories();

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 // Act
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

 // Assert
 assertNotNull(result);
 assertEquals(1, result.size());
 }

 @Test
 public void testGetRules_connectionConfig_returnsEmptyListWhenRulesIsEmpty() {
 // Arrange
 ConnectionConfig config = new ConnectionConfig();
 List<Rule> rules = new ArrayList<>();

 List<SqlCheckRuleFactory> candidates = createSqlCheckRuleFactories();

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 // Act
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, config, mock(JdbcOperations.class));

 // Assert
 assertEquals(Collections.emptyList(), result);
 }

 @Test
 public void testGetRules_connectionConfig_returnsListOfSqlCheckRule() {
 // Arrange
 ConnectionConfig config = new ConnectionConfig();
 List<Rule> rules = createRules();

 List<SqlCheckRuleFactory> candidates = createSqlCheckRuleFactories();

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);

 // Act
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, config, mock(JdbcOperations.class));

 // Assert
 assertNotNull(result);
 assertEquals(1, result.size());
 }

 @Test
 public void testFullFillRiskLevel_returnsListOfViolatedRules() {
 // Arrange
 List<Rule> rules = createRules();
 List<CheckViolation> violations = new ArrayList<>();

 Map<String, Rule> name2RuleMap = new HashMap<>();
 for (Rule rule : rules) {
 name2RuleMap.put("${" + rule.getMetadata().getName() + "}", rule);
 }

 CheckViolation violation1 = new CheckViolation();
 violation1.setType(RuleType.SQL_INJECTION);
 CheckViolation violation2 = new CheckViolation();
 violation2.setType(RuleType.SQL_STANDARD);

 violations.add(violation1);
 violations.add(violation2);

 // Act
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

 // Assert
 assertNotNull(result);
 assertEquals(2, result.size());

 for (Rule rule : result) {
 if (rule.getViolation() != null) {
 assertEquals(rule.getLevel(), rule.getViolation().getLevel());
 } else {
 assertEquals(0, rule.getLevel());
 }
 }
 }

 private List<Rule> createRules() {
 List<Rule> rules = new ArrayList<>();

 Rule rule1 = new Rule();
 rule1.setMetadata(new RuleMetadata());
 rule1.setLevel(1);
 rule1.setEnabled(true);

 Rule rule2 = new Rule();
 rule2.setMetadata(new RuleMetadata());
 rule2.setLevel(2);
 rule2.setEnabled(true);

 rules.add(rule1);
 rules.add(rule2);
 return rules;
 }

 private List<SqlCheckRuleFactory> createSqlCheckRuleFactories() {
 return Collections.singletonList(mock(SqlCheckRules.SqlCheckRuleFactory.class));
 }
}