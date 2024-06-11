import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRules;
import org.apache.commons.collections4.CollectionUtils;
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

    private ConnectionSession connectionSession;
    private SqlCheckReq sqlCheckReq;
    private Environment environment;
    private ConnectionConfig connectionConfig;
    private JdbcOperations jdbcOperations;
    private SingleConnectionDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        sqlCheckReq = mock(SqlCheckReq.class);
        environment = mock(Environment.class);
        connectionConfig = mock(ConnectionConfig.class);
        dataSource = mock(SingleConnectionDataSource.class);
        jdbcOperations = mock(JdbcOperations.class);
        jdbcTemplate = mock(JdbcTemplate.class);

        ReflectionTestUtils.setField(sqlCheckService, "dialectType", DialectType.MYSQL);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getSyncJdbcExecutor(anyString())).thenReturn(jdbcOperations);

        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");

        jdbcTemplate = new JdbcTemplate(dataSource);
        when(jdbcOperations.getJdbcTemplate()).thenReturn(jdbcTemplate);

        ReflectionTestUtils.setField(sqlCheckService, "consoleDataSourceFactory", new OBConsoleDataSourceFactory());
        ReflectionTestUtils.setField(sqlCheckService.getConsoleDataSourceFactory(), "dataSource", dataSource);
    }

    @Test
    public void testCheck() {
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        List<CheckViolation> checkViolations = new ArrayList<>();

        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(ruleSetId);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, connectionSession)).thenReturn(sqlCheckRules);

        sqlCheckService.check(connectionSession, sqlCheckReq);

        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(getRules(rules, connectionSession), times(1)).check(anyString());
    }

    @Test
    public void testCheckWithEmptySqls() {
        Long environmentId = 1L;
        List<OffsetString> sqls = new ArrayList<>();

        when(environmentService.detail(environmentId)).thenReturn(environment);

        List<CheckViolation> result = sqlCheckService.check(environmentId, "testdb", sqls, connectionConfig);

        assertEquals(Collections.emptyList(), result);
        verify(environmentService, times(1)).detail(environmentId);
    }

    @Test
    public void testGetRules() {
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();

        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(candidates);

        sqlCheckService.getRules(rules, connectionSession);

        verify(ruleService, times(1)).list(ruleSetId, QueryRuleMetadataParams.builder().build());
        verify(SqlCheckRules, times(1)).getAllFactories(DialectType.MYSQL, jdbcOperations);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.TEST);

        rules.add(new Rule(new RuleMetadata("TEST", RuleType.SQL_CHECK, 1)));
        violations.add(violation);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getLevel().intValue());
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, ConnectionSession session) {
        return sqlCheckService.getRules(rules, session);
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        return sqlCheckService.getRules(rules, dialectType, jdbc);
    }
}