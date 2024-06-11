import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
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
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    @SuppressWarnings("unused")
    private SqlCheckService sqlCheckService;

    private ConnectionSession session;

    private SqlCheckReq sqlCheckReq;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        session = mock(ConnectionSession.class);
        sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = Collections.singletonList(mock(SqlCheckRule.class));
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, session)).thenReturn(sqlCheckRules);

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    public void testCheckEmptyRules() {
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckEmptyScriptContent() {
        sqlCheckReq.setScriptContent("");

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckNoDbConnection() {
        when(getRules(anyList(), session)).thenThrow(new RuntimeException("No DB connection"));

        List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    public void testCheckWithEnvironment() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0, 25));
        ConnectionConfig config = new ConnectionConfig();
        List<Rule> rules = new ArrayList<>();
        List<SqlCheckRule> sqlCheckRules = Collections.singletonList(mock(SqlCheckRule.class));
        Environment environment = new Environment();

        when(environmentService.detail(environmentId)).thenReturn(environment);
        when(ruleService.list(eq(environment.getRulesetId()), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);
        when(getRules(rules, config.getDialectType(), any(JdbcOperations.class)))
                .thenReturn(sqlCheckRules);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
        assertEquals(1, violations.size());
    }

    @Test
    public void testCheckWithEnvironmentNoRuleset() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0, 25));
        ConnectionConfig config = new ConnectionConfig();

        when(environmentService.detail(environmentId)).thenReturn(null);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.emptyList();
        ConnectionConfig config = new ConnectionConfig();

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testCheckWithEnvironmentNoDbConnection() {
        Long environmentId = 1L;
        String databaseName = "testdb";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0, 25));
        ConnectionConfig config = new ConnectionConfig();

        when(getRules(anyList(), config.getDialectType(), any(JdbcOperations.class)))
                .thenThrow(new RuntimeException("No DB connection"));

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls, config);

        assertNotNull(violations);
        assertTrue(violations.isEmpty());
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, ConnectionConfig config) {
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbc = spy(new JdbcTemplate(new SingleConnectionDataSource("url", "username", "password")));
        doReturn(jdbc).when(session).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
        return getRules(rules, dialectType, jdbc);
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbc);
        return rules.stream().filter(rule -> {
            RuleMetadata metadata = rule.getMetadata();
            if (metadata == null || !Boolean.TRUE.equals(rule.getEnabled())) {
                return false;
            }
            return Objects.equals(metadata.getType(), RuleType.SQL_CHECK);
        }).map(rule -> {
            try {
                return SqlCheckRules.createByRule(candidates, dialectType, rule);
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}