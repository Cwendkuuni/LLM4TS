import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionConfig connectionConfig;
    private ConnectionSession connectionSession;
    private Environment environment;
    private JdbcOperations jdbcOperations;
    private JdbcTemplate jdbcTemplate;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionSession = mock(ConnectionSession.class);
        environment = new Environment();
        environment.setRulesetId(1L);

        dataSource = new SingleConnectionDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcOperations = jdbcTemplate;

        ReflectionTestUtils.setField(sqlCheckService, "dialectType", DialectType.MYSQL);
        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);

        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(environmentService.detail(anyLong()).getRulesetId()).thenReturn(1L);

        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        SqlChecker sqlChecker = new DefaultSqlChecker(connectionSession.getDialectType(), "\\;", sqlCheckRules);

        List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());
        fullFillRiskLevel(rules, checkViolations);

        List<CheckResult> results = SqlCheckUtil.buildCheckResults(checkViolations);
        assertEquals(results.size(), 0);
    }

    @Test
    public void testCheckWithViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        doReturn(sqlCheckRules).when(sqlCheckService).getRules(any(), any(), any());

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(connectionSession.getDialectType(), "\\;", sqlCheckRules);

        List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());
        fullFillRiskLevel(rules, checkViolations);

        List<CheckResult> results = SqlCheckUtil.buildCheckResults(checkViolations);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testCheckWithMultipleViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users; SELECT * FROM non_existing_table;");

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());
        rules.add(getSampleRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        doReturn(sqlCheckRules).when(sqlCheckService).getRules(any(), any(), any());

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(connectionSession.getDialectType(), "\\;", sqlCheckRules);

        List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());
        fullFillRiskLevel(rules, checkViolations);

        List<CheckResult> results = SqlCheckUtil.buildCheckResults(checkViolations);
        assertEquals(results.size(), 2);
    }

    @Test
    public void testCheckForEnvironment() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0));

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());

        doReturn(getSampleRules(rules)).when(sqlCheckService).getRules(any(), any(), any());

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        List<CheckResult> results = SqlCheckUtil.buildCheckResults(checkViolations);
        assertEquals(results.size(), 1);
    }

    @Test
    public void testCheckForEnvironmentWithMultipleQueries() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users; SELECT * FROM non_existing_table;", 0));

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());
        rules.add(getSampleRule());

        doReturn(getSampleRules(rules)).when(sqlCheckService).getRules(any(), any(), any());

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        List<CheckResult> results = SqlCheckUtil.buildCheckResults(checkViolations);
        assertEquals(results.size(), 2);
    }

    private List<SqlCheckRule> getSampleRules(List<Rule> rules) {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionConfig.getDialectType(),
                jdbcOperations);
        SqlCheckRule sqlCheckRule = sqlCheckRules.get(0);
        ReflectionTestUtils.setField(sqlCheckRule, "rule", rules.get(0));
        return sqlCheckRules;
    }

    private Rule getSampleRule() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("sample_rule");
        rule.setEnabled(true);
        rule.setLevel(1);
        return rule;
    }

    private void fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
        for (CheckViolation violation : violations) {
            String ruleName = "${" + violation.getType().getName() + "}";
            for (Rule rule : rules) {
                if (rule.getMetadata().getName().equals(ruleName)) {
                    violation.setLevel(rule.getLevel());
                    break;
                }
            }
        }
    }
}