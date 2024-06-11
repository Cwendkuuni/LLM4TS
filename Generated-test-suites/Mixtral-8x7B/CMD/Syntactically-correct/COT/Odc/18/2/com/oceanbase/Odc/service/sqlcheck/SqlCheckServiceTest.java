import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
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
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
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
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRule;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRuleFactory;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private SqlCheckReq sqlCheckReq;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = mock(SqlCheckReq.class);

        doReturn(DialectType.MYSQL).when(connectionSession).getDialectType();
        doReturn(true).when(sqlCheckReq).getEnabled();
        doReturn("testRule").when(sqlCheckReq).getName();
        doReturn("TEST_RULE").when(sqlCheckReq).getType();
        doReturn(1L).when(sqlCheckReq).getLevel();
    }

    @Test
    public void testCheck_withNoRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_withEmptySql() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule(sqlCheckReq));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
    }

    @Test
    public void testCheck_withRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule(sqlCheckReq));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        SqlChecker sqlChecker = spy(new DefaultSqlChecker(DialectType.MYSQL, "\\;", Collections.emptyList()));
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(sqlChecker);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        when(sqlChecker.check(anyString())).thenReturn(violations);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
        verify(sqlChecker, times(1)).check(anyString());
    }

    @Test
    public void testCheck_withRulesAndViolations() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule(sqlCheckReq));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        SqlChecker sqlChecker = spy(new DefaultSqlChecker(DialectType.MYSQL, "\\;", Collections.emptyList()));
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(sqlChecker);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        when(sqlChecker.check(anyString())).thenReturn(violations);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, result.size());
        verify(sqlChecker, times(1)).check(anyString());
    }

    @Test
    public void testCheck_withEnvironmentId() {
        Long environmentId = 1L;
        String databaseName = "testDatabase";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table"));

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        Environment environment = new Environment();
        environment.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        rules.add(createRule(sqlCheckReq));

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testDatabase");
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(dataSource);

        SqlCheckContext checkContext = new SqlCheckContext(1L);

        List<SqlCheckRule> checkRules = new ArrayList<>();
        checkRules.add(createSqlCheckRule(rules.get(0)));

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);

        when(environmentService.detail(environmentId)).thenReturn(environment);
        when(ruleService.list(environment.getRulesetId(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(factory.resetSchema(any(Function.class))).thenReturn(null);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        when(sqlChecker.check(anyList(), eq(checkContext))).thenReturn(violations);

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertEquals(1, result.size());
        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1))
                .list(environment.getRulesetId(), any(QueryRuleMetadataParams.class));
        verify(factory, times(1)).getDataSource();
        verify(factory, times(1)).resetSchema(any(Function.class));
        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(RowMapper.class));
        verify(sqlChecker, times(1)).check(anyList(), eq(checkContext));
    }

    @Test
    public void testGetRules_withEmptyRules() {
        List<Rule> rules = new ArrayList<>();

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, result.size());
    }

    @Test
    public void testGetRules_withRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule(sqlCheckReq));

        List<SqlCheckRule> factories = new ArrayList<>();
        factories.add(createSqlCheckRuleFactory(rules.get(0)));

        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(factories);

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(1, result.size());
    }

    @Test
    public void testFullFillRiskLevel_withEmptyRules() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel_withViolations() {
        List<Rule> rules = new ArrayList<>();
        rules.add(createRule(sqlCheckReq));

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
    }

    private Rule createRule(SqlCheckReq sqlCheckReq) {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName(sqlCheckReq.getName());
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setEnabled(sqlCheckReq.getEnabled());
        return rule;
    }

    private SqlCheckRule createSqlCheckRule(Rule rule) {
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        when(sqlCheckRule.getName()).thenReturn(rule.getMetadata().getName());
        when(sqlCheckRule.getType()).thenReturn(rule.getMetadata().getType());
        when(sqlCheckRule.getLevel()).thenReturn(rule.getLevel());
        return sqlCheckRule;
    }

    private SqlCheckRuleFactory createSqlCheckRuleFactory(Rule rule) {
        SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
        when(sqlCheckRuleFactory.create(any(DialectType.class), any(JdbcOperations.class), any(Rule.class)))
                .thenReturn(createSqlCheckRule(rule));
        return sqlCheckRuleFactory;
    }

}