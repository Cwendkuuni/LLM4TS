import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        sqlCheckService = new SqlCheckService();
        ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
        ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        List<Rule> rules = new ArrayList<>();
        List<Map<String, Object>> sqlCheckRules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

        sqlCheckService.check(session, req);

        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(session, times(1)).getDialectType();
        verify(session, times(1)).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
    }

    @Test
    public void testCheck_EmptySqlCheckRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheck_NoRuleSetId() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertEquals(Collections.emptyList(), result);
    }

    @Test
    public void testCheck_EnvironmentIdAndConfig() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString());
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        List<Rule> rules = Collections.singletonList(getRule());
        List<SqlCheckRule> sqlCheckRules = Collections.singletonList(getSqlCheckRule());
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        SqlCheckContext checkContext = mock(SqlCheckContext.class);
        JdbcTemplate jdbc = mock(JdbcTemplate.class);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(new OBConsoleDataSourceFactory(config, true, false).getDataSource()).thenReturn(new SingleConnectionDataSource());
        when(new OBConsoleDataSourceFactory(config, true, false).resetSchema(any())).thenReturn(null);
        when(new JdbcTemplate(new SingleConnectionDataSource())).thenReturn(jdbc);
        when(sqlCheckService.getRules(rules, config.getDialectType(), jdbc)).thenReturn(sqlCheckRules);

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(environmentService, times(1)).detail(environmentId);
        verify(ruleService, times(1)).list(env.getRulesetId(), any(QueryRuleMetadataParams.class));
        verify(new OBConsoleDataSourceFactory(config, true, false), times(1)).getDataSource();
        verify(new OBConsoleDataSourceFactory(config, true, false), times(1)).resetSchema(any());
        verify(jdbc, times(1)).query(anyString(), any(PreparedStatementSetter.class), any(ResultSetExtractor.class));
        verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), jdbc);
    }

    @Test
    public void testGetRules_EmptyRules() {
        List<Rule> rules = Collections.emptyList();

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, mock(ConnectionSession.class));

        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testGetRules_NoSqlCheckRules() {
        List<Rule> rules = Collections.singletonList(getRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, mock(ConnectionSession.class));

        assertEquals(Collections.emptyList(), sqlCheckRules);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(getRule());
        List<CheckViolation> violations = Collections.singletonList(getCheckViolation());
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, violatedRules.size());
        assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), violatedRules.get(0).getViolation());
    }

    private Rule getRule() {
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel("ERROR");
        rule.setMetadata(metadata);
        rule.setEnabled(true);
        return rule;
    }

    private SqlCheckRule getSqlCheckRule() {
        return mock(SqlCheckRule.class);
    }

    private CheckViolation getCheckViolation() {
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        return violation;
    }
}