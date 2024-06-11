import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
 public void setup() {
 MockitoAnnotations.initMocks(this);
 }

 // Test method doPreHandle with no ruleSetId and isIndividualTeam is true
 @Test
 public void testDoPreHandleNoRuleSetIdAndIsIndividualTeamTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), eq(Integer.class));
 }

 // Test method doPreHandle with isIndividualTeam is false and NEED\_SQL\_CONSOLE\_CHECK is false
 @Test
 public void testDoPreHandleIsIndividualTeamFalseAndNeedSqlConsoleCheckFalse() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), eq(Integer.class));
 }

 // Test method doPreHandle with isIndividualTeam is false, NEED\_SQL\_CONSOLE\_CHECK is true, and
 // handle returns true
 @Test
 public void testDoPreHandleIsIndividualTeamFalseAndNeedSqlConsoleCheckTrueAndHandleTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

 when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(true);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 }

 // Test method doPreHandle with isIndividualTeam is false, NEED\_SQL\_CONSOLE\_CHECK is true, and
 // handle returns false
 @Test
 public void testDoPreHandleIsIndividualTeamFalseAndNeedSqlConsoleCheckTrueAndHandleFalse() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

 when(sqlConsoleInterceptor.handle(request, response, session, context)).thenReturn(false);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertFalse(result);
 }

 // Test method handle with ruleSetId is null and isIndividualTeam is true
 @Test
 public void testHandleRuleSetIdIsNullAndIsIndividualTeamTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

 assertTrue(result);
 }

 // Test method handle with ruleSetId is not null, isIndividualTeam is false, and queryLimit is not present
 @Test
 public void testHandleRuleSetIdNotNullAndIsIndividualTeamFalseAndQueryLimitNotPresent() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);

 Optional<Integer> queryLimit = Optional.empty();

 when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(DialectType.class),
 any(Integer.class))).thenReturn(queryLimit);

 boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

 assertTrue(result);
 }

 // Test method handle with ruleSetId is not null, isIndividualTeam is false, and queryLimit is present
 @Test
 public void testHandleRuleSetIdNotNullAndIsIndividualTeamFalseAndQueryLimitPresent() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);

 Optional<Integer> queryLimit = Optional.of(100);

 when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(DialectType.class),
 any(Integer.class))).thenReturn(queryLimit);

 boolean result = sqlConsoleInterceptor.handle(request, response, session, context);

 assertTrue(result);
 }

 // Test method determineSqlType when AbstractSyntaxTree is null
 @Test
 public void testDetermineSqlTypeAbstractSyntaxTreeIsNull() {
 SqlTuple sqlTuple = new SqlTuple();
 DialectType dialectType = DialectType.MYSQL;

 SqlType result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 assertEquals(SqlType.OTHERS, result);
 }

 // Test method determineSqlType when AbstractSyntaxTree is not null and SyntaxErrorException is thrown
 @Test
 public void testDetermineSqlTypeAbstractSyntaxTreeIsNotNullAndSyntaxErrorExceptionIsThrown() {
 SqlTuple sqlTuple = new SqlTuple();
 DialectType dialectType = DialectType.MYSQL;

 when(sqlTuple.getAst()).thenThrow(new SyntaxErrorException("Test Syntax Error"));

 SqlType result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 assertEquals(SqlType.UNKNOWN, result);
 }

 // Test method determineSqlType when AbstractSyntaxTree is not null and SyntaxErrorException is not thrown
 @Test
 public void testDetermineSqlTypeAbstractSyntaxTreeIsNotNullAndSyntaxErrorExceptionIsNotThrown() {
 SqlTuple sqlTuple = new SqlTuple();
 DialectType dialectType = DialectType.MYSQL;
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);

 sqlTuple.setAst(ast);
 BasicResult basicResult = new BasicResult(SqlType.SELECT);
 when(ast.getParseResult()).thenReturn(basicResult);

 SqlType result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);

 assertEquals(SqlType.SELECT, result);
 }

 // Test method isIndividualTeam when organizationType is INDIVIDUAL
 @Test
 public void testIsIndividualTeamOrganizationTypeIsIndividual() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 boolean result = sqlConsoleInterceptor.isIndividualTeam();

 assertTrue(result);
 }

 // Test method isIndividualTeam when organizationType is TEAM
 @Test
 public void testIsIndividualTeamOrganizationTypeIsTeam() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);

 boolean result = sqlConsoleInterceptor.isIndividualTeam();

 assertFalse(result);
 }

 // Test method getExecuteStageName
 @Test
 public void testGetExecuteStageName() {
 String result = sqlConsoleInterceptor.getExecuteStageName();

 assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, result);
 }

 // Test method doAfterCompletion with status is not SUCCESS
 @Test
 public void testDoAfterCompletionStatusIsNotSuccess() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.ERROR);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 sqlConsoleInterceptor.doAfterCompletion(response, session, context);

 verify(authenticationFacade, never()).currentUser();
 }

 // Test method doAfterCompletion with status is SUCCESS, isIndividualTeam is true, and isEditable is true
 @Test
 public void testDoAfterCompletionStatusIsSuccessIndividualTeamIsTrueAndIsEditableIsTrue() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setResultSetMetaData(new ResultSetMetaData());
 ResultSetMetaData resultSetMetaData = response.getResultSetMetaData();
 resultSetMetaData.setEditable(true);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 sqlConsoleInterceptor.doAfterCompletion(response, session, context);

 verify(authenticationFacade, times(1)).currentUser();
 verify(resultSetMetaData, never()).setEditable(false);
 }

 // Test method doAfterCompletion with status is SUCCESS, isIndividualTeam is false, and isEditable is false
 @Test
 public void testDoAfterCompletionStatusIsSuccessIndividualTeamIsFalseAndIsEditableIsFalse() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setResultSetMetaData(new ResultSetMetaData());
 ResultSetMetaData resultSetMetaData = response.getResultSetMetaData();
 resultSetMetaData.setEditable(false);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);

 sqlConsoleInterceptor.doAfterCompletion(response, session, context);

 verify(authenticationFacade, times(1)).currentUser();
 verify(resultSetMetaData, times(1)).setEditable(false);
 }

 // Test method doAfterCompletion with status is SUCCESS, isIndividualTeam is false, and isEditable is true
 @Test
 public void testDoAfterCompletionStatusIsSuccessIndividualTeamIsFalseAndIsEditableIsTrue() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 response.setResultSetMetaData(new ResultSetMetaData());
 ResultSetMetaData resultSetMetaData = response.getResultSetMetaData();
 resultSetMetaData.setEditable(true);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();

 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);

 sqlConsoleInterceptor.doAfterCompletion(response, session, context);

 verify(authenticationFacade, times(1)).currentUser();
 verify(resultSetMetaData, never()).setEditable(false);
 }

}