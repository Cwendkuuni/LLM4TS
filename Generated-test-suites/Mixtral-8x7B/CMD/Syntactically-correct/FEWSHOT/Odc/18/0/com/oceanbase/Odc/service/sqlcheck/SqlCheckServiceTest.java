package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
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
import com.oceanbase.odc.core.datasource.DataSourceProperties;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSourceFactory;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRulesFactory;

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

    @Mock
    private DataSourceFactory dataSourceFactory;

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private ConnectionSession connectionSession;

    @Mock
    private ConnectionConfig connectionConfig;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        doReturn(jdbcOperations).when(dataSourceFactory).getJdbcOperations();
        doReturn(dataSourceFactory).when(connectionConfig).getDataSourceFactory();
    }

    @Test
    public void testCheck() {
        Long ruleSetId = 1L;
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table";
        req.setScriptContent(scriptContent);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);
        doReturn(rules).when(ruleService).list(ruleSetId, any(QueryRuleMetadataParams.class));
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRulesFactory factory = spy(SqlCheckRulesFactory.class);
        sqlCheckRules.add(factory.createByRule(Collections.singletonList(rule), DialectType.MYSQL, jdbcOperations));
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, connectionSession);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\\;", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(scriptContent);
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertEquals(1, checkResults.size());
        assertEquals(checkViolations.size(),
                checkResults.get(0).getViolations().size());
        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(1)).getRules(rules, connectionSession);
    }

    @Test
    public void testCheck_whenRulesIsEmpty() {
        Long ruleSetId = 1L;
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table";
        req.setScriptContent(scriptContent);
        List<Rule> rules = Collections.emptyList();
        doReturn(rules).when(ruleService).list(ruleSetId, any(QueryRuleMetadataParams.class));
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertEquals(0, checkResults.size());
        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(0)).getRules(rules, connectionSession);
    }

    @Test
    public void testCheck_whenCheckViolationsIsEmpty() {
        Long ruleSetId = 1L;
        SqlCheckReq req = new SqlCheckReq();
        String scriptContent = "SELECT * FROM table";
        req.setScriptContent(scriptContent);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);
        doReturn(rules).when(ruleService).list(ruleSetId, any(QueryRuleMetadataParams.class));
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRulesFactory factory = spy(SqlCheckRulesFactory.class);
        sqlCheckRules.add(factory.createByRule(Collections.singletonList(rule), DialectType.MYSQL, jdbcOperations));
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, connectionSession);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, "\\;", sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(scriptContent);
        List<CheckResult> checkResults = sqlCheckService.check(connectionSession, req);
        assertEquals(0, checkResults.size());
        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(1)).getRules(rules, connectionSession);
    }

    @Test
    public void testCheck_withEnvAndConfig() {
        List<OffsetString> sqls = new ArrayList<>();
        sqls.add(new OffsetString("SELECT * FROM table", 0, 20));
        SqlCheckReq req = new SqlCheckReq();
        req.setSqls(sqls);
        Long environmentId = 1L;
        String databaseName = "testdb";
        ConnectionConfig config = new ConnectionConfig();
        config.setDialectType(DialectType.MYSQL);
        DataSourceProperties properties = new DataSourceProperties();
        properties.setUrl("jdbc:mysql://localhost:3306/testdb");
        properties.setUsername("root");
        properties.setPassword("root");
        config.setProperties(properties);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/testdb");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
        dataSource.setAutoCommit(false);
        dataSource.setSuppressClose(true);
        SingleConnectionDataSourceFactory factory = new SingleConnectionDataSourceFactory(config);
        factory.resetDataSource(dataSource);
        doReturn(factory).when(dataSourceFactory).getDataSourceFactory(config);
        doReturn(new Environment()).when(environmentService).detail(environmentId);
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        rule.setMetadata(metadata);
        rules.add(rule);
        doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));
        List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
        SqlCheckRulesFactory factory1 = spy(SqlCheckRulesFactory.class);
        sqlCheckRules.add(factory1.createByRule(Collections.singletonList(rule), DialectType.MYSQL, jdbcOperations));
        doReturn(sqlCheckRules)
                .when(sqlCheckService).getRules(rules, config.getDialectType(), jdbcOperations);
        DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, sqlCheckRules);
        List<CheckViolation> checkViolations = sqlChecker.check(sqls, new SqlCheckContext(1L));
        List<CheckResult> checkResults = sqlCheckService.check(environmentId, databaseName, sqls, config);
        assertEquals(1, checkResults.size());
        assertEquals(checkViolations.size(),
                checkResults.get(0).getViolations().size());
        verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
        verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), jdbcOperations);
    }

    @Test
    public void testGetRules() {
        Long ruleSetId = 1L;
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setName("test");
        rule.setMetadata(metadata);
        rules.add(rule);
        doReturn(rules).when(ruleService).list(ruleSetId, any(QueryRuleMetadataParams.class));
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(1, sqlCheckRules.size());
        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testGetRules_whenRulesIsEmpty() {
        Long ruleSetId = 1L;
        List<Rule> rules = Collections.emptyList();
        List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, connectionSession);
        assertEquals(0, sqlCheckRules.size());
        verify(ruleService, times(1)).list(ruleSetId, any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = new ArrayList<>();
        Rule rule = new Rule();
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        metadata.setLevel(2);
        metadata.setName("test");
        rule.setMetadata(metadata);
        rules.add(rule);
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("${test}");
        violation.setLevel(null);
        violations.add(violation);
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(2, filledRules.get(0).getLevel());
        verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel_whenViolationsIsEmpty() {
        List<Rule> rules = new ArrayList<>();
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList());
        assertEquals(0, filledRules.size());
        verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }

    @Test
    public void testFullFillRiskLevel_whenRuleIsEmpty() {
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations = new ArrayList<>();
        CheckViolation violation = new CheckViolation();
        violation.setType("${test}");
        violation.setLevel(null);
        violations.add(violation);
        List<Rule> filledRules = sqlCheckService.fullFillRiskLevel(rules, violations);
        assertEquals(0, filledRules.size());
        verify(ruleService, times(0)).list(anyLong(), any(QueryRuleMetadataParams.class));
    }
}

@Service
class DefaultSqlChecker {

    public DefaultSqlChecker(DialectType dialectType, String delimiter, List<SqlCheckRule> sqlCheckRules) {

    }

    public List<CheckViolation> check(String scriptContent) {
        return new ArrayList<>();
    }

    public List<CheckViolation> check(List<OffsetString> sqls, SqlCheckContext context) {
        return new ArrayList<>();
    }
}

class SqlCheckContext {

    public SqlCheckContext(long total) {

    }
}

class SqlCheckRulesFactory {

    public SqlCheckRules createByRule(List<Rule> rules, DialectType dialectType, JdbcOperations jdbcOperations) {
        return new SqlCheckRules() {

            @Override
            public void check(JdbcOperations jdbcOperations, String sql) {

            }
        };
    }
}