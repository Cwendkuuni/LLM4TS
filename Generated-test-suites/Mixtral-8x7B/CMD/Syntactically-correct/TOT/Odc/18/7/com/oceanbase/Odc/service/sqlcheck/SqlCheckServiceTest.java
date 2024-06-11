import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

 private SqlCheckService sqlCheckService;

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sqlCheckService = new SqlCheckService();
 sqlCheckService.ruleService = ruleService;
 sqlCheckService.environmentService = environmentService;
 }

 @Test
 public void testCheckWithConnectionSession() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 List<Rule> rules = createRules();
 when(ruleService.list(eq(1L), any())).thenReturn(rules);

 SqlCheckReq req = new SqlCheckReq();
 req.setScriptContent("SELECT * FROM users;");

 List<CheckResult> results = sqlCheckService.check(session, req);
 assertEquals(1, results.size());
 assertEquals("testRule", results.get(0).getRuleName());
 assertEquals(CheckViolation.Level.WARNING, results.get(0).getLevel());
 }

 @Test
 public void testCheckWithConnectionConfig() {
 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 List<Rule> rules = createRules();
 when(environmentService.detail(1L)).thenReturn(createEnvironment());
 when(ruleService.list(eq(1L), any())).thenReturn(rules);

 List<OffsetString> sqls = Arrays.asList(new OffsetString("SELECT * FROM users;", 0, 22));

 List<CheckViolation> violations = sqlCheckService.check(1L, "dbName", sqls, config);
 assertEquals(1, violations.size());
 assertEquals("testRule", violations.get(0).getRuleName());
 assertEquals(CheckViolation.Level.WARNING, violations.get(0).getLevel());
 }

 private List<Rule> createRules() {
 List<Rule> rules = new ArrayList<>();
 rules.add(createRule());
 return rules;
 }

 private Rule createRule() {
 Rule rule = new Rule();
 RuleMetadata metadata = new RuleMetadata();
 metadata.setName("testRule");
 metadata.setType(RuleType.SQL_CHECK);
 rule.setMetadata(metadata);
 rule.setEnabled(true);
 return rule;
 }

 private Environment createEnvironment() {
 Environment env = new Environment();
 env.setRulesetId(1L);
 return env;
 }

}