import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
    private SingleConnectionDataSource dataSource;
    private JdbcOperations jdbcOperations;
    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        dataSource = new SingleConnectionDataSource();
        ReflectionTestUtils.setField(dataSource, "targetDataSource", new JdbcTemplate());
        jdbcOperations = mock(JdbcOperations.class);
        jdbcTemplate = new JdbcTemplate(dataSource);

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        connectionSession = mock(ConnectionSession.class);
        ReflectionTestUtils.setField(connectionSession, "dialectType", DialectType.MYSQL);
        ReflectionTestUtils.setField(connectionSession, "syncJdbcExecutor", map -> jdbcOperations);
    }

    @Test
    public void testCheck() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName("test_rule");
        ruleMetadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(ruleMetadata);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        sqlCheckRules.add(sqlCheckRule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, connectionSession)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(1, checkViolations.size());
        verify(sqlCheckRule).check(sqlCheckReq.getScriptContent());
    }

    @Test
    public void testCheckEmptySql() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("");

        List<Rule> rules = new ArrayList<>();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckEmptyRules() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = Collections.emptyList();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckEnvironment() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0));

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName("test_rule");
        ruleMetadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(ruleMetadata);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
        sqlCheckRules.add(sqlCheckRule);

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, connectionConfig.getDialectType(), jdbcTemplate)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(1, checkViolations.size());
        verify(sqlCheckRule).check(sqls, any(SqlCheckContext.class));
    }

    @Test
    public void testCheckEnvironmentEmptySqls() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = Collections.emptyList();

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckEnvironmentEmptyRules() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;", 0));

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = Collections.emptyList();

        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        SqlCheckRuleFactory sqlCheckRuleFactory = mock(SqlCheckRuleFactory.class);
        candidates.add(sqlCheckRuleFactory);

        when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(candidates);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(new CheckViolation());

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("test_rule", rules.get(0));

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(1, result.size());
    }

    private List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbcOperations) {
        return sqlCheckService.getRules(rules, dialectType, jdbcOperations);
    }
}