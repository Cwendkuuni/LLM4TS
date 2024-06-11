import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionConfig connectionConfig;
    private ConnectionSession connectionSession;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionSession = mock(ConnectionSession.class);
        jdbcTemplate = mock(JdbcTemplate.class);

        ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(jdbcTemplate);
    }

    @Test
    public void testCheck() {
        Long environmentId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 20));
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sqls);

        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(rule()));

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(1, checkViolations.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(environmentId);
    }

    @Test
    public void testCheck_noRules() {
        Long environmentId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 20));
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sqls);

        when(environmentService.detail(environmentId)).thenReturn(new Environment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(0, checkViolations.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(environmentId);
    }

    @Test
    public void testCheck_connectionSession() {
        Long ruleSetId = 1L;
        List<Rule> rules = Collections.singletonList(rule());
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());

        SqlCheckReq req = new SqlCheckReq();
        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, req);

        verify(connectionSession, times(1)).getRuleSetId();
        verify(connectionSession, times(1)).getDialectType();
        verify(connectionSession, times(1)).getSyncJdbcExecutor(anyString());
        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Collections.singletonList(rule());

        DialectType dialectType = DialectType.MYSQL;
        List<SqlCheckRuleFactory> candidates = sqlCheckService.getRules(rules, dialectType, jdbcTemplate);
        assertEquals(1, candidates.size());

        SqlCheckRuleFactory factory = candidates.get(0);
        SqlCheckRule sqlCheckRule = factory.create(rule());
        assertEquals("testRule", sqlCheckRule.getName());
        assertEquals(dialectType, sqlCheckRule.getDialectType());

        verify(jdbcTemplate, times(1)).query(anyString(), any(Object[].class), any(SqlCheckRuleFactory.RowMapper.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(rule());
        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, violatedRules.size());

        Rule violatedRule = violatedRules.get(0);
        assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), violatedRule.getViolation());
    }

    private Rule rule() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("testRule");
        rule.getMetadata().setType(RuleType.SQL_CHECK);
        rule.setLevel(1);
        rule.setEnabled(true);
        return rule;
    }
}