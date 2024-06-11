import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
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
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
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

@SuppressWarnings("unused")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private SqlCheckReq sqlCheckReq;

    private Environment environment;

    private ConnectionConfig connectionConfig;

    private List<Rule> rules;

    private List<SqlCheckRule> sqlCheckRules;

    private List<OffsetString> sqls;

    private SqlCheckContext checkContext;

    private JdbcOperations jdbcOperations;

    private JdbcTemplate jdbcTemplate;

    private SingleConnectionDataSource dataSource;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = new SqlCheckReq();
        environment = new Environment();
        connectionConfig = new ConnectionConfig();
        rules = new ArrayList<>();
        sqlCheckRules = new ArrayList<>();
        sqls = new ArrayList<>();
        checkContext = new SqlCheckContext(0L);
        jdbcOperations = mock(JdbcOperations.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Test method check with valid parameters
    @Test
    public void testCheckWithValidParameters() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkResults.size());
    }

    // Test method check with empty rules
    @Test
    public void testCheckWithEmptyRules() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkResults.size());
    }

    // Test method check with empty sqlCheckRules
    @Test
    public void testCheckWithEmptySqlCheckRules() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkResults.size());
    }

    // Test method check with empty sqls
    @Test
    public void testCheckWithEmptySqls() {
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckViolation> checkViolations = sqlCheckService.check(sqlCheckReq.getScriptContent(), sqlCheckRules);

        assertEquals(0, checkViolations.size());
    }

    // Test method check for exception handling
    @Test(expected = UnsupportedOperationException.class)
    public void testCheckForExceptionHandling() {
        when(connectionSession.getDialectType()).thenReturn(null);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        sqlCheckService.check(connectionSession, sqlCheckReq);
    }

    // Test method getRules with valid parameters
    @Test
    public void testGetRulesWithValidParameters() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());
    }

    // Test method getRules with empty rules
    @Test
    public void testGetRulesWithEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());
    }

    // Test method getRules for exception handling
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRulesForExceptionHandling() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(connectionSession.getDialectType()).thenReturn(null);

        sqlCheckService.getRules(rules, connectionSession);
    }

    // Test method fullFillRiskLevel with valid parameters
    @Test
    public void testFullFillRiskLevelWithValidParameters() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rules.add(rule);
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
    }

    // Test method fullFillRiskLevel with empty rules
    @Test
    public void testFullFillRiskLevelWithEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(0, result.size());
    }

    // Test method fullFillRiskLevel for exception handling
    @Test(expected = UnsupportedOperationException.class)
    public void testFullFillRiskLevelForExceptionHandling() {
        List<Rule> rules = null;
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        sqlCheckService.fullFillRiskLevel(rules, violations);
    }

    // Test method check with valid parameters
    @Test
    public void testCheckForConnectionConfigWithValidParameters() {
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(new OBConsoleDataSourceFactory(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(jdbcTemplate.getDataSource().getConnection());
        when(new JdbcTemplate(dataSource).queryForObject("SELECT 1", Integer.class)).thenReturn(1);

        List<CheckViolation> checkViolations = sqlCheckService.check(anyLong(), anyString(), anyList(),
                any(ConnectionConfig.class));

        assertEquals(0, checkViolations.size());
    }

    // Test method check with empty sqls
    @Test
    public void testCheckForConnectionConfigWithEmptySqls() {
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(new OBConsoleDataSourceFactory(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(jdbcTemplate.getDataSource().getConnection());
        when(new JdbcTemplate(dataSource).queryForObject("SELECT 1", Integer.class)).thenReturn(1);

        List<CheckViolation> checkViolations = sqlCheckService.check(anyLong(), anyString(), Collections.emptyList(),
                any(ConnectionConfig.class));

        assertEquals(0, checkViolations.size());
    }

    // Test method check for exception handling
    @Test(expected = UnsupportedOperationException.class)
    public void testCheckForConnectionConfigForExceptionHandling() {
        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(new OBConsoleDataSourceFactory(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(null);

        sqlCheckService.check(anyLong(), anyString(), anyList(), any(ConnectionConfig.class));
    }

    // Test method getRules with valid parameters
    @Test
    public void testGetRulesForConnectionConfigWithValidParameters() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(new OBConsoleDataSourceFactory(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(jdbcTemplate.getDataSource().getConnection());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionConfig.getDialectType(),
                jdbcOperations);

        assertEquals(0, sqlCheckRules.size());
    }

    // Test method getRules with empty rules
    @Test
    public void testGetRulesForConnectionConfigWithEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
        when(new OBConsoleDataSourceFactory(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(dataSource);
        when(dataSource.getConnection()).thenReturn(jdbcTemplate.getDataSource().getConnection());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionConfig.getDialectType(),
                jdbcOperations);

        assertEquals(0, sqlCheckRules.size());
    }

    // Test method getRules for exception handling
    @Test(expected = UnsupportedOperationException.class)
    public void testGetRulesForConnectionConfigForExceptionHandling() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(new OBConsoleDataSourceFactory(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(null);

        sqlCheckService.getRules(rules, connectionConfig.getDialectType(), jdbcOperations);
    }
}