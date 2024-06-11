import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleMetadata;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleViolation;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.SqlCheckRules;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.MockitoAnnotations.initMocks;

@ContextConfiguration
public class SqlCheckServiceTest {

private SqlCheckService sqlCheckService;
private RuleService ruleServiceMock;
private EnvironmentService environmentServiceMock;
private ConnectionSession connectionSessionMock;
private SingleConnectionDataSource dataSourceMock;
private JdbcOperations jdbcOperationsMock;
private SqlCheckReq sqlCheckReq;

@Before
public void setup() {
initMocks(this);

ruleServiceMock = Mockito.mock(RuleService.class);
environmentServiceMock = Mockito.mock(EnvironmentService.class);
connectionSessionMock = Mockito.mock(ConnectionSession.class);
dataSourceMock = new SingleConnectionDataSource();
jdbcOperationsMock = new JdbcTemplate(dataSourceMock);

sqlCheckService = new SqlCheckService();
sqlCheckService.setRuleService(ruleServiceMock);
sqlCheckService.setEnvironmentService(environmentServiceMock);
sqlCheckService.setJdbcExecutor(ConnectionSessionUtil.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY, jdbcOperationsMock));

doReturn(DialectType.MYSQL).when(connectionSessionMock).getDialectType();
doReturn(dataSourceMock).when(connectionSessionMock).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
doReturn(connectionSessionMock).when(environmentServiceMock).getConnectionSession();

sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setDelimiter(";");
sqlCheckReq.setScriptContent("select * from user;");
}

@After
public void tearDown() {
dataSourceMock.destroy();
}

@Test
public void testCheck() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null));
rule1.setEnabled(true);
RuleMetadata ruleMetadata2 = new RuleMetadata("rule2", "SQL_CHECK", null);
ruleMetadata2.setLevel("ERROR");
Rule rule2 = new Rule();
rule2.setMetadata(ruleMetadata2);
rule2.setEnabled(true);
rules.add(rule1);
rules.add(rule2);

doReturn(rules).when(ruleServiceMock).list(any(), any());

List<CheckResult> checkResults = sqlCheckService.check(connectionSessionMock, sqlCheckReq);
assertEquals(1, checkResults.size());
CheckViolation checkViolation = checkResults.get(0).getViolations().get(0);
assertEquals("rule2", checkViolation.getType().getName());
assertEquals("ERROR", checkViolation.getLevel());
}

@Test
public void testCheckEmptyRules() {
doReturn(Collections.emptyList()).when(ruleServiceMock).list(any(), any());

List<CheckResult> checkResults = sqlCheckService.check(connectionSessionMock, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Test
public void testCheckEmptySql() {
sqlCheckReq.setScriptContent("");
List<CheckResult> checkResults = sqlCheckService.check(connectionSessionMock, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Test
public void testCheckNoRulesEnabled() {
List<Rule> rules = new ArrayList<>();
Rule rule1 = new Rule();
rule1.setMetadata(new RuleMetadata("rule1", "SQL_CHECK", null));
rule1.setEnabled(false);
rules.add(rule1);

doReturn(rules).when(ruleServiceMock).list(any(), any());

List<CheckResult> checkResults = sqlCheckService.check(connectionSessionMock, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Test
public void testCheckNoDelimiter() {
sqlCheckReq.setDelimiter("");
List<CheckResult> checkResults = sqlCheckService.check(connectionSessionMock, sqlCheckReq);
assertEquals(0, checkResults.size());
}

@Configuration
static class TestConfiguration {

@Bean
@Primary
public SingleConnectionDataSource singleConnectionDataSource() {
return new SingleConnectionDataSource();
}

@Bean
public JdbcOperations jdbcOperations(SingleConnectionDataSource dataSource) {
return new JdbcTemplate(dataSource);
}

@Bean
public SqlCheckService sqlCheckService(RuleService ruleService, EnvironmentService environmentService,
JdbcOperations jdbcOperations) {
SqlCheckService sqlCheckService = new SqlCheckService();
sqlCheckService.setRuleService(ruleService);
sqlCheckService.setEnvironmentService(environmentService);
sqlCheckService.setJdbcExecutor(ConnectionSessionUtil.getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY, jdbcOperations));
return sqlCheckService;
}

@Bean
public RuleService ruleService() {
return Mockito.mock(RuleService.class);
}

@Bean
public EnvironmentService environmentService() {
return Mockito.mock(EnvironmentService.class);
}
}
}