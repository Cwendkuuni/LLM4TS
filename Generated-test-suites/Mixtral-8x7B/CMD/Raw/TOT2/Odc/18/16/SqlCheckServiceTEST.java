import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleServiceMock;
 @Mock
 private EnvironmentService environmentServiceMock;
 @Mock
 private JdbcOperations jdbcOperationsMock;
 @Mock
 private ConnectionConfig connectionConfigMock;
 @Mock
 private ConnectionSession connectionSessionMock;

 private SqlCheckService sqlCheckService;
 private SingleConnectionDataSource dataSourceMock;
 private JdbcTemplate jdbcTemplateMock;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);

 dataSourceMock = Mockito.mock(SingleConnectionDataSource.class);
 jdbcTemplateMock = new JdbcTemplate(dataSourceMock);
 sqlCheckService = new SqlCheckService();
 sqlCheckService.ruleService = ruleServiceMock;
 sqlCheckService.environmentService = environmentServiceMock;
 sqlCheckService.jdbc = jdbcTemplateMock;

 when(connectionConfigMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(connectionSessionMock.getSyncJdbcExecutor(any())).thenReturn(jdbcOperationsMock);
 }

 @Test
 public void testCheck() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata = new RuleMetadata();
 ruleMetadata.setType(RuleType.SQL_CHECK);
 ruleMetadata.setName("test_rule");
 Rule rule = new Rule();
 rule.setMetadata(ruleMetadata);
 rule.setLevel(1);
 rule.setEnabled(true);
 rules.add(rule);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(rules);

 // Act
 List<CheckResult> result = sqlCheckService.check(sessionMock, req);

 // Assert
 assertEquals(1, result.size());
 assertEquals(0, result.get(0).getViolations().size());
 }

 @Test
 public void testCheckWithViolations() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata = new RuleMetadata();
 ruleMetadata.setType(RuleType.SQL_CHECK);
 ruleMetadata.setName("test_rule");
 Rule rule = new Rule();
 rule.setMetadata(ruleMetadata);
 rule.setLevel(1);
 rule.setEnabled(true);
 rules.add(rule);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(rules);

 when(jdbcOperationsMock.queryForList(any(), any(), any()))
 .thenReturn(new ArrayList<>());

 // Act
 List<CheckResult> result = sqlCheckService.check(sessionMock, req);

 // Assert
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getViolations().size());
 }

 @Test
 public void testCheck_whenNoRules_returnsEmptyList() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(new ArrayList<>());

 // Act
 List<CheckResult> result = sqlCheckService.check(sessionMock, req);

 // Assert
 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_whenNoScriptContent_returnsEmptyList() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata = new RuleMetadata();
 ruleMetadata.setType(RuleType.SQL_CHECK);
 ruleMetadata.setName("test_rule");
 Rule rule = new Rule();
 rule.setMetadata(ruleMetadata);
 rule.setLevel(1);
 rule.setEnabled(true);
 rules.add(rule);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(rules);

 // Act
 List<CheckResult> result = sqlCheckService.check(sessionMock, req);

 // Assert
 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_withMultipleRules_returnsCorrectResults() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata1 = new RuleMetadata();
 ruleMetadata1.setType(RuleType.SQL_CHECK);
 ruleMetadata1.setName("test_rule1");
 Rule rule1 = new Rule();
 rule1.setMetadata(ruleMetadata1);
 rule1.setLevel(1);
 rule1.setEnabled(true);
 rules.add(rule1);

 RuleMetadata ruleMetadata2 = new RuleMetadata();
 ruleMetadata2.setType(RuleType.SQL_CHECK);
 ruleMetadata2.setName("test_rule2");
 Rule rule2 = new Rule();
 rule2.setMetadata(ruleMetadata2);
 rule2.setLevel(2);
 rule2.setEnabled(true);
 rules.add(rule2);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(rules);

 when(jdbcOperationsMock.queryForList(any(), any(), any()))
 .thenReturn(new ArrayList<>());

 // Act
 List<CheckResult> result = sqlCheckService.check(sessionMock, req);

 // Assert
 assertEquals(1, result.size());
 assertEquals(2, result.get(0).getViolations().size());
 }

 @Test
 public void testCheck_withViolationsAndRiskLevel() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata = new RuleMetadata();
 ruleMetadata.setType(RuleType.SQL_CHECK);
 ruleMetadata.setName("test_rule");
 Rule rule = new Rule();
 rule.setMetadata(ruleMetadata);
 rule.setLevel(1);
 rule.setEnabled(true);
 rules.add(rule);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(rules);

 CheckViolation violation1 = new CheckViolation();
 violation1.setRuleName("test_rule");
 violation1.setLevel(1);
 violation1.setMessage("Violation 1");

 CheckViolation violation2 = new CheckViolation();
 violation2.setRuleName("test_rule");
 violation2.setLevel(2);
 violation2.setMessage("Violation 2");

 List<CheckViolation> violations = new ArrayList<>();
 violations.add(violation1);
 violations.add(violation2);

 when(jdbcOperationsMock.queryForList(any(), any(), any()))
 .thenReturn(new ArrayList<>());

 Map<String, Rule> name2RuleMap = new HashMap<>();
 name2RuleMap.put("${test_rule}", rule);

 // Act
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

 // Assert
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getLevel());
 }

 @Test
 public void testGetRules_whenNoRules_returnsEmptyList() {
 // Arrange
 List<Rule> rules = sqlCheckService.getRules(new ArrayList<>(), connectionConfigMock);

 // Assert
 assertEquals(0, rules.size());
 }

 @Test
 public void testGetRules_whenRulesExist_returnsCorrectRules() {
 // Arrange
 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata = new RuleMetadata();
 ruleMetadata.setType(RuleType.SQL_CHECK);
 ruleMetadata.setName("test_rule");
 Rule rule = new Rule();
 rule.setMetadata(ruleMetadata);
 rule.setLevel(1);
 rule.setEnabled(true);
 rules.add(rule);

 List<SqlCheckRuleFactory> factories = new ArrayList<>();

 when(jdbcOperationsMock.queryForList(any(), any(), any()))
 .thenReturn(new ArrayList<>());

 // Act
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionConfigMock);

 // Assert
 assertEquals(1, result.size());
 }

 @Test
 public void testCheck_withMultipleOffsetStrings_returnsCorrectResults() {
 // Arrange
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users; SELECT * FROM orders;");

 ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
 when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
 when(sessionMock.getRuleSetId()).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 RuleMetadata ruleMetadata1 = new RuleMetadata();
 ruleMetadata1.setType(RuleType.SQL_CHECK);
 ruleMetadata1.setName("test_rule1");
 Rule rule1 = new Rule();
 rule1.setMetadata(ruleMetadata1);
 rule1.setLevel(1);
 rule1.setEnabled(true);
 rules.add(rule1);

 RuleMetadata ruleMetadata2 = new RuleMetadata();
 ruleMetadata2.setType(RuleType.SQL_CHECK);
 ruleMetadata2.setName("test_rule2");
 Rule rule2 = new Rule();
 rule2.setMetadata(ruleMetadata2);
 rule2.setLevel(2);
 rule2.setEnabled(true);
 rules.add(rule2);

 when(ruleServiceMock.list(1L, ArgumentMatchers.<QueryRuleMetadataParams>any()))
 .thenReturn(rules);

 when(jdbcOperationsMock.queryForList(any(), any(), any()))
 .thenReturn(new ArrayList<>());

 // Act
 List<CheckResult> result = sqlCheckService.check(connectionSessionMock, req);

 // Assert
 assertEquals(2, result.size());
 assertEquals(2, result.get(0).getViolations().size());
 assertEquals(2, result.get(1).getViolations().size());
 }
}