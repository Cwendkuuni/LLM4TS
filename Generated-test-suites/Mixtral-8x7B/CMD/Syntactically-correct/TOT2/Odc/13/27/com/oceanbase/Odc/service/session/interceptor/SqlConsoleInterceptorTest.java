import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.result.BasicResult;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private Environment environment;

 @InjectMocks
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 private BasicResult basicResult;
 private SqlAsyncExecuteReq sqlAsyncExecuteReq;
 private SqlAsyncExecuteResp sqlAsyncExecuteResp;
 private SqlExecuteResult sqlExecuteResult;
 private SqlTuple sqlTuple;

 @BeforeEach
 public void setup() {
 MockitoAnnotations.openMocks(this);

 basicResult = new BasicResult(SqlType.SELECT);
 sqlAsyncExecuteReq = new SqlAsyncExecuteReq();
 sqlAsyncExecuteResp = new SqlAsyncExecuteResp();
 sqlExecuteResult = new SqlExecuteResult();
 sqlTuple = new SqlTuple();
 sqlTuple.setOriginalSql("SELECT * FROM users;");
 sqlTuple.setAst(mock(AbstractSyntaxTree.class));
 }

 @Test
 public void testDoPreHandle_allowedSql() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlAsyncExecuteResp.setSqls(Collections.singletonList(sqlTuple));
 sqlExecuteResult.setResultSetMetaData(mock(SqlExecuteResult.ResultSetMetaData.class));
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlTuple.getAst().getParseResult()).thenReturn(basicResult);

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, mock(ConnectionSession.class),
 new HashMap<>());

 Assertions.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_disallowedSqlType() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlAsyncExecuteResp.setSqls(Collections.singletonList(sqlTuple));
 sqlExecuteResult.setResultSetMetaData(mock(SqlExecuteResult.ResultSetMetaData.class));
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlTuple.getAst().getParseResult()).thenReturn(basicResult);
 when(environment.getProperty("sqlConsole.allowedSqlTypes")).thenReturn("SELECT, INSERT");

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, mock(ConnectionSession.class),
 new HashMap<>());

 Assertions.assertFalse(result);
 }

 @Test
 public void testDoPreHandle_syntaxError() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlAsyncExecuteResp.setSqls(Collections.singletonList(sqlTuple));
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlTuple.getAst().getParseResult()).thenThrow(new SyntaxErrorException("Syntax error"));

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, mock(ConnectionSession.class),
 new HashMap<>());

 Assertions.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_disallowedCreatePl() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlAsyncExecuteResp.setSqls(Collections.singletonList(sqlTuple));
 sqlExecuteResult.setResultSetMetaData(mock(SqlExecuteResult.ResultSetMetaData.class));
 sqlTuple.setOriginalSql("CREATE PROCEDURE myProcedure();");
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, any(ConnectionSession.class)))
 .thenReturn(true);

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, mock(ConnectionSession.class),
 new HashMap<>());

 Assertions.assertFalse(result);
 }

 @Test
 public void testDoPreHandle_maxExecuteSqls() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlAsyncExecuteResp.setSqls(Arrays.asList(sqlTuple, sqlTuple, sqlTuple));
 sqlExecuteResult.setResultSetMetaData(mock(SqlExecuteResult.ResultSetMetaData.class));
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlConsoleRuleService.getProperties(anyLong(), any(SqlConsoleRules.class), any(DialectType.class), any()))
 .thenReturn(Optional.of(3));

 boolean result = sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, mock(ConnectionSession.class),
 new HashMap<>());

 Assertions.assertFalse(result);
 }

 @Test
 public void testDoAfterCompletion_disallowedEditResultSet() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlExecuteResult.setResultSetMetaData(mock(SqlExecuteResult.ResultSetMetaData.class));
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, any(ConnectionSession.class)))
 .thenReturn(true);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, mock(ConnectionSession.class), new HashMap<>());

 Assertions.assertFalse(sqlExecuteResult.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_disallowedExportResultSet() {
 sqlExecuteResult.setStatus(SqlExecuteStatus.SUCCESS);
 sqlExecuteResult.setResultSetMetaData(mock(SqlExecuteResult.ResultSetMetaData.class));
 when(authenticationFacade.currentUser()).thenReturn(mockUser());
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, any(ConnectionSession.class)))
 .thenReturn(true);

 sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, mock(ConnectionSession.class), new HashMap<>());

 Assertions.assertFalse(sqlExecuteResult.isAllowExport());
 }

 private ConnectionSession.User mockUser() {
 ConnectionSession.User user = new ConnectionSession.User();
 user.setOrganizationType(OrganizationType.ORGANIZATION);
 return user;
 }
}