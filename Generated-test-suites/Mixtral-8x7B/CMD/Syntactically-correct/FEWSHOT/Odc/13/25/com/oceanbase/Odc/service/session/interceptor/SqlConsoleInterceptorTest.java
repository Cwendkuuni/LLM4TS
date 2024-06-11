import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private ConnectionSessionUtil connectionSessionUtil;

 @Mock
 private RuleService ruleService;

 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @Mock
 private AbstractSyntaxTreeFactories abstractSyntaxTreeFactories;

 @InjectMocks
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @Before
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDoPreHandle_noRuleSetId_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(null).when(connectionSessionUtil).getRuleSetId(session);

 SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, null, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_individualTeam_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser()
 .getOrganizationType();
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);

 SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, null, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_noNeedSqlConsoleCheck_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);
 doReturn(false).when(context).get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK);

 SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, null, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_maxReturnRows_setsQueryLimit() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);
 doReturn(false).when(context).get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK);

 SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
 Map<String, Object> context = new HashMap<>();

 Optional<Integer> queryLimit = Optional.of(10);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
 .thenReturn(queryLimit);

 sqlConsoleInterceptor.doPreHandle(request, null, session, context);

 verify(request).setQueryLimit(10);
 }

 @Test
 public void testDoPreHandle_forbiddenToCreatePl_violatesRule() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);
 doReturn(false).when(context).get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK);

 SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
 SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
 SqlTuplesWithViolation sqlTuple = mock(SqlTuplesWithViolation.class);
 when(response.getSqls()).thenReturn(Arrays.asList(sqlTuple));
 when(sqlTuple.getViolatedRules()).thenReturn(Collections.emptyList());

 BasicResult basicResult = new BasicResult(SqlType.PL_DDL);
 when(sqlId2BasicResult.get(anyString())).thenReturn(basicResult);

 sqlConsoleInterceptor.sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);

 sqlConsoleInterceptor.handle(request, response, session, context);

 verify(sqlTuple.getViolatedRules(), times(1)).add(any(Rule.class));
 }

 @Test
 public void testDoPreHandle_allowSqlTypes_violatesRule() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);
 doReturn(false).when(context).get(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK);

 SqlAsyncExecuteReq request = mock(SqlAsyncExecuteReq.class);
 SqlAsyncExecuteResp response = mock(SqlAsyncExecuteResp.class);
 SqlTuplesWithViolation sqlTuple = mock(SqlTuplesWithViolation.class);
 when(response.getSqls()).thenReturn(Arrays.asList(sqlTuple));
 when(sqlTuple.getViolatedRules()).thenReturn(Collections.emptyList());

 BasicResult basicResult = new BasicResult(SqlType.CREATE_TABLE);
 when(sqlId2BasicResult.get(anyString())).thenReturn(basicResult);

 sqlConsoleInterceptor.sqlConsoleRuleService.getListProperties(1L, SqlConsoleRules.ALLOW_SQL_TYPES,
 DialectType.MYSQL, String.class);

 sqlConsoleInterceptor.handle(request, response, session, context);

 verify(sqlTuple.getViolatedRules(), times(1)).add(any(Rule.class));
 }

 @Test
 public void testDoAfterCompletion_forbiddenToEditResultSet_setsResultSetEditableFalse() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);

 SqlExecuteResult result = new SqlExecuteResult();
 result.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData());

 sqlConsoleInterceptor.doAfterCompletion(result, session, null);

 assertTrue(result.getResultSetMetaData().isEditable());

 sqlConsoleInterceptor.sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session);

 sqlConsoleInterceptor.doAfterCompletion(result, session, null);

 assertFalse(result.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_forbiddenToExportResultSet_setsAllowExportFalse() {
 ConnectionSession session = mock(ConnectionSession.class);
 doReturn(1L).when(connectionSessionUtil).getRuleSetId(session);

 SqlExecuteResult result = new SqlExecuteResult();

 sqlConsoleInterceptor.doAfterCompletion(result, session, null);

 assertTrue(result.isAllowExport());

 sqlConsoleInterceptor.sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session);

 sqlConsoleInterceptor.doAfterCompletion(result, session, null);

 assertFalse(result.isAllowExport());
 }

 @Test
 public void testIsIndividualTeam_individualTeam_returnsTrue() {
 doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser()
 .getOrganizationType();

 assertTrue(sqlConsoleInterceptor.isIndividualTeam());
 }

 @Test
 public void testIsIndividualTeam_nonIndividualTeam_returnsFalse() {
 doReturn(OrganizationType.ORGANIZATION).when(authenticationFacade).currentUser()
 .getOrganizationType();

 assertFalse(sqlConsoleInterceptor.isIndividualTeam());
 }

 @Test
 public void testDetermineSqlType_success_returnsBasicResult()
 throws SyntaxErrorException {
 SqlTuple sqlTuple = mock(SqlTuple.class);
 DialectType dialectType = DialectType.MYSQL;
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 BasicResult result = mock(BasicResult.class);
 when(sqlTuple.getAst()).thenReturn(ast);
 when(ast.getParseResult()).thenReturn(result);

 when(abstractSyntaxTreeFactories.getAstFactory(dialectType, 0)).thenReturn(mock(AbstractSyntaxTree.class));

 BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 assertEquals(result, basicResult);
 }

 @Test
 public void testDetermineSqlType_exception_returnsBasicResultWithSyntaxError()
 throws SyntaxErrorException {
 SqlTuple sqlTuple = mock(SqlTuple.class);
 DialectType dialectType = DialectType.MYSQL;
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 when(sqlTuple.getAst()).thenReturn(ast);
 when(ast.getParseResult()).thenThrow(new SyntaxErrorException("error"));

 when(abstractSyntaxTreeFactories.getAstFactory(dialectType, 0)).thenReturn(mock(AbstractSyntaxTree.class));

 BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 assertTrue(basicResult.isSyntaxError());
 }
}