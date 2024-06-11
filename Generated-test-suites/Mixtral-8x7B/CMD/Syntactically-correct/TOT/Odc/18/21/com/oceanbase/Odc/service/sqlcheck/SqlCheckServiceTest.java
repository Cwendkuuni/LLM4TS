import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
        ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource());
        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck_withEmptySession() {
        List<CheckResult> results = sqlCheckService.check(null, new SqlCheckReq());
        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void testCheck_withEmptySqlCheckReq() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        List<Rule> rules = createRules(1);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq());
        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void testCheck_withSqlCheckReq() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        List<Rule> rules = createRules(3);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);
        Assertions.assertEquals(3, results.size());

        CheckResult checkResult1 = results.get(0);
        Assertions.assertEquals("test_rule_1", checkResult1.getRuleName());
        Assertions.assertEquals(30, checkResult1.getLevel());
        Assertions.assertEquals(1, checkResult1.getViolations().size());

        CheckViolation violation1 = checkResult1.getViolations().get(0);
        Assertions.assertEquals("TestRule1", violation1.getType().getName());
        Assertions.assertEquals("Test message 1", violation1.getMessage());

        CheckResult checkResult2 = results.get(1);
        Assertions.assertEquals("test_rule_2", checkResult2.getRuleName());
        Assertions.assertEquals(20, checkResult2.getLevel());
        Assertions.assertEquals(1, checkResult2.getViolations().size());

        CheckViolation violation2 = checkResult2.getViolations().get(0);
        Assertions.assertEquals("TestRule2", violation2.getType().getName());
        Assertions.assertEquals("Test message 2", violation2.getMessage());

        CheckResult checkResult3 = results.get(2);
        Assertions.assertEquals("test_rule_3", checkResult3.getRuleName());
        Assertions.assertEquals(10, checkResult3.getLevel());
        Assertions.assertEquals(1, checkResult3.getViolations().size());

        CheckViolation violation3 = checkResult3.getViolations().get(0);
        Assertions.assertEquals("TestRule3", violation3.getType().getName());
        Assertions.assertEquals("Test message 3", violation3.getMessage());
    }

    @Test
    public void testCheck_emptyRules() {
        when(connectionSession.getRuleSetId()).thenReturn(1L);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);
        Assertions.assertEquals(0, results.size());
    }

    @Test
    public void testCheck_withEnvironment() {
        when(environmentService.detail(anyLong())).thenReturn(createEnvironment());

        List<CheckViolation> violations = sqlCheckService.check(1L, "db_name", Arrays.asList(new OffsetString("SELECT * FROM users;")), connectionConfig);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    public void testCheck_withViolations() {
        when(environmentService.detail(anyLong())).thenReturn(createEnvironment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(createRules(3));

        List<CheckViolation> violations = sqlCheckService.check(1L, "db_name", Arrays.asList(new OffsetString("SELECT * FROM users;")), connectionConfig);
        Assertions.assertEquals(3, violations.size());

        CheckViolation violation1 = violations.get(0);
        Assertions.assertEquals("TestRule1", violation1.getType().getName());
        Assertions.assertEquals("Test message 1", violation1.getMessage());

        CheckViolation violation2 = violations.get(1);
        Assertions.assertEquals("TestRule2", violation2.getType().getName());
        Assertions.assertEquals("Test message 2", violation2.getMessage());

        CheckViolation violation3 = violations.get(2);
        Assertions.assertEquals("TestRule3", violation3.getType().getName());
        Assertions.assertEquals("Test message 3", violation3.getMessage());
    }

    private List<Rule> createRules(int count) {
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Rule rule = new Rule();
            rule.setId(i + 1);
            rule.setMetadata(new RuleMetadata());
            rule.getMetadata().setName("test_rule_" + (i + 1));
            rule.setLevel(10 * (i + 1));
            rule.setEnabled(true);
            rules.add(rule);
        }
        return rules;
    }

    private Environment createEnvironment() {
        Environment environment = new Environment();
        environment.setId(1L);
        environment.setRulesetId(1L);
        return environment;
    }
}