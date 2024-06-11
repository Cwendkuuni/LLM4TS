import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
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

    private SqlCheckReq sqlCheckReq;

    private List<Rule> rules;

    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = new SqlCheckReq();
        rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("SQL_CHECK", "testRule", "Test Rule", RuleType.SQL_CHECK));
        rule.setEnabled(true);
        rules.add(rule);

        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);

        jdbcOperations = mock(JdbcOperations.class);
        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
    }

    @Test
    public void testCheck() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertFalse(CollectionUtils.isEmpty(checkResults));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(connectionSession);
    }

    @Test
    public void testCheckEmptySql() {
        sqlCheckReq.setScriptContent("");
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertTrue(CollectionUtils.isEmpty(checkResults));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ConnectionSessionUtil, times(1)).getRuleSetId(connectionSession);
    }

    @Test
    public void testCheckEnvironment() {
        ConnectionConfig connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        Environment environment = new Environment();
        environment.setRulesetId(1L);

        when(environmentService.detail(anyLong())).thenReturn(environment);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        sqlCheckService.check(1L, "testDatabase", asList(new OffsetString("SELECT * FROM users;")), connectionConfig, dataSource);

        verify(environmentService, times(1)).detail(anyLong());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertFalse(CollectionUtils.isEmpty(sqlCheckRules));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRulesEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertTrue(CollectionUtils.isEmpty(sqlCheckRules));

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRulesEmptySession() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(new ArrayList<>(), null);

        assertTrue(CollectionUtils.isEmpty(sqlCheckRules));

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(new RuleType("testType", "Test Type", "Test Description"));
        checkViolations.add(violation);

        Map<String, Rule> name2RuleMap = new HashMap<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("SQL_CHECK", "testRule", "Test Rule", RuleType.SQL_CHECK));
        rule.setLevel("HIGH");
        name2RuleMap.put("${testType}", rule);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(1, violatedRules.size());
        assertEquals("HIGH", violatedRules.get(0).getLevel());
        assertEquals("Test Rule", violatedRules.get(0).getRuleName());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevelEmptyViolations() {
        List<CheckViolation> checkViolations = new ArrayList<>();

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertTrue(CollectionUtils.isEmpty(violatedRules));

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevelEmptyRules() {
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(new ArrayList<>(), checkViolations);

        assertTrue(CollectionUtils.isEmpty(violatedRules));

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    private List<OffsetString> asList(String... sqls) {
        List<OffsetString> offsetStrings = new ArrayList<>();
        for (String sql : sqls) {
            OffsetString offsetString = new OffsetString();
            offsetString.setSql(sql);
            offsetStrings.add(offsetString);
        }
        return offsetStrings;
    }
}