package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
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

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

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

@Slf4j
@Service
@Validated
@SkipAuthorize("inside connect session")
public class SqlCheckService {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 @Mock
 private JdbcOperations jdbcOperations;
 @Mock
 private JdbcTemplate jdbcTemplate;

 @Autowired
 private SqlCheckService sqlCheckService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 sqlCheckService = spy(sqlCheckService);

 ReflectionTestUtils.setField(sqlCheckService, "jdbcOperations", jdbcOperations);
 ReflectionTestUtils.setField(sqlCheckService, "jdbcTemplate", jdbcTemplate);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = new SqlCheckReq();

 when(session.getDialectType()).thenReturn(DialectType.MYSQL);
 doReturn(1L).when(session).getRuleSetId();

 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.getMetadata().setType(RuleType.SQL_CHECK);
 rules.add(rule);

 doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
 sqlCheckRules.add(sqlCheckRule);
 doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);

 CheckViolation violation = new CheckViolation();
 violation.setLevel("ERROR");
 List<CheckViolation> checkViolations = Collections.singletonList(violation);

 DefaultSqlChecker sqlChecker = new DefaultSqlChecker(DialectType.MYSQL, null, sqlCheckRules);
 doReturn(checkViolations).when(sqlChecker).check(anyString());

 List<CheckResult> checkResults = sqlCheckService.check(session, req);

 verify(session, times(1)).getDialectType();
 verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
 verify(sqlCheckService, times(1)).getRules(rules, session);
 verify(sqlChecker, times(1)).check(anyString());

 assertNotNull(checkResults);
 assertEquals(1, checkResults.size());
 assertTrue(checkResults.get(0).getViolations().size() > 0);
 }

 @Test
 public void testCheckWithEnvironment() {
 Long environmentId = 1L;
 String databaseName = "testdb";
 List<OffsetString> sqls = Collections.singletonList(new OffsetString("SELECT * FROM test_table"));
 ConnectionConfig config = new ConnectionConfig();
 config.setDialectType(DialectType.MYSQL);

 Environment environment = new Environment();
 environment.setRulesetId(1L);

 doReturn(environment).when(environmentService).detail(environmentId);

 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.getMetadata().setType(RuleType.SQL_CHECK);
 rules.add(rule);

 doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

 OBConsoleDataSourceFactory factory = mock(OBConsoleDataSourceFactory.class);
 SingleConnectionDataSource dataSource = mock(SingleConnectionDataSource.class);
 JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);

 doReturn(dataSource).when(factory).getDataSource();
 doReturn(jdbcTemplate).when(dataSource).getObject();

 when(factory.resetSchema(any())).thenReturn(factory);

 List<SqlCheckRule> checkRules = new ArrayList<>();
 SqlCheckRule sqlCheckRule = mock(SqlCheckRule.class);
 checkRules.add(sqlCheckRule);
 doReturn(checkRules).when(sqlCheckService).getRules(rules, config.getDialectType(), jdbcTemplate);

 DefaultSqlChecker sqlChecker = new DefaultSqlChecker(config.getDialectType(), null, checkRules);
 List<CheckViolation> checkViolations = new ArrayList<>();
 checkViolations.add(new CheckViolation());
 doReturn(checkViolations).when(sqlChecker).check(anyList(), any(SqlCheckContext.class));

 List<CheckViolation> result = sqlCheckService.check(environmentId, databaseName, sqls, config);

 verify(environmentService, times(1)).detail(environmentId);
 verify(factory, times(1)).resetSchema(any());
 verify(jdbcTemplate, times(1)).execute(anyString());
 verify(sqlCheckService, times(1)).getRules(rules, config.getDialectType(), jdbcTemplate);
 verify(sqlChecker, times(1)).check(anyList(), any(SqlCheckContext.class));

 assertNotNull(result);
 assertEquals(1, result.size());
 }

 @Test
 public void testGetRules() {
 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.getMetadata().setType(RuleType.SQL_CHECK);
 rules.add(rule);

 DialectType dialectType = DialectType.MYSQL;
 JdbcOperations jdbcOperations = mock(JdbcOperations.class);

 List<SqlCheckRuleFactory> candidates = new ArrayList<>();
 SqlCheckRuleFactory factory = mock(SqlCheckRuleFactory.class);
 candidates.add(factory);

 when(SqlCheckRules.getAllFactories(dialectType, jdbcOperations)).thenReturn(candidates);

 List<SqlCheckRule> result = sqlCheckService.getRules(rules, dialectType, jdbcOperations);

 verify(SqlCheckRules, times(1)).getAllFactories(dialectType, jdbcOperations);
 verify(factory, times(rules.size())).create(any(), any(), any());

 assertEquals(rules.size(), result.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 Rule rule = new Rule();
 rule.setMetadata(new RuleMetadata());
 rule.getMetadata().setType(RuleType.SQL_CHECK);
 rule.setLevel("ERROR");
 rules.add(rule);

 List<CheckViolation> violations = new ArrayList<>();
 CheckViolation violation = new CheckViolation();
 violation.setType(rule.getMetadata());
 violations.add(violation);

 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

 assertEquals(1, result.size());
 assertEquals(rule.getLevel(), result.get(0).getLevel());
 }
}