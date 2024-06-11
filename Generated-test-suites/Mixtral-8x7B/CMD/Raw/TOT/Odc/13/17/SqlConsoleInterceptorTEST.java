import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.service.session.interceptor.BaseTimeConsumingInterceptor;
import com.oceanbase.odc.service.session.interceptor.SqlConsoleInterceptor;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

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
        ReflectionTestUtils.setField(sqlConsoleInterceptor, "authenticationFacade", authenticationFacade);
        ReflectionTestUtils.setField(sqlConsoleInterceptor, "ruleService", ruleService);
        ReflectionTestUtils.setField(sqlConsoleInterceptor, "sqlConsoleRuleService", sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_allowedSql() {
        // Prepare mock objects and input
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any())).thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        // Call method under test
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Verify and assert
        assertEquals(true, result);
        verify(sqlConsoleRuleService, times(0)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(0)).getByRulesetIdAndName(anyLong(), anyString());
    }

    @Test
    public void testDoPreHandle_forbiddenSql() {
        // Prepare mock objects and input
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any())).thenReturn(Optional.empty());
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);

        // Call method under test
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Verify and assert
        assertEquals(false, result);
        verify(sqlConsoleRuleService, times(0)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(0)).getByRulesetIdAndName(anyLong(), anyString());
    }

    @Test
    public void testDoPreHandle_maxRows() {
        // Prepare mock objects and input
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any())).thenReturn(
                Optional.of(10));

        // Call method under test
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Verify and assert
        assertEquals(true, result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any());
        verify(sqlConsoleRuleService, times(0)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(0)).getByRulesetIdAndName(anyLong(), anyString());
    }

    @Test
    public void testDoPreHandle_maxRowsExceeded() {
        // Prepare mock objects and input
        ConnectionSession session = mock(ConnectionSession.class);
        ConnectionSessionUtil.setRuleSetId(session, 1L);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setQueryLimit(15);
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any())).thenReturn(
                Optional.of(10));

        // Call method under test
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        // Verify and assert
        assertEquals(false, result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any());
        verify(sqlConsoleRuleService, times(0)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(0)).getByRulesetIdAndName(anyLong(), anyString());
    }

    @Test
    public void testDoAfterCompletion_allowedExport() {
        // Prepare mock objects and input
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);

        // Call method under test
        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        // Verify and assert
        assertEquals(true, response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_notAllowedExport() {
        // Prepare mock objects and input
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);

        ConnectionSession session = mock(ConnectionSession.class);
        when(authenticationFacade.currentUser()).thenReturn(createUser(OrganizationType.COMPANY));
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);

        // Call method under test
        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        // Verify and assert
        assertEquals(false, response.isAllowExport());
    }

    private com.oceanbase.odc.service.iam.auth.model.User createUser(OrganizationType organizationType) {
        com.oceanbase.odc.service.iam.auth.model.User user = new com.oceanbase.odc.service.iam.auth.model.User();
        user.setOrganizationType(organizationType);
        return user;
    }
}