import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SqlConsoleInterceptorTest {

 @Autowired
 SqlExecuteStages sqlExecuteStages;

 @Autowired
 ConnectionSession connectionSession;

 @Autowired
 ConnectionSessionUtil connectionSessionUtil;

 @Autowired
 AuthenticationFacade authenticationFacade;

 @Autowired
 RuleService ruleService;

 @Autowired
 SqlConsoleRuleService sqlConsoleRuleService;

 @Before
 public void setup() {
 sqlExecuteStages = mock(SqlExecuteStages.class);
 connectionSession = mock(ConnectionSession.class);
 connectionSessionUtil = mock(ConnectionSessionUtil.class);
 authenticationFacade = mock(AuthenticationFacade.class);
 ruleService = mock(RuleService.class);
 sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
 }

 @Test
 public void testDoPreHandle() {
 // Test with null request
 SqlAsyncExecuteReq request = null;
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 boolean result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, new HashMap<>());
 assertFalse(result);

 // Test with non-null request and ruleSetId
 request = new SqlAsyncExecuteReq();
 response = new SqlAsyncExecuteResp();
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);
 result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, new HashMap<>());
 assertTrue(result);

 // Test with ruleSetId and forbidden SQL console check
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
 .thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NEED_SQL_CONSOLE_CHECK, connectionSession))
 .thenReturn(true);
 request.setQueryLimit(15);
 result = new SqlConsoleInterceptor().doPreHandle(request, response, connectionSession, new HashMap<>());
 assertFalse(result);
 }

 @Test
 public void testHandle() {
 // Test with non-null request, response, and session
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 when(connectionSessionUtil.getRuleSetId(session)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
 .thenReturn(Optional.of(10));
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NEED_SQL_CONSOLE_CHECK, session))
 .thenReturn(false);
 request.setQueryLimit(15);
 boolean result = new SqlConsoleInterceptor().handle(request, response, session, context);
 assertTrue(result);

 // Test with forbidden SQL console check
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NEED_SQL_CONSOLE_CHECK, session))
 .thenReturn(true);
 result = new SqlConsoleInterceptor().handle(request, response, session, context);
 assertFalse(result);
 }

 @Test
 public void testDoAfterCompletion() {
 // Test with success status
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);
 ConnectionSession session = mock(ConnectionSession.class);
 Map<String, Object> context = new HashMap<>();
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);
 new SqlConsoleInterceptor().doAfterCompletion(response, session, context);
 assertTrue(response.isAllowExport());

 // Test with failure status
 response.setStatus(SqlExecuteStatus.FAILURE);
 new SqlConsoleInterceptor().doAfterCompletion(response, session, context);
 assertFalse(response.isAllowExport());

 // Test with NOT_ALLOWED_EDIT_RESULTSET forbidden
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
 .thenReturn(true);
 new SqlConsoleInterceptor().doAfterCompletion(response, session, context);
 assertFalse(response.getResultSetMetaData().isEditable());

 // Test with NOT_ALLOWED_EXPORT_RESULTSET forbidden
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
 .thenReturn(false);
 new SqlConsoleInterceptor().doAfterCompletion(response, session, context);
 assertTrue(response.isAllowExport());
 }

 @Test
 public void testIsIndividualTeam() {
 // Test with individual organization type
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);
 assertTrue(new SqlConsoleInterceptor().isIndividualTeam());

 // Test with organization organization type
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.ORGANIZATION);
 assertFalse(new SqlConsoleInterceptor().isIndividualTeam());
 }

 @Test
 public void testDetermineSqlType() {
 // Test with valid SQL tuple and dialect type
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));
 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setAst(ast);
 DialectType dialectType = DialectType.MYSQL;
 BasicResult result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, dialectType);
 assertEquals(SqlType.SELECT, result.getSqlType());

 // Test with invalid SQL tuple and dialect type
 when(ast.getParseResult()).thenThrow(new SyntaxErrorException("Syntax error"));
 result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, dialectType);
 assertEquals(SqlType.UNKNOWN, result.getSqlType());

 // Test with unknown SQL type and dialect type
 when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.UNKNOWN));
 result = new SqlConsoleInterceptor().determineSqlType(sqlTuple, dialectType);
 assertEquals(SqlType.OTHERS, result.getSqlType());
 }
}