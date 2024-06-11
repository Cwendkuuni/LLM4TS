import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class SqlCheckServiceTest {

    @MockBean
    private RuleService ruleService;

    @MockBean
    private EnvironmentService environmentService;

    @Autowired
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;
    private ConnectionConfig connectionConfig;
    private SqlCheckReq sqlCheckReq;
    private JdbcOperations jdbcOperations;

    @BeforeEach
    public void setUp() {
        connectionSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(connectionSession.getSyncJdbcExecutor(Mockito.anyString()))
                .thenReturn(jdbcOperations);

        connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource("jdbc:mysql://localhost:3306/test", "user", "password"));
        jdbcOperations = jdbcTemplate;
    }

    @Test
    public void testCheck_HappyPath() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null, null, null));
        rule.setEnabled(true);
        rules.add(rule);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(rules);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assertions.assertFalse(checkResults.isEmpty());
    }

    @Test
    public void testCheck_NoRules() {
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(new ArrayList<>());

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assertions.assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheck_DatabaseConnectionFailure() {
        ConnectionConfig failureConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(failureConfig.getDialectType()).thenReturn(DialectType.POSTGRESQL);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sqlCheckService.check(failureConfig, "select * from users;",
                    List.of(new OffsetString(0, "select * from users;")));
        });
    }

    @Test
    public void testCheck_MultipleSqls() {
        sqlCheckReq.setScriptContent("SELECT * FROM users;\nSELECT * FROM orders;");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null, null, null));
        rule.setEnabled(true);
        rules.add(rule);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(rules);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assertions.assertEquals(2, checkResults.size());
    }

    @Test
    public void testCheck_ExternalDatabase() {
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUrl("jdbc:mysql://external-db:3306/test");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null, null, null));
        rule.setEnabled(true);
        rules.add(rule);

        Mockito.when(environmentService.detail(Mockito.anyLong()))
                .thenReturn(new Environment());

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "test_database", List.of(new OffsetString(0, "SELECT * FROM users;")),
                connectionConfig);

        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void testGetRules_HappyPath() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null, null, null));
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assertions.assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testGetRules_NoRules() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<>(), connectionSession);

        Assertions.assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRules_EmptyRules() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, connectionSession);

        Assertions.assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRules_ExternalDatabase() {
        connectionConfig.setDialectType(DialectType.MYSQL);
        connectionConfig.setUrl("jdbc:mysql://external-db:3306/test");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test_rule", RuleType.SQL_CHECK, null, null, null));
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionConfig);

        Assertions.assertEquals(1, sqlCheckRules.size());
    }
}