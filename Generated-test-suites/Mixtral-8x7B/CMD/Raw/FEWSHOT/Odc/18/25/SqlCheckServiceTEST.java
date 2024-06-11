package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.QueryRuleMetadataParams;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleType;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;

@SkipAuthorize("inside connect session")
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 public SqlCheckServiceTest() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 Long ruleSetId = 1L;
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
 List<Rule> rules = new ArrayList<>();
 when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
 SqlChecker sqlChecker = mock(DefaultSqlChecker.class);
 when(sqlChecker.check(anyString())).thenReturn(Collections.emptyList());
 sqlCheckService = spy(sqlCheckService);
 doReturn(sqlChecker).when(sqlCheckService)
 .createSqlChecker(any(DialectType.class), anyString(), anyList());
 List<CheckViolation> checkViolations = new ArrayList<>();
 when(sqlChecker.check(anyString())).thenReturn(checkViolations);
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_withViolations() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();
 Long ruleSetId = 1L;
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(ruleSetId);
 List<Rule> rules = new ArrayList<>();
 when(ruleService.list(ruleSetId, any(QueryRuleMetadataParams.class)))
 .thenReturn(rules);
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 when(sqlCheckService.getRules(rules, session)).thenReturn(sqlCheckRules);
 SqlChecker sqlChecker = mock(DefaultSqlChecker.class);
 when(sqlChecker.check(anyString())).thenReturn(getCheckViolations());
 sqlCheckService = spy(sqlCheckService);
 doReturn(sqlChecker).when(sqlCheckService)
 .createSqlChecker(any(DialectType.class), anyString(), anyList());
 List<CheckViolation> checkViolations = new ArrayList<>();
 when(sqlChecker.check(anyString())).thenReturn(getCheckViolations());
 List<CheckResult> result = sqlCheckService.check(session, req);
 assertEquals(2, result.size());
 CheckResult first = result.get(0);
 CheckResult second = result.get(1);
 assertEquals(getCheckViolations().get(0).getType(), first.getType());
 assertEquals(getCheckViolations().get(1).getType(), second.getType());
 }

 @Test
 public void testCheck_environment() {
 // TODO: implement test for check(Long, String, List<OffsetString>, ConnectionConfig)
 }

 @Test
 public void testGetRules() {
 // TODO: implement test for getRules(List<Rule>, ConnectionSession)
 }

 @Test
 public void testGetRules_database() {
 // TODO: implement test for getRules(List<Rule>, DialectType, JdbcOperations)
 }

 @Test
 public void testFullFillRiskLevel() {
 // TODO: implement test for fullFillRiskLevel(List<Rule>, List<CheckViolation>)
 }

 private List<CheckViolation> getCheckViolations() {
 List<CheckViolation> violations = new ArrayList<>();
 violations.add(new CheckViolation("type1", "message1"));
 violations.add(new CheckViolation("type2", "message2"));
 return violations;
 }
}