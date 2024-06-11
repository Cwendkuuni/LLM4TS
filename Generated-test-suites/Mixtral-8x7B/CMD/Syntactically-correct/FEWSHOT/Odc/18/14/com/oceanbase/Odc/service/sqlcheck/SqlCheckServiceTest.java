package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.core.sql.split.SplitSqlUtil;
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

@Service
@Validated
@SkipAuthorize("inside connect session")
class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCheck() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);

        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        sqlCheckService.check(session, req);

        // Add assertions if needed
    }

    @Test
    public void testCheckWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);

        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());

        List<CheckResult> results = sqlCheckService.check(session, req);

        assertEquals(Collections.emptyList(), results);
    }

    @Test
    public void testCheckWithEmptySqlCheckRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = mock(SqlCheckReq.class);

        List<Rule> rules = new ArrayList<>();
        rules.add(mock(Rule.class));
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckResult> results = sqlCheckService.check(session, req);

        assertEquals(Collections.emptyList(), results);
    }

    @Test
    public void testCheckWithNonEmptySqlCheckRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        SqlCheckReq req = new SqlCheckReq();
        req.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());

        List<SqlCheckRule> sqlCheckRules = Collections.singletonList(getSampleSqlCheckRule());
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(ruleService.getRuleById(anyLong())).thenReturn(getSampleRule());

        List<CheckResult> results = sqlCheckService.check(session, req);

        assertEquals(1, results.size());
    }

    @Test
    public void testCheckWithSqls() {
        Long environmentId = 1L;
        String databaseName = "mydb";
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM users;", 0, 25));

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());

        List<SqlCheckRule> sqlCheckRules = Collections.singletonList(getSampleSqlCheckRule());
        when(environmentService.detail(environmentId)).thenReturn(getSampleEnvironment());
        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

        List<CheckViolation> violations = sqlCheckService.check(environmentId, databaseName, sqls,
                getSampleConnectionConfig());

        assertEquals(1, violations.size());
    }

    @Test
    public void testGetRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<Rule> rules = new ArrayList<>();
        rules.add(getSampleRule());

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(1, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(Collections.emptyList(), session);

        assertEquals(0, sqlCheckRules.size());
    }

    @Test
    public void testGetRulesWithNonEmptyRules() {
        ConnectionSession session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);

        List<Rule> rules = Collections.nCopies(3, getSampleRule());

        List<SqlCheckRuleFactory> factories = Collections.singletonList(getSampleSqlCheckRuleFactory());
        when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
                .thenReturn(factories);

        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

        assertEquals(3, sqlCheckRules.size());
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.nCopies(3, getSampleRule());
        List<CheckViolation> violations = new ArrayList<>();

        CheckViolation violation1 = new CheckViolation();
        violation1.setType(getSampleRule().getMetadata());
        violation1.setMessage("Violation 1");
        violations.add(violation1);

        CheckViolation violation2 = new CheckViolation();
        violation2.setType(getSampleRule().getMetadata());
        violation2.setMessage("Violation 2");
        violations.add(violation2);

        List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(2, result.size());
        assertEquals(getSampleRule().getLevel(), result.get(0).getLevel());
        assertEquals(getSampleRule().getLevel(), result.get(1).getLevel());
    }

    private Rule getSampleRule() {
        Rule rule = new Rule();
        rule.setId(1L);
        rule.setEnabled(true);

        RuleMetadata metadata = new RuleMetadata();
        metadata.setName("test_rule");
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel("ERROR");
        rule.setMetadata(metadata);

        return rule;
    }

    private SqlCheckRule getSampleSqlCheckRule() {
        return new SampleSqlCheckRule("SELECT * FROM users;", "test_rule", DialectType.MYSQL, getSampleRule());
    }

    private ConnectionConfig getSampleConnectionConfig() {
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        config.setDatabase("testdb");
        config.setHost("localhost");
        config.setPort(3306);
        config.setUsername("testuser");
        config.setPassword("testpassword");

        return config;
    }

    private Environment getSampleEnvironment() {
        Environment environment = new Environment();
        environment.setId(1L);
        environment.setRulesetId(1L);

        return environment;
    }

    private DataSourceFactory getSampleSqlCheckRuleFactory() {
        return new SingleConnectionDataSourceFactory(getSampleDataSource());
    }

    private SingleConnectionDataSource getSampleDataSource() {
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");

        return dataSource;
    }

}

class SampleSqlCheckRule implements SqlCheckRule {

    private final String sql;
    private final String ruleName;
    private final DialectType dialectType;
    private final Rule rule;

    public SampleSqlCheckRule(String sql, String ruleName, DialectType dialectType, Rule rule) {
        this.sql = sql;
        this.ruleName = ruleName;
        this.dialectType = dialectType;
        this.rule = rule;
    }

    @Override
    public List<CheckViolation> check(List<OffsetString> sqls, SqlCheckContext context) {
        List<CheckViolation> violations = new ArrayList<>();
        for (OffsetString sql : sqls) {
            if (StringUtils.equals(sql.getText(), this.sql)) {
                CheckViolation violation = new CheckViolation();
                violation.setType(this.rule.getMetadata());
                violation.setMessage("Sample violation");
                violations.add(violation);
            }
        }
        return violations;
    }

    @Override
    public DialectType getDialectType() {
        return this.dialectType;
    }

    @Override
    public Rule getRule() {
        return this.rule;
    }

    @Override
    public String getRuleName() {
        return this.ruleName;
    }

}