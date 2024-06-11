import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.Ordered;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteResult;
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
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

public class SqlConsoleInterceptorTest {

    private SqlConsoleInterceptor interceptor;
    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession session;

    @Before
    public void setup() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        interceptor = spy(new SqlConsoleInterceptor());
        doReturn(authenticationFacade).when(interceptor).authenticationFacade();
        doReturn(ruleService).when(interceptor).ruleService();
        doReturn(sqlConsoleRuleService).when(interceptor).sqlConsoleRuleService();
        session = mock(ConnectionSession.class);
        doReturn(OrganizationType.INDIVIDUAL).when(authenticationFacade).currentUser()
                .getOrganizationType();
        doReturn(null).when(session).getDialectType();
        doReturn(1L).when(session).getId();
        doReturn(ConnectionSessionUtil.createMockConnectionSession(1L)).when(interceptor).connectionSession();
    }

    @Test
    public void testGetOrder() {
        assertEquals(2, interceptor.getOrder());
    }

    @Test
    public void testDoPreHandle_not_individual_team() {
        doReturn(Optional.of(1L)).when(ConnectionSessionUtil).getRuleSetId(session);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        doReturn(true).when(interceptor).isIndividualTeam();

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
        verify(interceptor, times(1)).isIndividualTeam();
        verify(interceptor, times(0)).handle(any(), any(), any(), anyMap());
    }

    @Test
    public void testDoPreHandle_with_rule_set_id() {
        Long ruleSetId = 1L;
        doReturn(Optional.of(ruleSetId)).when(ConnectionSessionUtil).getRuleSetId(session);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        doReturn(false).when(interceptor).isIndividualTeam();

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
        verify(interceptor, times(1)).isIndividualTeam();
        verify(interceptor, times(1)).handle(any(), any(), any(), anyMap());
        verify(sqlConsoleRuleService, times(2)).getProperties(eq(ruleSetId), anyString(), any(), any());
    }

    @Test
    public void testDoPreHandle_without_rule_set_id() {
        doReturn(Optional.ofNullable(null)).when(ConnectionSessionUtil).getRuleSetId(session);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertTrue(result);
        verify(interceptor, times(1)).isIndividualTeam();
        verify(interceptor, times(0)).handle(any(), any(), any(), anyMap());
    }

    @Test
    public void testDoPreHandle_handle_returns_false() {
        Long ruleSetId = 1L;
        doReturn(Optional.of(ruleSetId)).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(false).when(interceptor).isIndividualTeam();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.getProperties(eq(ruleSetId), anyString(), any(), any()))
                .thenReturn(Optional.of(1));
        doReturn(false).when(interceptor).handle(any(), any(), any(), anyMap());

        boolean result = interceptor.doPreHandle(request, response, session, context);

        assertFalse(result);
        verify(interceptor, times(1)).isIndividualTeam();
        verify(sqlConsoleRuleService, times(1)).getProperties(eq(ruleSetId), anyString(), any(), any());
        verify(interceptor, times(1)).handle(any(), any(), any(), anyMap());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandle() {
        Long ruleSetId = 1L;
        doReturn(Optional.of(ruleSetId)).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(false).when(interceptor).isIndividualTeam();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.getProperties(eq(ruleSetId), anyString(), any(), any()))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(false);
        when(sqlConsoleRuleService.getListProperties(eq(ruleSetId), anyString(), any(), any()))
                .thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT")));

        interceptor.handle(request, response, session, context);

        verify(interceptor, times(1)).handle(any(), any(), any(), anyMap());
        verify(sqlConsoleRuleService, times(2)).getProperties(eq(ruleSetId), anyString(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(eq(ruleSetId), anyString(), any(), any());
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(ruleSetId), anyString());
        verify(response, times(1)).setViolatedRules(anyList());
    }

    @Test
    public void testHandle_isForbidden() {
        Long ruleSetId = 1L;
        doReturn(Optional.of(ruleSetId)).when(ConnectionSessionUtil).getRuleSetId(session);
        doReturn(false).when(interceptor).isIndividualTeam();
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        when(sqlConsoleRuleService.getProperties(eq(ruleSetId), anyString(), any(), any()))
                .thenReturn(Optional.of(1));
        when(sqlConsoleRuleService.isForbidden(any(), any())).thenReturn(true);

        interceptor.handle(request, response, session, context);

        verify(interceptor, times(1)).handle(any(), any(), any(), anyMap());
        verify(sqlConsoleRuleService, times(1)).getProperties(eq(ruleSetId), anyString(), any(), any());
        verify(sqlConsoleRuleService, times(0)).getListProperties(eq(ruleSetId), anyString(), any(), any());
        verify(ruleService, times(1)).getByRulesetIdAndName(eq(ruleSetId), anyString());
        verify(response, times(1)).setViolatedRules(anyList());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, interceptor.getExecuteStageName());
    }

    @Test
    public void testDoAfterCompletion() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        doReturn(true).when(interceptor).isIndividualTeam();

        interceptor.doAfterCompletion(response, session, new HashMap<>());

        verify(interceptor, times(1)).doAfterCompletion(any(), any(), anyMap());
        verify(response).setAllowExport(anyBoolean());
    }

    @Test
    public void testDoAfterCompletion_not_individual_team() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        doReturn(false).when(interceptor).isIndividualTeam();

        interceptor.doAfterCompletion(response, session, new HashMap<>());

        verify(interceptor, times(1)).doAfterCompletion(any(), any(), anyMap());
        verify(response, times(1)).setAllowExport(false);
    }

    @Test
    public void testDoAfterCompletion_not_success() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.FAILURE);
        doReturn(false).when(interceptor).isIndividualTeam();

        interceptor.doAfterCompletion(response, session, new HashMap<>());

        verify(interceptor, times(1)).doAfterCompletion(any(), any(), anyMap());
        verify(response, times(0)).setAllowExport(anyBoolean());
    }

    @Test
    public void testIsIndividualTeam() {
        assertTrue(interceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_syntax_error() throws SyntaxErrorException {
        SqlTuple sqlTuple = new SqlTuple();
        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(ast.getParseResult()).thenThrow(new SyntaxErrorException("test error"));
        sqlTuple.setAst(ast);
        sqlTuple.setOriginalSql("test sql");

        BasicResult result = interceptor.determineSqlType(sqlTuple, null);

        assertEquals(SqlType.UNKNOWN, result.getSqlType());
        assertTrue(result.isSyntaxError());
    }

    @Test
    public void testDetermineSqlType_other_dialect() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("test sql");
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.OTHERS, result.getSqlType());
    }

    @Test
    public void testDetermineSqlType_select() {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM dual");
        BasicResult result = interceptor.determineSqlType(sqlTuple, DialectType.ORACLE);

        assertEquals(SqlType.SELECT, result.getSqlType());
    }

    @Test
    public void testGetOrder_should_return_2() {
        assertEquals(2, interceptor.getOrder());
    }

}