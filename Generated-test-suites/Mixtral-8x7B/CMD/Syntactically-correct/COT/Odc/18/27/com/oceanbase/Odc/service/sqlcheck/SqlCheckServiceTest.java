import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlCheckServiceTest {

    @Autowired
    private SqlCheckService sqlCheckService;

    private ConnectionSession connectionSession;

    private SqlCheckReq sqlCheckReq;

    private List<Rule> rules;

    private List<SqlCheckRuleFactory> sqlCheckRuleFactories;

    private JdbcOperations jdbcOperations;

    @Before
    public void setup() {
        // Initialize mocks and test data
        rules = new ArrayList<>();
        sqlCheckRuleFactories = SqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations);
        connectionSession = Mockito.mock(ConnectionSession.class);
        sqlCheckReq = Mockito.mock(SqlCheckReq.class);

        // Set up behavior for mocks
        Mockito.when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        Mockito.when(connectionSession.getSyncJdbcExecutor(Mockito.anyString()))
                .thenReturn(jdbcOperations);
    }

    @Test
    public void testCheck_WithNoRules_ReturnsEmptyList() {
        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(Collections.emptyList());

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCheck_WithRules_ReturnsCheckResults() {
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK));
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK));
        rules.add(rule1);
        rules.add(rule2);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(rules);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assert.assertEquals(2, sqlCheckRules.size());
        Assert.assertEquals(SqlCheckRules.getFactoryByRule(sqlCheckRuleFactories, rule1),
                sqlCheckRules.get(0).getClass());
        Assert.assertEquals(SqlCheckRules.getFactoryByRule(sqlCheckRuleFactories, rule2),
                sqlCheckRules.get(1).getClass());
    }

    @Test
    public void testCheck_WithSqlCheckerException_HandlesException() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK));
        rules.add(rule);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(rules);

        Mockito.when(sqlChecker.check(Mockito.any()))
                .thenThrow(new SqlCheckException("Test exception"));

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testCheck_WithViolations_ReturnsCheckResults() {
        Rule rule = new Rule();
        rule.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK));
        rules.add(rule);

        Mockito.when(ruleService.list(Mockito.anyLong(), Mockito.any()))
                .thenReturn(rules);

        SqlCheckRule sqlCheckRule = SqlCheckRules.getFactoryByRule(sqlCheckRuleFactories, rule)
                .create(connectionSession.getDialectType(), "delimiter", rule);

        CheckViolation violation1 = new CheckViolation();
        violation1.setType(sqlCheckRule.getType());
        violation1.setMessage("Violation 1");
        violation1.setLine(1);
        violation1.setColumn(1);

        CheckViolation violation2 = new CheckViolation();
        violation2.setType(sqlCheckRule.getType());
        violation2.setMessage("Violation 2");
        violation2.setLine(2);
        violation2.setColumn(2);

        List<CheckViolation> checkViolations = new ArrayList<>();
        checkViolations.add(violation1);
        checkViolations.add(violation2);

        CheckResult checkResult = new CheckResult();
        checkResult.setSql("Test SQL");
        checkResult.setViolations(checkViolations);

        List<CheckResult> results = new ArrayList<>();
        results.add(checkResult);

        Mockito.when(sqlChecker.check(Mockito.any()))
                .thenReturn(checkViolations);

        List<CheckResult> serviceResults = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertEquals(1, serviceResults.size());
        Assert.assertEquals(checkResult.getSql(), serviceResults.get(0).getSql());
        Assert.assertEquals(2, serviceResults.get(0).getViolations().size());
        Assert.assertEquals(violation1.getMessage(), serviceResults.get(0).getViolations().get(0).getMessage());
        Assert.assertEquals(violation2.getMessage(), serviceResults.get(0).getViolations().get(1).getMessage());
    }

    @Test
    public void testCheck_WithNoRulesetId_ReturnsEmptyList() {
        Mockito.when(ConnectionSessionUtil.getRuleSetId(connectionSession))
                .thenReturn(null);

        List<CheckResult> result = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testGetRules_WithEmptyRules_ReturnsEmptyList() {
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), connectionSession);

        Assert.assertTrue(sqlCheckRules.isEmpty());
    }

    @Test
    public void testGetRules_WithRules_ReturnsSqlCheckRules() {
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK));
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK));
        rules.add(rule1);
        rules.add(rule2);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);

        Assert.assertEquals(2, sqlCheckRules.size());
        Assert.assertEquals(SqlCheckRules.getFactoryByRule(sqlCheckRuleFactories, rule1),
                sqlCheckRules.get(0).getClass());
        Assert.assertEquals(SqlCheckRules.getFactoryByRule(sqlCheckRuleFactories, rule2),
                sqlCheckRules.get(1).getClass());
    }

    @Test
    public void testFullFillRiskLevel_WithViolations_SetsLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK));
        rule1.setLevel(1);
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK));
        rule2.setLevel(2);
        rules.add(rule1);
        rules.add(rule2);

        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation1 = new CheckViolation();
        violation1.setType(RuleType.SQL_CHECK);
        violation1.setMessage("Violation 1");
        violation1.setLine(1);
        violation1.setColumn(1);
        CheckViolation violation2 = new CheckViolation();
        violation2.setType(RuleType.SQL_CHECK);
        violation2.setMessage("Violation 2");
        violation2.setLine(2);
        violation2.setColumn(2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        Assert.assertEquals(2, result.size());
        Assert.assertEquals(1, result.get(0).getLevel().intValue());
        Assert.assertEquals(2, result.get(1).getLevel().intValue());
    }

    @Test
    public void testFullFillRiskLevel_WithEmptyViolations_ReturnsEmptyList() {
        List<Rule> rules = new ArrayList<>();
        Rule rule1 = new Rule();
        rule1.setMetadata(new RuleMetadata("rule1", RuleType.SQL_CHECK));
        rule1.setLevel(1);
        Rule rule2 = new Rule();
        rule2.setMetadata(new RuleMetadata("rule2", RuleType.SQL_CHECK));
        rule2.setLevel(2);
        rules.add(rule1);
        rules.add(rule2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, new ArrayList<>());

        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testFullFillRiskLevel_WithNoRules_ReturnsEmptyList() {
        List<Rule> result = sqlCheckService.fullFillRiskLevel(Collections.emptyList(), new ArrayList<>());

        Assert.assertTrue(result.isEmpty());
    }
}