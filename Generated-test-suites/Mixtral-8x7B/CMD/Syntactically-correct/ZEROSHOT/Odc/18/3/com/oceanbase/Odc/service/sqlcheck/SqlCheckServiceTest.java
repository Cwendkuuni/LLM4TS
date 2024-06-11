package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
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
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private SqlCheckReq sqlCheckReq;

    private List<Rule> rules;

    private List<SqlCheckRuleFactory> candidates;

    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);

        sqlCheckReq = mock(SqlCheckReq.class);

        rules = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Rule rule = mock(Rule.class);
            when(rule.getMetadata()).thenReturn(mock(RuleMetadata.class));
            rules.add(rule);
        }

        candidates = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
            candidates.add(factory);
        }

        jdbcOperations = mock(JdbcOperations.class);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, eq(connectionSession))).thenReturn(candidates);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(candidates.size(), checkResults.size());
    }

    @Test
    public void testCheckWithEmptySqls() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, eq(connectionSession))).thenReturn(candidates);

        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(sqlCheckService.getRules(rules, eq(connectionSession))).thenReturn(candidates);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        Environment environment = mock(Environment.class);
        when(environmentService.detail(anyLong())).thenReturn(environment);

        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("test sql"));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, eq(config.getDialectType()), eq(jdbcOperations)))
                .thenReturn(candidates);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, config);

        assertEquals(candidates.size(), checkViolations.size());
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(candidates.size(), sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> checkViolations = new ArrayList<>();

        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("r1", RuleType.SQL_CHECK));
        rule1.setLevel("MEDIUM");
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(rule1.getMetadata());
        checkViolations.add(violation1);

        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("r2", RuleType.SQL_CHECK));
        rule2.setLevel("HIGH");
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(rule2.getMetadata());
        checkViolations.add(violation2);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(2, violatedRules.size());

        assertEquals("MEDIUM", violatedRules.get(0).getLevel());
        assertEquals(rule1.getViolation(), violatedRules.get(0).getViolation());

        assertEquals("HIGH", violatedRules.get(1).getLevel());
        assertEquals(rule2.getViolation(), violatedRules.get(1).getViolation());
    }
}