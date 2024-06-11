import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    private SqlCheckService sqlCheckService;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sqlCheckService = new SqlCheckService();
        sqlCheckService.setRuleService(ruleService);
        sqlCheckService.setEnvironmentService(environmentService);
    }

    @Test
    public void testCheck() {
        ConnectionSession sessionMock = Mockito.mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);

        when(ruleService.list(anyLong(), ArgumentMatchers.<QueryRuleMetadataParams>any()))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        sqlCheckRules.add(() -> {});
        SqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\n", sqlCheckRules);
        CheckViolation violation = new CheckViolation();
        violation.setType(SqlCheckRules.RESERVED_KEYWORD);
        List<CheckViolation> checkViolations = Collections.singletonList(violation);

        when(sessionMock.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sessionMock.getSyncJdbcExecutor(ArgumentMatchers.anyString()))
                .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));
        when(ruleService.list(anyLong(), ArgumentMatchers.<QueryRuleMetadataParams>any()))
                .thenReturn(rules);
        when(sqlChecker.check(ArgumentMatchers.anyString())).thenReturn(checkViolations);

        List<CheckResult> results = sqlCheckService.check(sessionMock, req);
        assertEquals(1, results.size());

        verify(ruleService, times(1)).list(anyLong(), ArgumentMatchers.<QueryRuleMetadataParams>any());
        verify(sessionMock, times(1)).getDialectType();
        verify(sessionMock, times(1))
                .getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
    }

    // Add test cases for other methods

    // ...

    // ...

    // ...

    // Add edge cases and exception handling scenarios

    // ...

    // ...

    // ...

}