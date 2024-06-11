package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Service
class SqlCheckServiceTest {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private EnvironmentService environmentService;

    @Test
    public void testCheck() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        when(sqlCheckService.check(session, req)).thenCallRealMethod();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getScriptContent()).thenReturn("SELECT * FROM users WHERE id = 1;");
        when(session.getRuleSetId()).thenReturn(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRules sqlCheckRule = mock(SqlCheckRules.class);
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setLevel("ERROR");
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(checkViolation);

        when(sqlCheckRule.check("SELECT * FROM users WHERE id = 1;")).thenReturn(checkViolations);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(1, result.size());
        assertEquals("ERROR", result.get(0).getLevel());
    }

    @Test
    public void testCheckWithEmptyRules() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        when(sqlCheckService.check(session, req)).thenCallRealMethod();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getScriptContent()).thenReturn("SELECT * FROM users WHERE id = 1;");
        when(session.getRuleSetId()).thenReturn(1L);

        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptySqlCheckRules() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        when(sqlCheckService.check(session, req)).thenCallRealMethod();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getScriptContent()).thenReturn("SELECT * FROM users WHERE id = 1;");
        when(session.getRuleSetId()).thenReturn(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        when(sqlCheckService.getRules(rules, session)).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEmptyViolations() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        when(sqlCheckService.check(session, req)).thenCallRealMethod();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(req.getScriptContent()).thenReturn("SELECT * FROM users WHERE id = 1;");
        when(session.getRuleSetId()).thenReturn(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        SqlCheckRules sqlCheckRule = mock(SqlCheckRules.class);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);

        when(sqlCheckRule.check("SELECT * FROM users WHERE id = 1;")).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckForConsole() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE id = 1;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(sqlCheckService.check(environmentId, databaseName, sqls, config)).thenCallRealMethod();

        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        JdbcOperations jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Class.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        List<SqlCheckRule> checkRules = new ArrayList<>();
        SqlCheckRules sqlCheckRule = mock(SqlCheckRules.class);
        checkRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, config.getDialectType(), jdbc)).thenReturn(checkRules);

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setLevel("ERROR");
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(checkViolation);

        when(sqlCheckRule.check(Collections.singletonList(sqls.get(0)), Mockito.any(SqlCheckContext.class)))
                .thenReturn(checkViolations);

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(1, result.size());
        assertEquals("ERROR", result.get(0).getLevel());
    }

    @Test
    public void testCheckForConsoleWithEmptyRules() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE id = 1;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(sqlCheckService.check(environmentId, databaseName, sqls, config)).thenCallRealMethod();

        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);

        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.emptyList());

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckForConsoleWithEmptySqlCheckRules() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE id = 1;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(sqlCheckService.check(environmentId, databaseName, sqls, config)).thenCallRealMethod();

        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(1L, Mockito.any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        when(sqlCheckService.getRules(rules, config.getDialectType(), Mockito.any(JdbcOperations.class)))
                .thenReturn(Collections.emptyList());

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheckForConsoleWithEmptyViolations() {
        SqlCheckService sqlCheckService = mock(SqlCheckService.class);
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users WHERE id = 1;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(sqlCheckService.check(environmentId, databaseName, sqls, config)).thenCallRealMethod();

        Environment env = new Environment();
        env.setRulesetId(1L);
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("TestRule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        rules.add(rule);

        JdbcOperations jdbc = mock(JdbcTemplate.class);
        when(jdbc.queryForObject(Mockito.anyString(), Mockito.any(Object[].class), Mockito.any(Class.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        List<SqlCheckRule> checkRules = new ArrayList<>();
        SqlCheckRules sqlCheckRule = mock(SqlCheckRules.class);
        checkRules.add(sqlCheckRule);
        when(sqlCheckService.getRules(rules, config.getDialectType(), jdbc)).thenReturn(checkRules);

        when(sqlCheckRule.check(Collections.singletonList(sqls.get(0)), Mockito.any(SqlCheckContext.class)))
                .thenReturn(Collections.emptyList());

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(0, result.size());
    }

}