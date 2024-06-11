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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq req;

    private ConnectionConfig config;

    private List<Rule> rules;

    private JdbcOperations jdbc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);

        session = mock(ConnectionSession.class);
        req = new SqlCheckReq();
        config = new ConnectionConfig();

        rules = new ArrayList<>();
        rules.add(new Rule());

        jdbc = new JdbcTemplate(new SingleConnectionDataSource());

        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
        sqlCheckService.setJdbcOperations(jdbc);
    }

    @Test
    public void testCheck() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());

        sqlCheckService.check(session, req);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptyRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.isEmpty());

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertTrue(checkResults.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptySqlCheckRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertTrue(sqlCheckRules.isEmpty());

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertTrue(checkResults.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptyScriptContent() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        req.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);
        assertTrue(checkResults.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptyEnvironmentId() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(0L, "", Collections.singletonList(new OffsetString()), config);
        assertTrue(checkViolations.isEmpty());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptyDatabaseName() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        config.setDatabaseName("");

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "", Collections.singletonList(new OffsetString()), config);
        assertTrue(checkViolations.isEmpty());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithEmptySqls() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "db", Collections.emptyList(), config);
        assertTrue(checkViolations.isEmpty());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertFalse(sqlCheckRules.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertTrue(sqlCheckRules.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRulesWithEmptySqlCheckRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertTrue(sqlCheckRules.isEmpty());

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(new CheckViolation()));
        assertEquals(1, violatedRules.size());

        Rule rule = violatedRules.get(0);
        assertNull(rule.getId());
        assertEquals(RuleViolation.fromCheckViolation(new CheckViolation()), rule.getViolation());
    }

}