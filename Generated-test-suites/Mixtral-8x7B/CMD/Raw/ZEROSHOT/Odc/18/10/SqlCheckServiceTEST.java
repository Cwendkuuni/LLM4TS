package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq sqlCheckReq;

    private List<Rule> rules;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        session = mock(ConnectionSession.class);

        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");
        sqlCheckReq.setDelimiter(";");

        rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null, null, null));
        rule1.setEnabled(true);
        rules.add(rule1);
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", "SQL_CHECK", null, null, null));
        rule2.setEnabled(true);
        rules.add(rule2);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);
        when(session.getDialectType())
                .thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(
                        new JdbcTemplate(new SingleConnectionDataSource("jdbc:mysql://localhost:3306/test", "user", "pass")));

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.rule1);
        violation1.setLevel(1);
        violation1.setMessage("Violation 1");
        checkViolations.add(violation1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(RuleType.rule2);
        violation2.setLevel(2);
        violation2.setMessage("Violation 2");
        checkViolations.add(violation2);

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(checkViolations, results.get(0).getViolations());
    }

    @Test
    public void testCheckEmptyList() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentIdAndConfig() {
        when(environmentService.detail(anyLong()))
                .thenReturn(new Environment());

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        when(jdbcOperations.queryForObject(
                anyString(),
                ArgumentMatchers.<Object[]>any(),
                ArgumentMatchers.<JdbcOperations>any()))
                .thenReturn(1);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null, null, null));
        rule1.setEnabled(true);
        rules.add(rule1);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRules.SqlCheckRuleFactory factory = mock(SqlCheckRules.SqlCheckRuleFactory.class);
        sqlCheckRules.add(
                new SqlCheckRules.SqlCheckRule(factory, rule1, jdbcOperations, DialectType.MYSQL));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.rule1);
        violation1.setLevel(1);
        violation1.setMessage("Violation 1");
        checkViolations.add(violation1);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0));

        List<CheckViolation> result = sqlCheckService.check(1L, "test", sqls, config);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(checkViolations, result);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null, null, null));
        rule1.setEnabled(true);
        rule1.setLevel(1);
        rules.add(rule1);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.rule1);
        violation1.setLevel(1);
        violation1.setMessage("Violation 1");
        violations.add(violation1);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevelEmptyViolations() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null, null, null));
        rule1.setEnabled(true);
        rule1.setLevel(1);
        rules.add(rule1);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelNoRules() {
        List<Rule> rules = new ArrayList<>();

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.rule1);
        violation1.setLevel(1);
        violation1.setMessage("Violation 1");
        violations.add(violation1);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(r -> r.getRule()
                        .getMetadata()
                        .getType()
                        .equals(RuleType.SQL_CHECK.getName())));
    }

    @Test
    public void testGetRulesEmptyRules() {
        List<SqlCheckRule> result = sqlCheckService.getRules(Collections.emptyList(), session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesEmptyName2RuleMap() {
        Map<String, Rule> name2RuleMap = new HashMap<>();

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null, null, null));
        rule1.setEnabled(true);
        rules.add(rule1);

        List<SqlCheckRule> result = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithMultipleViolations() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.rule1);
        violation1.setLevel(1);
        violation1.setMessage("Violation 1");
        checkViolations.add(violation1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(RuleType.rule2);
        violation2.setLevel(2);
        violation2.setMessage("Violation 2");
        checkViolations.add(violation2);

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertEquals(1, results.size());

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(r -> r.getRule()
                        .getMetadata()
                        .getType()
                        .equals(RuleType.SQL_CHECK.getName())));
    }

    @Test
    public void testGetRulesWithMultipleViolationsAndNoRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.rule1);
        violation1.setLevel(1);
        violation1.setMessage("Violation 1");
        checkViolations.add(violation1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(RuleType.rule2);
        violation2.setLevel(2);
        violation2.setMessage("Violation 2");
        checkViolations.add(violation2);

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertEquals(1, results.size());

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithNoViolations() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertEquals(1, results.size());

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream()
                .allMatch(r -> r.getRule()
                        .getMetadata()
                        .getType()
                        .equals(RuleType.SQL_CHECK.getName())));
    }

    @Test
    public void testGetRulesWithNoViolationsAndEmptyRules() {
        List<SqlCheckRule> result = sqlCheckService.getRules(Collections.emptyList(), session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithNullRules() {
        List<SqlCheckRule> result = sqlCheckService.getRules(null, session);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRulesWithNullSession() {
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}