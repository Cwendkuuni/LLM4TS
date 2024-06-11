import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mockJdbcTemplate());

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithViolations() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mockJdbcTemplate());

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "TestRule", "testRule", null, null, null, null));
        List<Rule> rules = Collections.singletonList(rule);

        SqlCheckRules sqlCheckRules = mock(SqlCheckRules.class);
        when(sqlCheckRules.check(ArgumentMatchers.<String>anyList())).thenReturn(Collections.singletonList(new CheckViolation(
                "TestType", "TestMessage")));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> result = sqlCheckService.check(session, req);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestType", result.get(0).getType().getName());
        assertEquals("TestMessage", result.get(0).getMessage());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(2L);

        when(environmentService.detail(environmentId)).thenReturn(env);

        List<Rule> rules = new ArrayList<>();
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testCheckWithEnvironmentAndViolations() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(2L);

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "TestRule", "testRule", null, null, null, null));
        List<Rule> rules = Collections.singletonList(rule);

        SqlCheckRules sqlCheckRules = mock(SqlCheckRules.class);
        when(sqlCheckRules.check(any())).thenReturn(Collections.singletonList(new CheckViolation("TestType", "TestMessage")));

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TestType", result.get(0).getType().getName());
        assertEquals("TestMessage", result.get(0).getMessage());
    }

    @Test
    public void testGetRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mockJdbcTemplate());

        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertTrue(CollectionUtils.isEmpty(result));
    }

    @Test
    public void testGetRulesWithSqlCheckRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(mockJdbcTemplate());

        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata(RuleType.SQL_CHECK, "TestRule", "testRule", null, null, null, null));
        List<Rule> rules = Collections.singletonList(rule);

        SqlCheckRules sqlCheckRules = mock(SqlCheckRules.class);
        when(sqlCheckRules.createByRule(any(), any(), any())).thenReturn(new SqlCheckRule(rule));

        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

        assertEquals(1, result.size());
        assertEquals(rule.getMetadata().getName(), result.get(0).getName());
    }

    private JdbcOperations mockJdbcTemplate() {
        return new JdbcTemplate(new SingleConnectionDataSource());
    }

}