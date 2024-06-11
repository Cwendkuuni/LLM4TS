import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckRule;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
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
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    private Environment environment;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);
        environment = new Environment();
        environment.setRulesetId(1L);
    }

    @Test
    public void testCheck() {
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata());
        rules.add(rule);
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        ReflectionTestUtils.setField(sqlCheckService, "sqlCheckRules", sqlCheckRules);

        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertEquals(0, results.size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheckEmptySqlList() {
        List<OffsetString> sqls = new ArrayList<>();

        List<Rule> rules = new ArrayList<>();
        ReflectionTestUtils.setField(sqlCheckService, "rules", rules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "dbName", sqls, connectionConfig);

        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        JdbcOperations jdbcOperations = new JdbcTemplate(new SingleConnectionDataSource());
        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        ReflectionTestUtils.setField(sqlCheckService, "candidates", candidates);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        assertEquals(0, sqlCheckRules.size());
        verify(sqlCheckService, times(1)).getRules(rules, connectionSession.getDialectType(), jdbcOperations);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(new Rule());

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(new CheckViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(0, result.size());
    }

    @After
    public void tearDown() {
        reset(connectionSession, ruleService, environmentService);
    }
}