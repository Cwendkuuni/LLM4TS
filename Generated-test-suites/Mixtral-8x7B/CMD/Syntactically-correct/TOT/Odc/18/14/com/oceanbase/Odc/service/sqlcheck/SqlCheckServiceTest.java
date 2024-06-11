import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.service.SqlCheckService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Autowired
    private SqlCheckService sqlCheckService;

    private ConnectionConfig connectionConfig;

    private Environment environment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
        ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        environment = new Environment();
        environment.setRulesetId(1L);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(any())).thenReturn(mock(JdbcOperations.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(1L, any())).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        Assertions.assertEquals(0, checkViolations.size());

        verify(ruleService, times(1)).list(1L, any());
    }

    @Test
    public void testCheckWithViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(any())).thenReturn(mock(JdbcOperations.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rule.setType(RuleType.SQL_CHECK);
        rules.add(rule);

        when(ruleService.list(1L, any())).thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("DROP TABLE users;");

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        when(sqlCheckRule.check(any())).thenReturn(Collections.singletonList(mock(CheckViolation.class)));
        sqlCheckRules.add(sqlCheckRule);
        ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        Assertions.assertEquals(1, checkViolations.size());
        verify(ruleService, times(1)).list(1L, any());
        verify(sqlCheckRule, times(1)).check(any());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(any())).thenReturn(mock(JdbcOperations.class));
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

        when(ruleService.list(1L, any())).thenReturn(Collections.emptyList());

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<CheckViolation> checkViolations = sqlCheckService.check(session, req);
        Assertions.assertEquals(0, checkViolations.size());

        verify(ruleService, times(1)).list(1L, any());
    }

    @Test
    public void testCheckForMultipleSqls() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForList(anyString(), any(Object[].class))).thenReturn(Collections.emptyList());

        connectionConfig.setJdbcOperations(jdbcOperations);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 26));
        sqls.add(new OffsetString("DROP TABLE users;", 27, 42));

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rule.setType(RuleType.SQL_CHECK);
        rules.add(rule);

        when(environmentService.detail(1L)).thenReturn(environment);
        when(ruleService.list(1L, any())).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_database", sqls, connectionConfig);
        Assertions.assertEquals(1, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);
        when(jdbcOperations.queryForList(anyString(), any(Object[].class))).thenReturn(Collections.emptyList());

        connectionConfig.setJdbcOperations(jdbcOperations);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rule.setType(RuleType.SQL_CHECK);
        rules.add(rule);

        List<SqlCheckRuleFactory> candidateFactories = new ArrayList<>();
        SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
        when(factory.createSqlCheckRule(any(), any(), any())).thenReturn(mock(SqlCheckRule.class));
        candidateFactories.add(factory);

        SqlCheckService sqlCheckServiceSpy = spy(sqlCheckService);
        doReturn(candidateFactories).when(sqlCheckServiceSpy).getFactories(any());

        List<SqlCheckRule> sqlCheckRules = sqlCheckServiceSpy.getRules(rules, connectionConfig);
        Assertions.assertEquals(1, sqlCheckRules.size());
    }

}