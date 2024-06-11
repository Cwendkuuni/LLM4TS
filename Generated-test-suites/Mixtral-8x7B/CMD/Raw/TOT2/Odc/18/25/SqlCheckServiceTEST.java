import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        connectionConfig = mock(ConnectionConfig.class);

        ReflectionTestUtils.setField(sqlCheckService, "dialectType", DialectType.MYSQL);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);
    }

    @Test
    public void testCheck_whenRulesArePresent_shouldReturnCheckResults() {
        Long ruleSetId = 1L;
        List<Rule> rules = createRules(ruleSetId, 3);
        SqlCheckReq sqlCheckReq = new SqlCheckReq("SELECT 1;");

        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(3, results.size());
        results.forEach(r -> assertEquals(0, r.getViolations().size()));
    }

    @Test
    public void testCheck_whenNoRules_shouldReturnEmptyList() {
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("SELECT 1;"));

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_whenEmptyScript_shouldReturnEmptyList() {
        List<Rule> rules = createRules(1L, 3);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq(""));

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_whenNoViolations_shouldReturnEmptyList() {
        List<Rule> rules = createRules(1L, 3);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, new SqlCheckReq("SELECT 1;"));

        assertEquals(0, results.size());
    }

    @Test
    public void testCheck_whenViolations_shouldReturnCheckResults() {
        Long ruleSetId = 1L;
        List<Rule> rules = createRules(ruleSetId, 3);
        SqlCheckReq sqlCheckReq = new SqlCheckReq("SELECT 1;");

        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(3, results.size());
        results.forEach(r -> {
            assertEquals(1, r.getViolations().size());
            assertEquals("TEST_VIOLATION", r.getViolations().get(0).getMessage());
        });
    }

    @Test
    public void testCheck_whenNoRulesForEnvironment_shouldReturnEmptyList() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("SELECT 1;", 0, 10));

        when(environmentService.detail(environmentId)).thenReturn(new Environment());

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(0, violations.size());
    }

    @Test
    public void testCheck_whenViolationsForEnvironment_shouldReturnCheckViolations() {
        Long environmentId = 1L;
        String databaseName = "test";
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("SELECT 1;", 0, 10));

        when(environmentService.detail(environmentId))
                .thenReturn(new Environment().setRulesetId(1L));

        when(ruleService.list(1L, any(QueryRuleMetadataParams.class)))
                .thenReturn(createRules(1L, 1));

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(1, violations.size());
        assertEquals("TEST_VIOLATION", violations.get(0).getMessage());
    }

    private List<Rule> createRules(Long ruleSetId, int count) {
        List<Rule> rules = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Rule rule = new Rule();
            rule.setId(i + 1);
            rule.setMetadata(new RuleMetadata().setType(RuleType.SQL_CHECK).setName("TEST_" + (i + 1)));
            rule.setLevel(i + 1);
            rule.setEnabled(true);
            rule.setRuleViolation(Collections.singletonList(
                    new RuleViolation().setMessage("TEST_VIOLATION").setSqlCheckType("TEST_VIOLATION")));
            rules.add(rule);
        }
        return rules;
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        return rules.stream()
                .filter(Objects::nonNull)
                .filter(rule -> Boolean.TRUE.equals(rule.getEnabled()))
                .map(rule -> {
                    try {
                        return SqlCheckRules.createByRule(SqlCheckRules.getAllFactories(dialectType, jdbc), dialectType, rule);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}