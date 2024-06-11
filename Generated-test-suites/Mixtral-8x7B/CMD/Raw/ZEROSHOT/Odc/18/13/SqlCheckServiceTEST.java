import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(sqlCheckService, "dialectType", DialectType.MYSQL);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = Mockito.mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM table");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder()
                .name("test_rule")
                .type(RuleType.SQL_CHECK)
                .build());
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        sqlCheckRules.add(sqlCheckRule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType(RuleType.SQL_CHECK);
        violation.setLevel("ERROR");
        checkViolations.add(violation);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, ";", sqlCheckRules);
        when(sqlChecker.check(req.getScriptContent())).thenReturn(checkViolations);

        List<CheckResult> results = sqlCheckService.check(session, req);
        assertEquals(1, results.size());
        CheckResult result = results.get(0);
        assertEquals("test_rule", result.getRuleName());
        assertEquals("ERROR", result.getLevel());
    }

    @Test
    public void testCheckWithEnv() {
        Long environmentId = 1L;
        String databaseName = "test_database";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = new Environment();
        env.setRulesetId(1L);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setMetadata(RuleMetadata.builder()
                .name("test_rule")
                .type(RuleType.SQL_CHECK)
                .build());
        rule.setEnabled(true);
        rules.add(rule);

        List<SqlCheckRule> checkRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        checkRules.add(sqlCheckRule);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(sqlCheckService.getRules(rules, config.getDialectType(), any(JdbcOperations.class)))
                .thenReturn(checkRules);

        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, checkRules);
        when(sqlChecker.check(Collections.singletonList(sqls.get(0)), any(SqlCheckContext.class)))
                .thenReturn(Collections.singletonList(new CheckViolation()));

        List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(1, checkViolations.size());
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.builder().metadata(
                RuleMetadata.builder().name("test_rule").type(RuleType.SQL_CHECK).build()).build());

        List<SqlCheckRuleFactory> candidates = new ArrayList<>();
        SqlCheckRuleFactory factory = Mockito.mock(SqlCheckRuleFactory.class);
        candidates.add(factory);

        when(SqlCheckRules.getAllFactories(DialectType.MYSQL, any(JdbcOperations.class)))
                .thenReturn(candidates);
        when(SqlCheckRules.createByRule(candidates, DialectType.MYSQL, rules.get(0)))
                .thenReturn(Mockito.mock(SqlCheckRule.class));

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, (ConnectionSession) null);
        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.builder().metadata(
                RuleMetadata.builder().name("test_rule").type(RuleType.SQL_CHECK).build()).build());

        List<CheckViolation> violations = new ArrayList<>();
        violations.add(CheckViolation.builder().type(RuleType.SQL_CHECK).build());

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(1, result.size());
        assertEquals("test_rule", result.get(0).getMetadata().getName());
        assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), result.get(0).getViolation());
    }

}