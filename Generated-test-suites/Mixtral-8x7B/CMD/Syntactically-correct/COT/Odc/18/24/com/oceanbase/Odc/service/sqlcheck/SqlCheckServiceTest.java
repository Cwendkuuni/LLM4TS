import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.connection.model.ConnectionConfig.Dialect;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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

    private ConnectionConfig connectionConfig;
    private ConnectionSession connectionSession;
    private Environment environment;

    @Before
    public void setup() {
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialect(Dialect.MYSQL);
        connectionConfig.setHost("test-host");
        connectionConfig.setPort(3306);
        connectionConfig.setDatabase("test-db");
        connectionConfig.setUsername("test-user");
        connectionConfig.setPassword("test-pw");

        connectionSession = Mockito.mock(ConnectionSession.class);
        Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        environment = Mockito.mock(Environment.class);

        ReflectionTestUtils.setField(sqlCheckService, "syncJdbcExecutor",
                (sql, args) -> new JdbcTemplate(new SingleConnectionDataSource(connectionConfig)));
    }

    @Test
    public void testCheck_EmptySession() {
        List<CheckResult> results = sqlCheckService.check(null, new SqlCheckReq());

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_EmptyRules() {
        List<Rule> rules = Collections.emptyList();
        when(ruleService.list(anyLong(), ArgumentMatchers.<QueryRuleMetadataParams>any()))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq());

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_EmptyCheckRules() {
        List<Rule> rules = Collections.singletonList(createRule());
        when(ruleService.list(anyLong(), ArgumentMatchers.<QueryRuleMetadataParams>any()))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testCheck() {
        List<Rule> rules = Collections.singletonList(createRule());
        when(ruleService.list(anyLong(), ArgumentMatchers.<QueryRuleMetadataParams>any()))
                .thenReturn(rules);

        String sqlContent = "SELECT * FROM users;";
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent(sqlContent);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertEquals(1, results.size());
    }

    @Test
    public void testCheckForEnvironments_EmptySqls() {
        when(environmentService.detail(anyLong()))
                .thenReturn(environment);

        List<CheckViolation> violations = sqlCheckService.check(1L, "test-db",
                Collections.emptyList(), connectionConfig);

        assertEquals(0, violations.size());
    }

    @Test
    public void testCheckForEnvironments() {
        when(environmentService.detail(anyLong()))
                .thenReturn(environment);

        String sqlContent = "SELECT * FROM users;";
        OffsetString offsetString = new OffsetString(sqlContent, 0);
        List<OffsetString> sqls = Collections.singletonList(offsetString);

        List<CheckViolation> violations = sqlCheckService.check(1L, "test-db", sqls, connectionConfig);

        assertEquals(1, violations.size());
    }

    @Test
    public void testGetRules_EmptyRules() {
        List<Rule> rules = Collections.emptyList();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Collections.singletonList(createRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel_EmptyViolations() {
        List<Rule> rules = Collections.singletonList(createRule());
        List<CheckViolation> violations = Collections.emptyList();

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(0, filledRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(createRule());
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        violation.setRuleName("test-rule");

        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(violation));

        assertEquals(1, filledRules.size());
    }

    private Rule createRule() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test-rule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel("ERROR");

        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        return rule;
    }
}