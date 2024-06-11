import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import org.mockito.ArgumentMatchers;
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

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq sqlCheckReq;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        sqlCheckReq = mock(SqlCheckReq.class);
    }

    @Test
    public void testCheck_emptyRuleSetId() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_noRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_noSqlCheckRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        List<Rule> rules = Collections.singletonList(getRule());
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testCheck_withSqlCheckRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
        List<Rule> rules = Collections.singletonList(getRule());
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
    }

    @Test
    public void testCheck_emptyScriptContent() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_checkViolationsEmpty() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<Rule> rules = Collections.singletonList(getRule());
        when(ruleService.list(1L, any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\\;", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check("");
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testCheck_checkViolationsNotEmpty() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<Rule> rules = Collections.singletonList(getRule());
        when(ruleService.list(1L, any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\\;", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check("SELECT * FROM table;");
        assertFalse(checkViolations.isEmpty());
    }

    @Test
    public void testCheck_environmentIdNull() {
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        List<CheckViolation> checkViolations = sqlCheckService.check(null, "", sqls, config);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testCheck_databaseNameEmpty() {
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "", sqls, config);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testCheck_sqlsEmpty() {
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = mock(ConnectionConfig.class);
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "database", sqls, config);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testCheck_checkRulesEmpty() {
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM table;", 0));
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getDialectType()).thenReturn(DialectType.MYSQL);
        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "database", sqls, config);
        assertTrue(checkViolations.isEmpty());
    }

    @Test
    public void testGetRules_rulesEmpty() {
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRules_checkRulesNotEmpty() {
        List<Rule> rules = Collections.singletonList(getRule());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_rulesEmpty() {
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations = new ArrayList<>();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertTrue(violatedRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_violationsEmpty() {
        List<Rule> rules = Collections.singletonList(getRule());
        List<CheckViolation> violations = Collections.emptyList();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertTrue(violatedRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_violationsNotEmpty() {
        List<Rule> rules = Collections.singletonList(getRule());
        List<CheckViolation> violations = Collections.singletonList(getCheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertFalse(violatedRules.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_noMatchingRule() {
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations = Collections.singletonList(getCheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertTrue(violatedRules.isEmpty());
    }

    private Rule getRule() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        return rule;
    }

    private CheckViolation getCheckViolation() {
        CheckViolation violation = new CheckViolation();
        violation.setType(mock(Rule.class));
        return violation;
    }

    private JdbcOperations getJdbcOperations() {
        return new JdbcTemplate(new SingleConnectionDataSource());
    }

    private DialectType getDialectType() {
        return DialectType.MYSQL;
    }

    private ConnectionConfig getConnectionConfig() {
        ConnectionConfig config = mock(ConnectionConfig.class);
        when(config.getDialectType()).thenReturn(getDialectType());
        return config;
    }

    private OffsetString getOffsetString() {
        return new OffsetString("SELECT * FROM table;", 0);
    }

    private List<OffsetString> getOffsetStrings() {
        return Collections.singletonList(getOffsetString());
    }

    private SqlCheckContext getSqlCheckContext() {
        return new SqlCheckContext(1L);
    }

    private Environment getEnvironment() {
        Environment environment = new Environment();
        environment.setRulesetId(1L);
        return environment;
    }
}