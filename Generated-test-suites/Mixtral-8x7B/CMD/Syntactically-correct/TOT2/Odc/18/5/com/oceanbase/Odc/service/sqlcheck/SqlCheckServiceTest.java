import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

 private SqlCheckService sqlCheckService;

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @Mock
 private ConnectionSession connectionSession;

 @Mock
 private ConnectionConfig connectionConfig;

 @Mock
 private JdbcOperations jdbcOperations;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 sqlCheckService = new SqlCheckService();
 sqlCheckService.ruleService = ruleService;
 sqlCheckService.environmentService = environmentService;

 when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
 when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);

 when(environmentService.detail(1L)).thenReturn(new Environment());
 when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
 when(connectionConfig.isConsole()).thenReturn(true);
 when(connectionConfig.getSchema()).thenReturn("test_database");

 when(ruleService.list(1L, QueryRuleMetadataParams.builder().build())).thenReturn(getRules());
 }

 @Test
 public void testCheck() {
 String scriptContent = "SELECT * FROM users;";
 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent(scriptContent);

 List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
 assertEquals(0, checkResults.size());

 // Add some test rules and test again
 List<Rule> testRules = getTestRules();
 when(ruleService.list(1L, QueryRuleMetadataParams.builder().build()))
 .thenReturn(testRules);

 CheckResult checkResult = checkResults.get(0);
 assertEquals(1, checkResults.size());
 assertEquals(CheckViolation.ViolationType.WARNING, checkResult.getViolations().get(0).getType());
 }

 @Test
 public void testCheckWithEnvironment() {
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT * FROM users;"));

 when(jdbcOperations.queryForList("SELECT * FROM users;"))
 .thenReturn(Collections.singletonList(new HashMap<>()));

 List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, connectionConfig);
 assertEquals(0, checkViolations.size());

 // Add some test rules and test again
 List<Rule> testRules = getTestRules();
 when(ruleService.list(1L, QueryRuleMetadataParams.builder().build()))
 .thenReturn(testRules);

 CheckViolation checkViolation = checkViolations.get(0);
 assertEquals(1, checkViolations.size());
 assertEquals(CheckViolation.ViolationType.WARNING, checkViolation.getType());
 }

 private List<Rule> getRules() {
 return Collections.singletonList(Rule.builder()
 .metadata(RuleMetadata.builder().name("test_rule").type(RuleType.SQL_CHECK).build())
 .enabled(true)
 .build());
 }

 private List<Rule> getTestRules() {
 List<Rule> testRules = new ArrayList<>();
 testRules.add(Rule.builder()
 .metadata(RuleMetadata.builder().name("test_rule").type(RuleType.SQL_CHECK).build())
 .enabled(true)
 .level(Rule.Level.WARNING)
 .build());
 testRules.add(Rule.builder()
 .metadata(RuleMetadata.builder().name("test_rule_2").type(RuleType.SQL_CHECK).build())
 .enabled(true)
 .level(Rule.Level.ERROR)
 .build());
 return testRules;
 }
}