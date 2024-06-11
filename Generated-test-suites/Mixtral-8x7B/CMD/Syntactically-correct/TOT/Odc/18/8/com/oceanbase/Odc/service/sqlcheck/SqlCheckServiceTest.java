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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.ruleService = ruleService;
        sqlCheckService.environmentService = environmentService;
    }

    @Test
    public void check_validInputs_returnsCheckResults() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(session, req);

        Assertions.assertEquals(0, results.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void check_withRules_returnsCheckResultsWithViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = createRules();
        SqlCheckRule sqlCheckRule = createSqlCheckRule(rules);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.createByRule(anyList(), any(DialectType.class), any(Rule.class))).thenReturn(sqlCheckRule);

        List<CheckResult> results = sqlCheckService.check(session, req);

        Assertions.assertEquals(1, results.size());
        CheckResult result = results.get(0);
        Assertions.assertEquals("SELECT * FROM users;", result.getSqlText());
        Assertions.assertEquals(1, result.getViolations().size());
        Assertions.assertEquals("DUPLICATE_KEY", result.getViolations().get(0).getType().getName());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(ruleService, times(1)).createByRule(anyList(), any(DialectType.class), any(Rule.class));
    }

    @Test
    public void check_withConnectionConfig_returnsCheckResults() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0, 25));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = createRules();
        SqlCheckRule sqlCheckRule = createSqlCheckRule(rules);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.createByRule(anyList(), any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(sqlCheckRule);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        Assertions.assertEquals(1, violations.size());
        CheckViolation violation = violations.get(0);
        Assertions.assertEquals("DUPLICATE_KEY", violation.getType().getName());

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), any(QueryRuleMetadataParams.class));
        verify(ruleService, times(1))
                .createByRule(anyList(), any(DialectType.class), any(JdbcOperations.class));
    }

    private List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rule.getMetadata().setName("DUPLICATE_KEY");
        rule.setEnabled(true);
        rule.setLevel("ERROR");
        rules.add(rule);
        return rules;
    }

    private SqlCheckRule createSqlCheckRule(List<Rule> rules) {
        return new SqlCheckRule() {
            @Override
            public List<CheckViolation> check(List<String> sqlScripts, SqlCheckContext context) {
                List<CheckViolation> violations = new ArrayList<>();
                violations.add(new CheckViolation("DUPLICATE_KEY"));
                return violations;
            }

            @Override
            public List<Rule> getRules() {
                return rules;
            }
        };
    }
}