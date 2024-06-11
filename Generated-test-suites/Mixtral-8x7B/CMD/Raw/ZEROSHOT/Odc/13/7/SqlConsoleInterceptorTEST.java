package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.oceanbase.odc.core.SessionContext;
import com.oceanbase.odc.core.connection.ConnectionSession;
import com.oceanbase.odc.core.connection.ConnectionSessionUtil;
import com.oceanbase.odc.core.exception.ServiceException;
import com.oceanbase.odc.core.iam.Principal;
import com.oceanbase.odc.core.iam.auth.AuthenticationFacade;
import com.oceanbase.odc.core.iam.auth.CurrentUser;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTree;
import com.oceanbase.odc.core.sql.parser.AbstractSyntaxTreeFactories;
import com.oceanbase.odc.service.iam.auth.AuthenticationFacadeImpl;
import com.oceanbase.odc.service.regulation.ruleset.Rule;
import com.oceanbase.odc.service.regulation.ruleset.RuleService;
import com.oceanbase.odc.service.regulation.ruleset.RuleServiceImpl;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleService;
import com.oceanbase.odc.service.regulation.ruleset.SqlConsoleRuleServiceImpl;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteReq;
import com.oceanbase.odc.service.session.model.SqlAsyncExecuteResp;
import com.oceanbase.odc.service.session.model.SqlExecuteResultImpl;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.odc.service.session.SessionContextImpl;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
public class SqlConsoleInterceptorTest {

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession connectionSession;
    private SessionContext sessionContext;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        authenticationFacade = mock(AuthenticationFacadeImpl.class);
        ruleService = mock(RuleServiceImpl.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleServiceImpl.class);

        connectionSession = mock(ConnectionSession.class);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        sessionContext = new SessionContextImpl();
        sessionContext.put(ConnectionSessionUtil.KEY_SESSION, connectionSession);

        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);
    }

    @Test
    public void testDoPreHandle_noRuleSetId() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertTrue(result);
        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), any());
    }

    @Test
    public void testDoPreHandle_individualOrganization() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertTrue(result);
        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), any());
    }

    @Test
    public void testDoPreHandle_noViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(10));

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertTrue(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, never()).getListProperties(anyLong(), any(), any(), any());
    }

    @Test
    public void testDoPreHandle_maxExecuteSqlsViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.getListProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        sqls.add(new SqlTuplesWithViolation(null, null, null, null));
        sqls.add(new SqlTuplesWithViolation(null, null, null, null));
        response.setSqls(sqls);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePlViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(List.of("SELECT")));
        when(sqlConsoleRuleService.isForbidden(any(), any()))
                .thenReturn(true);

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        sqls.add(new SqlTuplesWithViolation(null, null, null, null, null, "CREATE PROCEDURE", null, null));
        response.setSqls(sqls);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).isForbidden(any(), any());
    }

    @Test
    public void testDoPreHandle_allowSqlTypesViolation() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.getListProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        sqls.add(new SqlTuplesWithViolation(null, null, null, null, null, null, "INSERT", null));
        response.setSqls(sqls);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertFalse(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
    }

    @Test
    public void testDoPreHandle_noViolation_allowExecute() {
        // Given
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.getProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(anyLong(), any(), any(), any()))
                .thenReturn(Optional.of(List.of("SELECT")));

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        sqls.add(new SqlTuplesWithViolation(null, null, null, null));
        response.setSqls(sqls);

        // When
        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession,
                sessionContext.getAttributes());

        // Then
        assertTrue(result);
        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
    }

    @Test
    public void testDoAfterCompletion() {
        // Given
        SqlExecuteResult response = new SqlExecuteResultImpl();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        when(sqlConsoleRuleService.isForbidden(any(), any()))
                .thenReturn(false)
                .thenReturn(true);

        // When
        sqlConsoleInterceptor.doAfterCompletion(response, connectionSession, sessionContext.getAttributes());

        // Then
        assertEquals(SqlExecuteStatus.SUCCESS, response.getStatus());
        verify(connectionSession, times(1)).getOrganizationType();
        verify(sqlConsoleRuleService, times(1)).isForbidden(any(), any());
        verify(response, times(1)).setAllowExport(anyBoolean());
        verify(response, times(1)).getResultSetMetaData();
    }

    @Test
    public void testGetExecuteStageName() {
        // When
        String stageName = sqlConsoleInterceptor.getExecuteStageName();

        // Then
        assertEquals(stageName, SqlExecuteStages.SQL_CONSOLE_RULE);
    }

    @Test
    public void testGetOrder() {
        // When
        int order = sqlConsoleInterceptor.getOrder();

        // Then
        assertEquals(order, Ordered.HIGHEST_PRECEDENCE + 1);
    }
}