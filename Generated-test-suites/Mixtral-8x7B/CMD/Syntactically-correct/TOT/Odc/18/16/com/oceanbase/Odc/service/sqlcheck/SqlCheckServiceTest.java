import static org.junit.Assert.assertEquals;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Captor
    private ArgumentCaptor<QueryRuleMetadataParams> queryRuleMetadataParamsCaptor;

    private SqlCheckService sqlCheckService;

    private SingleConnectionDataSource dataSource;

    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        dataSource = new SingleConnectionDataSourceFactory().getDataSource();
        jdbcOperations = new JdbcTemplate(dataSource);

        sqlCheckService = spy(new SqlCheckService());

        doReturn(environmentService).when(sqlCheckService).environmentService;
        doReturn(ruleService).when(sqlCheckService).ruleService;

        doReturn(jdbcOperations).when(sqlCheckService).getJdbcOperations(anyLong(), any());
    }

    @Test
    public void testCheck() {
        Long envId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("SELECT * FROM users"));
        ConnectionConfig config = ConnectionConfig.builder()
                .dialectType(DialectType.MYSQL)
                .host("localhost")
                .port(3306)
                .username("testUser")
                .password("testPassword")
                .schema("testDb")
                .build();

        Environment environment = mock(Environment.class);
        when(environmentService.detail(envId)).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        List<Rule> rules = Collections.singletonList(getMockRule());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(envId, databaseName, sqls, config);

        assertEquals(1, violations.size());
        assertTrue(violations.get(0).getMessage().contains("Test Violation"));
    }

    @Test
    public void testCheckWithEmptyRules() {
        Long envId = 1L;
        String databaseName = "testDb";
        List<OffsetString> sqls = Collections.singletonList(OffsetString.of("SELECT * FROM users"));
        ConnectionConfig config = ConnectionConfig.builder()
                .dialectType(DialectType.MYSQL)
                .host("localhost")
                .port(3306)
                .username("testUser")
                .password("testPassword")
                .schema("testDb")
                .build();

        Environment environment = mock(Environment.class);
        when(environmentService.detail(envId)).thenReturn(environment);
        when(environment.getRulesetId()).thenReturn(1L);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckViolation> violations = sqlCheckService.check(envId, databaseName, sqls, config);

        assertEquals(0, violations.size());
    }

    @Test
    public void testGetRules() {
        Long envId = 1L;
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<Rule> rules = Collections.singletonList(getMockRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(1, sqlCheckRules.size());

        SqlCheckRule sqlCheckRule = sqlCheckRules.get(0);
        assertEquals("Test Rule", sqlCheckRule.getName());
        assertEquals(DialectType.MYSQL, sqlCheckRule.getDialectType());
        assertEquals(1, sqlCheckRule.getRule().getId().longValue());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        Long envId = 1L;
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(getMockRule());
        List<CheckViolation> violations = Collections.singletonList(getMockViolation());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(1, result.size());
        assertEquals("Test Rule", result.get(0).getName());
        assertEquals(1, result.get(0).getLevel().intValue());
    }

    private Rule getMockRule() {
        Rule rule = new Rule();
        rule.setId(1L);
        rule.setMetadata(getMockRuleMetadata());
        return rule;
    }

    private RuleMetadata getMockRuleMetadata() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("Test Rule");
        metadata.setType(RuleType.SQL_CHECK);
        return metadata;
    }

    private CheckViolation getMockViolation() {
        CheckViolation violation = new CheckViolation();
        violation.setType(getMockType());
        return violation;
    }

    private Rule.RuleType getMockType() {
        Rule.RuleType type = new Rule.RuleType();
        type.setName("Test Violation");
        return type;
    }

}