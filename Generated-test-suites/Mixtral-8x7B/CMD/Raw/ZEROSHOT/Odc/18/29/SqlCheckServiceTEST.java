import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.*;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    private SingleConnectionDataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        connectionSession = Mockito.mock(ConnectionSession.class);
        connectionConfig = Mockito.mock(ConnectionConfig.class);
        dataSource = Mockito.mock(SingleConnectionDataSource.class);
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcTemplate);

        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        when(dataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);

        ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules",
                SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate));
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder().name("rule1").type(RuleType.SQL_CHECK).build());
        rule.setEnabled(true);
        rules.add(rule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("select * from table1;");

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);

        assertEquals(1, checkResults.size());
        CheckResult checkResult = checkResults.get(0);
        assertEquals("rule1", checkResult.getRuleName());
        assertEquals(1, checkResult.getViolations().size());
        assertEquals(1, checkResult.getViolations().get(0).getStart());
        assertEquals(18, checkResult.getViolations().get(0).getEnd());
    }

    @Test
    public void testCheckWithOffsetStrings() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder().name("rule1").type(RuleType.SQL_CHECK).build());
        rule.setEnabled(true);
        rules.add(rule);

        when(environmentService.detail(anyLong()))
                .thenReturn(Environment.builder().id(1L).databaseName("testdb").build());

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(OffsetString.builder().offset(0).length(18).sql("select * from table1;").build());

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        assertEquals(1, checkViolations.size());
        CheckViolation checkViolation = checkViolations.get(0);
        assertEquals("rule1", checkViolation.getType().getName());
        assertEquals(0, checkViolation.getStart());
        assertEquals(18, checkViolation.getEnd());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder().name("rule1").type(RuleType.SQL_CHECK).build());
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(1, sqlCheckRules.size());
        SqlCheckRule sqlCheckRule = sqlCheckRules.get(0);
        assertEquals("rule1", sqlCheckRule.getName());
    }

    @Test
    public void testGetRulesWithJdbcOperations() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder().name("rule1").type(RuleType.SQL_CHECK).build());
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRuleFactory> sqlCheckRuleFactories =
                SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcTemplate);

        List<SqlCheckRule> sqlCheckRules =
                sqlCheckService.getRules(rules, DialectType.MYSQL, jdbcTemplate);

        assertEquals(1, sqlCheckRules.size());
        SqlCheckRule sqlCheckRule = sqlCheckRules.get(0);
        assertEquals("rule1", sqlCheckRule.getName());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder().name("rule1").type(RuleType.SQL_CHECK).level("HIGH").build());
        rule.setEnabled(true);
        rules.add(rule);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(CheckViolation.builder().type(rule.getMetadata()).start(0).end(18).build());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(1, violatedRules.size());
        Rule violatedRule = violatedRules.get(0);
        assertEquals("HIGH", violatedRule.getLevel());
    }
}