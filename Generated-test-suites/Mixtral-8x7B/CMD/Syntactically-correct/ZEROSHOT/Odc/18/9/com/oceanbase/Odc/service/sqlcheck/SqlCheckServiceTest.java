import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
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
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unchecked")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService = spy(new SqlCheckService());

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        List<Rule> rules = createRules();
        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules, session);
        DefaultSqlChecker sqlChecker = mock(DefaultSqlChecker.class);
        List<CheckViolation> checkViolations = createCheckViolations();
        when(sqlChecker.check(req.getScriptContent())).thenReturn(checkViolations);
        sqlCheckService.sqlChecker = sqlChecker;
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);
        List<CheckResult> results = sqlCheckService.check(session, req);
        verify(sqlChecker, times(1)).check(req.getScriptContent());
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getViolations());
        assertEquals(2, results.get(0).getViolations().size());
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        doReturn(Collections.emptyList()).when(sqlCheckService).getRules(any(), any());
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckWithEmptySqlCheckRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);
        List<Rule> rules = createRules();
        List<SqlCheckRule> sqlCheckRules = Collections.emptyList();
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);
        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(0, results.size());
    }

    @Test
    public void testCheckConnectionConfig() {
        Long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = createOffsetStrings();
        ConnectionConfig config = mock(ConnectionConfig.class);
        Environment env = mock(Environment.class);
        doReturn(env).when(environmentService).detail(environmentId);
        doReturn("test_ds_key").when(config).getDialectType();
        doReturn(databaseName).when(config).getDatabase();
        List<Rule> rules = createRules();
        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules, config);
        SqlCheckContext checkContext = mock(SqlCheckContext.class);
        JdbcOperations jdbc = mock(JdbcOperations.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(dataSource);
        when(dataSource.getJdbcOperations()).thenReturn(jdbc);
        DefaultSqlChecker sqlChecker = mock(DefaultSqlChecker.class);
        List<CheckViolation> checkViolations = createCheckViolations();
        when(sqlChecker.check(any(), any(SqlCheckContext.class))).thenReturn(checkViolations);
        sqlCheckService.sqlChecker = sqlChecker;
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, config.getDialectType(), jdbc);
        List<CheckViolation> results = sqlCheckService.check(environmentId, databaseName, sqls, config);
        verify(sqlChecker, times(1)).check(sqls, checkContext);
        assertEquals(2, results.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        doReturn(Collections.emptyList()).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);
        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        List<Rule> rules = createRules();
        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules, session);
        doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        assertEquals(sqlCheckRules, result);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = createRules();
        List<CheckViolation> checkViolations = createCheckViolations();
        List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, checkViolations);
        assertTrue(CollectionUtils.isNotEmpty(violatedRules));
        assertEquals(2, violatedRules.size());
        checkViolations.forEach(checkViolation -> {
            Rule rule = violatedRules.stream().filter(r -> r.getViolation().getRuleId().equals(checkViolation.getRuleId()))
                    .findFirst().get();
            assertEquals(rule.getLevel(), checkViolation.getLevel());
        });
    }

    private List<OffsetString> createOffsetStrings() {
        return Collections.singletonList(OffsetString.builder().sql("SELECT * FROM dual;").build());
    }

    private List<Rule> createRules() {
        List<Rule> rules = new ArrayList<>();
        Map<String, RuleMetadata> metadataMap = new HashMap<>();
        metadataMap.put("test_rule_1", RuleMetadata.builder().name("test_rule_1").type(RuleType.SQL_CHECK).build());
        metadataMap.put("test_rule_2", RuleMetadata.builder().name("test_rule_2").type(RuleType.SQL_CHECK).build());
        metadataMap.put("test_rule_3", RuleMetadata.builder().name("test_rule_3").type(RuleType.OTHER).build());
        metadataMap.forEach((k, v) -> {
            Rule rule = Rule.builder().metadata(v).enabled(true).build();
            rules.add(rule);
        });
        return rules;
    }

    private List<SqlCheckRule> createSqlCheckRules(List<Rule> rules, ConnectionSession session) {
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.getMetadata().getType() == RuleType.SQL_CHECK) {
                sqlCheckRules.add(mock(SqlCheckRule.class));
            }
        }
        return sqlCheckRules;
    }

    private List<SqlCheckRule> createSqlCheckRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        for (Rule rule : rules) {
            if (rule.getMetadata().getType() == RuleType.SQL_CHECK) {
                sqlCheckRules.add(mock(SqlCheckRule.class));
            }
        }
        return sqlCheckRules;
    }

    private List<CheckViolation> createCheckViolations() {
        CheckViolation violation1 =
                CheckViolation.builder().ruleId("test_rule_1").description("test_rule_1_description").build();
        CheckViolation violation2 =
                CheckViolation.builder().ruleId("test_rule_2").description("test_rule_2_description").build();
        return Collections.singletonList(violation1);
    }
}