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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class SqlCheckServiceTest {

    @Autowired
    private SqlCheckService sqlCheckService;

    @MockBean
    private RuleService ruleService;

    @MockBean
    private EnvironmentService environmentService;

    private ConnectionSession connectionSession;

    private ConnectionConfig connectionConfig;

    @Before
    public void setUp() {
        connectionSession = Mockito.mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);

        connectionConfig = Mockito.mock(ConnectionConfig.class);
        when(connectionConfig.getDialectType()).thenReturn(DialectType.MYSQL);

        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        when(connectionSession.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY))
                .thenReturn(jdbcTemplate);

        when(environmentService.detail(anyLong())).thenReturn(buildEnvironment());
    }

    @Test
    public void testCheck() {
        List<Rule> rules = Collections.singletonList(buildRule());

        SqlCheckReq req = SqlCheckReq.builder()
                .scriptContent("SELECT * FROM users WHERE id = 1;")
                .delimiter(";")
                .build();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(1));
    }

    @Test
    public void testCheckWithEmptySql() {
        List<Rule> rules = Collections.singletonList(buildRule());

        SqlCheckReq req = SqlCheckReq.builder()
                .scriptContent("")
                .delimiter(";")
                .build();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(0));
    }

    @Test
    public void testCheckWithEmptyRules() {
        List<Rule> rules = Collections.emptyList();

        SqlCheckReq req = SqlCheckReq.builder()
                .scriptContent("SELECT * FROM users WHERE id = 1;")
                .delimiter(";")
                .build();

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(connectionSession, req);

        assertThat(results, notNullValue());
        assertThat(results.size(), equalTo(0));
    }

    @Test
    public void testCheckWithNullSession() {
        sqlCheckService.check(null, SqlCheckReq.builder().build());

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheck_MultipleSqls() {
        List<Rule> rules = Collections.singletonList(buildRule());

        List<OffsetString> sqls = Arrays.asList(
                new OffsetString("SELECT * FROM users WHERE id = 1;", 0),
                new OffsetString("SELECT * FROM posts WHERE id = 1;", 30));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        assertThat(violations, notNullValue());
        assertThat(violations.size(), equalTo(2));
    }

    @Test
    public void testCheck_MultipleSqls_WithEmptySql() {
        List<Rule> rules = Collections.singletonList(buildRule());

        List<OffsetString> sqls = Arrays.asList(
                new OffsetString("", 0),
                new OffsetString("SELECT * FROM posts WHERE id = 1;", 30));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        assertThat(violations, notNullValue());
        assertThat(violations.size(), equalTo(1));
    }

    @Test
    public void testCheck_MultipleSqls_NoViolations() {
        List<Rule> rules = Collections.emptyList();

        List<OffsetString> sqls = Arrays.asList(
                new OffsetString("SELECT * FROM users WHERE id = 1;", 0),
                new OffsetString("SELECT * FROM posts WHERE id = 1;", 30));

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class)))
                .thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        assertThat(violations, notNullValue());
        assertThat(violations.size(), equalTo(0));
    }

    @Test
    public void testCheck_MultipleSqls_NullSession() {
        sqlCheckService.check(null, 1L, "testdb", null, connectionConfig);

        verify(ruleService, never()).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    private Environment buildEnvironment() {
        Environment environment = new Environment();
        environment.setRulesetId(1L);
        return environment;
    }

    private Rule buildRule() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);

        Rule rule = new Rule();
        rule.setMetadata(metadata);
        rule.setEnabled(true);

        return rule;
    }
}