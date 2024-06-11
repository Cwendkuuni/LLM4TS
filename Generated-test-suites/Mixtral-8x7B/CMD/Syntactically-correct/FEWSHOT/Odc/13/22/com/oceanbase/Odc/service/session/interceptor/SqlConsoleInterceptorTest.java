package com.oceanbase.odc.service.session.interceptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.oceanbase.tools.dbbrowser.parser.constant.SqlType;
import com.oceanbase.tools.sqlparser.SyntaxErrorException;

@Component
class TestSqlConsoleInterceptor {

    @Autowired
    private AuthenticationFacade authenticationFacade;
    @Autowired
    private RuleService ruleService;
    @Autowired
    private SqlConsoleRuleService sqlConsoleRuleService;

    private SqlConsoleInterceptor sqlConsoleInterceptor;
    private ConnectionSession connectionSession;
    private SqlAsyncExecuteReq sqlAsyncExecuteReq;
    private SqlAsyncExecuteResp sqlAsyncExecuteResp;
    private SqlExecuteResult sqlExecuteResult;
    private AbstractSyntaxTree abstractSyntaxTree;

    @Before
    public void setUp() {
        sqlConsoleInterceptor = new SqlConsoleInterceptor();
        connectionSession = mock(ConnectionSession.class);
        sqlAsyncExecuteReq = mock(SqlAsyncExecuteReq.class);
        sqlAsyncExecuteResp = mock(SqlAsyncExecuteResp.class);
        sqlExecuteResult = mock(SqlExecuteResult.class);
        abstractSyntaxTree = mock(AbstractSyntaxTree.class);

        when(connectionSession.getDialectType()).thenReturn(DialectType.MYSQL);
        when(sqlExecuteResult.getStatus()).thenReturn(SqlExecuteStatus.SUCCESS);
        when(sqlAsyncExecuteResp.getSqls()).thenReturn(Arrays.asList(new SqlTuplesWithViolation()));
    }

    @Test
    public void testIsIndividualTeam() {
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(sqlConsoleInterceptor.isIndividualTeam());

        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.TEAM);
        assertFalse(sqlConsoleInterceptor.isIndividualTeam());
    }

    @Test
    public void testDoPreHandle() {
        Map<String, Object> context = new HashMap<>();
        when(sqlAsyncExecuteReq.getQueryLimit()).thenReturn(100L);

        // Test case 1: null ruleSetId
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(null);
        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));

        // Test case 2: individual team
        when(authenticationFacade.currentUser().getOrganizationType()).thenReturn(OrganizationType.INDIVIDUAL);
        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));

        // Test case 3: no NEED_SQL_CONSOLE_CHECK
        when(ConnectionSessionUtil.getRuleSetId(connectionSession)).thenReturn(1L);
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, false);
        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));

        // Test case 4: allow execute
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(20));
        context.put(SqlConsoleInterceptor.NEED_SQL_CONSOLE_CHECK, true);
        assertTrue(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));

        // Test case 5: not allow execute
        when(sqlConsoleRuleService.getProperties(1L, SqlConsoleRules.MAX_RETURN_ROWS, DialectType.MYSQL, Integer.class))
                .thenReturn(Optional.of(10));
        when(sqlAsyncExecuteReq.getQueryLimit()).thenReturn(15L);
        assertFalse(sqlConsoleInterceptor.doPreHandle(sqlAsyncExecuteReq, sqlAsyncExecuteResp, connectionSession, context));
    }

    @Test
    public void testDoAfterCompletion() {
        Map<String, Object> context = new HashMap<>();

        // Test case 1: not allowed to edit result set
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EDIT_RESULTSET, connectionSession))
                .thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        assertFalse(sqlExecuteResult.getResultSetMetaData().isEditable());

        // Test case 2: allowed to export result set
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(false);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        assertTrue(sqlExecuteResult.isAllowExport());

        // Test case 3: not allowed to export result set
        when(sqlConsoleRuleService.isForbidden(SqlConsoleRules.NOT_ALLOWED_EXPORT_RESULTSET, connectionSession))
                .thenReturn(true);
        sqlConsoleInterceptor.doAfterCompletion(sqlExecuteResult, connectionSession, context);
        assertFalse(sqlExecuteResult.isAllowExport());
    }

    @Test
    public void testDetermineSqlType() throws SyntaxErrorException {
        SqlTuple sqlTuple = new SqlTuple();
        sqlTuple.setOriginalSql("SELECT * FROM table");

        // Test case 1: success
        when(abstractSyntaxTree.getParseResult()).thenReturn(new BasicResult(SqlType.SELECT));
        AbstractSyntaxTreeFactory abstractSyntaxTreeFactory = mock(AbstractSyntaxTreeFactory.class);
        when(abstractSyntaxTreeFactory.createSyntaxTree(sqlTuple, DialectType.MYSQL)).thenReturn(abstractSyntaxTree);
        sqlTuple.initAst(abstractSyntaxTreeFactory);
        BasicResult basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.SELECT, basicResult.getSqlType());

        // Test case 2: syntax error
        when(abstractSyntaxTree.getParseResult()).thenThrow(new SyntaxErrorException(""));
        basicResult = sqlConsoleInterceptor.determineSqlType(sqlTuple, DialectType.MYSQL);
        assertEquals(SqlType.UNKNOWN, basicResult.getSqlType());
        assertTrue(basicResult.isSyntaxError());
    }
}