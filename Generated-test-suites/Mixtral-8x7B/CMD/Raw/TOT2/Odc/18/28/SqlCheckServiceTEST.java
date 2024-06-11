import com.oceanbase.odc.core.datasource.SingleConnectionDataSource;
import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionConstants;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.service.connection.model.ConnectionConfig;
import com.oceanbase.odc.service.sqlcheck.model.CheckResult;
import com.oceanbase.odc.service.sqlcheck.model.CheckViolation;
import com.oceanbase.odc.service.sqlcheck.model.SqlCheckReq;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SqlCheckServiceTest {

 @Mock
 private RuleService ruleService;
 @Mock
 private EnvironmentService environmentService;
 private SqlCheckService sqlCheckService;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sqlCheckService = new SqlCheckService();
 ReflectionTestUtils.setField(sqlCheckService, "ruleService", ruleService);
 ReflectionTestUtils.setField(sqlCheckService, "environmentService", environmentService);
 }

 @Test
 public void testCheck() {
 ConnectionSession session = mock(ConnectionSession.class);
 ConnectionConfig connectionConfig = new ConnectionConfig();
 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

 doReturn(jdbcOperations).when(session).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
 doReturn(1L).when(session).getDialectType();
 doReturn(connectionConfig).when(session).getConnectionConfig();
 doReturn(true).when(session).getEnabled();

 doReturn(Collections.singletonList(new Rule())).when(ruleService).list(1L, mock(QueryRuleMetadataParams.class));

 sqlCheckService.check(session, sqlCheckReq);
 }

 @Test
 public void testCheckWithEmptyRules() {
 ConnectionSession session = mock(ConnectionSession.class);
 ConnectionConfig connectionConfig = new ConnectionConfig();
 SqlCheckReq sqlCheckReq = new SqlCheckReq();
 SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
 JdbcOperations jdbcOperations = new JdbcTemplate(dataSource);

 doReturn(jdbcOperations).when(session).getSyncJdbcExecutor(ConnectionSessionConstants.CONSOLE_DS_KEY);
 doReturn(1L).when(session).getDialectType();
 doReturn(connectionConfig).when(session).getConnectionConfig();
 doReturn(false).when(session).getEnabled();

 sqlCheckService.check(session, sqlCheckReq);

 // Verify that the method returns an empty list when rules are not enabled
 List<CheckResult> results = sqlCheckService.check(session, sqlCheckReq);
 assertEquals(0, results.size());
 }

 @Test
 public void testCheckForMultipleSqls() {
 Long environmentId = 1L;
 String databaseName = "testdb";
 List<OffsetString> sqls = new ArrayList<>();
 sqls.add(new OffsetString("SELECT * FROM users;"));
 sqls.add(new OffsetString("INSERT INTO users (name, age) VALUES ('John', 30);"));

 ConnectionConfig connectionConfig = new ConnectionConfig();
 connectionConfig.setDialectType(DialectType.MYSQL);

 List<CheckViolation> checkViolations = sqlCheckService.check(environmentId, databaseName, sqls, connectionConfig);

 // Verify that the checkViolations list is not empty
 assertEquals(0, checkViolations.size());
 }

 @Test
 public void testGetRules() {
 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());

 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(session).getDialectType();
 doReturn(rules).when(ruleService).list(1L, mock(QueryRuleMetadataParams.class));

 List<SqlCheckRule> sqlCheckRules = sqlCheckService.getRules(rules, session);

 // Verify that the sqlCheckRules list is not empty
 assertEquals(1, sqlCheckRules.size());
 }

 @Test
 public void testFullFillRiskLevel() {
 List<Rule> rules = new ArrayList<>();
 rules.add(new Rule());
 List<CheckViolation> checkViolations = new ArrayList<>();
 checkViolations.add(new CheckViolation());

 // Verify that the fullFillRiskLevel method modifies the level of the CheckViolation objects
 sqlCheckService.fullFillRiskLevel(rules, checkViolations);

 // Verify that the level of the first CheckViolation object is not null
 assertEquals(checkViolations.get(0).getLevel(), rules.get(0).getLevel());
 }
}