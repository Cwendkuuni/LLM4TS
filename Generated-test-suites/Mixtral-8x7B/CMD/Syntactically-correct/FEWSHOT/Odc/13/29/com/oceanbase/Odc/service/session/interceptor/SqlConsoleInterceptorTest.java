import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
@Slf4j
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private ConnectionSession session;

 @Mock
 private ConnectionSessionUtil connectionSessionUtil;

 @Mock
 private RuleService ruleService;

 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Captor
 private ArgumentCaptor<List<Rule>> ruleCaptor;

 @Before
 public void setup() {
 MockitoAnnotations.openMocks(this);
 doReturn(connectionSessionUtil).when(session).getConnectionSessionUtil();
 doReturn(Optional.of(1L)).when(connectionSessionUtil).getRuleSetId(session);
 }

 @Test
 public void testDoPreHandle_noRuleSetId() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.setAuthenticationFacade(authenticationFacade);
 interceptor.setRuleService(ruleService);
 interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = interceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 verify(context, never()).put(anyString(), anyBoolean());
 }

 @Test
 public void testDoPreHandle_notIndividualTeam() {
 doReturn(OrganizationType.ORGANIZATION).when(authenticationFacade).currentUser()
 .getOrganizationType();

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.setAuthenticationFacade(authenticationFacade);
 interceptor.setRuleService(ruleService);
 interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = interceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 verify(context, never()).put(anyString(), anyBoolean());
 }

 @Test
 public void testDoPreHandle_ruleSetIdAndNoRules() {
 doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser()
 .getOrganizationType();

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.setAuthenticationFacade(authenticationFacade);
 interceptor.setRuleService(ruleService);
 interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = interceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 verify(context, never()).put(anyString(), anyBoolean());
 }

 @Test
 public void testDoPreHandle_ruleSetIdAndRules() {
 doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser()
 .getOrganizationType();

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.setAuthenticationFacade(authenticationFacade);
 interceptor.setRuleService(ruleService);
 interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 ruleService.getByRulesetIdAndName(eq(1L), anyString()).thenReturn(Optional.of(new Rule()));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = interceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 verify(context, times(1)).put(NEED_SQL_CONSOLE_CHECK, true);
 }

 @Test
 public void testDoPreHandle_ruleSetIdAndRulesWithViolations() {
 doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser()
 .getOrganizationType();

 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 interceptor.setAuthenticationFacade(authenticationFacade);
 interceptor.setRuleService(ruleService);
 interceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.getSqls().add(new SqlTuplesWithViolation(
 new SqlTuple("select * from dual", 0, "", SqlType.SELECT, null, null, null, null),
 Arrays.asList(new RuleViolation("rule1", "desc1"), new RuleViolation("rule2", "desc2"))));
 Map<String, Object> context = new HashMap<>();

 boolean result = interceptor.doPreHandle(request, response, session, context);

 assertFalse(result);
 verify(context, times(1)).put(SQL_CONSOLE_INTERCEPTED, false);
 }

 @Test
 public void testDoAfterCompletion_success() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

 SqlExecuteResult response = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);

 interceptor.doAfterCompletion(response, session, new HashMap<>());

 assertTrue(response.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_notAllowedEditResultSet() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session).thenReturn(true);

 SqlExecuteResult response = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);

 interceptor.doAfterCompletion(response, session, new HashMap<>());

 assertFalse(response.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_notAllowedExportResultSet() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();
 sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session).thenReturn(true);

 SqlExecuteResult response = new SqlExecuteResult(SqlExecuteStatus.SUCCESS);

 interceptor.doAfterCompletion(response, session, new HashMap<>());

 assertFalse(response.isAllowExport());
 }

 @Test
 public void testGetOrder() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

 int result = interceptor.getOrder();

 assertEquals(2, result);
 }

 @Test
 public void testIsIndividualTeam() {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

 boolean result = interceptor.isIndividualTeam();

 assertFalse(result);
 }

 @Test
 public void testDetermineSqlType_success() throws SyntaxErrorException {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

 SqlTuple sqlTuple = new SqlTuple("select * from dual", 0, "", SqlType.SELECT, null, null, null, null);
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));
 sqlTuple.initAst(AbstractSyntaxTreeFactories.getAstFactory(DialectType.MYSQL, 0));
 sqlTuple.setAst(ast);

 BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 assertEquals(SqlType.SELECT, result.getSqlType());
 }

 @Test
 public void testDetermineSqlType_syntaxError() throws SyntaxErrorException {
 SqlConsoleInterceptor interceptor = new SqlConsoleInterceptor();

 SqlTuple sqlTuple = new SqlTuple("select * from d", 0, "", SqlType.SELECT, null, null, null, null);
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 when(ast.getParseResult()).thenThrow(new SyntaxErrorException("syntax error"));
 sqlTuple.initAst(AbstractSyntaxTreeFactories.getAstFactory(DialectType.MYSQL, 0));
 sqlTuple.setAst(ast);

 BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

 assertEquals(SqlType.UNKNOWN, result.getSqlType());
 assertTrue(result.getSyntaxError());
 }
}