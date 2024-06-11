package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

 private final SqlCheckService sqlCheckService = mock(SqlCheckService.class);
 private final RuleService ruleService = mock(RuleService.class);
 private final EnvironmentService environmentService = mock(EnvironmentService.class);
 private final ConnectionSession session = mock(ConnectionSession.class);
 private final SqlCheckReq req = new SqlCheckReq();
 private final SqlCheckRules sqlCheckRules = mock(SqlCheckRules.class);
 private final List<Rule> rules = new ArrayList<>();
 private final List<SqlCheckRule> sqlCheckRulesList = new ArrayList<>();
 private final CheckResult checkResult = new CheckResult();
 private final CheckViolation checkViolation = new CheckViolation();
 private final JdbcOperations jdbcOperations = mock(JdbcOperations.class);
 private final JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
 private final SingleConnectionDataSource singleConnectionDataSource = mock(SingleConnectionDataSource.class);
 private final OBConsoleDataSourceFactory obConsoleDataSourceFactory = mock(OBConsoleDataSourceFactory.class);
 private final Environment environment = new Environment();
 private final ConnectionConfig connectionConfig = new ConnectionConfig();
 private final OffsetString offsetString = new OffsetString();

 @Autowired
 public void setSqlCheckService(SqlCheckService sqlCheckService) {
 this.sqlCheckService = sqlCheckService;
 }

 @Autowired
 public void setRuleService(RuleService ruleService) {
 this.ruleService = ruleService;
 }

 @Autowired
 public void setEnvironmentService(EnvironmentService environmentService) {
 this.environmentService = environmentService;
 }

 @Test
 public void testCheck() {
 when(sqlCheckService.check(session, req)).thenReturn(Collections.singletonList(checkResult));
 assertEquals(Collections.singletonList(checkResult), sqlCheckService.check(session, req));
 }

 @Test
 public void testCheckWithEmptyList() {
 when(sqlCheckService.check(session, req)).thenReturn(Collections.emptyList());
 assertEquals(Collections.emptyList(), sqlCheckService.check(session, req));
 }

 @Test
 public void testGetRules() {
 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 when(session.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY)).thenReturn(jdbcOperations);
 when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);
 when(sqlCheckRules.getAllFactories(DialectType.MYSQL, jdbcOperations)).thenReturn(sqlCheckRulesList);
 assertEquals(sqlCheckRulesList, sqlCheckService.getRules(rules, session));
 }

 @Test
 public void testGetRulesWithEmptyList() {
 when(ruleService.list(Mockito.anyLong(), Mockito.any(QueryRuleMetadataParams.class))).thenReturn(rules);
 assertEquals(Collections.emptyList(), sqlCheckService.getRules(rules, session));
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> violatedRules = sqlCheckService.fullFillRiskLevel(rules, Collections.singletonList(checkViolation));
 assertNotNull(violatedRules);
 }

 @Test
 public void testFullFillRiskLevelWithEmptyList() {
 assertEquals(Collections.emptyList(), sqlCheckService.fullFillRiskLevel(rules, Collections.emptyList()));
 }

 @Test
 public void testCheckWithEmptyListOfSqls() {
 when(environmentService.detail(Mockito.anyLong())).thenReturn(environment);
 when(sqlCheckService.getRules(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
 .thenReturn(sqlCheckRulesList);
 assertEquals(Collections.emptyList(), sqlCheckService.check(1L, "databaseName", Collections.emptyList(), connectionConfig));
 }

 @Test
 public void testCheckWithNonEmptyListOfSqls() {
 when(environmentService.detail(Mockito.anyLong())).thenReturn(environment);
 when(sqlCheckService.getRules(Mockito.anyList(), Mockito.any(DialectType.class), Mockito.any(JdbcOperations.class)))
 .thenReturn(sqlCheckRulesList);
 when(obConsoleDataSourceFactory.getDataSource()).thenReturn(singleConnectionDataSource);
 when(singleConnectionDataSource.getJdbcTemplate()).thenReturn(jdbcTemplate);
 assertEquals(Collections.singletonList(checkViolation), sqlCheckService.check(1L, "databaseName",
 Collections.singletonList(offsetString), connectionConfig));
 }
}