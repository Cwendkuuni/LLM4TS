import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.service.collaboration.environment.Environment;
import com.oceanbase.odc.service.collaboration.environment.EnvironmentService;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.session.factory.OBConsoleDataSourceFactory;
import com.oceanbase.odc.service.sqlcheck.SqlCheckReq;
import com.oceanbase.odc.service.sqlcheck.SqlCheckService;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.TestContextUtils;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SqlCheckServiceTest {

private static final String DRIVER_CLASS_NAME = "org.mariadb.jdbc.Driver";
private static final String URL = "jdbc:mariadb://localhost:3306/testdb";
private static final String USER = "testuser";
private static final String PASSWORD = "testpassword";
private static final String SQL_SCRIPT = "SELECT * FROM users;";
private static final String INVALID_SQL_SCRIPT = "INVALID SQL";
private static final long ENVIRONMENT_ID = 1L;
private static final String DATABASE_NAME = "testdb";

private ApplicationContext applicationContext;
private EnvironmentService environmentService;
private RuleService ruleService;
private ConnectionSession connectionSession;

@Autowired
private SqlCheckService sqlCheckService;

@Before
public void setup() {
try {
Driver driver = new com.mysql.cj.jdbc.Driver();
DriverManager.registerDriver(driver);
} catch (SQLException e) {
e.printStackTrace();
}

applicationContext = new AnnotationConfigApplicationContext();
TestContext testContext = new TestContext(applicationContext);
TestContextUtils.injectFields(testContext, this);

environmentService = mock(EnvironmentService.class);
ruleService = mock(RuleService.class);

ConnectionConfig connectionConfig = ConnectionConfig.builder()
        .url(URL)
        .username(USER)
        .password(PASSWORD)
        .databaseName(DATABASE_NAME)
        .dialectType(DialectType.MYSQL)
        .build();

SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
dataSource.setDriverClass(DRIVER_CLASS_NAME);
dataSource.setUrl(URL);
dataSource.setUsername(USER);
dataSource.setPassword(PASSWORD);

ConnectionSessionUtil.setSession(ConnectionSessionConstants.CONSOLE_DS_KEY, connectionConfig, dataSource);

connectionSession = ConnectionSessionUtil.getSession(ConnectionSessionConstants.CONSOLE_DS_KEY);

}

@After
public void teardown() {
DriverManager.deregisterDriver(new com.mysql.cj.jdbc.Driver());
}

@Test
public void testCheckSuccess() {

// Mock environment
Environment environment = new Environment();
environment.setId(ENVIRONMENT_ID);
environment.setRulesetId(1L);
when(environmentService.detail(ENVIRONMENT_ID)).thenReturn(environment);

// Mock rules
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(RuleMetadata.builder().name("rule1").level("ERROR").type(RuleType.SQL_CHECK).build());
rule.setEnabled(true);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

// Perform check
SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent(SQL_SCRIPT);

List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

// Assert
assertEquals(0, checkResults.size());
verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
verifyNoMoreInteractions(ruleService);

}

@Test
public void testCheckFail() {

// Mock environment
Environment environment = new Environment();
environment.setId(ENVIRONMENT_ID);
environment.setRulesetId(1L);
when(environmentService.detail(ENVIRONMENT_ID)).thenReturn(environment);

// Mock rules
List<Rule> rules = new ArrayList<>();
Rule rule = new Rule();
rule.setMetadata(RuleMetadata.builder().name("rule1").level("ERROR").type(RuleType.SQL_CHECK).build());
rule.setEnabled(true);
rules.add(rule);

when(ruleService.list(anyLong(), any(QueryRuleMetadataParams.class))).thenReturn(rules);

// Perform check
SqlCheckReq sqlCheckReq = new SqlCheckReq();
sqlCheckReq.setScriptContent(INVALID_SQL_SCRIPT);

List<CheckResult> checkResults = sqlCheckService.check(connectionSession, sqlCheckReq);

// Assert
assertEquals(1, checkResults.size());
verify(ruleService, times(1)).list(anyLong(), any(QueryRuleMetadataParams.class));
verifyNoMoreInteractions(ruleService);

}

// Add more test cases for different methods and scenarios

}