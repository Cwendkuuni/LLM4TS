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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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

import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.core.sql.parser.result.SyntaxErrorException;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;

@Component
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private RuleService ruleService;

 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 @InjectMocks
 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 }

 @Test
 public void testDoPreHandle_noRuleSetId_returnTrue() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(null).when(session).getRuleSetId();

 // When
 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_individualTeam_returnTrue() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(OrganizationType.INDIVIDUAL.getValue()).when(authenticationFacade).currentUser()
 .getOrganizationType();
 doReturn(session).when(context).get(anyString());

 // When
 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_noNeedSqlConsoleCheck_returnTrue() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());

 // When
 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_sqlConsoleIntercepted_returnSqlConsoleIntercepted() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(true).when(context).put(eq(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED), anyBoolean());

 // When
 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertTrue(result);
 assertEquals(sqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED, context.get(SqlCheckInterceptor.SQL_CHECK_INTERCEPTED));
 }

 @Test
 public void testDoPreHandle_setQueryLimit_limitIsPresent() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());
 doReturn(Optional.of(10)).when(sqlConsoleRuleService).getProperties(anyLong(), any(), any(), any());

 // When
 sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertEquals(10, request.getQueryLimit().intValue());
 }

 @Test
 public void testDoPreHandle_setQueryLimit_limitIsNotPresent() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());
 doReturn(Optional.empty()).when(sqlConsoleRuleService).getProperties(anyLong(), any(), any(), any());

 // When
 sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertNull(request.getQueryLimit());
 }

 @Test
 public void testDoPreHandle_addViolatedRules_maxSqlSizeIsExceeded() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation[2]));
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());
 doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), any(), any(), any());

 // When
 sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 verify(response.getSqls().get(1).getViolatedRules(), times(1)).add(any(Rule.class));
 }

 @Test
 public void testDoPreHandle_noAddViolatedRules_maxSqlSizeIsNotExceeded() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation[2]));
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());
 doReturn(Optional.of(3)).when(sqlConsoleRuleService).getProperties(anyLong(), any(), any(), any());

 // When
 sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 verify(response.getSqls().get(1).getViolatedRules(), never()).add(any(Rule.class));
 }

 @Test
 public void testDoPreHandle_allowExecuteIsFalse_ruleIsPresent() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation[2]));
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());
 doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), any(), any(), any());
 doReturn(true).when(ruleService).getByRulesetIdAndName(anyLong(), anyString()).isPresent();

 // When
 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertFalse(result);
 }

 @Test
 public void testDoPreHandle_allowExecuteIsTrue_ruleIsNotPresent() {
 // Given
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 response.setSqls(Arrays.asList(new SqlTuplesWithViolation[2]));
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 doReturn(1L).when(session).getRuleSetId();
 doReturn(false).when(context).put(anyString(), anyBoolean());
 doReturn(Optional.of(1)).when(sqlConsoleRuleService).getProperties(anyLong(), any(), any(), any());
 doReturn(false).when(ruleService).getByRulesetIdAndName(anyLong(), anyString()).isPresent();

 // When
 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 // Then
 assertTrue(result);
 }

 @Test
 public void testDoAfterCompletion_statusIsSuccess_returnTrue() {
 // Given
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 // When
 boolean result = sqlConsoleInterceptor.doAfterCompletion(response, session, context);

 // Then
 assertTrue(result);
 }

 @Test
 public void testDoAfterCompletion_statusIsNotSuccess_returnFalse() {
 // Given
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.FAILED);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 // When
 boolean result = sqlConsoleInterceptor.doAfterCompletion(response, session, context);

 // Then
 false;
 }

 @Test
 public void testIsIndividualTeam_true() {
 // Given
 AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
 doReturn(OrganizationType.INDIVIDUAL.getValue()).when(authenticationFacade).currentUser()
 .getOrganizationType();

 // When
 boolean result = sqlConsoleInterceptor.isIndividualTeam();

 // Then
 assertTrue(result);
 }

 @Test
 public void testIsIndividualTeam_false() {
 // Given
 AuthenticationFacade authenticationFacade = mock(AuthenticationFacade.class);
 doReturn(OrganizationType.TEAM.getValue()).when(authenticationFacade).currentUser()
 .getOrganizationType();

 // When
 boolean result = sqlConsoleInterceptor.isIndividualTeam();

 // Then
 assertFalse(result);
 }

 @Test
 public void testDetermineSqlType_returnBasicResult() throws SyntaxErrorException {
 // Given
 @SuppressWarnings("unchecked")
 AbstractSyntaxTree<BasicResult> abstractSyntaxTree = mock(AbstractSyntaxTree.class);
 when(abstractSyntaxTree.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));
 AbstractSyntaxTreeFactories abstractSyntaxTreeFactories = mock(AbstractSyntaxTreeFactories.class);
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setAst(abstractSyntaxTree);
 DialectType dialectType = DialectType.MYSQL;

 // When
 BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 // Then
 assertEquals(SqlType.SELECT, result.getSqlType());
 }

 @Test
 public void testDetermineSqlType_returnBasicResultWithSyntaxError() throws SyntaxErrorException {
 // Given
 @SuppressWarnings("unchecked")
 AbstractSyntaxTree<BasicResult> abstractSyntaxTree = mock(AbstractSyntaxTree.class);
 when(abstractSyntaxTree.getParseResult()).thenThrow(new SyntaxErrorException());
 AbstractSyntaxTreeFactories abstractSyntaxTreeFactories = mock(AbstractSyntaxTreeFactories.class);
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setAst(abstractSyntaxTree);
 DialectType dialectType = DialectType.MYSQL;

 // When
 BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 // Then
 assertEquals(SqlType.UNKNOWN, result.getSqlType());
 assertTrue(result.getSyntaxError());
 }
}