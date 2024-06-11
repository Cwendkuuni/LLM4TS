import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck_withValidSessionAndRequest() {
        long ruleSetId = 1L;
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = createRules(1);
        when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = createSqlCheckRules(rules);
        when(ruleService.getSqlCheckRules(eq(session))).thenReturn(sqlCheckRules);

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertEquals(0, checkResults.size());

        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
        verify(ruleService, times(1)).getSqlCheckRules(eq(session));
    }

    @Test
    public void testCheck_withEmptyRequest() {
        ConnectionSession session = mock(ConnectionSession.class);

        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("");

        List<CheckResult> checkResults = sqlCheckService.check(session, req);

        assertEquals(0, checkResults.size());

        verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testCheck_withEnvironmentAndConfig() {
        long environmentId = 1L;
        String databaseName = "test_db";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM users;"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = createRules(1);
        when(environmentService.detail(environmentId)).thenReturn(env);
        when(ruleService.list(env.getRulesetId(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
        when(factory.getDataSource()).thenReturn(new SingleConnectionDataSource());
        when(factory.resetSchema(any())).thenReturn(factory);

        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(factory.getJdbcTemplate()).thenReturn(new JdbcTemplate(jdbc));

        List<SqlCheckRule> checkRules = createSqlCheckRules(rules);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, checkRules);

        List<CheckViolation> checkViolations = sqlChecker.check(sqls, new SqlCheckContext(1L));

        assertEquals(0, checkViolations.size());
    }

    private List<Rule> createRules(int count) {
        List<Rule> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Rule rule = new Rule();
            RuleMetadata metadata = new RuleMetadata();
            metadata.setName("test_rule" + i);
            metadata.setType(RuleType.SQL_CHECK);
            rule.setMetadata(metadata);
            result.add(rule);
        }
        return result;
    }

    private List<SqlCheckRule> createSqlCheckRules(List<Rule> rules) {
        List<SqlCheckRule> result = new ArrayList<>();
        for (Rule rule : rules) {
            result.add(mock(SqlCheckRule.class));
        }
        return result;
    }
}