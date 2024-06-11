import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private SqlCheckService sqlCheckService;

    private ConnectionConfig connectionConfig;

    private ConnectionSession connectionSession;

    private Environment environment;

    private SqlCheckReq sqlCheckReq;

    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionSession = mock(ConnectionSession.class);
        environment = new Environment();
        sqlCheckReq = new SqlCheckReq();
        jdbcOperations = new JdbcTemplate();

        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;

        sqlCheckService.connectionSession = connectionSession;
        sqlCheckService.jdbcTemplate = jdbcOperations;

        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);

        when(environmentService.detail(anyLong())).thenReturn(environment);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());
        when(factory.resetSchema(any())).thenReturn(null);
        sqlCheckService.consoleDataSourceFactory = factory;
    }

    @Test
    public void testCheck_HappyPath() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setLevel("INFO");

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(checkViolation);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
        assertEquals(1, checkResults.size());
        assertEquals(checkViolation.getLevel(), checkResults.get(0).getLevel());
    }

    @Test
    public void testCheck_EmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheck_NoRuleSetId() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheck_EmptySqlCheckReq() {
        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheck_WithEnvironment() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(environmentService.detail(anyLong())).thenReturn(environment);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setLevel("INFO");

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(checkViolation);

        sqlCheckService.check(environment.getId(), environment.getName(), Collections.singletonList(new OffsetString()), connectionConfig);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(environmentService, times(1)).detail(anyLong());
        verify(jdbcOperations, times(1)).batchUpdate(anyList());
    }

    @Test
    public void testGetRules_HappyPath() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testGetRules_EmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel_HappyPath() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setLevel("HIGH");
        rule.setEnabled(true);
        RuleViolation violation = new RuleViolation();
        violation.setType(RuleType.SQL_CHECK);
        rule.setViolation(violation);
        rules.add(rule);

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(RuleType.SQL_CHECK);
        List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertEquals(1, violatedRules.size());
        assertEquals("HIGH", violatedRules.get(0).getLevel());
    }

    @Test
    public void testFullFillRiskLevel_EmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), Collections.emptyList());
        assertEquals(0, violatedRules.size());
    }

    @Test
    public void testFullFillRiskLevel_EmptyCheckViolations() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.setLevel("HIGH");
        rule.setEnabled(true);
        RuleViolation violation = new RuleViolation();
        violation.setType(RuleType.SQL_CHECK);
        rule.setViolation(violation);
        rules.add(rule);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());
        assertEquals(0, violatedRules.size());
    }
}