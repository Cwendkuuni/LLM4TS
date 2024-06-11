import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    private static final String TEST_SCRIPT_CONTENT = "SELECT * FROM employees;";
    private static final String TEST_DATABASE_NAME = "testdb";
    private static final String TEST_SCRIPT_CONTENT_WITH_VIOLATIONS = "SELECT * FROM employees WHERE salary > 1000;";

    @Autowired
    private ApplicationContext applicationContext;

    private EnvironmentService environmentService;
    private RuleService ruleService;
    private SqlCheckService sqlCheckService;

    private ConnectionConfig connectionConfig;
    private ConnectionSession connectionSession;
    private SingleConnectionDataSource dataSource;
    private JdbcOperations jdbc;

    @Before
    public void setup() {
        environmentService = mock(EnvironmentService.class);
        ruleService = mock(RuleService.class);

        sqlCheckService = new SqlCheckService();
        ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);
        ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("password");
        dataSource.setAutoCommit(false);

        jdbc = new JdbcTemplate(dataSource);
        dataSource.afterPropertiesSet();

        connectionSession = ConnectionSessionUtil.createConnectionSession(connectionConfig, dataSource, jdbc);
    }

    @After
    public void teardown() {
        if (dataSource != null) {
            dataSource.destroy();
        }
    }

    @Test
    public void testCheckWithEmptySession() {
        sqlCheckService.check(null, new SqlCheckReq());

        verifyZeroInteractions(ruleService, environmentService);
    }

    @Test
    public void testCheckWithEmptyRequest() {
        sqlCheckService.check(connectionSession, new SqlCheckReq());

        verifyZeroInteractions(ruleService, environmentService);
    }

    @Test
    public void testCheckWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq().setScriptContent(TEST_SCRIPT_CONTENT));

        Assert.assertTrue(results.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheck() {
        List<Rule> rules = getTestRules();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq().setScriptContent(TEST_SCRIPT_CONTENT));

        Assert.assertFalse(results.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithViolations() {
        List<Rule> rules = getTestRulesWithViolations();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq().setScriptContent(TEST_SCRIPT_CONTENT_WITH_VIOLATIONS));

        Assert.assertFalse(results.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithMultipleSqls() {
        List<Rule> rules = getTestRules();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM employees;"));
        sqls.add(new OffsetString("SELECT * FROM customers;"));

        List<CheckViolation> violations = sqlCheckService.check(1L, TEST_DATABASE_NAME, sqls, connectionConfig);

        Assert.assertFalse(violations.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = getTestRules();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assert.assertEquals(2, sqlCheckRules.size());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);

        Assert.assertTrue(sqlCheckRules.isEmpty());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRulesWithNullSession() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, connectionSession);

        Assert.assertTrue(sqlCheckRules.isEmpty());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = getTestRules();

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("testType", "testMessage", 1));

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        Assert.assertEquals(2, filledRules.size());
        Assert.assertEquals(RuleType.SQL_CHECK, filledRules.get(0).getType());
        Assert.assertNull(filledRules.get(0).getViolation());

        Assert.assertEquals(RuleType.SQL_CHECK, filledRules.get(1).getType());
        Assert.assertNotNull(filledRules.get(1).getViolation());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), new ArrayList<>());

        Assert.assertTrue(filledRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(getTestRules(), new ArrayList<>());

        Assert.assertEquals(2, filledRules.size());

        Assert.assertNull(filledRules.get(0).getViolation());
        Assert.assertNull(filledRules.get(1).getViolation());
    }

    private List<Rule> getTestRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(getTestRule("testRule1", RuleType.SQL_CHECK));
        rules.add(getTestRule("testRule2", RuleType.SQL_CHECK));
        return rules;
    }

    private List<Rule> getTestRulesWithViolations() {
        List<Rule> rules = new ArrayList<>();
        rules.add(getTestRuleWithViolation("testRule1", RuleType.SQL_CHECK));
        rules.add(getTestRuleWithViolation("testRule2", RuleType.SQL_CHECK));
        return rules;
    }

    private Rule getTestRule(String name, RuleType type) {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(type);
        metadata.setName(name);
        metadata.setEnabled(true);

        rule.setMetadata(metadata);
        return rule;
    }

    private Rule getTestRuleWithViolation(String name, RuleType type) {
        Rule rule = getTestRule(name, type);
        rule.setViolation(new RuleViolation());
        return rule;
    }
}