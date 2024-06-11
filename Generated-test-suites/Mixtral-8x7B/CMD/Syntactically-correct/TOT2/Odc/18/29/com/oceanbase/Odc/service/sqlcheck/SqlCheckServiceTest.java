import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

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

 @Before
 public void setup() {
 connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.MYSQL);

 connectionSession = new ConnectionSession();
 connectionSession.setDialectType(DialectType.MYSQL);

 jdbcOperations = new JdbcTemplate(new SingleConnectionDataSource("jdbc:h2:mem:test", "sa", "sa"));
 }

 @Test
 public void testCheck_WhenRuleSetIdIsNull_ReturnsEmptyList() {
 List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("test", ""));
 assertEquals(Collections.emptyList(), results);
 }

 @Test
 public void testCheck_WhenNoRules_ReturnsEmptyList() {
 Long ruleSetId = 1L;
 when(ruleService.list(ruleSetId, any())).thenReturn(Collections.emptyList());

 List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("test", ""));
 assertEquals(Collections.emptyList(), results);
 }

 @Test
 public void testCheck_WhenScriptContentIsEmpty_ReturnsEmptyList() {
 Long ruleSetId = 1L;
 when(ruleService.list(ruleSetId, any())).thenReturn(createRules());

 List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("test", ""));
 assertEquals(Collections.emptyList(), results);
 }

 @Test
 public void testCheck_WhenRuleCheckingFails_ReturnsEmptyList() {
 Long ruleSetId = 1L;
 when(ruleService.list(ruleSetId, any())).thenReturn(createRules());
 when(ruleService.list(ruleSetId, any())).thenReturn(createRulesWithCheckMethodThrowingException());

 List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("test", ""));
 assertEquals(Collections.emptyList(), results);
 }

 @Test
 public void testCheck_WhenRulesExist_ReturnsCheckResults() {
 Long ruleSetId = 1L;
 when(ruleService.list(ruleSetId, any())).thenReturn(createRules());

 List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("test", "SELECT 1"));
 assertFalse(results.isEmpty());
 }

 @Test
 public void testCheck_WhenSqlsAreProvided_ReturnsCheckResults() {
 when(environmentService.detail(any())).thenReturn(createEnvironment());
 when(ruleService.list(any(), any())).thenReturn(createRules());

 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT 1", 0, 6));

 List<CheckViolation> violations = sqlCheckService.check(1L, "test", sqls, connectionConfig);
 assertFalse(violations.isEmpty());
 }

 private List<Rule> createRules() {
 return Collections.singletonList(createRule());
 }

 private List<Rule> createRulesWithCheckMethodThrowingException() {
 Rule rule = createRule();
 rule.setCheckMethod("throwException");
 return Collections.singletonList(rule);
 }

 private Rule createRule() {
 RuleMetadata metadata = new RuleMetadata();
 metadata.setName("test_rule");
 metadata.setType(RuleType.SQL_CHECK);

 Rule rule = new Rule();
 rule.setMetadata(metadata);
 rule.setEnabled(true);

 return rule;
 }

 private Environment createEnvironment() {
 Environment environment = new Environment();
 environment.setRulesetId(1L);

 return environment;
 }
}