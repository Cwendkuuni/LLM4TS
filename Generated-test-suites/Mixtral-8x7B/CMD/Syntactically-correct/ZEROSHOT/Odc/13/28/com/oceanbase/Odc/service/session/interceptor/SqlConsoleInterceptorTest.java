import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
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
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Component
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;

 @Mock
 private ConnectionSession connectionSession;

 @Mock
 private ConnectionSessionUtil connectionSessionUtil;

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

 @Test
 public void testDoPreHandle_noRuleSetId_returnsTrue() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_individualTeam_returnsTrue() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_ruleSetIdNotForbidden_returnsTrue() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(sqlConsoleRuleService.isForbidden(any(SqlConsoleRules.class), eq(connectionSession)))
 .thenReturn(false);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_ruleSetIdForbidden_returnsFalse() {
 when(connectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(sqlConsoleRuleService.isForbidden(any(SqlConsoleRules.class), eq(connectionSession)))
 .thenReturn(true);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);
 assertFalse(result);
 }

 @Test
 public void testDoAfterCompletion_success_setsAllowExport() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.SUCCESS);

 sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
 assertTrue(response.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_failure_doesNotSetAllowExport() {
 SqlExecuteResult response = new SqlExecuteResult();
 response.setStatus(SqlExecuteStatus.FAILURE);

 sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, new HashMap<>());
 assertFalse(response.isAllowExport());
 }

 @Test
 public void testIsIndividualTeam_individual_returnsTrue() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 boolean result = sqlConsoleInterceptor.isIndividualTeam();
 assertTrue(result);
 }

 @Test
 public void testIsIndividualTeam_team_returnsFalse() {
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);

 boolean result = sqlConsoleInterceptor.isIndividualTeam();
 assertFalse(result);
 }

 @Test
 public void testDetermineSqlType_validSql_returnsBasicResult() throws SyntaxErrorException {
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 when(ast.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setAst(ast);
 DialectType dialectType = DialectType.MYSQL;

 BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
 assertEquals(SqlType.SELECT, result.getSqlType());
 }

 @Test
 public void testDetermineSqlType_invalidSql_returnsBasicResultWithSyntaxError()
 throws SyntaxErrorException {
 AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
 when(ast.getParseResult()).thenThrow(new SyntaxErrorException("error"));

 SqlTuple sqlTuple = new SqlTuple();
 sqlTuple.setAst(ast);
 DialectType dialectType = DialectType.MYSQL;

 BasicResult result = sqlConsoleInterceptor.determineSqlType(sqlTuple, dialectType);
 assertFalse(result.isSyntaxError());
 assertEquals(SqlType.UNKNOWN, result.getSqlType());
 }
}