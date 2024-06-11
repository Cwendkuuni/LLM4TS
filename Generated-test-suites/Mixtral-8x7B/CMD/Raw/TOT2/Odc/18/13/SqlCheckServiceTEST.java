import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SqlCheckServiceTest {

@InjectMocks
private SqlCheckService sqlCheckService;

@Mock
private RuleService ruleService;

@Mock
private EnvironmentService environmentService;

@Mock
private JdbcOperations jdbcOperations;

@Mock
private JdbcTemplate jdbcTemplate;

@Before
public void setup() {
ReflectionTestUtils.setField(sqlCheckService, "dialectType", DialectType.MYSQL);
}

@Test
public void testCheck() {
ConnectionSession session = createConnectionSession();
SqlCheckReq sqlCheckReq = createSqlCheckReq();

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setEnabled(true);
RuleMetadata ruleMetadata = new RuleMetadata();
ruleMetadata.setType(RuleType.SQL_CHECK);
rule.setMetadata(ruleMetadata);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = new SqlCheckRule();
sqlCheckRule.setEnabled(true);
sqlCheckRules.add(sqlCheckRule);

when(ruleService.getSqlCheckRules(any(), any())).thenReturn(sqlCheckRules);

List<CheckViolation> checkViolations = sqlCheckService.check(session, sqlCheckReq);
assertEquals(0, checkViolations.size());
}

@Test
public void testCheck_withViolations() {
ConnectionSession session = createConnectionSession();
SqlCheckReq sqlCheckReq = createSqlCheckReq();

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setEnabled(true);
RuleMetadata ruleMetadata = new RuleMetadata();
ruleMetadata.setType(RuleType.SQL_CHECK);
rule.setMetadata(ruleMetadata);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = new SqlCheckRule();
sqlCheckRule.setEnabled(true);
sqlCheckRules.add(sqlCheckRule);

when(ruleService.getSqlCheckRules(any(), any())).thenReturn(sqlCheckRules);

when(jdbcOperations.queryForList(anyString(), anyObject(), any(Class.class))).thenReturn(Collections.singletonList(1));

List<CheckViolation> checkViolations = sqlCheckService.check(session, sqlCheckReq);
assertEquals(1, checkViolations.size());
}

@Test
public void testCheck_withCheckRules() {
ConnectionSession session = createConnectionSession();
SqlCheckReq sqlCheckReq = createSqlCheckReq();

List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setEnabled(true);
RuleMetadata ruleMetadata = new RuleMetadata();
ruleMetadata.setType(RuleType.SQL_CHECK);
rule.setMetadata(ruleMetadata);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

List<SqlCheckRule> sqlCheckRules = new ArrayList<>();
SqlCheckRule sqlCheckRule = new SqlCheckRule();
sqlCheckRule.setEnabled(true);
sqlCheckRules.add(sqlCheckRule);

when(ruleService.getSqlCheckRules(any(), any())).thenReturn(sqlCheckRules);

when(jdbcOperations.queryForList(anyString(), anyObject(), any(Class.class))).thenReturn(Collections.singletonList(1));

when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class))).thenReturn(Collections.singletonList(1));

List<CheckViolation> checkViolations = sqlCheckService.check(session, sqlCheckReq);
assertEquals(1, checkViolations.size());
}

private ConnectionSession createConnectionSession() {
ConnectionSession session = new ConnectionSession();
ReflectionTestUtils.setField(session, "id", 1L);
ReflectionTestUtils.setField(session, "userId", 1L);
ReflectionTestUtils.setField(session, "dialectType", DialectType.MYSQL);
ReflectionTestUtils.setField(session, "jdbcExecutorMap", new HashMap<>());
return session;
}

private SqlCheckReq createSqlCheckReq() {
SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setDelimiter(";");
sqlCheckReq.setScriptContent("SELECT * FROM users;");
return sqlCheckReq;
}
}