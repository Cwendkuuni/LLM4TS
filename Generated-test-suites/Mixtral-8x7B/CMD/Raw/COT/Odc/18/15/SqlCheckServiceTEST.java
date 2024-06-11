import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckContext;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
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

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        connectionSession = Mockito.mock(ConnectionSession.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        jdbcOperations = Mockito.mock(JdbcOperations.class);

        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
    }

    @Test
    public void testCheck_ShouldReturnEmptyListWhenRuleSetIdIsNull() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq());

        assertEquals(Collections.emptyList(), results);
    }

    @Test
    public void testCheck_ShouldReturnCheckResultsWhenRuleSetIdIsNotNull() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);

        List<Rule> rules = createRules();
        when(ruleService.list(ruleSetId, Mockito.any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        when(ruleService.createByRule(any(), any(), any())).thenReturn(sqlCheckRules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq());

        assertEquals(1, results.size());
    }

    @Test
    public void testCheck_ShouldReturnEmptyListWhenNoSqlCheckRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);

        List<Rule> rules = createRules();
        when(ruleService.list(ruleSetId, Mockito.any())).thenReturn(rules);

        when(ruleService.createByRule(any(), any(), any())).thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq());

        assertEquals(Collections.emptyList(), results);
    }

    @Test
    public void testCheck_ShouldReturnCheckResultsWhenSqlScriptIsEmpty() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);

        List<Rule> rules = createRules();
        when(ruleService.list(ruleSetId, Mockito.any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        when(ruleService.createByRule(any(), any(), any())).thenReturn(sqlCheckRules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertEquals(Collections.emptyList(), results);
    }

    @Test
    public void testCheck_ShouldReturnCheckResultsWhenSqlScriptIsProvided() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);

        List<Rule> rules = createRules();
        when(ruleService.list(ruleSetId, Mockito.any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        when(ruleService.createByRule(any(), any(), any())).thenReturn(sqlCheckRules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertEquals(1, results.size());
    }

    @Test
    public void testCheck_ShouldReturnCheckViolationsWhenCheckIsPerformed() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);

        List<Rule> rules = createRules();
        when(ruleService.list(ruleSetId, Mockito.any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        when(ruleService.createByRule(any(), any(), any())).thenReturn(sqlCheckRules);

        SqlCheckContext sqlCheckContext = new SqlCheckContext(1L);
        when(jdbcOperations.query(anyString(), any(), any(Object[].class))).thenReturn(Collections.emptyList());

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getViolations().size());
    }

    @Test
    public void testCheckEnvironment_ShouldReturnEmptyListWhenEnvironmentIdIsNull() {
        List<CheckViolation> violations = sqlCheckService.check(null, "", Collections.emptyList(), connectionConfig);

        assertEquals(Collections.emptyList(), violations);
    }

    @Test
    public void testCheckEnvironment_ShouldReturnEmptyListWhenDatabaseNameIsNull() {
        List<CheckViolation> violations = sqlCheckService.check(1L, null, Collections.emptyList(), connectionConfig);

        assertEquals(Collections.emptyList(), violations);
    }

    @Test
    public void testCheckEnvironment_ShouldReturnEmptyListWhenSqlsIsEmpty() {
        List<CheckViolation> violations = sqlCheckService.check(1L, "", Collections.emptyList(), connectionConfig);

        assertEquals(Collections.emptyList(), violations);
    }

    @Test
    public void testCheckEnvironment_ShouldReturnCheckViolationsWhenSqlScriptsAreProvided() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = createOffsetStrings();

        Environment environment = Mockito.mock(Environment.class);
        when(environment.getRulesetId()).thenReturn(1L);

        when(environmentService.detail(environmentId)).thenReturn(environment);

        List<Rule> rules = createRules();
        when(ruleService.list(environment.getRulesetId(), Mockito.any())).thenReturn(rules);

        SingleConnectionDataSource dataSource = Mockito.mock(SingleConnectionDataSource.class);
        when(dataSource.getConnection()).thenReturn(Mockito.mock(Connection.class));

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        when(connectionConfig.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        when(ruleService.createByRule(any(), any(), any())).thenReturn(sqlCheckRules);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(2, violations.size());
    }

    @Test
    public void testGetRules_ShouldReturnEmptyListWhenRulesAreEmpty() {
        List<Rule> rules = new ArrayList<>();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testGetRules_ShouldReturnSqlCheckRulesWhenRulesAndJdbcOperationsAreProvided() {
        List<Rule> rules = createRules();

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);

        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcTemplate);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(sqlCheckRules, result);
    }

    @Test
    public void testGetRules_ShouldReturnEmptyListWhenJdbcOperationsIsNull() {
        List<Rule> rules = createRules();

        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(null);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFullFillRiskLevel_ShouldReturnEmptyListWhenViolationsAreEmpty() {
        List<Rule> rules = createRules();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testFullFillRiskLevel_ShouldReturnViolatedRulesWhenViolationsAreProvided() {
        List<Rule> rules = createRules();
        CheckViolation violation1 = createCheckViolation(1L, "SQL01101", "test message 1");
        CheckViolation violation2 = createCheckViolation(2L, "SQL01102", "test message 2");

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation1);
        violations.add(violation2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getLevel().longValue());
        assertEquals("SQL01101", result.get(0).getViolation().getCode());
        assertEquals("test message 1", result.get(0).getViolation().getMessage());

        assertEquals(2, result.get(1).getLevel().longValue());
        assertEquals("SQL01102", result.get(1).getViolation().getCode());
        assertEquals("test message 2", result.get(1).getViolation().getMessage());
    }

    private List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();

        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setMetadata(new RuleMetadata("SQL01101", "Test Rule 1"));
        rule1.setLevel(1L);
        rule1.setEnabled(true);
        rules.add(rule1);

        Rule rule2 = new Rule();
        rule2.setId(2L);
        rule2.setMetadata(new RuleMetadata("SQL01102", "Test Rule 2"));
        rule2.setLevel(2L);
        rule2.setEnabled(true);
        rules.add(rule2);

        return rules;
    }

    private List<SqlCheckRule> createSqlCheckRules(List<Rule> rules) {
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();

        SqlCheckRule sqlCheckRule1 = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule1.getType()).thenReturn(RuleType.SQL_CHECK);
        sqlCheckRules.add(sqlCheckRule1);

        SqlCheckRule sqlCheckRule2 = Mockito.mock(SqlCheckRule.class);
        when(sqlCheckRule2.getType()).thenReturn(RuleType.SQL_CHECK);
        sqlCheckRules.add(sqlCheckRule2);

        return sqlCheckRules;
    }

    private CheckViolation createCheckViolation(Long id, String code, String message) {
        CheckViolation violation = new CheckViolation();
        violation.setId(id);
        violation.setCode(code);
        violation.setMessage(message);

        return violation;
    }

    private List<OffsetString> createOffsetStrings() {
        List<OffsetString> offsetStrings = new ArrayList<>();

        OffsetString offsetString1 = new OffsetString();
        offsetString1.setText("SELECT * FROM users;");
        offsetString1.setOffset(0);
        offsetStrings.add(offsetString1);

        OffsetString offsetString2 = new OffsetString();
        offsetString2.setText("SELECT * FROM products;");
        offsetString2.setOffset(100);
        offsetStrings.add(offsetString2);

        return offsetStrings;
    }
}