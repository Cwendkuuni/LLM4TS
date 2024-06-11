import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private ConnectionSession session;

 @Mock
 private ConnectionSessionUtil connectionSessionUtil;

 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Mock
 private RuleService ruleService;

 @InjectMocks
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 private SqlAsyncExecuteResp sqlAsyncExecuteResp;

 @Before
 public void setup() {
 sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 sqlAsyncExecuteResp.setSqls(Collections.singletonList(new SqlTuplesWithViolation()));
 }

 @Test
 public void testDoPreHandle_checkIsNullAndIndividualTeam() {
 Map<String, Object> context = new HashMap<>();
 when(connectionSessionUtil.getRuleSetId(session)).thenReturn(null);

 Boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), sqlAsyncExecuteResp, session,
 context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_notIndividualTeamAndruleSetIdIsNull() {
 Map<String, Object> context = new HashMap<>();
 when(connectionSessionUtil.getRuleSetId(session)).thenReturn(null);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.COMMUNITY);

 Boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), sqlAsyncExecuteResp, session,
 context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_needSqlConsoleCheckIsNull() {
 Map<String, Object> context = new HashMap<>();
 context.put(NEED_SQL_CONSOLE_CHECK, false);

 Boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), sqlAsyncExecuteResp, session,
 context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_sqlConsoleIntercepted() {
 Map<String, Object> context = new HashMap<>();
 context.put(NEED_SQL_CONSOLE_CHECK, true);

 Boolean result = sqlConsoleInterceptor.doPreHandle(mock(SqlAsyncExecuteReq.class), sqlAsyncExecuteResp, session,
 context);

 assertFalse(result);
 context.put(SQL_CONSOLE_INTERCEPTED, result);
 assertTrue(context.get(SQL_CONSOLE_INTERCEPTED) == result);
 }

 @Test
 public void testHandle_queryLimit() {
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation(), new SqlTuplesWithViolation()));

 sqlConsoleInterceptor.handle(mock(SqlAsyncExecuteReq.class), response, session, new HashMap<>());

 assertEquals(response.getSqls().get(0).getQueryLimit(),
 sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class)
 .get());
 }

 @Test
 public void testHandle_maxSqlSize() {
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation(), new SqlTuplesWithViolation()));

 sqlConsoleInterceptor.handle(mock(SqlAsyncExecuteReq.class), response, session, new HashMap<>());

 Optional<Integer> maxSqlSize = sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_EXECUTE_SQLS,
 DialectType.MYSQL, Integer.class);
 assertTrue(maxSqlSize.isPresent());
 assertEquals(maxSqlSize.get(), 1);
 }

 @Test
 public void testHandle_violatedRules() {
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation(), new SqlTuplesWithViolation()));

 sqlConsoleInterceptor.handle(mock(SqlAsyncExecuteReq.class), response, session, new HashMap<>());

 List<Rule> violatedRules = response.getSqls().get(0).getViolatedRules();
 assertEquals(violatedRules.size(), 1);
 }

 @Test
 public void testDoAfterCompletion_forbiddenToCreatePl() {
 SqlExecuteStatus sqlExecuteStatus = new SqlExecuteStatus();
 sqlExecuteStatus.setStatus(SqlExecuteStatus.Status.SUCCESS);
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(sqlExecuteStatus);
 sqlExecuteResult.setResultSetMetaData(new BasicResult(SqlType.CREATE_PL));

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

 assertTrue(sqlExecuteResult.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_notForbiddenToCreatePl() {
 SqlExecuteStatus sqlExecuteStatus = new SqlExecuteStatus();
 sqlExecuteStatus.setStatus(SqlExecuteStatus.Status.SUCCESS);
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(sqlExecuteStatus);
 sqlExecuteResult.setResultSetMetaData(new BasicResult(SqlType.CREATE_PL));

 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
 .thenReturn(false);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

 assertFalse(sqlExecuteResult.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_forbiddenToExport() {
 SqlExecuteStatus sqlExecuteStatus = new SqlExecuteStatus();
 sqlExecuteStatus.setStatus(SqlExecuteStatus.Status.SUCCESS);
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(sqlExecuteStatus);
 sqlExecuteResult.setAllowExport(false);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

 assertFalse(sqlExecuteResult.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_notForbiddenToExport() {
 SqlExecuteStatus sqlExecuteStatus = new SqlExecuteStatus();
 sqlExecuteStatus.setStatus(SqlExecuteStatus.Status.SUCCESS);
 SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
 sqlExecuteResult.setStatus(sqlExecuteStatus);
 sqlExecuteResult.setAllowExport(true);

 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
 .thenReturn(false);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, session, new HashMap<>());

 assertTrue(sqlExecuteResult.isAllowExport());
 }

 @Test
 public void testIsIndividualTeam_individualTeam() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 boolean result = sqlConsoleInterceptor.isIndividualTeam();

 assertTrue(result);
 }

 @Test
 public void testIsIndividualTeam_notIndividualTeam() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.COMMUNITY);

 boolean result = sqlConsoleInterceptor.isIndividualTeam();

 assertFalse(result);
 }

 @Test
 public void testDetermineSqlType_success() throws SyntaxErrorException {
 SqlTuple sqlTuple = mock(SqlTuple.class);
 when(sqlTuple.getAst()).thenReturn(mock(AbstractSyntaxTree.class));
 when(sqlTuple.getOriginalSql()).thenReturn("SELECT * FROM users");

 BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 assertEquals(result.getSqlType(), SqlType.SELECT);
 }

 @Test
 public void testDetermineSqlType_syntaxError() throws SyntaxErrorException {
 SqlTuple sqlTuple = mock(SqlTuple.class);
 when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException("syntax error"));
 when(sqlTuple.getOriginalSql()).thenReturn("SELECT * FROM users");

 BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 assertEquals(result.getSqlType(), SqlType.UNKNOWN);
 assertEquals(result.getSyntaxError(), true);
 }

}