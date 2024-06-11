import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

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

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession session;
    private JdbcOperations jdbcOperations;

    @Before
    public void setUp() {
        session = mock(ConnectionSession.class);
        jdbcOperations = mock(JdbcTemplate.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck_withEmptyRuleSetId_returnsEmptyList() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("test script"));
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_withNoRules_returnsEmptyList() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        when(ruleService.list(anyLong(), any())).thenReturn(Collections.emptyList());
        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("test script"));
        assertEquals(Collections.emptyList(), checkResults);
    }

    @Test
    public void testCheck_withRuleViolations_returnsCheckResults() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
        List<Rule> rules = createRules(1, "test_rule", "SQL_CHECK", true);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);
        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.TEST_RULE);
        violation.setLevel("ERROR");
        checkViolations.add(violation);
        when(ruleService.fullFillRiskLevel(anyList(), anyList())).thenReturn(rules);
        when(jdbcOperations.batchUpdate(any(), anyInt())).thenReturn(new int[0]);
        when(jdbcOperations.queryForInt(anyString(), any())).thenReturn(1);

        List<CheckResult> checkResults = sqlCheckService.check(session, new SqlCheckReq("test script"));
        assertEquals(1, checkResults.size());
        assertEquals("ERROR", checkResults.get(0).getLevel());
    }

    @Test
    public void testCheck_withSqlsAndConfig_returnsCheckViolations() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql = new OffsetString();
        sql.setContent("SELECT * FROM users;");
        sqls.add(sql);

        Environment environment = mock(Environment.class);
        when(environment.getRulesetId()).thenReturn(1L);
        when(environmentService.detail(anyLong())).thenReturn(environment);

        List<Rule> rules = createRules(1, "test_rule", "SQL_CHECK", true);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_db", sqls, config);
        assertEquals(1, checkViolations.size());
    }

    @Test
    public void testGetRules_withNoCandidates_returnsEmptyList() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testGetRules_withCandidates_returnsSqlCheckRules() {
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        List<Rule> rules = createRules(1, "test_rule", "SQL_CHECK", true);
        when(ruleService.list(anyLong(), any())).thenReturn(rules);

        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        candidates.add(mock(SqlCheckRules.SqlCheckRuleFactory.class));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);
        assertEquals(1, sqlCheckRules.size());
    }

    private List<Rule> createRules(long id, String name, String type, boolean enabled) {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setId(id);
        rule.setMetadata(new RuleMetadata(name, type, enabled));
        rules.add(rule);
        return rules;
    }

}