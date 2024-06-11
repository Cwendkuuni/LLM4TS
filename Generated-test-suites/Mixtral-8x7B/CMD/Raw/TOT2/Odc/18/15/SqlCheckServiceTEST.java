import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private ConnectionConfig config;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;

        session = mock(ConnectionSession.class);
        config = mock(ConnectionConfig.class);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

        List<CheckResult> results = sqlCheckService.check(session, req);

        assertEquals(0, results.size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(List.of(rule));

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("test", "test", 1, "test", "test"));

        when(ruleService.fullFillRiskLevel(anyList(), anyList())).thenReturn(violations);

        List<CheckResult> results = sqlCheckService.check(session, req);

        assertEquals(1, results.size());
        assertNotNull(results.get(0).getViolations());
        assertEquals(1, results.get(0).getViolations().size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService, times(1)).fullFillRiskLevel(anyList(), anyList());
    }

    @Test
    public void testCheckWithEnvironment() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 33));

        when(environmentService.detail(anyLong())).thenReturn(new Environment());

        when(config.getDialectType()).thenReturn(DialectType.MYSQL);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());

        when(factory.resetSchema(any(Function.class))).thenReturn(factory);

        when(config.getDataSourceFactory()).thenReturn(factory);

        SqlCheckContext checkContext = new SqlCheckContext(1L);

        when(config.getSyncJdbcExecutor(anyString())).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> checkRules = new ArrayList<>();
        checkRules.add(mock(SqlCheckRule.class));

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);

        when(sqlChecker.check(anyList(), any(SqlCheckContext.class))).thenReturn(new ArrayList<>());

        List<CheckViolation> violations = sqlCheckService.check(1L, "database", sqls, config);

        assertEquals(0, violations.size());

        verify(environmentService, times(1)).detail(anyLong());
        verify(config, times(1)).getDialectType();
        verify(factory, times(1)).getDataSource();
        verify(factory, times(1)).resetSchema(any(Function.class));
        verify(config, times(1)).getDataSourceFactory();
        verify(config, times(1)).getSyncJdbcExecutor(anyString());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(1, sqlCheckRules.size());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation("test", "test", 1, "test", "test"));

        when(ruleService.fullFillRiskLevel(anyList(), anyList())).thenReturn(violations);

        List<Rule> fullFilledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, fullFilledRules.size());
        assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), fullFilledRules.get(0).getViolation());

        verify(ruleService, times(1)).fullFillRiskLevel(anyList(), anyList());
    }
}