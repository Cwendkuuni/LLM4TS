import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
import com.oceanbase.odc.core.sql.execute.SqlExecuteStatus;
import com.oceanbase.odc.core.sql.execute.SqlTuple;
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

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession session;
    private SqlConsoleInterceptor sqlConsoleInterceptor;

    @Before
    public void setUp() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);
        session = mock(ConnectionSession.class);
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        sqlConsoleInterceptor.setAuthenticationFacade(authenticationFacade);
        sqlConsoleInterceptor.setRuleService(ruleService);
        sqlConsoleInterceptor.setSqlConsoleRuleService(sqlConsoleRuleService);

        when(authenticationFacade.currentUser()).thenReturn(createUser());
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
    }

    private org.springframework.security.core.userdetails.User createUser() {
        return new org.springframework.security.core.userdetails.User("testUser", "testPassword", true, true, true,
                true, new HashMap<>());
    }

    @Test
    public void testIsIndividualTeam() {
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());

        when(session.getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, sqlConsoleInterceptor.getExecuteStageName());
    }

    @Test
    public void testHandleAllowedExecute() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(any(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getProperties(any(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.of(2));

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandleForbiddenCreatePl() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.getSqls().add(createTuplesWithViolation("CREATE PROCEDURE", null));
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(any(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getProperties(any(), eq(SqlConsoleRules.MAX_EXECUTE_SQLS), any(), any()))
                .thenReturn(Optional.of(2));
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, session))
                .thenReturn(true);

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandleForbiddenSqlType() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.getSqls()
                .add(createTuplesWithViolation("SELECT", new BasicResult(SqlType.SELECT, null, null, null, null)));
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(any(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(any(), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(), any()))
                .thenReturn(Optional.of(Arrays.asList(SqlType.SELECT.name())));

        assertFalse(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testHandleSyntaxError() {
        SqlAsyncExecuteReq request = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp response = new SqlAsyncExecuteResp();
        response.getSqls()
                .add(createTuplesWithViolation("SELECT", new BasicResult(SqlType.SELECT, true, null, null, null)));
        Map<String, Object> context = new HashMap<>();

        when(sqlConsoleRuleService.getProperties(any(), eq(SqlConsoleRules.MAX_RETURN_ROWS), any(), any()))
                .thenReturn(Optional.of(10));
        when(sqlConsoleRuleService.getListProperties(any(), eq(SqlConsoleRules.ALLOW_SQL_TYPES), any(), any()))
                .thenReturn(Optional.of(Arrays.asList(SqlType.SELECT.name())));

        assertTrue(sqlConsoleInterceptor.handle(request, response, session, context));
    }

    @Test
    public void testDoAfterCompletionAllowedExport() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(createResultSetMetaData(true));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertTrue(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionForbiddenEditResultSet() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(createResultSetMetaData(false));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(true);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(false);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionForbiddenExportResultSet() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.SUCCESS);
        response.setResultSetMetaData(createResultSetMetaData(true));

        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, session))
                .thenReturn(false);
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, session))
                .thenReturn(true);

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertFalse(response.isAllowExport());
    }

    @Test
    public void testDoAfterCompletionFailure() {
        SqlExecuteResult response = new SqlExecuteResult();
        response.setStatus(SqlExecuteStatus.FAILURE);
        response.setResultSetMetaData(createResultSetMetaData(true));

        sqlConsoleInterceptor.doAfterCompletion(response, session, new HashMap<>());

        assertNull(response.getResultSetMetaData());
    }

    private SqlTuplesWithViolation createTuplesWithViolation(String sql, BasicResult parseResult) {
        SqlTuplesWithViolation tuplesWithViolation = new SqlTuplesWithViolation();
        tuplesWithViolation.setSqlTuple(createTuple(sql, parseResult));
        tuplesWithViolation.setViolatedRules(Collections.emptyList());
        return tuplesWithViolation;
    }

    private SqlTuple createTuple(String sql, BasicResult parseResult) {
        SqlTuple tuple = new SqlTuple();
        tuple.setOriginalSql(sql);
        tuple.setSqlId("testSqlId");
        tuple.setAst(mock(AbstractSyntaxTree.class));
        tuple.setParseResult(parseResult);
        return tuple;
    }

    private Map<String, Object> createResultSetMetaData(boolean editable) {
        Map<String, Object> resultSetMetaData = new HashMap<>();
        resultSetMetaData.put("editable", editable);
        return resultSetMetaData;
    }

}