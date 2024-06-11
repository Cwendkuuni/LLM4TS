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
import com.oceanbase.odc.core.shared.constant.OrganizationType;
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
import com.oceanbase.odc.service.session.model.SqlExecuteResult;
import com.oceanbase.odc.service.session.model.SqlTuplesWithViolation;
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
public class SqlConsoleInterceptorTest {

    @Autowired
    private SqlConsoleInterceptor interceptor;

    private AuthenticationFacade authenticationFacade;
    private RuleService ruleService;
    private SqlConsoleRuleService sqlConsoleRuleService;
    private ConnectionSession session;

    @Before
    public void setUp() {
        authenticationFacade = mock(AuthenticationFacade.class);
        ruleService = mock(RuleService.class);
        sqlConsoleRuleService = mock(SqlConsoleRuleService.class);

        session = mock(ConnectionSession.class);
        when(session.getDialectType()).thenReturn(DialectType.MYSQL);
        when(session.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        ConnectionSessionUtil.setRuleSetId(session, 1L);
    }

    @Test
    public void testDoPreHandle_noRuleSet_true() {
        when(ConnectionSessionUtil.getRuleSetId(session)).thenReturn(null);
        assertTrue(interceptor.doPreHandle(new SqlAsyncExecuteReq(), new SqlAsyncExecuteResp(), session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_individualTeam_true() {
        when(session.getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(interceptor.doPreHandle(new SqlAsyncExecuteReq(), new SqlAsyncExecuteResp(), session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_noViolations_true() {
        when(sqlConsoleRuleService.isForbidden(any(), eq(session))).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), eq(DialectType.MYSQL),
                eq(Integer.class)))
                        .thenReturn(Optional.of(10));

        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setQueryLimit(5);
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        resp.setSqls(
                Collections.singletonList(new SqlTuplesWithViolation(new SqlTuple(), Collections.emptyList())));

        assertTrue(interceptor.doPreHandle(req, resp, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_withViolations_false() {
        when(sqlConsoleRuleService.isForbidden(any(), eq(session))).thenReturn(false);
        when(sqlConsoleRuleService.getProperties(eq(1L), eq(SqlConsoleRules.MAX_RETURN_ROWS), eq(DialectType.MYSQL),
                eq(Integer.class)))
                        .thenReturn(Optional.of(10));

        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        req.setQueryLimit(5);
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = Collections.singletonList(
                new SqlTuplesWithViolation(new SqlTuple(),
                        Collections.singletonList(new RuleViolation(Rule.Level.ERROR, "message"))));
        resp.setSqls(sqls);

        assertFalse(interceptor.doPreHandle(req, resp, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_forbiddenCreatePl_false() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_CREATE_PL, eq(session))).thenReturn(true);
        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = Collections.singletonList(
                new SqlTuplesWithViolation(new SqlTuple("CREATE PROCEDURE"), Collections.emptyList()));
        resp.setSqls(sqls);

        assertFalse(interceptor.doPreHandle(req, resp, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_disallowedSqlType_false() {
        when(sqlConsoleRuleService.getListProperties(eq(1L), eq(SqlConsoleRules.ALLOW_SQL_TYPES), eq(DialectType.MYSQL),
                eq(String.class)))
                        .thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT")));

        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = Collections.singletonList(
                new SqlTuplesWithViolation(new SqlTuple("UPDATE"), Collections.emptyList()));
        resp.setSqls(sqls);

        assertFalse(interceptor.doPreHandle(req, resp, session, new HashMap<>()));
    }

    @Test
    public void testDoPreHandle_successful_true() {
        when(sqlConsoleRuleService.getListProperties(eq(1L), eq(SqlConsoleRules.ALLOW_SQL_TYPES), eq(DialectType.MYSQL),
                eq(String.class)))
                        .thenReturn(Optional.of(Arrays.asList("SELECT", "INSERT", "UPDATE")));

        SqlAsyncExecuteReq req = new SqlAsyncExecuteReq();
        SqlAsyncExecuteResp resp = new SqlAsyncExecuteResp();
        List<SqlTuplesWithViolation> sqls = Collections.singletonList(
                new SqlTuplesWithViolation(new SqlTuple("UPDATE"), Collections.emptyList()));
        resp.setSqls(sqls);

        assertTrue(interceptor.doPreHandle(req, resp, session, new HashMap<>()));
    }

    @Test
    public void testDoAfterCompletion_forbiddenEditResultSet_false() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, eq(session)))
                .thenReturn(true);

        SqlExecuteResult result = new SqlExecuteResult();
        result.setResultSetMetaData(new SqlExecuteResult.ResultSetMetaData(null, true));

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertNull(result.getResultSetMetaData().isEditable());
    }

    @Test
    public void testDoAfterCompletion_allowedExport_true() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, eq(session)))
                .thenReturn(false);

        SqlExecuteResult result = new SqlExecuteResult();

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertTrue(result.isAllowExport());
    }

    @Test
    public void testDoAfterCompletion_forbiddenExport_false() {
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, eq(session)))
                .thenReturn(true);

        SqlExecuteResult result = new SqlExecuteResult();

        interceptor.doAfterCompletion(result, session, new HashMap<>());

        assertFalse(result.isAllowExport());
    }

    @Test
    public void testGetExecuteStageName() {
        assertEquals(SqlExecuteStages.SQL_CONSOLE_RULE, interceptor.getExecuteStageName());
    }

}