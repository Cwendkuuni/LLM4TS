import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
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
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private JdbcOperations jdbcOperations;
    private SingleConnectionDataSource dataSource;
    private ConnectionSession session;
    private ConnectionConfig connectionConfig;
    private SqlCheckReq sqlCheckReq;

    @Before
    public void setUp() {
        dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");

        jdbcOperations = new JdbcTemplate(dataSource);

        session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        session.setSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY, jdbcOperations);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setDelimiter(";");
        sqlCheckReq.setScriptContent("SELECT * FROM users; DELETE FROM users WHERE id = 1;");
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        rules.add(getRule("test_rule_1", RuleType.SQL_CHECK));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);

        assertEquals(2, checkResults.size());
    }

    @Test
    public void testCheckEmptyList() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);

        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEmptyScriptContent() {
        sqlCheckReq.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);

        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEmptyRulesetId() {
        ConnectionSessionUtil.setRuleSetId(session, null);

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);

        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckEnvironment() {
        List<Rule> rules = new ArrayList<>();
        rules.add(getRule("test_rule_1", RuleType.SQL_CHECK));
        when(environmentService.detail(anyLong())).thenReturn(getEnvironment("testdb"));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", getSqls(), connectionConfig);

        assertEquals(2, checkViolations.size());
    }

    @Test
    public void testCheckEmptyEnvironment() {
        when(environmentService.detail(anyLong())).thenReturn(null);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", getSqls(), connectionConfig);

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(getRule("test_rule_1", RuleType.SQL_CHECK));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesEmptyList() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);

        assertEquals(0, sqlCheckRules.size());
    }

    private List<OffsetString> getSqls() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0));
        sqls.add(new OffsetString("DELETE FROM users WHERE id = 1;", 18));
        return sqls;
    }

    private Rule getRule(String name, RuleType type) {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName(name);
        rule.getMetadata().setType(type);
        rule.setEnabled(true);
        return rule;
    }

    private Environment getEnvironment(String databaseName) {
        Environment environment = new Environment();
        environment.setRulesetId(1L);
        return environment;
    }

}