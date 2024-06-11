package com.oceanbase.odc.service.sqlcheck;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
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
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @InjectMocks
    private SqlCheckService sqlCheckService;

    private JdbcOperations jdbcOperations;

    private ConnectionConfig connectionConfig;

    private ConnectionSession connectionSession;

    @Before
    public void setUp() {
        jdbcOperations = Mockito.mock(JdbcOperations.class);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("testuser");
        dataSource.setPassword("testpassword");
        dataSource.afterPropertiesSet();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcOperations = jdbcTemplate;

        connectionConfig = new ConnectionConfig();
        connectionConfig.setDialectType(DialectType.MYSQL);

        connectionSession = Mockito.mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
    }

    @Test
    public void testCheck() {
        SqlCheckReq sqlCheckReq = new SqlCheckReq();
        sqlCheckReq.setScriptContent("SELECT * FROM users;");

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setEnabled(true);
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName("TestRule");
        ruleMetadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(ruleMetadata);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        sqlCheckRules.add(sqlCheckRule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, connectionSession)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = sqlCheckService.check(connectionSession, sqlCheckReq);

        Assert.assertEquals(1, checkViolations.size());
        Mockito.verify(sqlCheckRule, Mockito.times(1)).check(sqlCheckReq.getScriptContent());
    }

    @Test
    public void testCheckWithOffsetStrings() {
        List<OffsetString> sqls = new ArrayList<>();
        OffsetString sql1 = new OffsetString("SELECT * FROM users;", 0);
        sqls.add(sql1);

        Environment environment = new Environment();
        environment.setId(1L);

        when(environmentService.detail(anyLong())).thenReturn(environment);

        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        rule.setEnabled(true);
        RuleMetadata ruleMetadata = new RuleMetadata();
        ruleMetadata.setName("TestRule");
        ruleMetadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(ruleMetadata);
        rules.add(rule);

        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRule sqlCheckRule = Mockito.mock(SqlCheckRule.class);
        sqlCheckRules.add(sqlCheckRule);

        when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
        when(getRules(rules, connectionConfig.getDialectType(), jdbcOperations)).thenReturn(sqlCheckRules);

        List<CheckViolation> checkViolations = sqlCheckService.check(1L, "testdb", sqls, connectionConfig);

        Assert.assertEquals(1, checkViolations.size());
        Mockito.verify(sqlCheckRule, Mockito.times(1)).check(sqls);
    }

    public List<SqlCheckRule> getRules(List<Rule> rules, DialectType dialectType, JdbcOperations jdbc) {
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }
        List<SqlCheckRuleFactory> candidates = SqlCheckRules.getAllFactories(dialectType, jdbc);
        return rules.stream().filter(rule -> {
            RuleMetadata metadata = rule.getMetadata();
            if (metadata == null || !Boolean.TRUE.equals(rule.getEnabled())) {
                return false;
            }
            return Objects.equals(metadata.getType(), RuleType.SQL_CHECK);
        }).map(rule -> {
            try {
                return SqlCheckRules.createByRule(candidates, dialectType, rule);
            } catch (Exception e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}