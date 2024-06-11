import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.result.BasicResult;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

@Component
public class SqlConsoleInterceptorTest {

 @Mock
 private AuthenticationFacade authenticationFacade;
 @Mock
 private RuleService ruleService;
 @Mock
 private SqlConsoleRuleService sqlConsoleRuleService;

 private SqlConsoleInterceptor sqlConsoleInterceptor;

 @Before
 public void setUp() {
 MockitoAnnotations.openMocks(this);
 sqlConsoleInterceptor = new SqlConsoleInterceptor();
 sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
 sqlConsoleInterceptor.setRuleService(ruleService);
 sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
 }

 @Test
 public void testDoPreHandle_noRuleSetId_returnsTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.NORMAL));

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, new ConnectionSession(), context);

 Assert.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_individualTeam_returnsTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.INDIVIDUAL));

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, new ConnectionSession(), context);

 Assert.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_ruleSetIdNotExists_returnsTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.NORMAL));
 when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(null);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, new ConnectionSession(), context);

 Assert.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_notForbiddenToCreatePl_returnsTrue() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.NORMAL));
 when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, any(ConnectionSession.class)))
 .thenReturn(false);

 boolean result = sqlConsoleInterceptor.doPreHandle(request, response, new ConnectionSession(), context);

 Assert.assertTrue(result);
 }

 @Test
 public void testDoPreHandle_forbiddenToCreatePl_throwsException() {
 SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
 SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.NORMAL));
 when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, any(ConnectionSession.class)))
 .thenReturn(true);

 try {
 sqlConsoleInterceptor.doPreHandle(request, response, new ConnectionSession(), context);
 Assert.fail("Expected exception not thrown");
 } catch (Exception e) {
 Assert.assertEquals("PL creation is forbidden", e.getMessage());
 }
 }

 @Test
 public void testDoPreHandle_notForbiddenToEditResultSet_returnsTrue() {
 SqlExecuteResult response = new SqlExecuteResult();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.NORMAL));
 when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, any(ConnectionSession.class)))
 .thenReturn(false);

 sqlConsoleInterceptor.doAfterCompletion(response, new ConnectionSession(), context);

 Assert.assertTrue(response.isAllowExport());
 }

 @Test
 public void testDoPreHandle_forbiddenToEditResultSet_returnsFalse() {
 SqlExecuteResult response = new SqlExecuteResult();
 Map<String, Object> context = new HashMap<>();

 when(authenticationFacade.currentUser()).thenReturn(getUserWithOrganizationType(OrganizationType.NORMAL));
 when(ConnectionSessionUtil.getRuleSetId(any(ConnectionSession.class))).thenReturn(1L);
 when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, any(ConnectionSession.class)))
 .thenReturn(true);

 sqlConsoleInterceptor.doAfterCompletion(response, new ConnectionSession(), context);

 Assert.assertFalse(response.isAllowExport());
 }

 private User getUserWithOrganizationType(OrganizationType organizationType) {
 return new User("testUser", organizationType);
 }

 static class User {
 private final String username;
 private final OrganizationType organizationType;

 User(String username, OrganizationType organizationType) {
 this.username = username;
 this.organizationType = organizationType;
 }

 public String getUsername() {
 return username;
 }

 public OrganizationType getOrganizationType() {
 return organizationType;
 }
 }
}