package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import org.apache.commons.lang3.ObjectUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
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

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    private RuleService ruleService;
    private EnvironmentService environmentService;
    private SqlCheckService sqlCheckService;
    private ConnectionSession session;

    @Before
    public void setUp() {
        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);

        sqlCheckService = spy(new SqlCheckService());
        doReturn(ruleService).when(sqlCheckService).ruleService;
        doReturn(environmentService).when(sqlCheckService).environmentService;

        session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = createRules();
        doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(createSqlCheckRule("SELECT", "only_select_allowed", "info"));
        sqlCheckRules.add(createSqlCheckRule("*", "no_wildcards", "warning"));
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("only_select_allowed", "info"));
        violations.add(new CheckViolation("no_wildcards", "warning"));
        CheckResult result = new CheckResult("test.sql", violations);
        List<CheckResult> expected = Collections.singletonList(result);

        List<CheckResult> actual = sqlCheckService.check(session, req);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheck_withNoRules() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        doReturn(Collections.emptyList()).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        List<CheckResult> actual = sqlCheckService.check(session, req);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testCheckWithEnvironments() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 18));

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = createRules();
        doReturn(rules).when(ruleService).list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class));

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(createSqlCheckRule("SELECT", "only_select_allowed", "info"));
        sqlCheckRules.add(createSqlCheckRule("*", "no_wildcards", "warning"));
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, config.getDialectType(), jdbcOperations);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("only_select_allowed", "info"));
        violations.add(new CheckViolation("no_wildcards", "warning"));
        CheckResult result = new CheckResult("test.sql", violations);
        List<CheckResult> expected = Collections.singletonList(result);

        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(factory.resetSchema(any(Function.class))).thenReturn(factory);
        when(factory.createJdbcOperations()).thenReturn(jdbcOperations);

        doReturn(factory).when(sqlCheckService).createOBConsoleDataSourceFactory(config);

        List<CheckResult> actual = sqlCheckService.check(env.getRulesetId(), "test_db", sqls, config);
        assertEquals(expected, actual);
    }

    @Test
    public void testCheckWithEnvironments_noSqls() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        Environment env = new Environment();
        env.setRulesetId(1L);

        doReturn(Collections.emptyList()).when(ruleService).list(eq(env.getRulesetId()), any(QueryRuleMetadataParams.class));

        List<CheckResult> actual = sqlCheckService.check(env.getRulesetId(), "test_db", Collections.emptyList(), config);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = createRules();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(createSqlCheckRule("SELECT", "only_select_allowed", "info"));
        sqlCheckRules.add(createSqlCheckRule("*", "no_wildcards", "warning"));

        doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);

        List<SqlCheckRule> actual = sqlCheckService.getRules(rules, session);
        assertEquals(sqlCheckRules, actual);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = createRules();
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("only_select_allowed", "info"));
        violations.add(new CheckViolation("no_wildcards", "warning"));

        List<Rule> expected = new ArrayList<>();
        expected.add(new Rule(null, null, null, null, "info", RuleType.SQL_CHECK, 0, null));
        expected.add(new Rule(null, null, null, null, "warning", RuleType.SQL_CHECK, 0, null));

        List<Rule> actual = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(expected, actual);
    }

    private List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();
        RuleMetadata metadata1 = new RuleMetadata();
        metadata1.setName("only_select_allowed");
        metadata1.setType(RuleType.SQL_CHECK);
        Rule rule1 = new Rule(1L, metadata1, true, null);

        RuleMetadata metadata2 = new RuleMetadata();
        metadata2.setName("no_wildcards");
        metadata2.setType(RuleType.SQL_CHECK);
        Rule rule2 = new Rule(2L, metadata2, true, null);

        rules.add(rule1);
        rules.add(rule2);
        return rules;
    }

    private SqlCheckRule createSqlCheckRule(String keyword, String ruleName, String level) {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName(ruleName);
        metadata.setType(RuleType.SQL_CHECK);

        Map<String, Object> properties = new HashMap<>();
        properties.put(keyword, true);

        return new SqlCheckRule(metadata, properties, level);
    }

    private OBConsoleDataSourceFactory createOBConsoleDataSourceFactory(ConnectionConfig config) {
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(factory.resetSchema(any(Function.class))).thenReturn(factory);
        when(factory.createJdbcOperations()).thenReturn(jdbcOperations);

        return factory;
    }
}