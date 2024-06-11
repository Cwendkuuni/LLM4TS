package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("deprecation")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(mock(JdbcOperations.class));
    }

    @Test
    public void testCheck() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users WHERE id = 1;");

        List<Rule> rules = new ArrayList<>();
        Map<String, Rule> name2RuleMap = new HashMap<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("test_rule");
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(true);
        rule.setLevel(1);
        rules.add(rule);
        name2RuleMap.put("${test_rule}", rule);

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertNotNull(result.getViolation());
        assertEquals("test_rule", result.getViolation().getType().getName());
        assertEquals(1, result.getViolation().getLevel());
        assertFalse(result.getViolation().getMessage().isEmpty());
    }

    @Test
    public void testCheckEmptyList() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("");

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckEnvironments() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users WHERE id = 1;", 0));

        Environment environment = new Environment();
        environment.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        Map<String, Rule> name2RuleMap = new HashMap<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("test_rule");
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(true);
        rule.setLevel(1);
        rules.add(rule);
        name2RuleMap.put("${test_rule}", rule);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForList(ArgumentMatchers.anyString(), ArgumentMatchers.anyObject()))
                .thenReturn(Collections.singletonList(new HashMap<>()));

        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(dataSource.getConnection()).thenReturn(mock(ConnectionSession.class));
        when(dataSource.getJdbcTemplate()).thenReturn(jdbcOperations);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        config.setDataSource(dataSource);

        when(environmentService.detail(ArgumentMatchers.anyLong()))
                .thenReturn(environment);

        List<SqlCheckRule> sqlCheckRules =
                sqlCheckService.getRules(rules, config.getDialectType(), jdbcOperations);

        assertFalse(sqlCheckRules.isEmpty());
        assertEquals(1, sqlCheckRules.size());

        SqlCheckRule sqlCheckRule = sqlCheckRules.get(0);
        assertNotNull(sqlCheckRule.getName());
        assertEquals("test_rule", sqlCheckRule.getName());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());
        rules.get(0).setMetadata(new RuleMetadata());
        rules.get(0).getMetadata().setName("test_rule");
        rules.get(0).getMetadata().setType(RuleType.SQL_CHECK);
        rules.get(0).setEnabled(true);
        rules.get(0).setLevel(1);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(rules.get(0).getMetadata());
        violation.setLevel(1);
        violations.add(violation);

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, filledRules.size());
        assertEquals(1, filledRules.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevelEmptyViolations() {
        List<Rule> rules = new ArrayList<>();
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertTrue(filledRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelEmptyRules() {
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), violations);

        assertTrue(filledRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelNullViolations() {
        List<Rule> rules = new ArrayList<>();
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, null);

        assertTrue(filledRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelNullRules() {
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(null, violations);

        assertTrue(filledRules.isEmpty());
    }

    @Test
    public void testGetRulesEmptyRules() {
        List<Rule> rules = Collections.emptyList();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRulesNullRules() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, connectionSession);

        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRulesWithRules() {
        List<Rule> rules = new ArrayList<>();

        SqlCheckRuleFactory factory = mock(SqlCheckRules.SqlCheckRuleFactory.class);
        when(factory.getName()).thenReturn("test_factory");

        when(ruleService.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertTrue(sqlCheckRules.isEmpty());
    }
}