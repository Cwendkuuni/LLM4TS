package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.ModelRule;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    private static final DialectType DIALECT_TYPE = DialectType.MYSQL;
    private static final int ENVIRONMENT_ID = 1;
    private static final String DATABASE_NAME = "test_database";
    private static final String SQL_SCRIPT = "SELECT * FROM users;";
    private static final String SQL_SCRIPT_WITH_COMMENT = "-- This is a test script\nSELECT * FROM users;";
    private static final String SQL_DELIMITER = ";";

    private SqlCheckService sqlCheckService;
    private RuleService ruleService;
    private EnvironmentService environmentService;
    private ConnectionSession connectionSession;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        ruleService = mock(RuleService.class);
        environmentService = mock(EnvironmentService.class);
        connectionSession = mock(ConnectionSession.class);
        jdbcOperations = mock(JdbcOperations.class);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;

        when(connectionSession.getDialectType()).thenReturn(DIALECT_TYPE);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcOperations);

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        when(jdbcOperations.getJdbcTemplate()).thenReturn(jdbcTemplate);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        when(jdbcOperations.getDataSource()).thenReturn(dataSource);

        when(environmentService.detail(ENVIRONMENT_ID)).thenReturn(new Environment());

        List<Rule> rules = new ArrayList<>();
        ModelRule rule = new ModelRule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", 1, null));
        rules.add(rule);
        when(ruleService.list(1L, mock(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckRules.registerFactory(new TestSqlCheckRuleFactory());
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(SQL_SCRIPT);
        req.setDelimiter(SQL_DELIMITER);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertNotNull(checkResults);
        assertTrue(checkResults.isEmpty());

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("test_rule", "Test Rule"));
        checkViolations.add(violation);

        SqlCheckRules.getTestFactory().setCheckViolations(checkViolations);

        checkResults = sqlCheckService.check(connectionSession, req);
        assertFalse(checkResults.isEmpty());
        assertEquals(1, checkResults.size());
        assertEquals(violation.getType().getName(), checkResults.get(0).getType());
    }

    @Test
    public void testCheckWithEnvironment() {
        OffsetString sql = new OffsetString(SQL_SCRIPT_WITH_COMMENT, 0, SQL_SCRIPT_WITH_COMMENT.length());
        List<OffsetString> sqls = Collections.singletonList(sql);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DIALECT_TYPE);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("test_rule", "Test Rule"));
        checkViolations.add(violation);

        SqlCheckRules.getTestFactory().setCheckViolations(checkViolations);

        List<CheckViolation> result = sqlCheckService.check(ENVIRONMENT_ID, DATABASE_NAME, sqls, config);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(violation.getType().getName(), result.get(0).getType());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
        assertTrue(rules.isEmpty());

        List<Rule> ruleList = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", 1, null));
        ruleList.add(rule);

        when(ruleService.list(1L, mock(QueryRuleMetadataParams.class))).thenReturn(ruleList);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.singletonList(rule), connectionSession);
        assertEquals(1, sqlCheckRules.size());
        assertEquals(ruleList.get(0).getMetadata().getName(), sqlCheckRules.get(0).getName());

        sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        ModelRule rule = new ModelRule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", 1, null));
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("test_rule", "Test Rule"));
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, result.size());
        assertEquals(rule.getMetadata().getLevel(), result.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevelWithNullRule() {
        List<Rule> rules = new ArrayList<>();
        ModelRule rule = new ModelRule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", 1, null));
        rules.add(rule);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("nonexistent_rule", "Nonexistent Rule"));
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevelWithNullViolations() {
        List<Rule> rules = new ArrayList<>();
        ModelRule rule = new ModelRule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "test_rule", 1, null));
        rules.add(rule);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, null);
        assertTrue(result.isEmpty());
    }

    public static class TestSqlCheckRuleFactory implements SqlCheckRuleFactory {

        private List<CheckViolation> checkViolations;

        public void setCheckViolations(List<CheckViolation> checkViolations) {
            this.checkViolations = checkViolations;
        }

        @Override
        public SqlCheckRule create(@NonNull Rule rule, @NonNull DialectType dialectType,
                @NonNull JdbcOperations jdbc) {
            return SqlCheckRules.getTestFactory().create(rule, dialectType, jdbc);
        }

        @Override
        public List<CheckViolation> check(@NonNull List<OffsetString> sqls,
                @NonNull SqlCheckContext checkContext) {
            return checkViolations;
        }
    }
}