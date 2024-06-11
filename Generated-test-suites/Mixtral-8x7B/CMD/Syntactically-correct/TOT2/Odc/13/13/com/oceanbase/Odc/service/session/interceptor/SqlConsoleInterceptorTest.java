import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class SqlConsoleInterceptorTest {

@Mock
private AuthenticationFacade authenticationFacade;

private SqlConsoleInterceptor sqlConsoleInterceptor;

@BeforeEach
public void init() {
MockitoAnnotations.initMocks(this);
sqlConsoleInterceptor = new SqlConsoleInterceptor();
sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
}

@Test
public void testDoPreHandle_whenRuleSetIdIsNull_thenReturnsTrue() {
SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
Map<String, Object> context = new HashMap<>();
ConnectionSession session = mock(ConnectionSession.class);
when(session.getRuleSetId()).thenReturn(null);

boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
Assertions.assertTrue(result);
}

@Test
public void testDoPreHandle_whenIsIndividualTeamIsTrue_thenReturnsTrue() {
SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
Map<String, Object> context = new HashMap<>();
ConnectionSession session = mock(ConnectionSession.class);
when(authenticationFacade.currentUser().getOrganizationType())
.thenReturn(OrganizationType.INDIVIDUAL);
when(session.getRuleSetId()).thenReturn(1L);

boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
Assertions.assertTrue(result);
}

@Test
public void testDoPreHandle_whenRuleSetIdIsNotNullAndIsNotIndividualTeam_thenReturnsInterceptedResult() {
SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
Map<String, Object> context = new HashMap<>();
ConnectionSession session = mock(ConnectionSession.class);
when(authenticationFacade.currentUser().getOrganizationType())
.thenReturn(OrganizationType.ORGANIZATION);
when(session.getRuleSetId()).thenReturn(1L);
when(session.getDialectType()).thenReturn(DialectType.MYSQL);

when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
.thenReturn(Optional.of(10));

when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
.thenReturn(false);

List<SqlTuplesWithViolation> sqlTuples = new ArrayList<>();
SqlTuplesWithViolation sqlTupleWithViolation = new SqlTuplesWithViolation();
sqlTupleWithViolation.setViolatedRules(null);
sqlTuples.add(sqlTupleWithViolation);

response.setSqls(sqlTuples);

boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);
Assertions.assertTrue(result);
Assertions.assertEquals(1, context.size());
Assertions.assertEquals(true, context.get(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED));
}

@Test
public void testDoAfterCompletion_whenStatusIsSuccessAndIsIndividualTeamIsTrue_thenAllowExport() {
SqlExecuteResult response = new SqlExecuteResult();
response.setStatus(SqlExecuteStatus.SUCCESS);
ConnectionSession session = mock(ConnectionSession.class);
when(authenticationFacade.currentUser().getOrganizationType())
.thenReturn(OrganizationType.INDIVIDUAL);
when(session.getRuleSetId()).thenReturn(1L);

sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
Assertions.assertTrue(response.isAllowExport());
}

@Test
public void testDoAfterCompletion_whenStatusIsSuccessAndIsIndividualTeamIsFalse_thenAllowExportBasedOnRules() {
SqlExecuteResult response = new SqlExecuteResult();
response.setStatus(SqlExecuteStatus.SUCCESS);
ConnectionSession session = mock(ConnectionSession.class);
when(authenticationFacade.currentUser().getOrganizationType())
.thenReturn(OrganizationType.ORGANIZATION);
when(session.getRuleSetId()).thenReturn(1L);

when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
.thenReturn(false);

sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
Assertions.assertTrue(response.isAllowExport());

when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
.thenReturn(true);

sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());
Assertions.assertFalse(response.isAllowExport());
}

@Test
public void testIsIndividualTeam_whenOrganizationTypeIsIndividual_thenReturnsTrue() {
Assertions.assertTrue(sqlConsoleInterceptor.isIndividualTeam(OrganizationType.INDIVIDUAL));
}

@Test
public void testIsIndividualTeam_whenOrganizationTypeIsNotIndividual_thenReturnsFalse() {
Assertions.assertFalse(sqlConsoleInterceptor.isIndividualTeam(OrganizationType.ORGANIZATION));
}

@Test
public void testDetermineSqlType_whenSyntaxError_thenReturnsSyntaxErrorAndSqlTypeUnknown() {
SqlTuple sqlTuple = new SqlTuple();
AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
when(ast.getParseResult()).thenThrow(new SyntaxErrorException("Syntax error"));
sqlTuple.setAst(ast);

BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
Assertions.assertTrue(result.isSyntaxError());
Assertions.assertEquals(SqlType.UNKNOWN, result.getSqlType());
}

@Test
public void testDetermineSqlType_whenNoSyntaxError_thenReturnsSqlType() {
SqlTuple sqlTuple = new SqlTuple();
AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));
sqlTuple.setAst(ast);

BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
Assertions.assertFalse(result.isSyntaxError());
Assertions.assertEquals(SqlType.SELECT, result.getSqlType());
}

}