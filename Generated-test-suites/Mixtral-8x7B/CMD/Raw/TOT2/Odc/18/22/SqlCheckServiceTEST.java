import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
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
import static org.mockito.ArgumentMatchers.eq;
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
    private SqlCheckReq sqlCheckReq;
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        connectionConfig = new ConnectionConfig();
        sqlCheckReq = new SqlCheckReq();

        jdbcOperations = mock(JdbcOperations.class);
        ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);

        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
    }

    @Test
    public void testCheck_withValidParameters_returnsCheckResults() {
        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(eq(1L), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        when(getRules(rules, connectionSession)).thenReturn(sqlCheckRules);

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setLevel("INFO");
        List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);
        when(new DefaultSqlChecker(any(DialectType.class), anyString(), eq(sqlCheckRules))
                .check(anyString())).thenReturn(checkViolations);

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(1, checkResults.size());
        assertEquals("INFO", checkResults.get(0).getLevel());
        verify(ruleService, times(1)).list(eq(1L), any(QueryRuleMetadataParams.class));
        verify(getRules(rules, connectionSession), times(1));
        verify(new DefaultSqlChecker(any(DialectType.class), anyString(), eq(sqlCheckRules))
                .check(anyString()), times(1));
    }

    @Test
    public void testCheck_whenNoRules_returnsEmptyList() {
        when(ruleService.list(eq(1L), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkResults.size());
        verify(ruleService, times(1)).list(eq(1L), any(QueryRuleMetadataParams.class));
        verify(getRules(Collections.emptyList(), connectionSession), never());
        verify(new DefaultSqlChecker(any(DialectType.class), anyString(), anyList()), never());
    }

    @Test
    public void testCheck_withConnectionConfig_returnsCheckViolations() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT 1;"));

        Environment env = new Environment();
        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("testDriver");
        dataSource.setUrl("jdbc:test://localhost");
        dataSource.setUsername("testUser");
        dataSource.setPassword("testPassword");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        when(new SingleConnectionDataSource(any(ConnectionConfig.class), anyBoolean(), anyBoolean())
                .getDataSource()).thenReturn(dataSource);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        when(getRules(rules, connectionConfig.getDialectType(), jdbcTemplate)).thenReturn(sqlCheckRules);

        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setLevel("WARN");
        List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);
        when(new DefaultSqlChecker(any(DialectType.class), anyString(), eq(sqlCheckRules))
                .check(anyList(), any(SqlCheckContext.class))).thenReturn(checkViolations);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(1, violations.size());
        assertEquals("WARN", violations.get(0).getLevel());
        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), any(QueryRuleMetadataParams.class));
        verify(new SingleConnectionDataSource(any(ConnectionConfig.class), anyBoolean(), anyBoolean()), times(1))
                .getDataSource();
        verify(new DefaultSqlChecker(any(DialectType.class), anyString(), eq(sqlCheckRules))
                .check(anyList(), any(SqlCheckContext.class)), times(1));
    }

    @Test
    public void testGetRules_withValidParameters_returnsSqlCheckRules() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        candidates.add(new TestSqlCheckRuleFactory());

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)), times(1));
        verifyNoMoreInteractions(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)));
    }

    @Test
    public void testFullFillRiskLevel_withValidParameters_returnsViolatedRules() {
        List<Rule> rules = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(RuleType.SQL_CHECK);
        List<CheckViolation> checkViolations = Collections.singletonList(checkViolation);

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("${test}", new Rule());

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(1, violatedRules.size());
        assertEquals(RuleType.SQL_CHECK, violatedRules.get(0).getMetadata().getType());
        verifyNoMoreInteractions(ruleService);
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, ConnectionSession session) {
        return sqlCheckService.getRules(rules, session);
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        return sqlCheckService.getRules(rules, dialectType, jdbc);
    }
}

class TestSqlCheckRuleFactory implements SqlCheckRuleFactory {

    @Override
    public boolean support(Rule rule, DialectType dialectType, JdbcOperations jdbc) {
        return false;
    }

    @Override
    public SqlCheckRule create(Rule rule, DialectType dialectType, JdbcOperations jdbc) {
        return null;
    }
}