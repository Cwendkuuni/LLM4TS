package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

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
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testCheck() {
    ConnectionSession session = mock(ConnectionSession.class);
    SqlCheckReq req = new SqlCheckReq();
    List<Rule> rules = new ArrayList<>();
    List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
    when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
    when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    when(session.getSyncJdbcExecutor(anyString())).thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));
    when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
        .thenReturn(sqlCheckRules);

    List<CheckResult> results = sqlCheckService.check(session, req);

    verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    verify(session, times(1)).getDialectType();
    verify(session, times(1)).getSyncJdbcExecutor(anyString());
    verify(SqlCheckRules, times(1)).getAllFactories(any(DialectType.class), any(JdbcOperations.class));
    assertEquals(0, results.size());
  }

  @Test
  public void testCheckWithViolations() {
    ConnectionSession session = mock(ConnectionSession.class);
    SqlCheckReq req = new SqlCheckReq();
    List<Rule> rules = new ArrayList<>();
    List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
    List<CheckViolation> violations = new ArrayList<>();
    CheckViolation violation = new CheckViolation();
    violation.setType(RuleType.SQL_CHECK);
    violations.add(violation);
    when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
    when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    when(session.getSyncJdbcExecutor(anyString()))
        .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));
    when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
        .thenReturn(sqlCheckRules);

    List<CheckResult> results = sqlCheckService.check(session, req);

    verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    verify(session, times(1)).getDialectType();
    verify(session, times(1)).getSyncJdbcExecutor(anyString());
    verify(SqlCheckRules, times(1)).getAllFactories(any(DialectType.class), any(JdbcOperations.class));
    assertEquals(1, results.size());
  }

  @Test
  public void testCheckForEnvironments() {
    Long environmentId = 1L;
    String databaseName = "testdb";
    ConnectionConfig config = new ConnectionConfig();
    List<OffsetString> sqls = new ArrayList<>();
    List<Rule> rules = new ArrayList<>();
    List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
    SqlCheckContext checkContext = new SqlCheckContext(1);
    when(environmentService.detail(environmentId)).thenReturn(new Environment());
    when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
    OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
    when(new OBConsoleDataSourceFactory(config, true, false)
            .getDataSource())
        .thenReturn(new SingleConnectionDataSource());
    when(factory.resetSchema(any())).thenReturn(null);
    when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
        .thenReturn(sqlCheckRules);

    List<CheckViolation> result =
        sqlCheckService.check(environmentId, databaseName, sqls, config);

    verify(environmentService, times(1)).detail(environmentId);
    verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    verify(factory, times(1)).resetSchema(any());
    verify(SqlCheckRules, times(1)).getAllFactories(any(DialectType.class), any(JdbcOperations.class));
    assertEquals(0, result.size());
  }

  @Test
  public void testCheckForEnvironmentsWithViolations() {
    Long environmentId = 1L;
    String databaseName = "testdb";
    ConnectionConfig config = new ConnectionConfig();
    List<OffsetString> sqls = new ArrayList<>();
    List<Rule> rules = new ArrayList<>();
    List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
    List<CheckViolation> violations = new ArrayList<>();
    CheckViolation violation = new CheckViolation();
    violation.setType(RuleType.SQL_CHECK);
    violations.add(violation);
    SqlCheckContext checkContext = new SqlCheckContext(1);
    when(environmentService.detail(environmentId)).thenReturn(new Environment());
    when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
    OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
    when(new OBConsoleDataSourceFactory(config, true, false)
            .getDataSource())
        .thenReturn(new SingleConnectionDataSource());
    when(factory.resetSchema(any())).thenReturn(null);
    when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
        .thenReturn(sqlCheckRules);

    List<CheckViolation> result =
        sqlCheckService.check(environmentId, databaseName, sqls, config);

    verify(environmentService, times(1)).detail(environmentId);
    verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    verify(factory, times(1)).resetSchema(any());
    verify(SqlCheckRules, times(1)).getAllFactories(any(DialectType.class), any(JdbcOperations.class));
    assertEquals(1, result.size());
  }

  @Test
  public void testGetRules() {
    ConnectionSession session = mock(ConnectionSession.class);
    List<Rule> rules = new ArrayList<>();
    when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
    when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    when(session.getSyncJdbcExecutor(anyString()))
        .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));

    List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

    verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    verify(session, times(1)).getDialectType();
    verify(session, times(1)).getSyncJdbcExecutor(anyString());
    assertEquals(0, result.size());
  }

  @Test
  public void testGetRulesWithViolations() {
    ConnectionSession session = mock(ConnectionSession.class);
    List<Rule> rules = new ArrayList<>();
    List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
    sqlCheckRules.add(mock(SqlCheckRule.class));
    when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);
    when(session.getDialectType()).thenReturn(DialectType.MYSQL);
    when(session.getSyncJdbcExecutor(anyString()))
        .thenReturn(new JdbcTemplate(new SingleConnectionDataSource()));
    when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
        .thenReturn(sqlCheckRules);

    List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

    verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
    verify(session, times(1)).getDialectType();
    verify(session, times(1)).getSyncJdbcExecutor(anyString());
    verify(SqlCheckRules, times(1)).getAllFactories(any(DialectType.class), any(JdbcOperations.class));
    assertEquals(1, result.size());
  }

  @Test
  public void testFullFillRiskLevel() {
    List<Rule> rules = new ArrayList<>();
    List<CheckViolation> violations = new ArrayList<>();
    CheckViolation violation = new CheckViolation();
    violation.setType(RuleType.SQL_CHECK);
    violations.add(violation);
    when(rules.stream().collect(Collectors.toMap(r -> r.getMetadata().getName(), r -> r)))
        .thenReturn(rules);

    List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

    assertEquals(1, result.size());
  }
}