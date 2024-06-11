package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.rule.SqlCheckRules;

@Slf4j
public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(Collections.emptyList());
 when(req.getScriptContent()).thenReturn("test script");

 List<CheckResult> result = sqlCheckService.check(session, req);

 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_withRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 when(req.getScriptContent()).thenReturn("test script");

 when(session.getDialectType()).thenReturn(DialectType.MYSQL);

 SqlCheckRules sqlCheckRules = mock(SqlCheckRules.class);
 when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
 .thenReturn(Collections.singletonList(sqlCheckRules));

 when(sqlCheckRules.createByRule(anyList(), any(DialectType.class), any(Rule.class)))
 .thenReturn(new SqlCheckRule());

 List<CheckResult> result = sqlCheckService.check(session, req);

 assertNotNull(result);
 assertEquals(0, result.size());
 }

 @Test
 public void testCheck_withViolations() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);

 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 when(req.getScriptContent()).thenReturn("test script");

 when(session.getDialectType()).thenReturn(DialectType.MYSQL);

 SqlCheckRules sqlCheckRules = mock(SqlCheckRules.class);
 when(SqlCheckRules.getAllFactories(any(DialectType.class), any(JdbcOperations.class)))
 .thenReturn(Collections.singletonList(sqlCheckRules));

 when(sqlCheckRules.createByRule(anyList(), any(DialectType.class), any(Rule.class)))
 .thenReturn(new SqlCheckRule());

 List<CheckViolation> violations = Collections.singletonList(new CheckViolation());

 when(sqlCheckRules.check(anyString(), any(SqlCheckContext.class)))
 .thenReturn(violations);

 List<CheckResult> result = sqlCheckService.check(session, req);

 assertNotNull(result);
 assertEquals(1, result.size());
 }

 // Add more test cases for other methods as needed

}