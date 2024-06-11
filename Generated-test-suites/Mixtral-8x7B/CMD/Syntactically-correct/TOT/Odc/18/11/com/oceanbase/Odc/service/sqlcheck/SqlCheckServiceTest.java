import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession connectionSession;
    private ConnectionConfig connectionConfig;
    private JdbcOperations jdbcOperations;
    private SqlCheckReq sqlCheckReq;

    @Before
    public void setUp() {
        connectionSession = Mockito.mock(ConnectionSession.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        jdbcOperations = Mockito.mock(JdbcTemplate.class);
        sqlCheckReq = Mockito.mock(SqlCheckReq.class);

        ReflectionTestUtils.setField(sqlCheckService, "dialectType", DialectType.MYSQL);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);
        when(sqlCheckReq.getScriptContent()).thenReturn("SELECT * FROM users WHERE id = ?");

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("test");
        dataSource.setPassword("test");

        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionConfig.getDataSource()).thenReturn(dataSource);
    }

    @After
    public void tearDown() {
        sqlCheckService = null;
        connectionSession = null;
        connectionConfig = null;
        jdbcOperations = null;
        sqlCheckReq = null;
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setMetadata(new RuleMetadata(1L, "TestRule1", RuleType.SQL_CHECK, "test", "test", 1, null, null));
        rules.add(rule1);
        Rule rule2 = new Rule();
        rule2.setId(2L);
        rule2.setMetadata(new RuleMetadata(2L, "TestRule2", RuleType.SQL_CHECK, "test", "test", 2, null, null));
        rules.add(rule2);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        Assert.assertFalse(CollectionUtils.isEmpty(sqlCheckRules));

        CheckViolation violation1 = new CheckViolation();
        violation1.setType(rule1.getMetadata());
        violation1.setRuleId(rule1.getId());
        violation1.setMessage("Test violation 1");
        violation1.setLine(1);

        CheckViolation violation2 = new CheckViolation();
        violation2.setType(rule2.getMetadata());
        violation2.setRuleId(rule2.getId());
        violation2.setMessage("Test violation 2");
        violation2.setLine(2);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation1);
        checkViolations.add(violation2);

        SqlCheckContext sqlCheckContext = new SqlCheckContext(2);
        when(sqlCheckReq.getDelimiter()).thenReturn("\n");
        when(sqlCheckService.check(connectionSession, sqlCheckReq)).thenReturn(checkViolations);

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertEquals(2, results.size());
        Assert.assertEquals(violation1.getMessage(), results.get(0).getMessage());
        Assert.assertEquals(violation1.getLine(), results.get(0).getLine());
        Assert.assertEquals(violation2.getMessage(), results.get(1).getMessage());
        Assert.assertEquals(violation2.getLine(), results.get(1).getLine());
    }

    @Test
    public void testCheckEmptyRules() {
        List<Rule> rules = new ArrayList<>();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        Assert.assertTrue(CollectionUtils.isEmpty(sqlCheckRules));

        when(sqlCheckService.check(connectionSession, sqlCheckReq)).thenReturn(new ArrayList<>());

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckNoRules() {
        ReflectionTestUtils.setField(sqlCheckService, "ruleService", null);

        when(sqlCheckService.check(connectionSession, sqlCheckReq)).thenReturn(new ArrayList<>());

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckEmptyScriptContent() {
        when(sqlCheckReq.getScriptContent()).thenReturn("");

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckEnvironment() {
        String databaseName = "testdb";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users WHERE id = ?", 0));

        Environment environment = new Environment();
        environment.setId(1L);
        environment.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setMetadata(new RuleMetadata(1L, "TestRule1", RuleType.SQL_CHECK, "test", "test", 1, null, null));
        rules.add(rule1);

        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, databaseName, sqls, connectionConfig);

        Assert.assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setMetadata(new RuleMetadata(1L, "TestRule1", RuleType.SQL_CHECK, "test", "test", 1, null, null));
        rules.add(rule1);
        Rule rule2 = new Rule();
        rule2.setId(2L);
        rule2.setMetadata(new RuleMetadata(2L, "TestRule2", RuleType.SQL_CHECK, "test", "test", 2, null, null));
        rules.add(rule2);

        List<SqlCheckRuleFactory> factories = new ArrayList<>();

        SqlCheckRuleFactory factory1 = Mockito.mock(SqlCheckRuleFactory.class);
        when(factory1.create(Mockito.any(Rule.class), Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
                .thenReturn(new SqlCheckRule() {});
        factories.add(factory1);

        SqlCheckRuleFactory factory2 = Mockito.mock(SqlCheckRuleFactory.class);
        when(factory2.create(Mockito.any(Rule.class), Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
                .thenReturn(null);
        factories.add(factory2);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, jdbcOperations);

        Assert.assertEquals(1, sqlCheckRules.size());
        Assert.assertNotNull(sqlCheckRules.get(0));
    }

    @Test
    public void testGetRulesEmptyRules() {
        List<Rule> rules = new ArrayList<>();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, jdbcOperations);

        Assert.assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setMetadata(new RuleMetadata(1L, "TestRule1", RuleType.SQL_CHECK, "test", "test", 1, null, null));
        rules.add(rule1);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType("${TestRule1}");
        violation1.setRuleId(1L);
        violation1.setMessage("Test violation 1");
        violation1.setLine(1);
        violations.add(violation1);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals(1, result.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevelEmptyRules() {
        List<Rule> rules = new ArrayList<>();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, new ArrayList<>());

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelEmptyViolations() {
        List<Rule> rules = new ArrayList<>();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, new ArrayList<>());

        Assert.assertTrue(result.isEmpty());
    }
}