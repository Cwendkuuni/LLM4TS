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

import java.util.Arrays;
import java.util.Collections;
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
import com.oceanbase.odc.core.shared.constant.DialectType;
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
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
public class SqlConsoleInterceptorTest {

    private static final String SQL_1 = "SELECT * FROM table1";
    private static final String SQL_2 = "INSERT INTO table1 (column1, column2) VALUES (1, 'test')";
    private static final String SQL_3 = "CREATE PROCEDURE test_proc () BEGIN END";

    @Autowired
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession session;
    private SqlAsyncExecuteReq request;
    private SqlAsyncExecuteResp response;

    @Before
    public void setUp() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);

        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.authenticationFacade = authenticationFacade;
        sqlConsoleInterceptor.ruleService = ruleService;
        sqlConsoleInterceptor.sqlConsoleRuleService = sqlConsoleRuleService;

        session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        request = new SqlAsyncExecuteReq();
        request.setSqls(Arrays.asList(new SqlTuplesWithViolation(new SqlTuple(SQL_1), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_2), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_3), Collections.emptyList())));

        response = new SqlAsyncExecuteResp(SqlExecuteStatus.SUCCESS,
                Arrays.asList(new SqlTuplesWithViolation(new SqlTuple(SQL_1), Collections.emptyList()),
                        new SqlTuplesWithViolation(new SqlTuple(SQL_2), Collections.emptyList()),
                        new SqlTuplesWithViolation(new SqlTuple(SQL_3), Collections.emptyList())));
    }

    @Test
    public void testDoPreHandle_notTeam() {
        sqlConsoleInterceptor.doPreHandle(request, response, session, new HashMap<>());

        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), anyString(), any(DialectType.class), any());
        verify(sqlConsoleRuleService, never()).getListProperties(anyLong(), anyString(), any(DialectType.class),
                any());
    }

    @Test
    public void testDoPreHandle_notNeedCheck() {
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), anyString(), any(DialectType.class), any());
        verify(sqlConsoleRuleService, never()).getListProperties(anyLong(), anyString(), any(DialectType.class),
                any());
    }

    @Test
    public void testDoPreHandle_maxSqlSizeViolation() {
        when(sqlConsoleRuleService.getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()),
                any(DialectType.class), any())).thenReturn(Optional.of(Arrays.asList("INSERT")));

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()),
                any(DialectType.class), anyInt())).thenReturn(Optional.of(3));

        request.setSqls(Arrays.asList(new SqlTuplesWithViolation(new SqlTuple(SQL_1), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_2), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_3), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_1), Collections.emptyList())));

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(response.getSqls().get(0).getViolatedRules().contains(createRuleViolation(0, 0, 4, 3)));
        assertTrue(response.getSqls().get(1).getViolatedRules().contains(createRuleViolation(1, 0, 4, 3)));
        assertTrue(response.getSqls().get(2).getViolatedRules().contains(createRuleViolation(2, 0, 4, 3)));
        assertTrue(response.getSqls().get(3).getViolatedRules().contains(createRuleViolation(3, 0, 4, 3)));

        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()),
                any(DialectType.class), anyInt());
    }

    @Test
    public void testDoPreHandle_maxSqlSizeNoViolation() {
        when(sqlConsoleRuleService.getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()),
                any(DialectType.class), any())).thenReturn(Optional.of(Arrays.asList("INSERT")));

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()),
                any(DialectType.class), anyInt())).thenReturn(Optional.of(4));

        request.setSqls(Arrays.asList(new SqlTuplesWithViolation(new SqlTuple(SQL_1), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_2), Collections.emptyList()),
                new SqlTuplesWithViolation(new SqlTuple(SQL_3), Collections.emptyList())));

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertFalse(response.getSqls().get(0).getViolatedRules().contains(createRuleViolation(0, 0, 4, 3)));
        assertFalse(response.getSqls().get(1).getViolatedRules().contains(createRuleViolation(1, 0, 4, 3)));
        assertFalse(response.getSqls().get(2).getViolatedRules().contains(createRuleViolation(2, 0, 4, 3)));

        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS.getRuleName()),
                any(DialectType.class), anyInt());
    }

    @Test
    public void testDoPreHandle_maxReturnRowsViolation() {
        when(sqlConsoleRuleService.getProperties(anyLong(), eq(SqlConsoleRules.MAX_RETURN_ROWS.getRuleName()),
                any(DialectType.class), any())).thenReturn(Optional.of(10));

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        request.setQueryLimit(20);

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertEquals(10, request.getQueryLimit().intValue());

        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(),
                eq(SqlConsoleRules.MAX_RETURN_ROWS.getRuleName()), any(DialectType.class), any());
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePl() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session)).thenReturn(true);
        when(sqlConsoleRuleService.getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()),
                any(DialectType.class), any())).thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT")));

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertTrue(response.getSqls().get(2).getViolatedRules().contains(createRuleViolation(2, 0, 4, 0)));

        verify(sqlConsoleRuleService, times(1)).isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session);
        verify(sqlConsoleRuleService, times(1))
                .getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()), any(DialectType.class),
                        any());
    }

    @Test
    public void testDoPreHandle_allowedSqlTypes() {
        when(sqlConsoleRuleService.getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()),
                any(DialectType.class), any())).thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT")));

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        sqlConsoleInterceptor.doPreHandle(request, response, session, context);

        assertFalse(response.getSqls().get(0).getViolatedRules().contains(createRuleViolation(0, 0, 4, 0)));
        assertFalse(response.getSqls().get(1).getViolatedRules().contains(createRuleViolation(1, 0, 4, 0)));
        assertFalse(response.getSqls().get(2).getViolatedRules().contains(createRuleViolation(2, 0, 4, 0)));

        verify(sqlConsoleRuleService, times(1))
                .getListProperties(anyLong(), eq(SqlConsoleRules.ALLOW_SQL_TYPES.getRuleName()), any(DialectType.class),
                        any());
    }

    @Test
    public void testDoPreHandle_notAllowedExport() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session)).thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(SqlExecuteStatus.SUCCESS, null), session,
                new HashMap<>());

        verify(response, times(1)).setAllowExport(false);

        verify(sqlConsoleRuleService, times(1))
                .isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session);
    }

    @Test
    public void testDoPreHandle_allowedExport() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(new SqlExecuteResult(SqlExecuteStatus.SUCCESS, null), session,
                new HashMap<>());

        verify(response, times(1)).setAllowExport(true);

        verify(sqlConsoleRuleService, times(1))
                .isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session);
    }

    private Rule createRuleViolation(int sqlIndex, int offset, int length, int ruleId) {
        Rule rule = new Rule();
        rule.setViolation(new RuleViolation(null, null, offset, 0, length));
        rule.setId(ruleId);
        return rule;
    }

}