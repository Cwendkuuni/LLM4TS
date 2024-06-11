import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionSession = mock(ConnectionSession.class);
        connectionConfig = mock(ConnectionConfig.class);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(any())).thenReturn(jdbcOperations);

        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(connectionConfig.getDataSource()).thenReturn(dataSource);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", null));
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(req.getScriptContent());

        assertEquals(0, checkViolations.size());

        // Add a violation
        SqlCheckRuleFactory factory = mock(SqlCheckRules.SqlCheckRuleFactory.class);
        when(factory.check(any(), any())).thenReturn(new CheckViolation("test_rule", "error message"));
        when(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations)).thenReturn(List.of(factory));

        checkViolations = sqlChecker.check(req.getScriptContent());

        assertEquals(1, checkViolations.size());
        assertTrue(checkViolations.get(0).getMessage().contains("error message"));
    }

    @Test
    public void testCheckWithOffsetStrings() {
        OffsetString sql1 = new OffsetString("SELECT * FROM users;", 0, 22);
        OffsetString sql2 = new OffsetString("SELECT * FROM posts;", 23, 42);

        List<OffsetString> sqls = List.of(sql1, sql2);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", null));
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionConfig);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(sqls, new SqlCheckContext(2));

        assertEquals(0, checkViolations.size());

        // Add a violation
        SqlCheckRuleFactory factory = mock(SqlCheckRules.SqlCheckRuleFactory.class);
        when(factory.check(any(), any())).thenReturn(new CheckViolation("test_rule", "error message"));
        when(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations)).thenReturn(List.of(factory));

        checkViolations = sqlChecker.check(sqls, new SqlCheckContext(2));

        assertEquals(1, checkViolations.size());
        assertTrue(checkViolations.get(0).getMessage().contains("error message"));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", null));
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(1, sqlCheckRules.size());
        assertTrue(sqlCheckRules.get(0) instanceof SqlCheckRules.SqlCheckRule);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", null));
        rule.setEnabled(true);
        rule.setLevel("HIGH");
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation("test_rule", "error message");
        violations.add(violation);

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, filledRules.size());
        assertEquals("HIGH", filledRules.get(0).getLevel());
        assertEquals("error message", filledRules.get(0).getViolation().getMessage());
    }
}