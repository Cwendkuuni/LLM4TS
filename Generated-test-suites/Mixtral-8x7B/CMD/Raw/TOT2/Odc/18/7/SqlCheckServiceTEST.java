import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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

    @Before
    public void setup() {
        connectionSession = new ConnectionSession();
        ReflectionTestUtils.setField(connectionSession, "id", 1L);
        ReflectionTestUtils.setField(connectionSession, "dialectType", DialectType.MYSQL);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        ReflectionTestUtils.setField(connectionSession, "syncJdbcExecutorMap", Collections.singletonMap("console", new JdbcTemplate(dataSource)));
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(connectionSession, req);
        assertEquals(0, result.size());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.checkViolations(connectionSession, req);
        assertEquals(0, violations.size());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckWithViolations() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.fullFillRiskLevel(any(), any())).thenReturn(Collections.singletonList(new Rule()));

        List<CheckResult> result = sqlCheckService.check(connectionSession, req);
        assertEquals(1, result.size());

        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService).fullFillRiskLevel(any(), any());
    }

    @Test
    public void testCheckOffsetStrings() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 25));

        Environment env = new Environment();
        env.setRulesetId(2L);

        List<Rule> rules = new ArrayList<>();
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(0, violations.size());

        verify(environmentService).detail(environmentId);
        verify(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<SqlCheckRuleFactory> factories = Collections.singletonList(new SqlCheckRuleFactory() {});

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(0, sqlCheckRules.size());

        sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, new JdbcTemplate(new SingleConnectionDataSource()));
        assertEquals(0, sqlCheckRules.size());

        ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", factories);

        sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());

        sqlCheckRules = sqlCheckService.getRules(rules, DialectType.MYSQL, new JdbcTemplate(new SingleConnectionDataSource()));
        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(0, result.size());

        ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);

        result = sqlCheckService.fullFillRiskLevel(rules, violations);
        verify(ruleService).fullFillRiskLevel(any(), any());
    }

    @Test
    public void testFullFillRiskLevelWithViolations() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(violation);

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("${SQL_CHECK}", new Rule());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, result.size());
        assertEquals(RuleViolation.fromCheckViolation(violation), result.get(0).getViolation());
    }
}