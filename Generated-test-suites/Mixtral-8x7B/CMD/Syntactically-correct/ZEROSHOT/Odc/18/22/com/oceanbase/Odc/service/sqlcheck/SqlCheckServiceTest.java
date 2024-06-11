package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
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

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService = spy(new SqlCheckService());

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();
        List<CheckResult> checkResults = new ArrayList<>();

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
        when(sqlCheckService.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);
        doReturn(checkResults).when(sqlCheckService).SqlCheckUtil.buildCheckResults(checkViolations);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(checkResults, result);
        verify(sqlCheckService, times(1)).getRules(rules, session);
        verify(sqlCheckService, times(1)).check(anyList(), any(SqlCheckContext.class));
    }

    @Test
    public void testCheckWithSqls() {
        Long environmentId = 1L;
        String databaseName = "databaseName";
        List<OffsetString> sqls = new ArrayList<>();
        ConnectionConfig config = mock(ConnectionConfig.class);
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);
        SqlCheckContext checkContext = mock(SqlCheckContext.class);

        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, config.getDialectType(), jdbc)).thenReturn(sqlCheckRules);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcTemplate()).thenReturn(jdbc);
        doReturn(jdbc).when(sqlCheckService).resetSchema(jdbc, databaseName);
        when(sqlCheckService.check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), jdbc);
        verify(sqlCheckService, times(1)).check(anyList(), any(SqlCheckContext.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        ConnectionSession session = mock(ConnectionSession.class);
        List<SqlCheckRule> expectedRules = new ArrayList<>();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, session)).thenCallRealMethod();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertEquals(expectedRules, result);
        verify(sqlCheckService, times(1)).getRules(rules, session);
    }

    @Test
    public void testGetRulesWithJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = mock(JdbcOperations.class);
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        List<SqlCheckRule> expectedRules = new ArrayList<>();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, dialectType, jdbc)).thenCallRealMethod();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbc);

        assertEquals(expectedRules, result);
        verify(sqlCheckService, times(1)).getRules(rules, dialectType, jdbc);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> violatedRules = new ArrayList<>();
        Rule rule = new Rule();
        RuleViolation violation = new RuleViolation();
        CheckViolation checkViolation = new CheckViolation();

        rule.setLevel("INFO");
        violation.setLevel("ERROR");
        violation.setType(new RuleType());
        checkViolation.setType(new RuleType());
        checkViolation.setLevel(null);

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("${SQL_CHECK_TEST}", rule);

        violations.add(checkViolation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(violatedRules, result);
        assertEquals("ERROR", checkViolation.getLevel());
        verify(sqlCheckService, times(1)).fullFillRiskLevel(rules, violations);
    }

    @Test
    public void testResetSchema() {
        JdbcTemplate jdbc = mock(JdbcTemplate.class);

        sqlCheckService.resetSchema(jdbc, "databaseName");

        verify(jdbc, times(1)).execute("resetSchema");
    }
}