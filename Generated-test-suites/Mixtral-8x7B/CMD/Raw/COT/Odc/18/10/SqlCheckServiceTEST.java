import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private SqlCheckReq sqlCheckReq;

    private Environment environment;

    private ConnectionConfig connectionConfig;

    private JdbcOperations jdbcOperations;

    private SingleConnectionDataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        connectionSession = createConnectionSessionMock();
        sqlCheckReq = new SqlCheckReq();
        environment = new Environment();
        connectionConfig = new ConnectionConfig();

        dataSource = new SingleConnectionDataSource();
        jdbcOperations = Mockito.mock(JdbcOperations.class);
        jdbcTemplate = new JdbcTemplate(dataSource);

        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
    }

    @After
    public void tearDown() {
        dataSource.destroy();
    }

    @Test
    public void testCheck_WithEmptyRulesList() {
        Mockito.when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(new ArrayList<>());

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(results.isEmpty());
    }

    @Test
    public void testCheck_WithNonEmptyRulesList() {
        List<Rule> rules = createRulesList(10);
        Mockito.when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertEquals(10, results.size());
    }

    @Test
    public void testCheck_WithEmptySqlsList() {
        List<OffsetString> sqls = new ArrayList<>();
        List<CheckViolation> violations = sqlCheckService.check(environment.getId(), environment.getDatabaseName(),
                sqls, connectionConfig);

        Assert.assertTrue(violations.isEmpty());
    }

    @Test
    public void testCheck_WithNonEmptySqlsList() {
        List<OffsetString> sqls = createOffsetStringsList(10);
        List<CheckViolation> violations = sqlCheckService.check(environment.getId(), environment.getDatabaseName(),
                sqls, connectionConfig);

        Assert.assertEquals(10, violations.size());
    }

    @Test
    public void testGetRules_WithEmptyRulesList() {
        List<Rule> rules = sqlCheckService.getRules(new ArrayList<>(), connectionSession);

        Assert.assertTrue(rules.isEmpty());
    }

    @Test
    public void testGetRules_WithNonEmptyRulesList() {
        List<Rule> rules = createRulesList(10);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assert.assertEquals(10, sqlCheckRules.size());
    }

    @Test
    public void testGetRules_WithEmptyCandidatesList() {
        List<Rule> rules = createRulesList(10);
        Mockito.when(SqlCheckRules.getAllFactories(ArgumentMatchers.any(DialectType.class), ArgumentMatchers.any(JdbcOperations.class)))
                .thenReturn(new ArrayList<>());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assert.assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRules_WithNonEmptyCandidatesList() {
        List<Rule> rules = createRulesList(10);
        List<SqlCheckRuleFactory> candidates = createSqlCheckRuleFactoriesList(10);

        Mockito.when(SqlCheckRules.getAllFactories(ArgumentMatchers.any(DialectType.class), ArgumentMatchers.any(JdbcOperations.class)))
                .thenReturn(candidates);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assert.assertEquals(10, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel_WithEmptyRulesList() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        Assert.assertTrue(filledRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_WithNonEmptyRulesList() {
        List<Rule> rules = createRulesList(10);
        List<CheckViolation> violations = createCheckViolationsList(10);

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        Assert.assertEquals(10, filledRules.size());
    }

    private List<Rule> createRulesList(int size) {
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Rule rule = new Rule();
            RuleMetadata metadata = new RuleMetadata();
            metadata.setType(RuleType.SQL_CHECK);
            rule.setMetadata(metadata);
            rules.add(rule);
        }
        return rules;
    }

    private List<OffsetString> createOffsetStringsList(int size) {
        List<OffsetString> sqls = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            OffsetString sql = new OffsetString();
            sqls.add(sql);
        }
        return sqls;
    }

    private List<CheckViolation> createCheckViolationsList(int size) {
        List<CheckViolation> violations = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            CheckViolation violation = new CheckViolation();
            violations.add(violation);
        }
        return violations;
    }

    private ConnectionSession createConnectionSessionMock() {
        ConnectionSession connectionSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(connectionSession.getDialectType())
                .thenReturn(DialectType.MYSQL);
        Mockito.when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);
        return connectionSession;
    }

    private List<SqlCheckRuleFactory> createSqlCheckRuleFactoriesList(int size) {
        List<SqlCheckRuleFactory> factories = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            factories.add(Mockito.mock(SqlCheckRuleFactory.class));
        }
        return factories;
    }
}