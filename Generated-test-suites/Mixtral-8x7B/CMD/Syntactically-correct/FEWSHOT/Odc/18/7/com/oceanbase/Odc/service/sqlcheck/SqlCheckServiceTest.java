package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
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

@Service
@Validated
@SkipAuthorize("inside connect session")
class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private ConnectionSession session;
 @Mock
 private ConnectionSessionUtil sessionUtil;
 @Mock
 private SingleConnectionDataSource dataSource;
 @Mock
 private JdbcOperations jdbc;
 @Mock
 private JdbcTemplate jdbcTemplate;
 @Mock
 private ConnectionConfig config;
 @Mock
 private Environment env;
 @Mock
 private SqlCheckRules sqlCheckRules;
 @Mock
 private SqlCheckRuleFactory sqlCheckRuleFactory;
 @Mock
 private DefaultSqlChecker defaultSqlChecker;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck() {
 SqlCheckReq req = new SqlCheckReq();
 when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(ruleService.list(any(), any())).thenReturn(Collections.emptyList());
 when(sqlCheckService.getRules(anyList(), any(ConnectionSession.class)))
 .thenReturn(Collections.emptyList());

 sqlCheckService.check(session, req);
 }

 @Test
 public void testCheck_withViolations() {
 SqlCheckReq req = new SqlCheckReq();
 when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(ruleService.list(any(), any())).thenReturn(getRules());
 when(sqlCheckService.getRules(anyList(), any(ConnectionSession.class)))
 .thenReturn(getSqlCheckRules());
 when(defaultSqlChecker.check(anyString())).thenReturn(getCheckViolations());

 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getViolations().size());
 }

 @Test
 public void testCheck_withEnvironment() {
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("select * from table"));
 when(environmentService.detail(anyLong())).thenReturn(env);
 when(env.getRulesetId()).thenReturn(1L);
 when(ruleService.list(any(), any())).thenReturn(getRules());
 when(sqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
 .thenReturn(Collections.singletonList(sqlCheckRuleFactory));
 when(sqlCheckRuleFactory.createByRule(any(), any(DialectType.class), any(Rule.class)))
 .thenReturn(getSqlCheckRule());
 when(defaultSqlChecker.check(anyList(), any(SqlCheckContext.class)))
 .thenReturn(getCheckViolations());

 List<CheckViolation> result = sqlCheckService.check(1L, "test", sqls, config);
 assertEquals(1, result.size());
 assertEquals(1, result.get(0).getViolations().size());
 }

 @Test
 public void testGetRules() {
 List<Rule> rules = getRules();
 when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(ruleService.list(any(), any())).thenReturn(rules);
 when(sqlCheckService.getRules(anyList(), any(ConnectionSession.class)))
 .thenReturn(getSqlCheckRules());

 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);
 assertEquals(1, result.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = getRules();
 List<CheckViolation> violations = getCheckViolations();
 when(sessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(ruleService.list(any(), any())).thenReturn(rules);

 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);
 assertEquals(1, result.size());
 assertEquals("test", result.get(0).getLevel());
 }

 private List<Rule> getRules() {
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.getMetadata().setName("test");
 rule.getMetadata().setType(RuleType.SQL_CHECK);
 rule.setEnabled(true);
 return Collections.singletonList(rule);
 }

 private List<SqlCheckRule> getSqlCheckRules() {
 return Collections.singletonList(getSqlCheckRule());
 }

 private SqlCheckRule getSqlCheckRule() {
 return mock(SqlCheckRule.class);
 }

 private List<CheckViolation> getCheckViolations() {
 CheckViolation violation = new CheckViolation();
 violation.setType(new RuleType());
 violation.getType().setName("test");
 violation.setLevel("test");
 return Collections.singletonList(violation);
 }
}