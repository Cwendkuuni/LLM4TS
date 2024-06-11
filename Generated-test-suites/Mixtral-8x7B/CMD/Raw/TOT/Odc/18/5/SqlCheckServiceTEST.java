import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleServiceMock;

    @Mock
    private EnvironmentService environmentServiceMock;

    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq sqlCheckReq;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleServiceMock);
        sqlCheckService.setEnvironmentService(environmentServiceMock);

        session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        when(ruleServiceMock.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(rules);

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(0, checkResults.size());
    }

    @Test
    public void testCheckWithViolations() {
        List<Rule> rules = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.SQL_INJECTION);
        violation1.setLevel(1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(RuleType.SQL_COMMENT);
        violation2.setLevel(2);
        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation1);
        checkViolations.add(violation2);

        SqlCheckRule rule1 = Mockito.mock(SqlCheckRule.class);
        when(rule1.check(ArgumentMatchers.anyString())).thenReturn(checkViolations);
        rules.add(rule1);

        when(ruleServiceMock.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(rules);

        List<CheckResult> checkResults = sqlCheckService.check(session, sqlCheckReq);
        assertEquals(2, checkResults.size());
        assertEquals(violation1.getType(), checkResults.get(0).getType());
        assertEquals(1, checkResults.get(0).getLevel());
        assertEquals(violation2.getType(), checkResults.get(1).getType());
        assertEquals(2, checkResults.get(1).getLevel());
    }

    @Test
    public void testCheckWithEnvironment() {
        when(environmentServiceMock.detail(1L))
                .thenReturn(buildEnvironment(DialectType.MYSQL));

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 21));

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "test_db", sqls, config);
        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithEnvironmentAndViolations() {
        when(environmentServiceMock.detail(1L))
                .thenReturn(buildEnvironment(DialectType.MYSQL));

        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users; -- test comment", 0, 25));

        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.SQL_COMMENT);
        violation1.setLevel(1);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation1);

        SqlCheckRule rule1 = Mockito.mock(SqlCheckRule.class);
        when(rule1.check(ArgumentMatchers.anyList()))
                .thenReturn(checkViolations);

        List<SqlCheckRule> rules = new ArrayList<>();
        rules.add(rule1);

        when(ruleServiceMock.list(ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(rules);

        List<CheckViolation> resultCheckViolations = sqlCheckService.check(1L, "test_db", sqls, config);
        assertEquals(1, resultCheckViolations.size());
        assertEquals(violation1.getType(), resultCheckViolations.get(0).getType());
        assertEquals(1, resultCheckViolations.get(0).getLevel());
    }

    private Environment buildEnvironment(DialectType dialectType) {
        Environment environment = new Environment();
        environment.setId(1L);
        environment.setRulesetId(2L);
        environment.setDialectType(dialectType);
        return environment;
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, ConnectionSession session) {
        return sqlCheckService.getRules(rules, session);
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        return sqlCheckService.getRules(rules, dialectType, jdbc);
    }

    private List<Rule> fullFillRiskLevel(List<Rule> rules, List<CheckViolation> violations) {
        return sqlCheckService.fullFillRiskLevel(rules, violations);
    }
}