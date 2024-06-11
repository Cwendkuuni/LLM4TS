package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
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

    private SqlCheckReq checkReq;

    private List<Rule> rules;

    private List<SqlCheckRule> sqlCheckRules;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        session = mock(ConnectionSession.class);

        checkReq = new SqlCheckReq();
        checkReq.setScriptContent("SELECT * FROM users;");

        rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setLevel("ERROR");
        RuleMetadata ruleMetadata1 = new RuleMetadata();
        ruleMetadata1.setName("rule1");
        ruleMetadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(ruleMetadata1);
        rules.add(rule1);

        sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule1 = new SqlCheckRule();
        sqlCheckRule1.setRule(rule1);
        sqlCheckRule1.setType("SQL_INJECTION");
        sqlCheckRules.add(sqlCheckRule1);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(mock(JdbcOperations.class));

        List<CheckResult> checkResults = sqlCheckService.check(session, checkReq);

        assertNotNull(checkResults);
        assertEquals(1, checkResults.size());

        CheckResult checkResult = checkResults.get(0);
        assertEquals("SELECT * FROM users;", checkResult.getSql());
        assertEquals("ERROR", checkResult.getLevel());
        assertEquals("SQL_INJECTION", checkResult.getType());

        ArgumentCaptor<List<Rule>> ruleArgumentCaptor = ArgumentCaptor.forClass(List.class);
        verify(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .execute(any(String.class), ruleArgumentCaptor.capture());
        List<Rule> rulesUsed = ruleArgumentCaptor.getValue();
        assertEquals(1, rulesUsed.size());
        assertEquals(rule1.getId(), rulesUsed.get(0).getId());
    }

    @Test
    public void testCheckEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(session, checkReq);

        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckEmptyScriptContent() {
        checkReq.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(session, checkReq);

        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckNoRulesEnabled() {
        rules.forEach(rule -> rule.setEnabled(false));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> checkResults = sqlCheckService.check(session, checkReq);

        assertTrue(checkResults.isEmpty());
    }

    @Test
    public void testCheckEnvironment() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        Environment environment = new Environment();
        environment.setId(1L);
        environment.setRulesetId(1L);
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 23));

        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(mock(javax.sql.Connection.class));
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        when(factory.getJdbcTemplate()).thenReturn(jdbcTemplate);

        List<CheckViolation> checkViolations = sqlCheckService.check(environment.getId(), "dbName", sqls, config);

        assertEquals(1, checkViolations.size());
        CheckViolation checkViolation = checkViolations.get(0);
        assertEquals(0, checkViolation.getOffset());
        assertEquals(23, checkViolation.getLength());
        assertEquals("SELECT * FROM users;", checkViolation.getSql());

        verify(factory, times(1)).getDataSource();
        verify(dataSource, times(1)).getConnection();
        verify(jdbcTemplate, times(1)).query(any(String.class), any(SqlCheckRule.class), any(SqlCheckContext.class));
    }

    @Test
    public void testCheckEnvironmentEmptySqls() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<OffsetString> sqls = new ArrayList<>();

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "dbName", sqls, config);

        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(session);

        assertEquals(1, sqlCheckRules.size());
        assertEquals(rule1.getId(), sqlCheckRules.get(0).getRule().getId());
        assertEquals("SQL_INJECTION", sqlCheckRules.get(0).getType());

        verify(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY), never())
                .execute(any(String.class), any(List.class));
    }

    @Test
    public void testGetRulesEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(session);

        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRulesEmptySqlCheckRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(List.of(new Rule()));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(session);

        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType("SQL_INJECTION");
        violations.add(violation1);

        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setId(1L);
        rule1.setLevel("ERROR");
        RuleMetadata ruleMetadata1 = new RuleMetadata();
        ruleMetadata1.setName("rule1");
        ruleMetadata1.setType(RuleType.SQL_CHECK);
        rule1.setMetadata(ruleMetadata1);
        rules.add(rule1);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
        assertEquals("ERROR", result.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevelNoViolations() {
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertTrue(result.isEmpty());
    }

}