package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
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
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

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

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = mock(SqlCheckReq.class);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(mock(JdbcOperations.class));
    }

    @Test
    public void testCheck() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        List<Rule> rules = createRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "DELIMITER", sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation("test1", 1, 1, CheckViolation.Level.ERROR));
        checkViolations.add(new CheckViolation("test2", 2, 2, CheckViolation.Level.WARNING));

        when(sqlChecker.check(anyString())).thenReturn(checkViolations);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(2, result.size());
        CheckResult checkResult1 = result.get(0);
        assertEquals("test1", checkResult1.getName());
        assertEquals(1, checkResult1.getLine());
        assertEquals(1, checkResult1.getColumn());
        assertEquals(CheckViolation.Level.ERROR, checkResult1.getLevel());

        CheckResult checkResult2 = result.get(1);
        assertEquals("test2", checkResult2.getName());
        assertEquals(2, checkResult2.getLine());
        assertEquals(2, checkResult2.getColumn());
        assertEquals(CheckViolation.Level.WARNING, checkResult2.getLevel());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptySqlCheckerCheck() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        List<Rule> rules = createRules();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "DELIMITER", Collections.emptyList());

        when(sqlChecker.check(anyString())).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithNullRuleServiceList() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(null);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetRules() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(mock(JdbcOperations.class));

        List<Rule> rules = createRules();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertNotNull(sqlCheckRules);
        assertEquals(2, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(mock(JdbcOperations.class));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithNullRules() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(mock(JdbcOperations.class));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(null, connectionSession);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        Rule rule1 = new Rule();
        rule1.setLevel(Rule.Level.ERROR);
        rule1.setMetadata(new RuleMetadata("test1", RuleType.SQL_CHECK));

        Rule rule2 = new Rule();
        rule2.setLevel(Rule.Level.WARNING);
        rule2.setMetadata(new RuleMetadata("test2", RuleType.SQL_CHECK));

        List<Rule> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);

        CheckViolation violation1 = new CheckViolation("test1", 1, 1);
        CheckViolation violation2 = new CheckViolation("test2", 2, 2);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation1);
        violations.add(violation2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(2, result.size());
        assertEquals(Rule.Level.ERROR, result.get(0).getLevel());
        assertEquals(Rule.Level.WARNING, result.get(1).getLevel());
    }

    @Test
    public void testFullFillRiskLevelWithEmptyViolations() {
        List<Rule> rules = createRules();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevelWithNullViolations() {
        List<Rule> rules = createRules();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, null);

        assertEquals(0, result.size());
    }

    private List<Rule> createRules() {
        Rule rule1 = new Rule();
        rule1.setLevel(Rule.Level.ERROR);
        rule1.setMetadata(new RuleMetadata("test1", RuleType.SQL_CHECK));

        Rule rule2 = new Rule();
        rule2.setLevel(Rule.Level.WARNING);
        rule2.setMetadata(new RuleMetadata("test2", RuleType.SQL_CHECK));

        return List.of(rule1, rule2);
    }

    private List<SqlCheckRule> createSqlCheckRules(List<Rule> rules) {
        SqlCheckRuleFactory factory1 = mock(SqlCheckRules.SqlCheckRuleFactory.class);
        SqlCheckRuleFactory factory2 = mock(SqlCheckRules.SqlCheckRuleFactory.class);

        when(factory1.create(any(), any(), any())).thenReturn(mock(SqlCheckRule.class));
        when(factory2.create(any(), any(), any())).thenReturn(mock(SqlCheckRule.class));

        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        candidates.add(factory1);
        candidates.add(factory2);

        return rules.stream().map(rule -> SqlCheckRules.createByRule(candidates, DialectType.MYSQL, rule))
                .collect(Collectors.toList());
    }
}