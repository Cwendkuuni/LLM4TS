import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
import org.springframework.core.Ordered;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.shared.constant.OrganizationType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.model.Rule;
import com.oceanbase.odc.service.regulation.ruleset.model.RuleViolation;
import com.oceanbase.odc.service.regulation.ruleset.model.SqlConsoleRules;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@SuppressWarnings("unchecked")
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

 @Test
 public void testDoPreHandle_noRuleSetId_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_individualTeam_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.INDIVIDUAL);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_notForbiddenToCreatePl_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.isForbidden(any(Rule.class), any(ConnectionSession.class)))
 .thenReturn(false);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoPreHandle_forbiddenToCreatePl_returnsFalse() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.isForbidden(any(Rule.class), any(ConnectionSession.class)))
 .thenReturn(true);

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertFalse(result);
 }

 @Test
 public void testDoPreHandle_notAllowedSqlTypes_returnsFalse() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.getListProperties(any(Long.class), any(Rule.class), any(DialectType.class),
 any(Class.class)))
 .thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT")));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqlType("UPDATE");
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertFalse(result);
 }

 @Test
 public void testDoPreHandle_allowedSqlTypes_returnsTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.getListProperties(any(Long.class), any(Rule.class), any(DialectType.class),
 any(Class.class)))
 .thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT", "UPDATE")));

 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 request.setSqlType("UPDATE");
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

 assertTrue(result);
 }

 @Test
 public void testDoAfterCompletion_forbiddenToEditResultset_setsEditableFalse() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.isForbidden(any(Rule.class), any(ConnectionSession.class)))
 .thenReturn(true);

 SqlExecuteResult response = new SqlExecuteResult();
 response.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData());

 sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

 assertFalse(response.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_allowedToEditResultset_setsEditableTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.isForbidden(any(Rule.class), any(ConnectionSession.class)))
 .thenReturn(false);

 SqlExecuteResult response = new SqlExecuteResult();
 response.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData());

 sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

 assertTrue(response.getResultSetMetaData().isEditable());
 }

 @Test
 public void testDoAfterCompletion_forbiddenToExport_setsAllowExportFalse() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.isForbidden(any(Rule.class), any(ConnectionSession.class)))
 .thenReturn(true);

 SqlExecuteResult response = new SqlExecuteResult();

 sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

 assertFalse(response.isAllowExport());
 }

 @Test
 public void testDoAfterCompletion_allowedToExport_setsAllowExportTrue() {
 ConnectionSession session = mock(ConnectionSession.class);
 when(authenticationFacade.currentUser().getOrganizationType())
 .thenReturn(OrganizationType.TEAM);
 when(sqlConsoleRuleService.isForbidden(any(Rule.class), any(ConnectionSession.class)))
 .thenReturn(false);

 SqlExecuteResult response = new SqlExecuteResult();

 sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

 assertTrue(response.isAllowExport());
 }

 @Test
 public void testGetOrder_returns2() {
 int order = sqlConsoleInterceptor.getOrder();

 assertEquals(2, order);
 }
}