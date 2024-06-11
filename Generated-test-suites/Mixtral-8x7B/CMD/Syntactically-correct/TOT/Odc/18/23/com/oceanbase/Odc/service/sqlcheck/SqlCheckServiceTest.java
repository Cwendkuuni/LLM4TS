import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private LocalValidatorFactoryBean validator;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private ConnectionConfig connectionConfig;

    private Environment environment;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        session = Mockito.mock(ConnectionSession.class);
        Mockito.when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        connectionConfig = Mockito.mock(ConnectionConfig.class);
        Mockito.when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        environment = Mockito.mock(Environment.class);
        Mockito.when(environment.getRulesetId()).thenReturn(1L);
    }

    @Test
    public void testCheck_shouldReturnCheckResults() {
        String scriptContent = "SELECT * FROM users;";

        List<Rule> rules = new ArrayList<>();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setName("test_rule");
        metadata.setLevel("ERROR");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        Mockito.when(ruleService.list(1L, QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(scriptContent);

        List<CheckResult> results = sqlCheckService.check(session, req);

        Assert.assertFalse(results.isEmpty());
        CheckResult result = results.get(0);
        Assert.assertEquals("test_rule", result.getRuleName());
        Assert.assertEquals("ERROR", result.getLevel());
    }

    @Test(expected = ConstraintViolationException.class)
    public void testCheck_whenSqlIsNull_shouldThrowException() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(null);

        sqlCheckService.check(session, req);
    }

    @Test
    public void testCheck_whenNoRules_shouldReturnEmptyList() {
        List<Rule> rules = Collections.emptyList();

        Mockito.when(ruleService.list(1L, QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(session, req);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheck_whenNoEnabledRules_shouldReturnEmptyList() {
        List<Rule> rules = new ArrayList<>();

        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setName("test_rule");
        metadata.setLevel("ERROR");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(false);
        rules.add(rule);

        Mockito.when(ruleService.list(1L, QueryRuleMetadataParams.builder().build()))
                .thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(session, req);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheck_whenRuleFactoryFails_shouldReturnEmptyList() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule("test_rule", RuleType.SQL_CHECK, DialectType.MYSQL));

        SqlCheckRules.registerFactory(
                rule -> rule.getMetadata().getName().equals("test_rule"),
                () -> {
                    throw new RuntimeException("Factory creation failed");
                });

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(session, req);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckSqls_shouldReturnCheckViolations() {
        String scriptContent = "SELECT * FROM users;";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString(0, scriptContent.length(), scriptContent));

        List<Rule> rules = new ArrayList<>();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setName("test_rule");
        metadata.setLevel("ERROR");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        OBConsoleDataSourceFactory factory = Mockito.mock(OBConsoleDataSourceFactory.class);
        Mockito.when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());
        JdbcOperations jdbc = new JdbcTemplate(factory.getDataSource());
        Mockito.when(jdbc.queryForObject("SELECT DATABASE()", String.class)).thenReturn("test_db");

        List<SqlCheckRule> checkRules = Collections.singletonList(createSqlCheckRule("test_rule", jdbc));

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "test_db", sqls, connectionConfig);

        Assert.assertFalse(violations.isEmpty());
        CheckViolation violation = violations.get(0);
        Assert.assertEquals("test_rule", violation.getType().getName());
    }

    @Test
    public void testGetRules_shouldReturnRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule("test_rule", RuleType.SQL_CHECK, DialectType.MYSQL));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        Assert.assertFalse(sqlCheckRules.isEmpty());
        Assert.assertEquals("test_rule", sqlCheckRules.get(0).getName());
    }

    @Test
    public void testGetRules_whenNoRules_shouldReturnEmptyList() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);

        Assert.assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_shouldReturnViolatedRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule("test_rule", RuleType.SQL_CHECK, DialectType.MYSQL));

        CheckViolation violation = new CheckViolation();
        violation.setType(createRule("test_rule", RuleType.SQL_CHECK, DialectType.MYSQL).getMetadata());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(violation));

        Assert.assertFalse(violatedRules.isEmpty());
        Assert.assertEquals("ERROR", violatedRules.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevel_whenNoViolations_shouldReturnEmptyList() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule("test_rule", RuleType.SQL_CHECK, DialectType.MYSQL));

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        Assert.assertTrue(violatedRules.isEmpty());
    }

    private Rule createRule(String name, RuleType type, DialectType dialect) {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(type);
        metadata.setName(name);
        metadata.setLevel("ERROR");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        return rule;
    }

    private SqlCheckRule createSqlCheckRule(String name, JdbcOperations jdbc) {
        return Mockito.mock(SqlCheckRule.class);
    }

    @After
    public void teardown() {
        Mockito.framework().clearInlineMocks();
    }
}