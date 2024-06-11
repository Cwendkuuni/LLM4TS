package com.oceanbase.odc.service.sqlcheck;

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
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
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
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession connectionSession;
    private SqlCheckReq sqlCheckReq;
    private List<Rule> rules;
    private List<SqlCheckRule> sqlCheckRules;
    private List<CheckViolation> checkViolations;
    private List<CheckResult> checkResults;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = new SqlCheckReq();

        rules = new ArrayList<>();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel("ERROR");
        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(mock(SqlCheckRule.class));

        checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation());

        checkResults = new ArrayList<>();
        checkResults.add(new CheckResult());

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(mock(JdbcTemplate.class));
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(rules.get(0)));
        when(sqlCheckService.getRules(rules, connectionSession))
                .thenReturn(sqlCheckRules);
        when(sqlCheckService.fullFillRiskLevel(anyList(), anyList()))
                .thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(checkResults, result);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(1)).getRules(rules, connectionSession);
        verify(sqlCheckService, times(1)).fullFillRiskLevel(anyList(), anyList());
    }

    @Test
    public void testCheck_emptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(Collections.emptyList(), result);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, never()).getRules(rules, connectionSession);
        verify(sqlCheckService, never()).fullFillRiskLevel(anyList(), anyList());
    }

    @Test
    public void testCheckWithEnvironmentAndConfig() {
        when(environmentService.detail(anyLong()))
                .thenReturn(mock(Environment.class));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);
        when(sqlCheckService.getRules(rules, any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(sqlCheckRules);
        when(sqlCheckService.fullFillRiskLevel(anyList(), anyList()))
                .thenReturn(rules);

        List<CheckViolation> result = sqlCheckService.check(1L, "test_database",
                Collections.singletonList(new OffsetString("test_sql", 0, 10)),
                new ConnectionConfig());

        assertEquals(checkViolations, result);

        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(1)).getRules(rules, any(DialectType.class), any(JdbcOperations.class));
        verify(sqlCheckService, times(1)).fullFillRiskLevel(anyList(), anyList());
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(sqlCheckRules, result);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, never()).fullFillRiskLevel(anyList(), anyList());
    }

    @Test
    public void testGetRules_emptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<SqlCheckRule> result = sqlCheckService.getRules(Collections.emptyList(), connectionSession);

        assertEquals(Collections.emptyList(), result);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, never()).fullFillRiskLevel(anyList(), anyList());
    }

    @Test
    public void testFullFillRiskLevel() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(rules.size(), result.size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel_emptyViolations() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());

        assertEquals(0, result.size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }
}