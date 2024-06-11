package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.DataSourceFactory;
import com.oceanbase.odc.core.datasource.OBConsoleDataSourceFactory;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleMetadata;
import com.oceanbase.odc.service.session.factory.SessionFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@SuppressWarnings("unused")
public class SqlCheckServiceTest {

    @InjectMocks
    private SqlCheckService sqlCheckService;

    @Mock
    private RuleService ruleService;

    @Mock
    private EnvironmentService environmentService;

    @Mock
    private SessionFactory sessionFactory;

    @Mock
    private DataSourceFactory dataSourceFactory;

    @Captor
    private ArgumentCaptor<SqlCheckRules.SqlCheckRuleFactory> sqlCheckRuleFactoryCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sqlCheckService = spy(sqlCheckService);
    }

    @Test
    public void testCheck() throws SQLException {
        long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM test_table"));
        ConnectionConfig config = new ConnectionConfig();
        List<Rule> rules = Collections.singletonList(getRule());

        Environment env = mock(Environment.class);
        when(env.getRulesetId()).thenReturn(1L);

        List<SqlCheckRule> sqlCheckRules = Collections.singletonList(getSqlCheckRule());

        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(dataSource.getConnection()).thenReturn(mock(Connection.class));
        when(dataSourceFactory.getDataSource(any())).thenReturn(dataSource);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        doReturn(env).when(environmentService).detail(environmentId);
        doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, config.getDialectType(), jdbcOperations);
        doReturn(jdbcOperations).when(jdbcTemplate).getJdbcOperations();

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), jdbcOperations);
        verify(jdbcTemplate, times(1)).getJdbcOperations();
        verify(dataSource, times(1)).getConnection();
    }

    @Test
    public void testGetRules() {
        List<Rule> rules = Collections.singletonList(getRule());
        DialectType dialectType = DialectType.MYSQL;
        JdbcOperations jdbcOperations = mock(JdbcOperations.class);

        List<SqlCheckRules.SqlCheckRuleFactory> factories = new ArrayList<>();
        factories.add(mock(SqlCheckRules.SqlCheckRuleFactory.class));

        when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(factories);

        sqlCheckService.getRules(rules, dialectType, jdbcOperations);

        verify(SqlCheckRules, times(1)).getAllFactories(dialectType, jdbcOperations);
    }

    @Test
    public void testFullFillRiskLevel() {
        List<Rule> rules = Collections.singletonList(getRule());
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation("testType", "testMessage"));

        Map<String, Rule> name2RuleMap = new HashMap<>();
        name2RuleMap.put("${testType}", getRule());

        sqlCheckService.fullFillRiskLevel(rules, violations);

        assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), rules.get(0).getViolation());
    }

    @Test
    public void testFullFillRiskLevel_noRules() {
        List<Rule> rules = Collections.emptyList();
        List<CheckViolation> violations = Collections.singletonList(new CheckViolation("testType", "testMessage"));

        sqlCheckService.fullFillRiskLevel(rules, violations);

        assertNull(violations.get(0).getLevel());
    }

    @Test
    public void testCheck_noRules() {
        long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM test_table"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = mock(Environment.class);
        when(env.getRulesetId()).thenReturn(1L);

        doReturn(env).when(environmentService).detail(environmentId);

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(sqlCheckService, times(0)).getRules(anyList(), any(), any());
    }

    @Test
    public void testCheck_noSqlCheckRules() {
        long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM test_table"));
        ConnectionConfig config = new ConnectionConfig();

        Environment env = mock(Environment.class);
        when(env.getRulesetId()).thenReturn(1L);

        List<Rule> rules = Collections.singletonList(getRule());

        DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
        when(dataSourceFactory.getDataSource(any())).thenReturn(mock(SingleConnectionDataSource.class));

        doReturn(env).when(environmentService).detail(environmentId);
        doReturn(Collections.emptyList()).when(sqlCheckService).getRules(rules, config.getDialectType(), any());
        doReturn(dataSourceFactory).when(sqlCheckService).createDataSourceFactory(any());

        sqlCheckService.check(environmentId, databaseName, sqls, config);

        verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), any());
        verify(dataSourceFactory, times(1)).getDataSource(any());
    }

    @Test
    public void testCreateConnectionSession() {
        long environmentId = 1L;
        String databaseName = "testDB";

        Environment env = mock(Environment.class);
        when(env.getRulesetId()).thenReturn(1L);

        DataSourceFactory dataSourceFactory = mock(DataSourceFactory.class);
        SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
        when(dataSourceFactory.getDataSource(any())).thenReturn(dataSource);

        doReturn(env).when(environmentService).detail(environmentId);
        doReturn(dataSourceFactory).when(sqlCheckService).createDataSourceFactory(any());

        ConnectionSession session = sqlCheckService.createConnectionSession(environmentId, databaseName);

        verify(sqlCheckService, times(1)).createDataSourceFactory(eq(databaseName));
        verify(dataSource, times(1)).afterPropertiesSet();
        assertEquals(databaseName, session.getSchema());
        assertEquals(DialectType.MYSQL, session.getDialectType());
    }

    @Test
    public void testCreateConnectionSession_noDataSourceFactory() {
        long environmentId = 1L;
        String databaseName = "testDB";

        Environment env = mock(Environment.class);
        when(env.getRulesetId()).thenReturn(1L);

        doReturn(env).when(environmentService).detail(environmentId);
        doReturn(null).when(sqlCheckService).createDataSourceFactory(any());

        assertFalse(sqlCheckService.createConnectionSession(environmentId, databaseName).isValid());
    }

    @Test
    public void testCreateSqlCheckReq() {
        long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM test_table"));

        Environment env = mock(Environment.class);
        when(env.getRulesetId()).thenReturn(1L);

        doReturn(env).when(environmentService).detail(environmentId);

        SqlCheckReq req = sqlCheckService.createSqlCheckReq(environmentId, databaseName, sqls);

        assertEquals(environmentId, req.getEnvironmentId());
        assertEquals(databaseName, req.getDatabaseName());
        assertEquals(sqls, req.getSqls());
    }

    @Test
    public void testCreateSqlCheckReq_noEnvironment() {
        long environmentId = 1L;
        String databaseName = "testDB";
        List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM test_table"));

        doReturn(null).when(environmentService).detail(environmentId);

        SqlCheckReq req = sqlCheckService.createSqlCheckReq(environmentId, databaseName, sqls);

        assertNull(req);
    }

    private Rule getRule() {
        RuleMetadata metadata = new RuleMetadata();
        metadata.setType(RuleType.SQL_CHECK);
        return new Rule().setMetadata(metadata);
    }

    private SqlCheckRules.SqlCheckRule getSqlCheckRule() {
        return mock(SqlCheckRules.SqlCheckRule.class);
    }

    private SessionFactory createSessionFactoryMock() {
        return mock(SessionFactory.class);
    }

    private DataSourceFactory createDataSourceFactoryMock() {
        DataSourceFactory factory = mock(DataSourceFactory.class);
        when(factory.getDataSource(any())).thenReturn(new SingleConnectionDataSource());
        return factory;
    }

}