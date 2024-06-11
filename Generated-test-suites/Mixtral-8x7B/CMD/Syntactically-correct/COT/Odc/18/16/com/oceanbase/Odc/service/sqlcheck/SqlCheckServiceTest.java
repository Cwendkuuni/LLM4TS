import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;
    private JdbcOperations jdbcOperations;
    private JdbcTemplate jdbcTemplate;
    private SingleConnectionDataSource dataSource;

    @Before
    public void setUp() {
        connectionSession = mock(ConnectionSession.class);
        dataSource = new SingleConnectionDataSource();
        ReflectionTestUtils.setField(connectionSession, "dataSource", dataSource);

        jdbcOperations = mock(JdbcOperations.class);
        jdbcTemplate = new JdbcTemplate(dataSource);
        ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);
        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
        ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);
        ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
    }

    @After
    public void tearDown() {
        dataSource.destroy();
    }

    @Test
    public void testCheck_NoRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_NoScriptContent() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("");
        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_WithScriptContent() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(mock(Rule.class)));

        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM dual;");
        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(1, results.size());
    }

    @Test
    public void testCheck_WithOffsetStrings() {
        Long environmentId = 1L;
        when(environmentService.detail(environmentId)).thenReturn(mock(Environment.class));

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM dual;", 0, 0));
        
        List<CheckViolation> violations = sqlCheckService.check(environmentId, "test_db", sqls, config);

        assertEquals(1, violations.size());
    }

    @Test
    public void testGetRules_EmptyRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> rules = sqlCheckService.getRules(connectionSession);

        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRules_NonEmptyRules() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(mock(Rule.class)));

        List<SqlCheckRule> rules = sqlCheckService.getRules(connectionSession);

        assertEquals(1, rules.size());
    }

    @Test
    public void testGetRules_EmptyRulesWithJdbcOperations() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> rules = sqlCheckService.getRules(connectionSession, jdbcOperations);

        assertEquals(0, rules.size());
    }

    @Test
    public void testGetRules_NonEmptyRulesWithJdbcOperations() {
        Long ruleSetId = 1L;
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
                .thenReturn(Collections.singletonList(mock(Rule.class)));

        List<SqlCheckRule> rules = sqlCheckService.getRules(connectionSession, jdbcOperations);

        assertEquals(1, rules.size());
    }

    @Test
    public void testFullFillRiskLevel_EmptyViolations() {
        List<Rule> rules = Collections.singletonList(mock(Rule.class));
        List<CheckViolation> violations = new ArrayList<>();

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(0, result.size());
    }

    @Test
    public void testFullFillRiskLevel_NonEmptyViolations() {
        List<Rule> rules = Collections.singletonList(mock(Rule.class));
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        List<CheckViolation> violations = Collections.singletonList(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
        assertEquals(RuleViolation.fromCheckViolation(violation), result.get(0).getViolation());
    }

    @Test(expected = Exception.class)
    public void testCreateByRule_Exception() throws Exception {
        List<Rule> rules = Collections.singletonList(mock(Rule.class));
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        ReflectionTestUtils.setField(sqlCheckService, "candidates", candidates);

        sqlCheckService.createByRule(rules, DialectType.MYSQL, jdbcOperations);
    }
}