import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionConfig;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfigBuilder;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionConfig connectionConfig;
    private ConnectionSession connectionSession;
    private JdbcOperations jdbcOperations;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionConfig = new ConnectionConfigBuilder().build();
        connectionSession = Mockito.mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        jdbcOperations = new JdbcTemplate(new SingleConnectionDataSource());
        when(connectionSession.getSyncJdbcExecutor(any())).thenReturn(jdbcOperations);
        ConnectionSessionUtil.setConnectionSession(connectionSession);
    }

    @Test
    public void testCheck() {
        // Prepare test data
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;"));
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent(sqls);

        // Mock dependencies
        Environment environment = new Environment();
        environment.setRulesetId(2L);
        when(environmentService.detail(anyLong())).thenReturn(environment);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setLevel("ERROR");
        rule.setMetadata(new RuleMetadata("TEST_RULE", RuleType.SQL_CHECK, "test_rule", 1, null));
        rules.add(rule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule.check(any(String.class))).thenReturn(Collections.singletonList(new CheckViolation("TEST_RULE", "test message", 1, 1)));

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(sqlCheckRule);

        when(ruleService.getSqlCheckRuleFactories(any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations));

        // Call the method to test
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        // Assert the result
        Assertions.assertEquals(1, checkResults.size());
        Assertions.assertEquals("ERROR", checkResults.get(0).getLevel());
        Assertions.assertEquals("test message", checkResults.get(0).getMessage());
    }

    @Test
    public void testCheckWithEmptyRules() {
        // Prepare test data
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;"));
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent(sqls);

        // Mock dependencies
        Environment environment = new Environment();
        environment.setRulesetId(2L);
        when(environmentService.detail(anyLong())).thenReturn(environment);

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        when(ruleService.getSqlCheckRuleFactories(any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations));

        // Call the method to test
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        // Assert the result
        Assertions.assertTrue(CollectionUtils.isEmpty(checkResults));
    }

    @Test
    public void testGetRules() {
        // Prepare test data
        List<Rule> rules = new ArrayList<>();

        // Mock dependencies
        when(ruleService.getSqlCheckRuleFactories(any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations));

        // Call the method to test
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        // Assert the result
        Assertions.assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        // Prepare test data
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setLevel("ERROR");
        rule.setMetadata(new RuleMetadata("TEST_RULE", RuleType.SQL_CHECK, "test_rule", 1, null));
        rules.add(rule);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation("TEST_RULE", "test message", 1, 1);
        checkViolations.add(violation);

        // Call the method to test
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        // Assert the result
        Assertions.assertEquals(1, filledRules.size());
        Assertions.assertEquals("ERROR", filledRules.get(0).getLevel());
        Assertions.assertEquals("test message", ((CheckViolation) filledRules.get(0).getViolation()).getMessage());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        // Prepare test data
        List<Rule> rules = new ArrayList<>();

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation("TEST_RULE", "test message", 1, 1);
        checkViolations.add(violation);

        // Call the method to test
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        // Assert the result
        Assertions.assertTrue(CollectionUtils.isEmpty(filledRules));
    }
}