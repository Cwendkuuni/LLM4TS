import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;
    private SqlCheckReq sqlCheckReq;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        connectionSession = createConnectionSession();
        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setDelimiter(";");
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcOperations = jdbcTemplate;
        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        when(sqlCheckService.getRules(rules, connectionSession)).thenReturn(sqlCheckRules);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithViolations() {
        List<Rule> rules = createRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules();
        when(sqlCheckService.getRules(rules, connectionSession)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = createCheckViolations();
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, sqlCheckReq.getDelimiter(), sqlCheckRules);
        when(sqlChecker.check(sqlCheckReq.getScriptContent())).thenReturn(checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
        assertEquals(1, checkResults.size());
        assertEquals(checkViolations.size(), checkResults.get(0).getCheckViolations().size());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0));
        ConnectionConfig connectionConfig = new ConnectionConfig();

        Environment environment = new Environment();
        when(environmentService.detail(environmentId)).thenReturn(environment);

        List<Rule> rules = createRules();
        when(ruleService.list(environment.getRulesetId(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules();
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        when(sqlChecker.check(Collections.singletonList(sqls.get(0)), any(SqlCheckContext.class)))
                .thenReturn(checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = createRules();
        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(sqlCheckRules, result);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = createRules();
        List<CheckViolation> checkViolations = createCheckViolations();

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(1, violatedRules.size());
        assertEquals(RuleViolation.fromCheckViolation(checkViolations.get(0)), violatedRules.get(0).getViolation());
    }

    private ConnectionSession createConnectionSession() {
        ConnectionSession session = new ConnectionSession();
        session.setDialectType(DialectType.MYSQL);
        Map<String, Object> properties = new HashMap<>();
        properties.put(ConnectionSessionConstants.CONSOLE_DS_KEY, new SingleConnectionDataSource());
        session.setProperties(properties);
        return session;
    }

    private List<Rule> createRules() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("test", RuleType.SQL_CHECK, null));
        rule.setEnabled(true);
        List<Rule> rules = Collections.singletonList(rule);
        return rules;
    }

    private List<SqlCheckRule> createSqlCheckRules() {
        SqlCheckRule sqlCheckRule = new SqlCheckRule() {
            @Override
            public CheckViolation check(String sql) {
                return new CheckViolation();
            }
        };
        return Collections.singletonList(sqlCheckRule);
    }

    private List<CheckViolation> createCheckViolations() {
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(RuleType.SQL_CHECK);
        List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);
        return checkViolations;
    }
}