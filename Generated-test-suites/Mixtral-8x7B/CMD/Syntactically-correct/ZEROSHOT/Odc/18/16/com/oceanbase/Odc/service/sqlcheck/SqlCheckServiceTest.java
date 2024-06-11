package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
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
    @Validated
    private SqlCheckService sqlCheckService = new SqlCheckService();

    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        List<Rule> rules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        rules.add(sqlCheckRule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        verify(sqlCheckRule).check(req.getScriptContent());
        assertEquals(results.size(), 1);
    }

    @Test
    public void testCheck_emptyRules() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);
        assertEquals(results.size(), 0);
    }

    @Test
    public void testCheck_environment() {
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        Environment environment = new Environment();
        environment.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        rules.add(sqlCheckRule);
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "testdb", sqls, config);
        verify(sqlCheckRule).check(sqls, any(SqlCheckContext.class));
        assertEquals(violations.size(), 1);
    }

    @Test
    public void testGetRules_session() {
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(sqlCheckRules.size(), 0);
    }

    @Test
    public void testGetRules_jdbc() {
        List<Rule> rules = new ArrayList<>();
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
        candidates.add(sqlCheckRuleFactory);

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(candidates);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

        verify(sqlCheckRuleFactory, times(1)).createByRule(candidates, dialectType, rule);
        assertEquals(sqlCheckRules.size(), 1);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation = new CheckViolation();

        Rule rule = new Rule();
        rule.setLevel("ERROR");
        rule.setViolation(RuleViolation.fromCheckViolation(violation));

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("TEST", rule);

        violation.setType(RuleType.TEST);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(violation));

        assertEquals(violatedRules.size(), 1);
        assertEquals(violatedRules.get(0).getLevel(), "ERROR");
    }

}