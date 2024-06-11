import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 private ConnectionConfig connectionConfig;
 private ConnectionSession connectionSession;
 private JdbcOperations jdbcOperations;
 private Environment environment;
 private List<Rule> rules;

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionSession = mock(ConnectionSession.class);
 jdbcOperations = mock(JdbcTemplate.class);
 environment = mock(Environment.class);
 rules = new ArrayList<>();
 ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
 }

 @Test
 public void check_shouldReturnEmptyListWhenRuleSetIdIsNull() {
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);
 SqlCheckReq req = new SqlCheckReq();
 List<CheckResult> result = sqlCheckService.check(connectionSession, req);
 assertTrue(result.isEmpty());
 }

 @Test
 public void check_shouldReturnEmptyListWhenNoRulesFound() {
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
 SqlCheckReq req = new SqlCheckReq();
 List<CheckResult> result = sqlCheckService.check(connectionSession, req);
 assertTrue(result.isEmpty());
 }

 @Test
 public void check_shouldReturnCheckResultsWhenRulesFound() {
 when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 SqlCheckReq req = new SqlCheckReq();
 List<CheckResult> result = sqlCheckService.check(connectionSession, req);
 assertFalse(result.isEmpty());
 }

 @Test
 public void check_shouldReturnEmptyListWhenSqlsIsEmpty() {
 when(environmentService.detail(anyLong())).thenReturn(environment);
 when(environment.getRulesetId()).thenReturn(1L);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 List<OffsetString> sqls = new ArrayList<>();
 List<CheckViolation> result = sqlCheckService.check(1L, "dbName", sqls, connectionConfig);
 assertTrue(result.isEmpty());
 }

 @Test
 public void check_shouldReturnCheckViolationsWhenSqlsNotEmpty() {
 when(environmentService.detail(anyLong())).thenReturn(environment);
 when(environment.getRulesetId()).thenReturn(1L);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 OffsetString sql = new OffsetString("SELECT * FROM users;");
 List<CheckViolation> result = sqlCheckService.check(1L, "dbName", Collections.singletonList(sql), connectionConfig);
 assertFalse(result.isEmpty());
 }

 @Test
 public void getRules_shouldReturnEmptyListWhenRulesIsEmpty() {
 List<Rule> result = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
 assertTrue(result.isEmpty());
 }

 @Test
 public void getRules_shouldReturnListOfSqlCheckRulesWhenRulesNotEmpty() {
 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 List<RuleMetadata> metadataList = new ArrayList<>();
 metadataList.add(new RuleMetadata());
 metadataList.get(0).setName("rule1");
 metadataList.get(0).setType(RuleType.SQL_CHECK);
 metadataList.get(0).setEnabled(true);
 Rule rule1 = new Rule();
 rule1.setMetadata(metadataList.get(0));
 List<Rule> rules = new ArrayList<>();
 rules.add(rule1);
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
 List<SqlCheckRuleFactory> candidates = new ArrayList<>();
 SqlCheckRuleFactory factory1 = mock(SqlCheckRuleFactory.class);
 candidates.add(factory1);
 when(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations)).thenReturn(candidates);
 when(factory1.create(any(), any(), any())).thenReturn(new SqlCheckRule());
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);
 assertEquals(1, result.size());
 }

 @Test
 public void fullFillRiskLevel_shouldReturnEmptyListWhenRulesOrViolationsIsEmpty() {
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
 assertTrue(result.isEmpty());
 }

 @Test
 public void fullFillRiskLevel_shouldReturnListOfRulesWithRiskLevelWhenViolationsNotEmpty() {
 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());
 rules.get(0).setMetadata(new RuleMetadata());
 rules.get(0).getMetadata().setName("rule1");
 CheckViolation violation1 = new CheckViolation();
 violation1.setType(RuleType.SQL_CHECK);
 violation1.setRuleName("${rule1}");
 List<CheckViolation> violations = new ArrayList<>();
 violations.add(violation1);
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
 assertEquals(1, result.size());
 assertEquals(rules.get(0).getLevel(), result.get(0).getLevel());
 }
}