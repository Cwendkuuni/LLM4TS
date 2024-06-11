import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private ConnectionConfig config;

    private SqlCheckReq req;

    private Environment environment;

    private SqlCheckRuleFactory factory;

    private SqlCheckRule rule;

    private List<Rule> rules;

    private List<CheckViolation> violations;

    private List<CheckResult> results;

    private JdbcOperations jdbcOperations;

    private SingleConnectionDataSource dataSource;

    private OBConsoleDataSourceFactory dataSourceFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize mocks
        session = mock(ConnectionSession.class);
        config = mock(ConnectionConfig.class);
        req = mock(SqlCheckReq.class);
        environment = mock(Environment.class);
        factory = mock(SqlCheckRuleFactory.class);
        rule = mock(SqlCheckRule.class);
        rules = new ArrayList<>();
        violations = new ArrayList<>();
        results = new ArrayList<>();
        jdbcOperations = mock(JdbcOperations.class);
        dataSource = mock(SingleConnectionDataSource.class);
        dataSourceFactory = mock(OBConsoleDataSourceFactory.class);

        // Set up mock behavior
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getDelimiter()).thenReturn(";");
        when(req.getScriptContent()).thenReturn("SELECT * FROM users;");
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        when(factory.create(any(), any(), any())).thenReturn(rule);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);
        when(dataSourceFactory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcOperations()).thenReturn(jdbcOperations);

        // Initialize list of rules
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        Rule rule1 = new Rule();
        rule1.setMetadata(metadata);
        rules.add(rule1);

        // Initialize violations and results
        CheckViolation violation = new CheckViolation();
        violation.setType(rule1);
        violation.setMessage("Test violation");
        violation.setLevel("ERROR");
        violations.add(violation);

        CheckResult result = new CheckResult();
        result.setRuleName("test_rule");
        result.setViolations(violations);
        results.add(result);
    }

    @Test
    public void testCheck() {
        when(rule.check(any(), any())).thenReturn(violations);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertEquals(results.size(), checkResults.size());
        for (int i = 0; i < results.size(); i++) {
            assertEquals(results.get(i).getRuleName(), checkResults.get(i).getRuleName());
            assertEquals(results.get(i).getViolations().size(), checkResults.get(i).getViolations().size());
            for (int j = 0; j < results.get(i).getViolations().size(); j++) {
                assertEquals(results.get(i).getViolations().get(j).getType(), checkResults.get(i).getViolations().get(j).getType());
                assertEquals(results.get(i).getViolations().get(j).getMessage(), checkResults.get(i).getViolations().get(j).getMessage());
                assertEquals(results.get(i).getViolations().get(j).getLevel(), checkResults.get(i).getViolations().get(j).getLevel());
            }
        }
    }

    @Test
    public void testCheckEmptyRules() {
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheck2() {
        when(rule.check(any(), any())).thenReturn(violations);

        List<CheckViolation> checkViolations = sqlCheckService.check(config, "test_database", Collections.singletonList("SELECT * FROM users;"), config);

        assertNotNull(checkViolations);
        assertEquals(violations.size(), checkViolations.size());
        for (int i = 0; i < violations.size(); i++) {
            assertEquals(violations.get(i).getType(), checkViolations.get(i).getType());
            assertEquals(violations.get(i).getMessage(), checkViolations.get(i).getMessage());
            assertEquals(violations.get(i).getLevel(), checkViolations.get(i).getLevel());
        }
    }

    @Test
    public void testCheckEmptySqls() {
        List<CheckViolation> checkViolations = sqlCheckService.check(config, "test_database", Collections.emptyList(), config);

        assertNotNull(checkViolations);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any())).thenReturn(rules);
        when(factory.create(any(), any(), any())).thenReturn(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertNotNull(sqlCheckRules);
        assertEquals(1, sqlCheckRules.size());
        assertEquals(rule, sqlCheckRules.get(0));
    }

    @Test
    public void testGetRulesEmptyRules() {
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);

        assertNotNull(sqlCheckRules);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("${test_rule}", rules.get(0));
        violations.add(new CheckViolation());

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(filledRules);
        assertEquals(1, filledRules.size());
        assertEquals(RuleType.SQL_CHECK, filledRules.get(0).getMetadata().getType());
    }

    @Test
    public void testFullFillRiskLevelEmptyViolations() {
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertNotNull(filledRules);
        assertTrue(filledRules.isEmpty());
    }

}