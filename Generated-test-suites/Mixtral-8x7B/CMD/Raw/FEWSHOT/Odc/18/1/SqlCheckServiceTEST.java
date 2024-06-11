package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;

import com.oceanbase.odc.core.authority.util.SkipAuthorize;
import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.sql.split.OffsetString;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.collaboration.environment.model.Environment;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleType;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
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
 @Mock
 private JdbcOperations jdbcOperations;
 @Mock
 private JdbcTemplate jdbcTemplate;

 private SqlCheckService sqlCheckService;

 @Before
 public void setup() {
 MockitoAnnotations.openMocks(this);
 sqlCheckService = new SqlCheckService();
 sqlCheckService.ruleService = ruleService;
 sqlCheckService.environmentService = environmentService;
 sqlCheckService.jdbcOperations = jdbcOperations;
 sqlCheckService.jdbcTemplate = jdbcTemplate;
 }

 // Test check method with ConnectionSession and SqlCheckReq as parameters
 @Test
 public void testCheck() {
 // Initialize your objects and mock the required methods
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

 // Call the method to test
 List<CheckResult> result = sqlCheckService.check(session, req);

 // Assert the result
 assertEquals(Collections.emptyList(), result);
 }

 // Test check method with Long, String, List<OffsetString>, and ConnectionConfig as parameters
 @Test
 public void testCheckWithLongStringListConnectionConfig() {
 // Initialize your objects and mock the required methods
 Long environmentId = 1L;
 String databaseName = "testdb";
 List<OffsetString> sqls = new ArrayList<>();
 ConnectionConfig config = mock(ConnectionConfig.class);

 when(environmentService.detail(environmentId)).thenReturn(new Environment());
 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(new ArrayList<>());

 // Call the method to test
 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

 // Assert the result
 assertEquals(Collections.emptyList(), result);
 }

 // Test getRules method with List<Rule> and ConnectionSession as parameters
 @Test
 public void testGetRulesWithListRuleConnectionSession() {
 // Initialize your objects and mock the required methods
 List<Rule> rules = new ArrayList<>();
 ConnectionSession session = mock(ConnectionSession.class);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 // Call the method to test
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

 // Assert the result
 assertEquals(Collections.emptyList(), result);
 }

 // Test getRules method with List<Rule>, DialectType, and JdbcOperations as parameters
 @Test
 public void testGetRulesWithListRuleDialectTypeJdbcOperations() {
 // Initialize your objects and mock the required methods
 List<Rule> rules = new ArrayList<>();
 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcOperations.class);

 when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

 // Call the method to test
 List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

 // Assert the result
 assertEquals(Collections.emptyList(), result);
 }

 // Test fullFillRiskLevel method with List<Rule> and List<CheckViolation> as parameters
 @Test
 public void testFullFillRiskLevel() {
 // Initialize your objects and mock the required methods
 List<Rule> rules = new ArrayList<>();
 List<CheckViolation> violations = new ArrayList<>();

 // Call the method to test
 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

 // Assert the result
 assertEquals(Collections.emptyList(), result);
 }
}