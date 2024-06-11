import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private JdbcOperations jdbcOperations;
 @Mock
 private ConnectionSession session;
 @Mock
 private ConnectionConfig connectionConfig;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 private SingleConnectionDataSource dataSource;
 private JdbcTemplate jdbcTemplate;
 private SqlCheckReq sqlCheckReq;
 private List<Rule> rules;
 private CheckViolation checkViolation;
 private CheckResult checkResult;
 private SqlCheckContext sqlCheckContext;
 private List<CheckViolation> checkViolations;
 private List<SqlCheckRule> sqlCheckRules;

 @Before
 public void setup() {
 dataSource = new SingleConnectionDataSource();
 jdbcTemplate = new JdbcTemplate(dataSource);
 sqlCheckReq = new SqlCheckReq();
 rules = new ArrayList<>();
 checkViolation = new CheckViolation();
 checkResult = new CheckResult();
 sqlCheckContext = new SqlCheckContext(1L);
 checkViolations = new ArrayList<>();
 checkViolations.add(checkViolation);
 sqlCheckRules = new ArrayList<>();

 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
 when(environmentService.detail(anyLong())).thenReturn(new Environment());
 when(ruleService.list(anyLong(), any())).thenReturn(rules);
 when(new DefaultSqlChecker(any(DialectType.class), anyString(), anyList())).thenReturn(new DefaultSqlChecker(DialectType.MYSQL, "", sqlCheckRules));
 }

 @Test
 public void testCheck() {
 sqlCheckService.check(session, sqlCheckReq);
 verify(ruleService).list(anyLong(), any());
 verify(session).getDialectType();
 verify(session).getSyncJdbcExecutor(anyString());
 }

 @Test
 public void testCheckWithViolations() {
 sqlCheckRules.add(new SqlCheckRule() {});
 when(new DefaultSqlChecker(any(DialectType.class), anyString(), anyList()).check(anyString())).thenReturn(checkViolations);
 when(SqlCheckUtil.buildCheckResults(checkViolations)).thenReturn(Collections.singletonList(checkResult));

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(1, results.size());
 assertEquals(checkResult, results.get(0));
 }

 @Test
 public void testCheck_EmptyRuleList() {
 when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(0, results.size());
 }

 @Test
 public void testCheck_EmptyScriptContent() {
 sqlCheckReq.setScriptContent("");

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(0, results.size());
 }

 @Test
 public void testCheck_NoEnabledRules() {
 rules.add(new Rule());
 when(ruleService.list(anyLong(), any())).thenReturn(rules);

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(0, results.size());
 }

 @Test
 public void testCheck_NoRuleMetadata() {
 rules.add(new Rule().setEnabled(true));
 when(ruleService.list(anyLong(), any())).thenReturn(rules);

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(0, results.size());
 }

 @Test
 public void testCheck_NoRuleType() {
 rules.add(new Rule().setEnabled(true).setMetadata(new RuleMetadata().setType(null)));
 when(ruleService.list(anyLong(), any())).thenReturn(rules);

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(0, results.size());
 }

 @Test
 public void testCheck_InvalidJdbcOperations() {
 when(session.getSyncJdbcExecutor(anyString())).thenReturn(null);

 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

 assertEquals(0, results.size());
 }

 @Test
 public void testCheck_WithEnvironmentId() {
 sqlCheckService.check(1L, "test_db", Collections.singletonList(new OffsetString("SELECT * FROM DUAL")), connectionConfig);

 verify(environmentService).detail(1L);
 }

 @Test
 public void testGetRules_WithSession() {
 sqlCheckService.getRules(rules, session);

 verify(session).getDialectType();
 }

 @Test
 public void testGetRules_WithJdbcOperations() {
 sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcOperations);

 verifyNoMoreInteractions(jdbcOperations);
 }

 @Test
 public void testFullFillRiskLevel() {
 Map<String, Rule> name2RuleMap = new HashMap<>();
 name2RuleMap.put("test_rule", new Rule().setLevel(1));
 checkViolation.setType(new Rule.RuleType().setName("test_rule"));

 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(checkViolation));

 assertEquals(1, violatedRules.size());
 assertEquals(1, violatedRules.get(0).getLevel());
 }

 @Test
 public void testFullFillRiskLevel_NoRules() {

 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), Collections.singletonList(checkViolation));

 assertEquals(0, violatedRules.size());
 }

 @Test
 public void testFullFillRiskLevel_NoRuleName() {

 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(checkViolation));

 assertEquals(0, violatedRules.size());
 }

}