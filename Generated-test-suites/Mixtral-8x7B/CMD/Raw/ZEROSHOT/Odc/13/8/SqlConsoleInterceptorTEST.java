import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.Ordered;

import com.oceanbase.odc.core.session.ConnectionSession;
import com.oceanbase.odc.core.session.ConnectionSessionUtil;
import com.oceanbase.odc.core.shared.constant.DialectType;
import com.oceanbase.odc.core.sql.execute.SqlExecuteStages;
import com.oceanbase.odc.core.sql.execute.model.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.model.SqlTuple;
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

@RunWith(MockitoJUnitRunner.class)
public class SqlConsoleInterceptorTest {

    @InjectMocks
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Mock
    private AuthenticationFacade authenticationFacade;

    @Mock
    private RuleService ruleService;

    @Mock
    private SqlConsoleRuleService sqlConsoleRuleService;

    private ConnectionSession connectionSession;

    @Before
    public void setup() {
        connectionSession = mock(ConnectionSession.class);
        ConnectionSessionUtil.setRuleSetId(connectionSession, 1L);
    }

    @Test
    public void testDoPreHandle_notNeedSqlConsoleCheck() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, never()).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, never()).getByRulesetIdAndName(anyLong(), any());
        verify(authenticationFacade, never()).currentUser();
        verify(connectionSession, never()).getDialectType();
        verify(connectionSession, never()).getOrganizationType();
        verify(response, never()).setQueryLimit(anyInt());
        verify(context, never()).put(anyString(), anyBoolean());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_individualTeam() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        verify(sqlConsoleRuleService, never()).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, never()).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, never()).getByRulesetIdAndName(anyLong(), any());
        verify(authenticationFacade, never()).currentUser();
        verify(connectionSession, never()).getDialectType();
        verify(connectionSession, times(1)).getOrganizationType();
        verify(response, never()).setQueryLimit(anyInt());
        verify(context, never()).put(anyString(), anyBoolean());

        assertTrue(result);
    }

    @Test
    public void testDoPreHandle_notAllowedCreatePl() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, connectionSession))
                .thenReturn(true);
        when(ruleService.getByRulesetIdAndName(anyLong(), any())).thenReturn(Optional.of(new Rule()));

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuplesWithViolation sql = new SqlTuplesWithViolation();
        sql.setSqlTuple(new SqlTuple());
        sql.setViolatedRules(new ArrayList<>());
        sqls.add(sql);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(sqls);

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(1)).getByRulesetIdAndName(anyLong(), any());
        verify(authenticationFacade, times(1)).currentUser();
        verify(connectionSession, times(1)).getDialectType();
        verify(connectionSession, times(1)).getOrganizationType();
        verify(response, times(1)).setQueryLimit(anyInt());
        verify(context, times(1)).put(eq(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED), eq(true));
        verify(sql.getSqlTuple(), times(1)).setAst(any(AbstractSyntaxTree.class));

        assertTrue(result);
        assertTrue(response.isIntercepted());
    }

    @Test
    public void testDoPreHandle_forbiddenToExecute() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXECUTE_SQLS, connectionSession))
                .thenReturn(true);
        when(ruleService.getByRulesetIdAndName(anyLong(), any()))
                .thenReturn(Optional.of(new Rule()));

        List<SqlTuplesWithViolation> sqls = new ArrayList<>();
        SqlTuplesWithViolation sql = new SqlTuplesWithViolation();
        sql.setSqlTuple(new SqlTuple());
        sql.setViolatedRules(new ArrayList<>());
        sqls.add(sql);
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        request.setSqls(sqls);

        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();

        Map<String, Object> context = new HashMap<>();
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);

        boolean result = sqlConsoleInterceptor.doPreHandle(request, response, connectionSession, context);

        verify(sqlConsoleRuleService, times(1)).getProperties(anyLong(), any(), any(), any());
        verify(sqlConsoleRuleService, times(1)).getListProperties(anyLong(), any(), any(), any());
        verify(ruleService, times(1)).getByRulesetIdAndName(anyLong(), any());
        verify(authenticationFacade, times(1)).currentUser();
        verify(connectionSession, times(1)).getDialectType();
        verify(connectionSession, times(1)).getOrganizationType();
        verify(response, never()).setQueryLimit(anyInt());
        verify(context, times(1)).put(eq(SqlConsoleInterceptor.SQL_CONSOLE_INTERCEPTED), eq(true));
        verify(sql.getSqlTuple(), never()).setAst(any(AbstractSyntaxTree.class));

        assertTrue(result);
        assertTrue(response.isIntercepted());
    }

    @Test
    public void testDoAfterCompletion_notAllowedEditResultSet() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(true);

        SqlExecuteResult result = new SqlExecuteResult();

        sqlConsoleInterceptor.doAfterCompletion(result, connectionSession, new HashMap<>());

        verify(result.getResultSetMetaData(), times(1)).setEditable(false);
    }

    @Test
    public void testDoAfterCompletion_notAllowedExportResultSet() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(true);

        SqlExecuteResult result = new SqlExecuteResult();

        sqlConsoleInterceptor.doAfterCompletion(result, connectionSession, new HashMap<>());

        assertFalse(result.isAllowExport());
    }

    @Test
    public void testGetOrder() {
        assertEquals(Ordered.LOWEST_PRECEDENCE, sqlConsoleInterceptor.getOrder());
    }

    @Test
    public void testIsIndividualTeam_individual() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);

        assertTrue(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testIsIndividualTeam_team() {
        when(connectionSession.getOrganizationType()).thenReturn(OrganizationType.TEAM);

        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDetermineSqlType_parseSuccess() throws SyntaxErrorException {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM users;");

        AbstractSyntaxTree ast = mock(AbstractSyntaxTree.class);
        when(ast.getParseResult().getSqlType()).thenReturn(SqlType.SELECT);
        sqlTuple.initAst(ast);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.SELECT, basicResult.getSqlType());
        assertFalse(basicResult.isSyntaxError());
    }

    @Test
    public void testDetermineSqlType_parseFailure() throws SyntaxErrorException {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM users;");

        when(sqlConsoleInterceptor.getExecuteStageName()).thenReturn(SqlExecuteStages.FORMAT);

        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);

        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
        assertTrue(basicResult.isSyntaxError());
    }
}