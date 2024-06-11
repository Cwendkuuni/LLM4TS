import com.oceanbase.odc.core.datasource.ConsoleConnectionConfig;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    private Environment environment;
    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        connectionSession = mock(ConnectionSession.class);
        connectionConfig = new ConsoleConnectionConfig();
        sqlCheckReq = new SqlCheckReq();
        environment = new Environment();

        jdbcOperations = mock(JdbcOperations.class);
        when(connectionSession.getSyncJdbcExecutor(any())).thenReturn(jdbcOperations);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("test");
        dataSource.setPassword("test");
        when(connectionSession.getJdbcTemplate(any())).thenReturn(new JdbcTemplate(dataSource));

        when(environmentService.detail(anyLong())).thenReturn(environment);
    }

    @Test
    public void testCheck() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        doNothing().when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        List<CheckViolation> expectedCheckViolations = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(rule.getMetadata());
        expectedCheckViolations.add(checkViolation);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, ";", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(sqlCheckReq.getScriptContent());

        assertEquals(expectedCheckViolations, checkViolations);
    }

    @Test
    public void testCheckWithOffsetStrings() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        sqlCheckReq.setScriptContent("SELECT * FROM users; SELECT * FROM products;");

        doNothing().when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        List<CheckViolation> expectedCheckViolations = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(rule.getMetadata());
        expectedCheckViolations.add(checkViolation);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, ";", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(sqlCheckReq.getScriptContent());

        assertEquals(expectedCheckViolations, checkViolations);
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());

        doNothing().when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel(1);
        rule.setMetadata(metadata);
        rules.add(rule);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation checkViolation = new CheckViolation();
        checkViolation.setType(metadata);
        checkViolations.add(checkViolation);

        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);

        assertEquals(1, violatedRules.size());
        assertEquals(1, violatedRules.get(0).getLevel());
    }

    @Test
    public void testCheckWithEmptyRules() {
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        doNothing().when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkViolations.size());
    }

    @Test
    public void testCheckWithEmptySql() {
        sqlCheckReq.setScriptContent("");

        doNothing().when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.singletonList(new Rule()));

        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, sqlCheckReq);

        assertEquals(0, checkViolations.size());
    }
}