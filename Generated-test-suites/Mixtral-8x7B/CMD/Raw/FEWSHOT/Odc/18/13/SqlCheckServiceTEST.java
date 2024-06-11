package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;
    @Mock
    private EnvironmentService environmentService;
    @Mock
    private ConnectionSession session;
    @Mock
    private JdbcOperations jdbc;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private SingleConnectionDataSource dataSource;
    @Mock
    private OBConsoleDataSourceFactory factory;
    @Mock
    private ConnectionConfig config;
    @Mock
    private Environment env;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheck() {
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckResult checkResult = new CheckResult();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
        when(sqlCheckService.check(anyString(), eq(sqlCheckRules))).thenReturn(checkViolations);

        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());

        assertEquals(checkResult, result.get(0));
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(session);
        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(sqlCheckService, times(1)).getRules(rules, session);
        verify(sqlChecker, times(1)).check(anyString(), eq(sqlCheckRules));
    }

    @Test
    public void testCheck_emptyRules() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());

        assertTrue(result.isEmpty());
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(session);
        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheck_emptySqlCheckRules() {
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();

        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, QueryRuleMetadataParams.builder().build())).thenReturn(rules);
        when(sqlCheckService.getRules(rules, session)).thenReturn(new ArrayList<>());

        List<CheckResult> result = sqlCheckService.check(session, new SqlCheckReq());

        assertTrue(result.isEmpty());
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(session);
        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(sqlCheckService, times(1)).getRules(rules, session);
        verify(sqlChecker, never()).check(anyString(), anyListOf(SqlCheckRule.class));
    }

    @Test
    public void testCheck_environmentId() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();
        List<Rule> rules = new ArrayList<>();

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(env.getRulesetId()).thenReturn(2L);
        when(ruleService.list(2L, QueryRuleMetadataParams.builder().build())).thenReturn(rules);

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(environmentService, times(1)).detail(environmentId);
        verify(env, times(1)).getRulesetId();
        verify(ruleService, times(1)).list(2L, QueryRuleMetadataParams.builder().build());
        verify(config, never()).getDialectType();
        verify(jdbc, never()).query(anyString(), any(Object[].class), any(RowMapper.class));
        verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), jdbc);
        verify(sqlChecker, never()).check(anyListOf(OffsetString.class), any(SqlCheckContext.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = new JdbcTemplate();

        sqlCheckService.getRules(rules, dialectType, jdbc);

        verify(SqlCheckRules, times(1)).getAllFactories(dialectType, jdbc);
        verify(rules, times(1)).stream();
        verify(rules, times(1))
                .filter(r -> Objects.equals(r.getMetadata().getType(), RuleType.SQL_CHECK));
        verify(rules, times(1)).map(rule -> {
            try {
                return SqlCheckRules.createByRule(candidates, dialectType, rule);
            } catch (Exception e) {
                return null;
            }
        });
    }

    @Test
    public void testGetRules_emptyRules() {
        List<Rule> rules = new ArrayList<>();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertTrue(result.isEmpty());
        verify(rules, times(1)).stream();
        verify(rules, times(1))
                .filter(r -> Objects.equals(r.getMetadata().getType(), RuleType.SQL_CHECK));
        verify(rules, times(1)).map(rule -> {
            try {
                return SqlCheckRules.createByRule(anyList(), any(DialectType.class), any(Rule.class));
            } catch (Exception e) {
                return null;
            }
        });
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        verify(rules, times(1)).stream();
        verify(checkViolations, times(1)).forEach(c -> {
            String name = "${" + c.getType().getName() + "}";
            Rule rule = rules.stream().filter(r -> Objects.equals(r.getMetadata().getName(), name)).findFirst()
                    .orElse(null);
            if (Objects.nonNull(rule)) {
                c.setLevel(rule.getLevel());
                Rule newRule = new Rule();
                newRule.setLevel(rule.getLevel());
                newRule.setViolation(RuleViolation.fromCheckViolation(c));
                checkViolations.add(newRule);
            }
        });
    }

    @Test
    public void testFullFillRiskLevel_emptyRules() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        verify(rules, never()).stream();
        verify(checkViolations, never()).forEach(c -> {
            String name = "${" + c.getType().getName() + "}";
            Rule rule = rules.stream().filter(r -> Objects.equals(r.getMetadata().getName(), name)).findFirst()
                    .orElse(null);
            if (Objects.nonNull(rule)) {
                c.setLevel(rule.getLevel());
                Rule newRule = new Rule();
                newRule.setLevel(rule.getLevel());
                newRule.setViolation(RuleViolation.fromCheckViolation(c));
                checkViolations.add(newRule);
            }
        });
    }

}