package com.oceanbase.odc.service.sqlcheck;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
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
class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;

 @Mock
 private EnvironmentService environmentService;

 @InjectMocks
 private SqlCheckService sqlCheckService;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 doReturn(1L).when(session).getRuleSetId();

 List<Rule> rules = new ArrayList<>();
 doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);

 Checker sqlChecker = mock(Checker.class);
 doReturn(sqlChecker).when(sqlCheckService).newSqlChecker(any(DialectType.class), any(String.class), anyList());

 List<CheckViolation> checkViolations = new ArrayList<>();
 doReturn(checkViolations).when(sqlChecker).check(anyString());

 List<CheckResult> checkResults = sqlCheckService.check(session, req);

 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheck_withViolations() {
 ConnectionSession session = mock(ConnectionSession.class);
 SqlCheckReq req = mock(SqlCheckReq.class);

 doReturn(1L).when(session).getRuleSetId();

 List<Rule> rules = new ArrayList<>();
 rules.add(mock(Rule.class));
 doReturn(rules).when(ruleService).list(anyLong(), any(QueryRuleMetadataParams.class));

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 sqlCheckRules.add(mock(SqlCheckRule.class));
 doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, session);

 Checker sqlChecker = mock(Checker.class);
 doReturn(sqlChecker).when(sqlCheckService).newSqlChecker(any(DialectType.class), any(String.class), anyList());

 List<CheckViolation> checkViolations = new ArrayList<>();
 checkViolations.add(mock(CheckViolation.class));
 doReturn(checkViolations).when(sqlChecker).check(anyString());

 List<CheckResult> checkResults = sqlCheckService.check(session, req);

 assertEquals(1, checkResults.size());
 assertEquals(checkViolations.size(), checkResults.get(0).getViolations().size());
 }

 @Test
 public void testCheck_withNullSession() {
 SqlCheckReq req = mock(SqlCheckReq.class);

 List<CheckResult> checkResults = sqlCheckService.check(null, req);

 assertEquals(0, checkResults.size());
 }

 @Test
 public void testCheck_withNullReq() {
 ConnectionSession session = mock(ConnectionSession.class);

 List<CheckResult> checkResults = sqlCheckService.check(session, null);

 assertEquals(0, checkResults.size());
 }

 @Test
 public void testNewSqlChecker() {
 DialectType dialectType = DialectType.MYSQL;
 String delimiter = ";";
 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();

 Checker sqlChecker = sqlCheckService.newSqlChecker(dialectType, delimiter, sqlCheckRules);

 assertNotNull(sqlChecker);
 }

 @Test
 public void testGetRules() {
 List<Rule> rules = new ArrayList<>();

 ConnectionSession session = mock(ConnectionSession.class);
 JdbcOperations jdbcOperations = mock(JdbcOperations.class);

 doReturn(jdbcOperations).when(session).getSyncJdbcExecutor(anyString());

 List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
 doReturn(sqlCheckRules).when(sqlCheckService).getRules(rules, dialectType, jdbcOperations);

 List<SqlCheckRule> result = sqlCheckService.getRules(rules, session);

 assertEquals(sqlCheckRules, result);
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 rules.add(mock(Rule.class));

 List<CheckViolation> violations = new ArrayList<>();
 violations.add(mock(CheckViolation.class));

 List<Rule> result = sqlCheckService.fullFillRiskLevel(rules, violations);

 assertEquals(1, result.size());
 assertEquals(RuleViolation.fromCheckViolation(violations.get(0)), result.get(0).getViolation());
 assertEquals(violations.get(0).getLevel(), result.get(0).getLevel());
 }

}

class Checker {

 public Checker(DialectType dialectType, String delimiter, List<SqlCheckRule> sqlCheckRules) {
 }

 public List<CheckViolation> check(String scriptContent) {
 return Collections.emptyList();
 }

}

class SqlCheckRule {

}

class SingleConnectionDataSource extends org.springframework.jdbc.datasource.SingleConnectionDataSource {

}

class OBConsoleDataSourceFactory {

 public OBConsoleDataSourceFactory(ConnectionConfig config, boolean autoCommit, boolean isolated) {
 }

 public SingleConnectionDataSource getDataSource() {
 return mock(SingleConnectionDataSource.class);
 }

 public void resetSchema(String origin) {
 }

}

class SqlCheckContext {

 public SqlCheckContext(long size) {
 }
}

class SqlCheckUtil {

 public static List<CheckResult> buildCheckResults(List<CheckViolation> checkViolations) {
 return Collections.emptyList();
 }

}

class SqlCheckRules {

 public static List<SqlCheckRuleFactory> getAllFactories(DialectType dialectType, JdbcOperations jdbc) {
 return Collections.emptyList();
 }

 public static SqlCheckRule createByRule(List<SqlCheckRuleFactory> candidates, DialectType dialectType, Rule rule)
 throws Exception {
 return null;
 }
}

class QueryRuleMetadataParams {

 public QueryRuleMetadataParams() {
 }

}

class ConnectionSessionUtil {

 public static Long getRuleSetId(ConnectionSession session) {
 return null;
 }
}

class ConnectionConfig {

 public DialectType getDialectType() {
 return null;
 }

}

class CheckViolation {

 public CheckViolation(@NotNull String type, @NotNull String message, int line, int column, @NotNull RuleViolation violation) {
 }

 public String getType() {
 return null;
 }

 public int getLine() {
 return 0;
 }

 public int getColumn() {
 return 0;
 }

 public RuleViolation getViolation() {
 return null;
 }

 public int getLevel() {
 return 0;
 }

 public void setLevel(int level) {
 }
}

class CheckResult {

 public CheckResult(@NotNull String sql, @NotNull List<CheckViolation> violations) {
 }

 public String getSql() {
 return null;
 }

 public List<CheckViolation> getViolations() {
 return null;
 }
}